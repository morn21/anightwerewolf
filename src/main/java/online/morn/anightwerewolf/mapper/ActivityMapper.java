package online.morn.anightwerewolf.mapper;

import online.morn.anightwerewolf.DO.ActivityDO;
import org.apache.ibatis.annotations.Param;

/**
 * 场次表
 * @auther Horner 2017/11/26 11:58
 */
public interface ActivityMapper {

    /**
     * 添加场次
     * @auther Horner 2017/11/19 9:04
     * @param activityDO
     * @return
     */
    public Integer insert(@Param("activityDO") ActivityDO activityDO);

    /**
     * 修改场次 根据场次ID
     * @auther Horner 2017/11/27 0:56
     * @param activityDO
     * @return
     */
    public Integer updateById(@Param("activityDO") ActivityDO activityDO);

    /**
     * 查询未完成场次 根据房间ID
     * @auther Horner 2017/11/26 15:08
     * @param roomId
     * @return
     */
    public ActivityDO selectUnfinishedActivityByRoomId(@Param("roomId") String roomId);

    /**
     * 查询场次 根据场次ID
     * @auther Horner 2017/11/26 22:34
     * @param id
     * @return
     */
    public ActivityDO selectActivityById(@Param("id") String id);
}
