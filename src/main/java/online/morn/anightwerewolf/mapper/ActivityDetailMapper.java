package online.morn.anightwerewolf.mapper;

import online.morn.anightwerewolf.DO.ActivityDetailDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 场次明细表
 * @auther Horner 2017/11/26 11:50
 */
public interface ActivityDetailMapper {

    /**
     * 添加场次明细
     * @auther Horner 2017/11/26 16:16
     * @param activityDetailDO
     * @return
     */
    public Integer insert(@Param("activityDetailDO") ActivityDetailDO activityDetailDO);

    /**
     * 修改场次明细 根据场次明细ID
     * @auther Horner 2017/11/27 0:56
     * @param activityDetailDO
     * @return
     */
    public Integer updateById(@Param("activityDetailDO") ActivityDetailDO activityDetailDO);

    /**
     * 查询场次明细列表 根据场次ID
     * @auther Horner 2017/11/26 16:15
     * @param activityId
     * @return
     */
    public List<ActivityDetailDO> selectActivityDetailListByActivityId(@Param("activityId") String activityId);

    /**
     * 查询场次明细 根据场次ID和座号
     * @auther Horner 2017/11/26 19:54
     * @param activityId
     * @param seatNum
     * @return
     */
    public ActivityDetailDO selectActivityDetailByActivityIdAndSeatNum(@Param("activityId") String activityId, @Param("seatNum") Integer seatNum);
}
