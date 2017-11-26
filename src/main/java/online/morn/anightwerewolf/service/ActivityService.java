package online.morn.anightwerewolf.service;

import online.morn.anightwerewolf.DO.ActivityDO;
import online.morn.anightwerewolf.util.MyException;

/**
 * 场次服务
 * @auther Horner 2017/11/26 15:07
 */
public interface ActivityService {

    /**
     * 查询未完成场次 根据房间ID
     * @auther Horner 2017/11/26 15:09
     * @param roomId
     * @return
     * @throws MyException
     */
    public ActivityDO findUnfinishedActivityByRoomId(String roomId) throws MyException;
}
