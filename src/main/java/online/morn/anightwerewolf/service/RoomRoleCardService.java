package online.morn.anightwerewolf.service;

import online.morn.anightwerewolf.DO.RoleCardDO;
import online.morn.anightwerewolf.DO.RoomRoleCardDO;
import online.morn.anightwerewolf.util.MyException;

import java.util.List;

/**
 * 房间角色牌服务
 * @auther Horner 2017/11/26 15:39
 */
public interface RoomRoleCardService {

    /**
     * 生成房间角色牌列表
     * @auther Horner 2017/11/26 16:07
     * @param roomId
     * @param roleCardDOList
     * @return
     * @throws MyException
     */
    public List<RoomRoleCardDO> generateRoomRoleCard(String roomId, List<RoleCardDO> roleCardDOList) throws MyException;

    /**
     * 查询房间角色牌列表 根据房间ID
     * @auther Horner 2017/11/26 0:58
     * @param roomId
     * @return
     * @throws MyException
     */
    public List<RoomRoleCardDO> findRoomRoleCardListByRoomId(String roomId) throws MyException;;
}
