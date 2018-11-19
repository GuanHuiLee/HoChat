package com.zgg.hochat.bean;

public class GetGroupMembersResult {


    /**
     * displayName :
     * role : 1
     * createdAt : 2016-11-22T03:06:13.000Z
     * updatedAt : 2016-11-22T03:06:13.000Z
     * user : {"id":"xNlpDTUmw","nickname":"zl01","portraitUri":""}
     */

    private String displayName;
    private int role;
    private String createdAt;
    private String updatedAt;
    private UserBean user;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * id : xNlpDTUmw
         * nickname : zl01
         * portraitUri :
         */

        private String id;
        private String nickname;
        private String portraitUri;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPortraitUri() {
            return portraitUri;
        }

        public void setPortraitUri(String portraitUri) {
            this.portraitUri = portraitUri;
        }
    }
}
