package online.morn.anightwerewolf.mapper;

import online.morn.anightwerewolf.DO.UserDO;
import org.apache.ibatis.annotations.Param;

/**
 * 用户表
 * @auther Horner 2017/11/26 0:23
 */
public interface UserMapper {

    /**
     * 添加用户
     * @auther Horner 2017/11/26 0:39
     * @param userDO
     * @return
     */
    public Integer insert(@Param("userDO") UserDO userDO);

    /**
     * 查询最大用户名的值
     * @auther Horner 2017/11/26 0:39
     * @return
     */
    public Integer selectMaxNameValue();

    /**
     * 查询用户根据ID
     * @auther Horner 2017/11/26 0:44
     * @param id
     * @return
     */
    public UserDO selectUserById(@Param("id") String id);
}
