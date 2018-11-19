package com.zgg.hochat.bean;

public class GetGroupsResult {


    /**
     * role : 0
     * group : {"id":"pG4lQsHkY","name":"我的群","portraitUri":"","creatorId":"7w0UxC8IB","memberCount":7}
     */

    private int role;
    private GroupBean group;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public GroupBean getGroup() {
        return group;
    }

    public void setGroup(GroupBean group) {
        this.group = group;
    }

    public static class GroupBean {
        /**
         * id : pG4lQsHkY
         * name : 我的群
         * portraitUri :
         * creatorId : 7w0UxC8IB
         * memberCount : 7
         */

        private String id;
        private String name;
        private String portraitUri;
        private String creatorId;
        private int memberCount;

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

        public String getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(String creatorId) {
            this.creatorId = creatorId;
        }

        public int getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(int memberCount) {
            this.memberCount = memberCount;
        }
    }
}
