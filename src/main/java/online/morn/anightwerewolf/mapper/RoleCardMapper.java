package online.morn.anightwerewolf.mapper;

import online.morn.anightwerewolf.DO.RoleCardDO;

import java.util.List;

public interface RoleCardMapper {

    /**
     * 查询全部角色卡
     * @auther Horner 2017/11/19 4:56
     * @return
     */
    public List<RoleCardDO> selectAllList();
}
