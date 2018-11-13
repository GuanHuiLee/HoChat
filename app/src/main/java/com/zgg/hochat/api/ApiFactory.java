package com.zgg.hochat.api;


import com.zgg.hochat.utils.Constant;

import java.util.concurrent.TimeUnit;

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