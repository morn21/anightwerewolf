package online.morn.anightwerewolf.service;

import online.morn.anightwerewolf.DO.UserDO;
import online.morn.anightwerewolf.util.MyException;
import org.apache.ibatis.annotations.Param;

/**
 * 用户服务
 * @auther Horner 2017/11/26 15:27
 */
public interface UserService {

    /**
     * 生成用户
     * @auther Horner 2017/11/26 15:25
     * @return
     * @throws MyException
     */
    public UserDO generateUser() throws MyException;

    /**
     * 查询用户根据ID
     * @auther Horner 2017/11/27 20:52
     * @param id
     * @return
     * @throws MyException
     */
    public UserDO findUserById(String id) throws MyException;
}
