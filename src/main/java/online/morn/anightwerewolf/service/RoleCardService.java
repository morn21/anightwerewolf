package online.morn.anightwerewolf.service;

import online.morn.anightwerewolf.DO.RoleCardDO;
import online.morn.anightwerewolf.util.MyException;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色卡服务
 * @auther Horner 2017/11/26 1:08
 */
public interface RoleCardService {

    /**
     * 查询全部角色卡
     * @auther Horner 2017/11/19 4:56
     * @return
     * @throws MyException
     */
    public List<RoleCardDO> findRoleCardList() throws MyException;

    /**
     * 查询角色卡根据房间ID
     * @auther Horner 2017/11/26 1:11
     * @param roomId
     * @return
     * @throws MyException
     */
    public List<RoleCardDO> findRoleCardByRoomId(String roomId) throws MyException;


    /**
     * 查询角色卡 根据ID
     * @auther Horner 2017/11/28 23:15
     * @param id
     * @return
     * @throws MyException
     */
    public RoleCardDO findRoleCardById(String id) throws MyException;
}
