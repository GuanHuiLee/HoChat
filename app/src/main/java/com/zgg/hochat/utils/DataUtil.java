package com.zgg.hochat.utils;

import com.tencent.mmkv.MMKV;

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

    public static String getUser() {
        String userName = getMmkv().decodeString(Constant.USER_NAME, "");
        return userName;
    }


    public static void setUser(String name) {
        getMmkv().encode(Constant.USER_NAME, name);
    }
}
