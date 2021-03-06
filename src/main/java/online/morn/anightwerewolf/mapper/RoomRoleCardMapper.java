package online.morn.anightwerewolf.mapper;

import online.morn.anightwerewolf.DO.RoomRoleCardDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 房间角色牌表
 * @auther Horner 2017/11/26 0:35
 */
public interface RoomRoleCardMapper {

    /**
     * 添加房间角色牌
     * @auther Horner 2017/11/26 0:52
     * @param roomRoleCardDO
     * @return
     */
    public Integer insert(@Param("roomRoleCardDO") RoomRoleCardDO roomRoleCardDO);

    /**
     * 查询房间角色牌列表 根据房间ID
     * @auther Horner 2017/11/26 0:58
     * @param roomId
     * @return
     */
    public List<RoomRoleCardDO> selectRoomRoleCardListByRoomId(@Param("roomId") String roomId);
}
