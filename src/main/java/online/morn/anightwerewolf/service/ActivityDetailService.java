package online.morn.anightwerewolf.service;

import online.morn.anightwerewolf.DO.ActivityDetailDO;
import online.morn.anightwerewolf.util.MyException;

import java.util.List;

/**
 * 场次明细服务
 * @auther Horner 2017/11/26 15:39
 */
public interface ActivityDetailService {

    /**
     * 查询场次明细列表 根据场次ID
     * @auther Horner 2017/11/26 16:49
     * @param activityId
     * @return
     * @throws MyException
     */
    public List<ActivityDetailDO> findActivityDetailListByActivityId(String activityId) throws MyException;
}
