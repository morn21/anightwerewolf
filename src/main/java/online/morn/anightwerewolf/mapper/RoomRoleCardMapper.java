package online.morn.anightwerewolf.mapper;

import online.morn.anightwerewolf.DO.RoomRoleCardDO;
import org.apache.ibatis.annotations.Param;

public interface RoomRoleCardMapper {

    public Integer insert(@Param("roomRoleCardDO") RoomRoleCardDO roomRoleCardDO);
}
