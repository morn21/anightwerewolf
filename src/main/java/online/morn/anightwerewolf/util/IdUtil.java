package online.morn.anightwerewolf.util;

import java.util.UUID;

/**
 * ID工具
 * @auther Horner 2017/11/20 22:22
 */
public class IdUtil {

    /**
     * 获得ID
     * @auther Horner 2017/11/20 23:19
     * @return
     */
    public static String getId(){
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号
        return uuid.replaceAll("-", "").toUpperCase();
    }

}