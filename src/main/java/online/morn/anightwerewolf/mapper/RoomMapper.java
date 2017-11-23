package online.morn.anightwerewolf.mapper;

import online.morn.anightwerewolf.DO.RoomDO;
import org.apache.ibatis.annotations.Param;

public interface RoomMapper {

    /**
     * 插入房间
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
}
