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
import online.morn.anightwerewolf.util.ActivityStatus;
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
            activityDO = new ActivityDO();
            activityDO.setId(IdUtil.getId());
            activityDO.setRoomId(roomId);
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
        if(ActivityStatus.NOT_BEGIN.equals(activityDO.getStatus())){
            List<ActivityDetailDO> activityDetailDOList = activityDetailService.findActivityDetailListByActivityId(activityId);
            RoomDO roomDO = roomService.findRoomById(activityDO.getRoomId());
            if(roomDO.getPeopleCount() == (activityDetailDOList.size() - 3)){//符合开始条件
                activityDO.setStatus(ActivityStatus.NOT_SKILL);//未执行技能
                this.fillSkillRoleId(activityDO);
                /*RoleDO roleDO = roleService.findRoleByActivityId(activityId);
                if(roleDO == null){
                    activityDO.setSkillRoleId(null);
                } else{
                    activityDO.setSkillRoleId(roleDO.getId());
                }*/
                this.changeById(activityDO);
                //this.changeActivitySkillRoleIdById(activityId);
            }
        } else if(ActivityStatus.NOT_SKILL.equals(activityDO.getStatus())){
            this.fillSkillRoleId(activityDO);
            this.changeById(activityDO);

            List<ActivityDetailDO> activityDetailDOList = activityDetailService.findActivityDetailListByActivityId(activityId);
            boolean isSkillOverFlag = true;
            for(ActivityDetailDO detailDO : activityDetailDOList){
                if(detailDO.getSkillStatus() == 0){
                    isSkillOverFlag = false;
                }
            }
            if(isSkillOverFlag){
                RoomDO roomDO = roomService.findRoomById(activityDO.getRoomId());
                int speakNum = RandomUtil.generateRandom(roomDO.getPeopleCount()) + 1;
                activityDO.setStatus(ActivityStatus.NOT_VOTE);
                activityDO.setSpeakNum(speakNum);
            }
        }
        return this.changeById(activityDO);
    }

    private ActivityDO fillSkillRoleId(ActivityDO activityDO) throws MyException {
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
