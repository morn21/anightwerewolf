package online.morn.anightwerewolf.mapper;

import online.morn.anightwerewolf.DO.RoomDO;
import org.apache.ibatis.annotations.Param;

/**
 * 房间表
 * @auther Horner 2017/11/26 0:34
 */
public interface RoomMapper {

    /**
     * 添加房间
     * @auther Horner 2017/11/19 9:04
     * @param roomDO
     * @return
     */
    public Integer insert(@Param("roomDO") RoomDO roomDO);

    /**
     * 查询最大房间名的值
     * @auther Horner 2017/11/19 9:46
     * @return
     */
    public Integer selectMaxNameValue();

    /**
     * 查询房间根据房间名和密码
     * @auther Horner 2017/11/23 1:32
     * @param name
     * @param password
     * @return
     */
    public RoomDO selectRoomByNameAndPassword(@Param("name") String name, @Param("password") String password);

    /**
     * 查询房间根据ID
     * @auther Horner 2017/11/26 0:17
     * @param id
     * @return
     */
    public RoomDO selectRoomById(@Param("id") String id);
}
