package com.zgg.hochat.bean;

public class GetGroupDetailResult {

    /**
     * id : KC6kot3ID
     * name : RongCloud
     * portraitUri :
     * memberCount : 13
     * maxMemberCount : 500
     * creatorId : I8cpNlo7t
     * type : 1
     * bulletin : null
     * deletedAt : null
     */

    private String id;
    private String name;
    private String portraitUri;
    private int memberCount;
    private int maxMemberCount;
    private String creatorId;
    private int type;
    private Object bulletin;
    private Object deletedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public int getMaxMemberCount() {
        return maxMemberCount;
    }

    public void setMaxMemberCount(int maxMemberCount) {
        this.maxMemberCount = maxMemberCount;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getBulletin() {
        return bulletin;
    }

    public void setBulletin(Object bulletin) {
        this.bulletin = bulletin;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }
}
