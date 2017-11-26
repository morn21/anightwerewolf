package online.morn.anightwerewolf.service;

import online.morn.anightwerewolf.DO.UserDO;
import online.morn.anightwerewolf.util.MyException;

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
}
