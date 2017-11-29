package online.morn.anightwerewolf.DO;

/**
 * 房间角色牌表
 * @auther Horner 2017/11/26 0:28
 */
public class RoomRoleCardDO {
    private String id;
    private String roomId;
    private String roleCardId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoleCardId() {
        return roleCardId;
    }

    public void setRoleCardId(String roleCardId) {
        this.roleCardId = roleCardId;
    }
}
