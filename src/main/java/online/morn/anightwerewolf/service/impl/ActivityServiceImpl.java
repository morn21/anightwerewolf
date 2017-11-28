package online.morn.anightwerewolf.service.impl;

import online.morn.anightwerewolf.DO.ActivityDO;
import online.morn.anightwerewolf.mapper.ActivityMapper;
import online.morn.anightwerewolf.service.ActivityService;
import online.morn.anightwerewolf.util.ActivityStatus;
import online.morn.anightwerewolf.util.IdUtil;
import online.morn.anightwerewolf.util.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 场次服务
 * @auther Horner 2017/11/26 15:07
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

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
}
