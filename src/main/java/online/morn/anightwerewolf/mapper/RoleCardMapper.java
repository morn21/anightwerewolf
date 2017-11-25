package online.morn.anightwerewolf.mapper;

import online.morn.anightwerewolf.DO.RoleCardDO;
import online.morn.anightwerewolf.DO.RoomDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色卡表
 * @auther Horner 2017/11/26 0:20
 */
public interface RoleCardMapper {

    /**
     * 查询全部角色卡
     * @auther Horner 2017/11/19 4:56
     * @return
     */
    public List<RoleCardDO> selectAllList();

    public List<RoleCardDO> selectRoleCardListByIdList(@Param("roleCardIdList") List<String> roleCardIdList);
}
