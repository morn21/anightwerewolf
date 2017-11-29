package online.morn.anightwerewolf.service;

import online.morn.anightwerewolf.DO.RoleDO;
import online.morn.anightwerewolf.util.MyException;

import java.util.List;

/**
 * 角色服务
 * @auther Horner 2017/11/26 1:08
 */
public interface RoleService {

    /**
     * 查询全部角色
     * @auther Horner 2017/11/29 19:36
     * @return
     * @throws MyException
     */
    public List<RoleDO> findRoleList() throws MyException;

    /**
     * 查询角色根据房间ID
     * @auther Horner 2017/11/29 19:36
     * @param roomId
     * @return
     * @throws MyException
     */
    public List<RoleDO> findRoleByRoomId(String roomId) throws MyException;


    /**
     * 查询角色 根据ID
     * @auther Horner 2017/11/29 19:38
     * @param id
     * @return
     * @throws MyException
     */
    public RoleDO findRoleById(String id) throws MyException;

    /**
     * 查询角色 根据当前场次 正在执行技能的
     * @auther Horner 2017/11/30 0:58
     * @param activityId
     * @return
     * @throws MyException
     */
    public RoleDO findRoleByActivityId(String activityId) throws MyException;
}