package online.morn.anightwerewolf.service;

import online.morn.anightwerewolf.DO.ActivityDO;
import online.morn.anightwerewolf.util.MyException;
import org.apache.ibatis.annotations.Param;

/**
 * 场次服务
 * @auther Horner 2017/11/26 15:07
 */
public interface ActivityService {

    /**
     * 查询未完成场次 根据房间ID（没有就新建一个）
     * @auther Horner 2017/11/26 15:09
     * @param roomId
     * @return
     * @throws MyException
     */
    public ActivityDO findUnfinishedActivityByRoomId(String roomId) throws MyException;

    /**
     * 修改场次 根据场次ID
     * @auther Horner 2017/11/27 0:56
     * @param activityDO
     * @return
     */
    public Integer changeById(ActivityDO activityDO) throws MyException;

    /**
     * 查询场次 根据场次ID
     * @auther Horner 2017/11/26 22:34
     * @param id
     * @return
     * @throws MyException
     */
    public ActivityDO findActivityById(String id) throws MyException;
}
