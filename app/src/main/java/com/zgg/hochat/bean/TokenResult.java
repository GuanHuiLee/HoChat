package com.zgg.hochat.bean;

public class TokenResult {

    /**
     * code : 200
     * userId : 17685302679
     * token : GBI6xSNnC6RAMz1j4BRfPaq6llhCPIoVbeoNn5uHIEY/Qu1fVSA2DTzPmwZd2FNNwhDEgjsH9Rs7rqrrtfwN74kSXo0tpo9L
     */

    private int code;
    private String userId;
    private String token;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
