package com.zgg.hochat.bean;

import java.util.List;

public class CreateCroupInput {

    /**
     * name : RongCloud
     * memberIds : ["AUj8X32w1","ODbpJIgrL"]
     */

    private String name;
    private List<String> memberIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
    }

    public CreateCroupInput(String name, List<String> memberIds) {
        this.name = name;
        this.memberIds = memberIds;
    }
}
