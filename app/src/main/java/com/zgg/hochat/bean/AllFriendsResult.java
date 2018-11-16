package com.zgg.hochat.bean;

public class AllFriendsResult {

    /**
     * displayName :
     * message :
     * status : 10
     * updatedAt : 2018-11-16T01:34:46.000Z
     * user : {"id":"kFpN4KiZn","nickname":"15519138713","region":"86","phone":"15519138713","portraitUri":""}
     */

    private String displayName;
    private String message;
    private int status;
    private String updatedAt;
    private UserBean user;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
         * id : kFpN4KiZn
         * nickname : 15519138713
         * region : 86
         * phone : 15519138713
         * portraitUri :
         */

        private String id;
        private String nickname;
        private String region;
        private String phone;
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

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPortraitUri() {
            return portraitUri;
        }

        public void setPortraitUri(String portraitUri) {
            this.portraitUri = portraitUri;
        }
    }
}
