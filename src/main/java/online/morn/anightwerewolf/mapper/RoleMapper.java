package online.morn.anightwerewolf.mapper;

import online.morn.anightwerewolf.DO.RoleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色表
 * @auther Horner 2017/11/29 19:27
 */
public interface RoleMapper {

    /**
     * 查询全部角色
     * @auther Horner 2017/11/19 4:56
     * @return
     */
    public List<RoleDO> selectRoleList();

    /**
     * 查询角色列表 根据ID列表
     * @auther Horner 2017/11/26 11:53
     * @param roleIdList
     * @return
     */
    public List<RoleDO> selectRoleListByIdList(@Param("roleIdList") List<String> roleIdList);
}
