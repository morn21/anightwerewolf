package online.morn.anightwerewolf.DO;

import java.sql.Date;

/**
 * 用户表
 * @auther Horner 2017/11/26 0:23
 */
public class UserDO {
    private String id;
    private Date createTime;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
