package com.zgg.hochat.utils;

import com.tencent.mmkv.MMKV;
import com.zgg.hochat.bean.RegisterInput;

public class DataUtil {
    private static MMKV mmkv;

    public static MMKV getMmkv() {
        if (mmkv != null)
            return mmkv;
        else return mmkv = MMKV.defaultMMKV();
    }

    public static String getToken() {
        String token = getMmkv().decodeString(Constant.TOKEN, "");
        return token;
    }

    public static void setToken(String token) {
        getMmkv().encode(Constant.TOKEN, token);
    }

    public static RegisterInput getUser() {
        String userName = getMmkv().decodeString(Constant.USER_NAME, "");
        String pwd = getMmkv().decodeString(Constant.USER_PWD, "");
        return new RegisterInput(userName, pwd);
    }


    public static void setUser(String name, String pwd) {
        getMmkv().encode(Constant.USER_NAME, name);
        getMmkv().encode(Constant.USER_PWD, pwd);
    }

    public static void setUserId(String id) {
        getMmkv().encode(Constant.USER_ID, id);
    }

    public static String getUserId() {
        return getMmkv().decodeString(Constant.USER_ID);
    }

    public static void setUserPortrait(String id) {
        getMmkv().encode(Constant.USER_PORTRAIT, id);
    }

    public static String getUserPortrait() {
        return getMmkv().decodeString(Constant.USER_PORTRAIT);
    }

    public static void setCookie(String id) {
        getMmkv().encode(Constant.COOKIE, id);
    }

    public static String getCookie() {
        return getMmkv().decodeString(Constant.COOKIE);
    }

    public static void setNickName(String id) {
        getMmkv().encode(Constant.NIKE_NAME, id);
    }

    public static String getNickName() {
        return getMmkv().decodeString(Constant.NIKE_NAME);
    }
}
