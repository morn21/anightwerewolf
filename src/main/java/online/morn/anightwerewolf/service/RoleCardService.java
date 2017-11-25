package online.morn.anightwerewolf.service;

import online.morn.anightwerewolf.DO.RoleCardDO;

import java.util.List;

/**
 * 角色卡服务
 * @auther Horner 2017/11/26 1:08
 */
public interface RoleCardService {

    /**
     * 查询角色卡根据房间ID
     * @auther Horner 2017/11/26 1:11
     * @param roomId
     * @return
     */
    public List<RoleCardDO> findRoleCardByRoomId(String roomId);
}
