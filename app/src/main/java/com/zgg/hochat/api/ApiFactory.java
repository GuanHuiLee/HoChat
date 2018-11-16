package com.zgg.hochat.api;


import android.content.Context;
import android.content.SharedPreferences;

import com.zgg.hochat.utils.Constant;
import com.zgg.hochat.utils.DataUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by niujingtong on 2018/7/6.
 * 模块：
 */
public class ApiFactory {
    private static ApiService apiService;

    private static final OkHttpClient mClient;

    private static final Retrofit mRetrofit;

    static {
        mClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor())
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = new ArrayList<>();
                        String token = DataUtil.getCookie();

                        if (token != null) {
                            Cookie cookie = Cookie.parse(url, token);
                            cookies.add(cookie);
                        }

                        return cookies;

                    }
                })
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_IP)
                .client(mClient)//设置读写连接超时
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiService getService() {
        if (apiService == null)
            apiService = mRetrofit.create(ApiService.class);
        return apiService;
    }
}