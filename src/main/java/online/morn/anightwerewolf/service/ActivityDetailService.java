package online.morn.anightwerewolf.service;

import online.morn.anightwerewolf.DO.ActivityDetailDO;
import online.morn.anightwerewolf.util.MyException;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 场次明细服务
 * @auther Horner 2017/11/26 15:39
 */
public interface ActivityDetailService {

    /**
     * 添加场次明细
     * @auther Horner 2017/11/26 19:55
     * @param userId
     * @param activityId
     * @param seatNum
     * @return
     * @throws MyException
     */
    public ActivityDetailDO addActivityDetail(String userId, String activityId, Integer seatNum) throws MyException;

    /**
     * 修改场次 根据ID
     * @auther Horner 2017/11/29 0:06
     * @param activityDetailDO
     * @return
     * @throws MyException
     */
    public Integer changeById(ActivityDetailDO activityDetailDO) throws MyException;

    /**
     * 修改场次明细 根据场次明细列表的ID
     * @auther Horner 2017/11/27 0:56
     * @param activityDetailDOList
     * @return
     * @throws MyException
     */
    public Integer changeByList(List<ActivityDetailDO> activityDetailDOList) throws MyException;

    /**
     * 查询场次明细列表 根据场次ID
     * @auther Horner 2017/11/26 16:49
     * @param activityId
     * @return
     * @throws MyException
     */
    public List<ActivityDetailDO> findActivityDetailListByActivityId(String activityId) throws MyException;
}
