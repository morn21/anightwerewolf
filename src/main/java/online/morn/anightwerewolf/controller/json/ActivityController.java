package online.morn.anightwerewolf.controller.json;

import com.alibaba.fastjson.JSONObject;
import online.morn.anightwerewolf.DO.*;
import online.morn.anightwerewolf.service.*;
import online.morn.anightwerewolf.util.MapMaker;
import online.morn.anightwerewolf.util.MyException;
import online.morn.anightwerewolf.util.enumeration.ActivityStatus;
import online.morn.anightwerewolf.util.enumeration.RoleId;
import online.morn.anightwerewolf.util.enumeration.SessionKey;
import online.morn.anightwerewolf.util.skillExtendInfoVO.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 场次
 * @auther Horner 2017/11/26 21:38
 */
@RequestMapping(value = "/activity")
@RestController
public class ActivityController {
    private Logger logger = Logger.getLogger(RoomController.class);

    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityDetailService activityDetailService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleCardService roleCardService;
    @Autowired
    private SkillService skillService;

    /**
     * 询问场次状态
     * @auther Horner 2017/12/3 1:21
     * @param modelMap
     * @param request
     * @param activityId
     * @return
     */
    @RequestMapping(value = "/askActivityStatus.json", method = {RequestMethod.GET , RequestMethod.POST})
    public String askActivityStatus(ModelMap modelMap, HttpServletRequest request, String activityId) {
        try {
            /**参数验证*/
            if(StringUtils.isBlank(activityId)){
                throw new MyException("场次ID不能为空");
            }
            /**Session取值*/
            UserDO userDO = (UserDO)request.getSession().getAttribute(SessionKey.USER);//获得用户实例
            RoomDO roomDO = (RoomDO)request.getSession().getAttribute(SessionKey.ROOM);//获得房间实例
            if(roomDO == null){
                throw new MyException("房间未登录");
            }
            /**场次信息*/
            ActivityDO activityDO = activityService.findActivityById(activityId);
            if(!activityDO.getRoomId().equals(roomDO.getId())){
                throw new MyException("您不在本场次的房间内");
            }
            /**查到当前用户所在场次明细位置*/
            ActivityDetailDO myActivityDetailDO = null;
            List<ActivityDetailDO> activityDetailDOList = activityDetailService.findActivityDetailListByActivityId(activityId);
            for(ActivityDetailDO detailDO : activityDetailDOList){
                if(userDO.getId().equals(detailDO.getUserId())){
                    myActivityDetailDO = detailDO;
                }
            }
            /**获得本场次状态*/
            String status = activityDO.getStatus();
            Map<String,Object> dataMap = new HashMap<>();
            if(myActivityDetailDO == null){//当前用户没有参与本场次
                status = "NOT_JOIN";//特殊的状态类型 不存库的
            } else{//当前用户参与了本场次
                /**添加本场次状态的相关数据*/
                if(ActivityStatus.NOT_BEGIN.equals(status)){//未开始
                    dataMap.put("mySeatNum",myActivityDetailDO.getSeatNum());//我的座号
                    dataMap.put("peopleCount",roomDO.getPeopleCount());//本房间场次的总人数
                    dataMap.put("lockPeopleCount",activityDetailDOList.size());//已锁定座号的人数
                } else if(ActivityStatus.NOT_SKILL.equals(status)){//未执行技能
                    dataMap.put("mySeatNum",myActivityDetailDO.getSeatNum());//我的座号
                    dataMap.put("peopleCount",roomDO.getPeopleCount());//本房间场次的总人数
                    /**本房间下 角色以及角色牌*/
                    List<RoleDO> roleDOList = roleService.findRoleByRoomId(roomDO.getId());//获本房间全部角色
                    List<RoleCardDO> roleCardDOList = roleCardService.findRoleCardByRoomId(roomDO.getId());//获本房间全部角色牌
                    Map<String,RoleDO> roleDOMap = MapMaker.makeRoleMap(roleDOList);
                    Map<String,RoleCardDO> roleCardDOMap = MapMaker.makeRoleCardMap(roleCardDOList);
                    dataMap.put("roleCount",roleDOList.size());//本房间的角色数量
                    dataMap.put("cardCount",roleCardDOList.size());//本房间的卡牌数量
                    /**我的初始 角色以及角色卡*/
                    RoleCardDO myRoleCardDO = roleCardDOMap.get(myActivityDetailDO.getInitialRoleCardId());
                    RoleDO myRoleDO = roleDOMap.get(myRoleCardDO.getRoleId());
                    dataMap.put("myRole",myRoleDO);//我的角色牌
                    dataMap.put("myRoleCard",myRoleCardDO);//我的角色牌
                    /**当前正在执行技能的角色*/
                    RoleDO currentRoleDO = roleService.findRoleById(activityDO.getSkillRoleId());
                    dataMap.put("currentRole",currentRoleDO);//当前正在执行技能的角色
                    /**已执行完技能*/
                    int skillRoleCount = 0;
                    for(RoleDO roleDO : roleDOList){
                        if(roleDO.getId().equals(currentRoleDO.getId())){
                            break;
                        }
                        skillRoleCount++;
                    }
                    dataMap.put("skillRoleCount",skillRoleCount);//已经执行完技能的角色数量
                    /**当前正在执行技能的角色是否存在于底牌当中*/
                    for(ActivityDetailDO detailDO : activityDetailDOList){
                        if(detailDO.getSeatNum() < 0){//底牌
                            RoleCardDO roleCardDO = roleCardDOMap.get(detailDO.getInitialRoleCardId());
                            if(roleCardDO.getRoleId().equals(currentRoleDO.getId())){
                                executeSkillByNobody(detailDO);//【私有方法】执行技能 -- 底牌标识为已执行技能
                            }
                        }
                    }
                    /**当前用户是否需要执行技能*/
                    boolean isSkillByCurrent = false;
                    if(myActivityDetailDO.getSkillStatus() == 0 && currentRoleDO.getId().equals(myRoleDO.getId())){
                        isSkillByCurrent = true;
                    }
                    dataMap.put("isSkillByCurrent",isSkillByCurrent);//当前用户是否需要执行技能
                    /**我的技能状态*/
                    dataMap.put("mySkillStatus",myActivityDetailDO.getSkillStatus());//我的技能状态
                    dataMap.put("mySkillExtendInfo",JSONObject.parse(myActivityDetailDO.getSkillExtendInfo()));//我的技能扩展信息
                } else if(ActivityStatus.NOT_VOTE.equals(status)){//未投票
                    dataMap.put("speakNum",activityDO.getSpeakNum());//发言号
                    dataMap.put("peopleCount",roomDO.getPeopleCount());//本房间场次的总人数
                    dataMap.put("mySeatNum",myActivityDetailDO.getSeatNum());//我的座号
                    /**本房间下 角色以及角色牌*/
                    List<RoleDO> roleDOList = roleService.findRoleByRoomId(roomDO.getId());//获本房间全部角色
                    List<RoleCardDO> roleCardDOList = roleCardService.findRoleCardByRoomId(roomDO.getId());//获本房间全部角色牌
                    Map<String,RoleDO> roleDOMap = MapMaker.makeRoleMap(roleDOList);
                    Map<String,RoleCardDO> roleCardDOMap = MapMaker.makeRoleCardMap(roleCardDOList);
                    /**我的初始 角色以及角色卡*/
                    RoleCardDO myRoleCardDO = roleCardDOMap.get(myActivityDetailDO.getInitialRoleCardId());
                    RoleDO myRoleDO = roleDOMap.get(myRoleCardDO.getRoleId());
                    dataMap.put("myRole",myRoleDO);//我的角色牌
                    dataMap.put("myRoleCard",myRoleCardDO);//我的角色牌
                    /**我的技能状态*/
                    dataMap.put("mySkillStatus",myActivityDetailDO.getSkillStatus());//我的技能状态
                    dataMap.put("mySkillExtendInfo",JSONObject.parse(myActivityDetailDO.getSkillExtendInfo()));//我的技能扩展信息
                    /**投票状态*/
                    int voteCount = 0;//已投票的数量
                    for(ActivityDetailDO detail : activityDetailDOList){
                        if(detail.getSeatNum() > 0 && detail.getVoteNum() != null){
                            voteCount++;
                        }
                    }
                    dataMap.put("myVoteNum",myActivityDetailDO.getVoteNum());//我的投票号
                    dataMap.put("voteCount",voteCount);//已投票数量
                } else if(ActivityStatus.END.equals(status)){//结束
                    /////////////////////////////////////////////////
                }
            }
            dataMap.put("status",status);

            modelMap.put("success",true);
            modelMap.put("data",dataMap);
        } catch (MyException e) {
            modelMap.put("success",false);
            modelMap.put("msg",e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return JSONObject.toJSONString(modelMap);
    }

    /**
     * 【私有方法】执行技能 -- 底牌标识为已执行技能
     * @auther Horner 2017/12/9 1:22
     * @param detailDO
     * @throws MyException
     */
    private void executeSkillByNobody(final ActivityDetailDO detailDO) throws MyException {
        JSONObject skillExtendInfo = JSONObject.parseObject(detailDO.getSkillExtendInfo());
        if(skillExtendInfo.getBoolean("isTread") == null){
            skillExtendInfo.put("isTread",true);
            detailDO.setSkillExtendInfo(skillExtendInfo.toJSONString());
            activityDetailService.changeById(detailDO);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);
                        detailDO.setSkillStatus(1);
                        activityDetailService.changeById(detailDO);
                        activityService.changeActivityStatus(detailDO.getActivityId());//更新场次状态 根据场次ID
                    } catch (MyException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }

    /**
     * 执行技能 -- 根据我的角色
     * @auther Horner 2017/12/3 1:21
     * @param modelMap
     * @param request
     * @param activityId
     * @return
     */
    @RequestMapping(value = "/executeSkillByMyRole.json", method = {RequestMethod.GET , RequestMethod.POST})
    public String executeSkillByMyRole(ModelMap modelMap, HttpServletRequest request, String activityId, String isSelectedSeatNumListStr) {
        try {
            /**参数验证*/
            if(StringUtils.isBlank(activityId)){
                throw new MyException("场次ID不能为空");
            }
            if(StringUtils.isBlank(isSelectedSeatNumListStr)){
                throw new MyException("选中座号列表异常");
            }
            List<Integer> isSelectedSeatNumList = JSONObject.parseArray(isSelectedSeatNumListStr, Integer.class);
            /**Session取值*/
            UserDO userDO = (UserDO)request.getSession().getAttribute(SessionKey.USER);//获得用户实例
            RoomDO roomDO = (RoomDO)request.getSession().getAttribute(SessionKey.ROOM);//获得房间实例
            if(roomDO == null){
                throw new MyException("房间未登录");
            }
            skillService.executeMySkill(userDO.getId(),activityId,isSelectedSeatNumList);//执行我的技能
            modelMap.put("success",true);
        } catch (MyException e) {
            modelMap.put("success",false);
            modelMap.put("msg",e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return JSONObject.toJSONString(modelMap);
    }

    /**
     * 执行投票
     * @auther Horner 2017/12/5 0:25
     * @param modelMap
     * @param request
     * @param activityId
     * @param isSelectedSeatNum 选中的座号
     * @return
     */
    @RequestMapping(value = "/executeVote.json", method = {RequestMethod.GET , RequestMethod.POST})
    public String executeVote(ModelMap modelMap, HttpServletRequest request, String activityId, Integer isSelectedSeatNum) {
        try{
            /**参数验证*/
            if(StringUtils.isBlank(activityId)){
                throw new MyException("场次ID不能为空");
            }
            if(isSelectedSeatNum == null){
                throw new MyException("选中座号不能为空");
            }
            /**Session取值*/
            UserDO userDO = (UserDO)request.getSession().getAttribute(SessionKey.USER);//获得用户实例
            RoomDO roomDO = (RoomDO)request.getSession().getAttribute(SessionKey.ROOM);//获得房间实例
            if(roomDO == null){
                throw new MyException("房间未登录");
            }
            /**场次信息*/
            ActivityDO activityDO = activityService.findActivityById(activityId);
            if(!activityDO.getRoomId().equals(roomDO.getId())){
                throw new MyException("您不在本场次的房间内");
            }
            if(!ActivityStatus.NOT_VOTE.equals(activityDO.getStatus())){
                throw new MyException("场次状态错误");
            }
            /**场次明细信息*/
            ActivityDetailDO myActivityDetailDO = null;
            List<ActivityDetailDO> activityDetailDOList = activityDetailService.findActivityDetailListByActivityId(activityId);
            for(ActivityDetailDO detailDO : activityDetailDOList){
                if(userDO.getId().equals(detailDO.getUserId())){
                    myActivityDetailDO = detailDO;
                }
            }
            if(myActivityDetailDO == null){
                throw new MyException("您不存在于本场次中");
            }
            myActivityDetailDO.setVoteNum(isSelectedSeatNum);
            activityDetailService.changeById(myActivityDetailDO);//修改场次明细 -- 投票号
            int rows = activityService.changeActivityStatus(activityId);//更新场次状态 根据场次ID
            if(rows == 1){//说明本场次结束 可以计算输赢结果了
                activityDO = activityService.findActivityById(activityId);
                if(activityDO.getStatus().equals(ActivityStatus.END)){
                    activityDO.setWinStatus(0);////////////////////////////暂时先把已经结束的设置胜利状态为0
                }
                /*List<ActivityDetailDO> resultDetailDOList = activityDetailService.findActivityDetailListByActivityId(activityId);
                for(ActivityDetailDO detailDO : resultDetailDOList){

                }*/
            }
            modelMap.put("success",true);
        } catch (MyException e) {
            modelMap.put("success",false);
            modelMap.put("msg",e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return JSONObject.toJSONString(modelMap);
    }

    /**
     * 查看场次结果列表
     * @auther Horner 2017/12/5 23:40
     * @param modelMap
     * @param request
     * @param activityId
     * @return
     */
    @RequestMapping(value = "/findResultList.json", method = {RequestMethod.GET , RequestMethod.POST})
    public String findResultList(ModelMap modelMap, HttpServletRequest request, String activityId) {
        try{
            /**参数验证*/
            if(StringUtils.isBlank(activityId)){
                throw new MyException("场次ID不能为空");
            }
            /**Session取值*/
            //UserDO userDO = (UserDO)request.getSession().getAttribute(SessionKey.USER);//获得用户实例
            RoomDO roomDO = (RoomDO)request.getSession().getAttribute(SessionKey.ROOM);//获得房间实例
            /**本房间下 角色以及角色牌*/
            //List<RoleDO> roleDOList = roleService.findRoleByRoomId(roomDO.getId());//获本房间全部角色
            List<RoleCardDO> roleCardDOList = roleCardService.findRoleCardByRoomId(roomDO.getId());//获本房间全部角色牌
            //Map<String,RoleDO> roleDOMap = this.makeRoleMap(roleDOList);
            Map<String,RoleCardDO> roleCardDOMap = MapMaker.makeRoleCardMap(roleCardDOList);
            List<ActivityDetailDO> activityDetailDOList = activityDetailService.findActivityDetailListByActivityId(activityId);
            activityDetailDOList = this.sortingDetail(activityDetailDOList);//【私有方法】场次明细排序
            List<Map<String,Object>> resultList = new ArrayList<>();//结果列表
            for(ActivityDetailDO detailDO : activityDetailDOList){
                Map<String,Object> resultMap = new HashMap<>();
                resultMap.put("seatNum",detailDO.getSeatNum());
                resultMap.put("initialRoleCardName",roleCardDOMap.get(detailDO.getInitialRoleCardId()).getName());
                resultMap.put("finalRoleCardName",roleCardDOMap.get(detailDO.getFinalRoleCardId()).getName());
                resultMap.put("voteNum",detailDO.getVoteNum());
                resultList.add(resultMap);
            }
            modelMap.put("data",resultList);
            modelMap.put("success",true);
        } catch (MyException e) {
            modelMap.put("success",false);
            modelMap.put("msg",e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
        }
        return JSONObject.toJSONString(modelMap);
    }

    /**
     * 【私有方法】场次明细排序
     * @auther Horner 2017/12/5 23:52
     * @param activityDetailDOList
     * @return
     */
    private List<ActivityDetailDO> sortingDetail(List<ActivityDetailDO> activityDetailDOList){
        Map<Integer,ActivityDetailDO> detailMap = new HashMap<>();
        for(ActivityDetailDO detailDO : activityDetailDOList){
            detailMap.put(detailDO.getSeatNum(),detailDO);
        }
        List<ActivityDetailDO> newDetailList = new ArrayList<>();
        for(int i = -1; i > -4 ; i--){
            newDetailList.add(detailMap.get(i));
        }
        for(int i = 1 ; i < activityDetailDOList.size() - 2 ; i++){
            newDetailList.add(detailMap.get(i));
        }
        return newDetailList;
    }
}
