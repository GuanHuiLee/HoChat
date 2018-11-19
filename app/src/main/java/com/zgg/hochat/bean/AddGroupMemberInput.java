package com.zgg.hochat.bean;

import java.util.List;

public class AddGroupMemberInput {

    /**
     * groupId : KC6kot3ID
     * memberIds : ["52dzNbLBZ"]
     */

    private String groupId;
    private List<String> memberIds;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
    }

    public AddGroupMemberInput(String groupId, List<String> memberIds) {
        this.groupId = groupId;
        this.memberIds = memberIds;
    }
}
