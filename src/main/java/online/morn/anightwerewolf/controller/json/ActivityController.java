package online.morn.anightwerewolf.controller.json;

import com.alibaba.fastjson.JSONObject;
import online.morn.anightwerewolf.DO.*;
import online.morn.anightwerewolf.service.ActivityDetailService;
import online.morn.anightwerewolf.service.ActivityService;
import online.morn.anightwerewolf.service.RoleCardService;
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
    private RoleCardService roleCardService;

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
            UserDO userDO = (UserDO)request.getSession().getAttribute(SessionKey.USER);//获得用户实例
            /**验证*/
            RoomDO roomDO = (RoomDO)request.getSession().getAttribute(SessionKey.ROOM);//获得房间实例
            if(roomDO == null){
                throw new MyException("房间未登录");
            }
            if(StringUtils.isBlank(activityId)){
                throw new MyException("场次ID不能为空");
            }
            ActivityDO activityDO = activityService.findActivityById(activityId);
            if(!activityDO.getRoomId().equals(roomDO.getId())){
                throw new MyException("您不在本场次的房间内");
            }
            /**查到当前用户所在场次明细位置 以及 场次明细Map*/
            ActivityDetailDO myActivityDetailDO = null;
            Map<String,ActivityDetailDO> detailByRoleCardIdMap = new HashMap<>();//以起始角色牌ID为Key 场次明细为值 的Map
            List<ActivityDetailDO> activityDetailDOList = activityDetailService.findActivityDetailListByActivityId(activityId);
            for(ActivityDetailDO detailDO : activityDetailDOList){
                detailByRoleCardIdMap.put(detailDO.getInitialRoleCardId(),detailDO);//put Map
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
                    dataMap.put("seatNum",myActivityDetailDO.getSeatNum());
                    dataMap.put("peopleCount",roomDO.getPeopleCount());//本房间场次的总人数
                    dataMap.put("lockPeopleCount",activityDetailDOList.size());//已锁定座号的人数
                } else if(ActivityStatus.NOT_SKILL.equals(status)){//未执行技能
                    dataMap.put("cardCount",roomDO.getPeopleCount() + 3);//本房间场次的总牌数
                    dataMap.put("myRoleCard",roleCardService.findRoleCardById(myActivityDetailDO.getInitialRoleCardId()));//当前用户的角色牌
                    List<RoleCardDO> RoleCardDOList = roleCardService.findRoleCardByRoomId(roomDO.getId());
                    int currentOrderNum = 1;//当前执行牌位序号
                    for(RoleCardDO roleCardDO : RoleCardDOList){//按执行顺序遍历本房间的所有角色牌（模拟上帝宣读）
                        ActivityDetailDO orderDetail = detailByRoleCardIdMap.get(roleCardDO.getId());//执行顺序的场次明细
                        if(orderDetail.getSkillDescription() == null){//当前该执行技能的明细
                            boolean isNobody = true;//当前是否是轮到底牌角色执行
                            boolean isCurrentUser = false;//是否轮到当前用户执行
                            if(orderDetail.getSeatNum() > 0){//是用户
                                isNobody = false;
                                if(orderDetail.getId().equals(myActivityDetailDO.getId())){
                                    isCurrentUser = true;
                                }
                            }
                            dataMap.put("isNobody",isNobody);//当前是否是轮到底牌角色执行
                            dataMap.put("isCurrentUser",isCurrentUser);//是否轮到当前用户执行
                            dataMap.put("currentRoleCard",roleCardDO);//当前执行技能的角色牌
                            dataMap.put("currentOrderNum",currentOrderNum);//当前执行牌位序号
                            break;
                        }
                        currentOrderNum++;
                    }
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
}
