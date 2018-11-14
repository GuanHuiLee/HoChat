package com.zgg.hochat.bean;

public class LoginInput {
    private String region;
    private String phone;
    private String password;

    public LoginInput(String region, String phone, String password) {
        this.region = region;
        this.phone = phone;
        this.password = password;
    }
}
