package online.morn.anightwerewolf.DO;

import java.sql.Date;

/**
 * 房间表
 * @auther Horner 2017/11/19 9:00
 */
public class RoomDO {
    private String id;
    private Date createTime;
    private String name;
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
