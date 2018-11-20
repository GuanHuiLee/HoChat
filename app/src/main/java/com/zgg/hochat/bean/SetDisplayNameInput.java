package com.zgg.hochat.bean;

public class SetDisplayNameInput {


    /**
     * friendId : RfqHbcjes
     * displayName : 备注
     */

    private String friendId;
    private String displayName;

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public SetDisplayNameInput(String friendId, String displayName) {
        this.friendId = friendId;
        this.displayName = displayName;
    }
}
