package online.morn.anightwerewolf.service;

import online.morn.anightwerewolf.DO.RoomDO;
import online.morn.anightwerewolf.util.MyException;

/**
 * 房间服务
 * @auther Horner 2017/11/26 15:36
 */
public interface RoomService {

    /**
     * 生成房间
     * @auther Horner 2017/11/26 15:41
     * @param password
     * @param peopleCount
     * @return
     * @throws MyException
     */
    public RoomDO generateRoom(String password, Integer peopleCount) throws MyException;

    /**
     * 登录房间
     * @auther Horner 2017/11/26 15:51
     * @param name
     * @param password
     * @return
     * @throws MyException
     */
    public RoomDO loginRoom(String name, String password) throws MyException;

    /**
     * 查找房间 根据房间ID
     * @auther Horner 2017/11/26 17:59
     * @param id
     * @return
     * @throws MyException
     */
    public RoomDO findRoomById(String id) throws MyException;
}
