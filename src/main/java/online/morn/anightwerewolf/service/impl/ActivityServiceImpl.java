package online.morn.anightwerewolf.service.impl;

import online.morn.anightwerewolf.DO.ActivityDO;
import online.morn.anightwerewolf.DO.ActivityDetailDO;
import online.morn.anightwerewolf.DO.RoleDO;
import online.morn.anightwerewolf.DO.RoomDO;
import online.morn.anightwerewolf.mapper.ActivityMapper;
import online.morn.anightwerewolf.service.ActivityDetailService;
import online.morn.anightwerewolf.service.ActivityService;
import online.morn.anightwerewolf.service.RoleService;
import online.morn.anightwerewolf.service.RoomService;
import online.morn.anightwerewolf.util.enumeration.ActivityStatus;
import online.morn.anightwerewolf.util.IdUtil;
import online.morn.anightwerewolf.util.MyException;
import online.morn.anightwerewolf.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 场次服务
 * @auther Horner 2017/11/26 15:07
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ActivityDetailService activityDetailService;
    @Autowired
    private RoomService roomService;

    @Override
    public ActivityDO findUnfinishedActivityByRoomId(String roomId) throws MyException {
        ActivityDO activityDO = activityMapper.selectUnfinishedActivityByRoomId(roomId);
        if(activityDO == null){
            RoomDO roomDO = roomService.findRoomById(roomId);
            int speakNum = RandomUtil.generateRandom(roomDO.getPeopleCount()) + 1;
            activityDO = new ActivityDO();
            activityDO.setId(IdUtil.getId());
            activityDO.setRoomId(roomId);
            activityDO.setSpeakNum(speakNum);
            activityDO.setStatus(ActivityStatus.NOT_BEGIN);//未开始
            Integer rows = activityMapper.insert(activityDO);
            if(rows == null || rows == 0){
                throw new MyException("添加场次失败");
            }
        }
        return activityDO;
    }

    @Override
    public Integer changeById(ActivityDO activityDO) throws MyException {
        return activityMapper.updateById(activityDO);
    }

    @Override
    public ActivityDO findActivityById(String id) throws MyException{
        ActivityDO activityDO = activityMapper.selectActivityById(id);
        if(activityDO == null){
            throw new MyException("根据场次ID查找场次信息，没找到");
        }
        return activityDO;
    }

    @Override
    public Integer changeActivityStatus(String activityId) throws MyException {
        ActivityDO activityDO = this.findActivityById(activityId);
        List<ActivityDetailDO> activityDetailDOList = activityDetailService.findActivityDetailListByActivityId(activityId);
        if(ActivityStatus.NOT_BEGIN.equals(activityDO.getStatus())){
            RoomDO roomDO = roomService.findRoomById(activityDO.getRoomId());
            if(roomDO.getPeopleCount() == (activityDetailDOList.size() - 3)){//符合开始条件
                activityDO.setStatus(ActivityStatus.NOT_SKILL);//未执行技能
                this.computeSkillRoleId(activityDO);//计算当前正在执行技能的角色 并写给场次信息中
                this.changeById(activityDO);
            }
        } else if(ActivityStatus.NOT_SKILL.equals(activityDO.getStatus())){
            this.computeSkillRoleId(activityDO);//计算当前正在执行技能的角色 并写给场次信息中
            this.changeById(activityDO);
            boolean isSkillOverFlag = true;
            for(ActivityDetailDO detailDO : activityDetailDOList){
                if(detailDO.getSkillStatus() == 0){
                    isSkillOverFlag = false;
                }
            }
            if(isSkillOverFlag){
                //RoomDO roomDO = roomService.findRoomById(activityDO.getRoomId());
                activityDO.setStatus(ActivityStatus.NOT_VOTE);
            }
        } else if(ActivityStatus.NOT_VOTE.equals(activityDO.getStatus())){
            boolean isVoteOverFlag = true;
            for(ActivityDetailDO detailDO : activityDetailDOList){
                if(detailDO.getSeatNum() > 0 && detailDO.getVoteNum() == null){//场上有一个未投票的都算未结束
                    isVoteOverFlag = false;
                }
            }
            if(isVoteOverFlag){
                activityDO.setStatus(ActivityStatus.END);
            }
        }
        return this.changeById(activityDO);
    }

    /**
     * 【私有方法】计算当前正在执行技能的角色 并写给场次信息中
     * @auther Horner 2017/12/5 0:39
     * @param activityDO
     * @return
     * @throws MyException
     */
    private ActivityDO computeSkillRoleId(ActivityDO activityDO) throws MyException {
        RoleDO roleDO = roleService.findRoleByActivityId(activityDO.getId());
        if(roleDO == null){
            activityDO.setSkillRoleId(null);
        } else{
            activityDO.setSkillRoleId(roleDO.getId());
        }
        return activityDO;
    }

    /*private Integer changeActivitySkillRoleIdById(String activityId) throws MyException {
        ActivityDO activityDO = this.findActivityById(activityId);
        RoleDO roleDO = roleService.findRoleByActivityId(activityId);
        if(roleDO == null){
            activityDO.setSkillRoleId(null);
        } else{
            activityDO.setSkillRoleId(roleDO.getId());
        }
        return this.changeById(activityDO);
    }*/
}
