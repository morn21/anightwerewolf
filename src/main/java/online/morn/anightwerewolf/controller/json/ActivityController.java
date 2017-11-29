package online.morn.anightwerewolf.controller.json;

import com.alibaba.fastjson.JSONObject;
import online.morn.anightwerewolf.DO.*;
import online.morn.anightwerewolf.service.*;
import online.morn.anightwerewolf.util.ActivityStatus;
import online.morn.anightwerewolf.util.MyException;
import online.morn.anightwerewolf.util.SessionKey;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
    private RoomService roomService;

    /**
     * 询问场次状态
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
                    /**本房间下 角色以及角色牌*/
                    List<RoleDO> roleDOList = roleService.findRoleByRoomId(roomDO.getId());//获本房间全部角色
                    List<RoleCardDO> roleCardDOList = roleCardService.findRoleCardByRoomId(roomDO.getId());//获本房间全部角色牌
                    Map<String,RoleDO> roleDOMap = this.makeRoleMap(roleDOList);
                    Map<String,RoleCardDO> roleCardDOMap = this.makeRoleCardMap(roleCardDOList);
                    dataMap.put("roleCount",roleDOList.size());//本房间的角色数量
                    dataMap.put("cardCount",roleCardDOList.size());//本房间的卡牌数量
                    /**我的初始 角色以及角色卡*/
                    RoleCardDO myRoleCardDO = roleCardDOMap.get(myActivityDetailDO.getInitialRoleCardId());
                    RoleDO myRoleDO = roleDOMap.get(myRoleCardDO.getRoleId());
                    dataMap.put("myRole",myRoleDO);//我的角色牌
                    dataMap.put("myRoleCard",myRoleCardDO);//我的角色牌
                    /**我是否已执行技能*/
                    boolean isSkillByMe = false;
                    if(myActivityDetailDO.getSkillStatus() != 0){
                        isSkillByMe = true;
                    }
                    dataMap.put("isSkillByMe",isSkillByMe);//我是否已执行技能
                    /**当前正在执行技能的角色*/
                    RoleDO currentRoleDO = roleService.findRoleById(activityDO.getSkillRoleId());
                    dataMap.put("currentRole",currentRoleDO);//当前正在执行技能的角色
                    /**已执行完技能 和 正在执行技能的角色数量*/
                    int skillRoleCount = 1;
                    for(RoleDO roleDO : roleDOList){
                        if(roleDO.getId().equals(currentRoleDO.getId())){
                            break;
                        }
                        skillRoleCount++;
                    }
                    dataMap.put("skillRoleCount",skillRoleCount);//已经执行完技能的角色数量
                    /**当前正在执行技能的角色是否存在于底牌当中*/
                    boolean isNobody = false;
                    for(ActivityDetailDO detailDO : activityDetailDOList){
                        if(detailDO.getSeatNum() < 0){//底牌
                            RoleCardDO roleCardDO = roleCardDOMap.get(detailDO.getInitialRoleCardId());
                            if(roleCardDO.getRoleId().equals(currentRoleDO.getId())){
                                isNobody = true;
                            }
                        }
                    }
                    dataMap.put("isNobody",isNobody);//当前正在执行技能的角色是否存在于底牌当中
                    /**当前用户是否需要执行技能*/
                    boolean isSkillByCurrent = false;
                    if(!isSkillByMe && currentRoleDO.getId().equals(myRoleDO.getId())){
                        isSkillByCurrent = true;
                    }
                    dataMap.put("isSkillByCurrent",isSkillByCurrent);//当前用户是否需要执行技能
                } else if(ActivityStatus.NOT_VOTE.equals(status)){//未投票
                    /////////////////////////////////////////////////
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
     * 执行技能 -- 底牌标识为已执行技能
     * @auther Horner 2017/11/29 23:36
     * @param modelMap
     * @param request
     * @param activityId
     * @return
     */
    @RequestMapping(value = "/executeSkillByNobody.json", method = {RequestMethod.GET , RequestMethod.POST})
    public String executeSkillByNobody(ModelMap modelMap, HttpServletRequest request, String activityId) {
        try {
            /**参数验证*/
            if(StringUtils.isBlank(activityId)){
                throw new MyException("场次ID不能为空");
            }
            /**Session取值*/
            RoomDO roomDO = (RoomDO)request.getSession().getAttribute(SessionKey.ROOM);//获得房间实例
            if(roomDO == null){
                throw new MyException("房间未登录");
            }
            /**场次信息*/
            ActivityDO activityDO = activityService.findActivityById(activityId);
            if(!activityDO.getRoomId().equals(roomDO.getId())){
                throw new MyException("您不在本场次的房间内");
            }
            if(!ActivityStatus.NOT_SKILL.equals(activityDO.getStatus())){
                throw new MyException("场次状态错误");
            }
            /**标识当前正在执行技能的底牌为已执行*/
            boolean isChangeFlag = false;//对数据库做出修改的旗帜
            List<ActivityDetailDO> activityDetailDOList = activityDetailService.findActivityDetailListByActivityId(activityId);
            for(ActivityDetailDO detailDO : activityDetailDOList){
                if(detailDO.getSeatNum() < 0 && detailDO.getSkillStatus() == 0){//找出未执行技能的底牌
                    RoleCardDO roleCardDO = roleCardService.findRoleCardById(detailDO.getInitialRoleCardId());
                    if(roleCardDO.getRoleId().equals(activityDO.getSkillRoleId())){
                        detailDO.setSkillStatus(1);
                        activityDetailService.changeById(detailDO);
                        isChangeFlag = true;
                    }
                }
            }
            if(isChangeFlag){
                activityService.changeActivityStatus(activityId);
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
     * （私有方法）制作角色Map --- key为角色ID
     * @auther Horner 2017/11/29 19:58
     * @param roleDOList
     * @return
     */
    private Map<String,RoleDO> makeRoleMap(List<RoleDO> roleDOList){
        Map<String,RoleDO> roleMap = new HashMap<>();
        for(RoleDO roleDO : roleDOList){
            roleMap.put(roleDO.getId(),roleDO);
        }
        return roleMap;
    }

    /**
     * （私有方法）制作角色牌Map --- key为角色牌ID
     * @auther Horner 2017/11/29 19:58
     * @param roleCardDOList
     * @return
     */
    private Map<String,RoleCardDO> makeRoleCardMap(List<RoleCardDO> roleCardDOList){
        Map<String,RoleCardDO> roleCardMap = new HashMap<>();
        for(RoleCardDO roleCardDO : roleCardDOList){
            roleCardMap.put(roleCardDO.getId(),roleCardDO);
        }
        return roleCardMap;
    }

}
