package online.morn.anightwerewolf.mapper;

import online.morn.anightwerewolf.DO.UserinfoDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户信息
 */
public interface UserinfoMapper {

    /**
     * 根据条件查列表
     * @param startRow
     * @param length
     * @param userinfoDO
     * @return
     */
    public List<UserinfoDO> selectListByCondition(@Param("startRow") Integer startRow, @Param("length") Integer length, @Param("userinfoDO") UserinfoDO userinfoDO);

    /**
     * 根据条件查数量
     * @param userinfoDO
     * @return
     */
    public Integer selectCountByCondition(@Param("userinfoDO") UserinfoDO userinfoDO);
}
