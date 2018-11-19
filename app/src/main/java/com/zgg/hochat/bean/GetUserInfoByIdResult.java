package com.zgg.hochat.bean;

/**
 * Created by AMing on 16/1/4.
 * Company RongCloud
 */
public class GetUserInfoByIdResult {


    /**
     * id : sdf9sd0df98
     * nickname : Tom
     * portraitUri : http://test.com/user/abc123.jpg
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
