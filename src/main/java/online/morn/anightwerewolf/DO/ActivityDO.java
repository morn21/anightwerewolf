package online.morn.anightwerewolf.DO;

import java.sql.Date;

/**
 * 场次表
 * @auther Horner 2017/11/26 11:58
 */
public class ActivityDO {
    private String id;
    private Date createTime;
    private Date updateTime;
    private String roomId;
    private Integer winStatus;

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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Integer getWinStatus() {
        return winStatus;
    }

    public void setWinStatus(Integer winStatus) {
        this.winStatus = winStatus;
    }
}
