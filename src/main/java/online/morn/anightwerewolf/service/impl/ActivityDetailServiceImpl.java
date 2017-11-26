package online.morn.anightwerewolf.service.impl;

import online.morn.anightwerewolf.DO.ActivityDetailDO;
import online.morn.anightwerewolf.mapper.ActivityDetailMapper;
import online.morn.anightwerewolf.service.ActivityDetailService;
import online.morn.anightwerewolf.util.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 场次明细服务
 * @auther Horner 2017/11/26 15:39
 */
@Service
public class ActivityDetailServiceImpl implements ActivityDetailService {

    @Autowired
    private ActivityDetailMapper activityDetailMapper;

    @Override
    public List<ActivityDetailDO> findActivityDetailListByActivityId(String activityId) throws MyException {
        return activityDetailMapper.selectActivityDetailListByActivityId(activityId);
    }
}
