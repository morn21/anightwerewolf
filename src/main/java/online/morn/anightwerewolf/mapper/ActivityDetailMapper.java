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
     * 查询场次明细列表 根据场次ID
     * @auther Horner 2017/11/26 16:15
     * @param activityId
     * @return
     */
    public List<ActivityDetailDO> selectActivityDetailListByActivityId(@Param("activityId") String activityId);
}
