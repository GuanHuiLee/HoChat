package com.zgg.hochat.api;

import com.zgg.hochat.bean.TokenResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @POST("user/getToken.json")
    @FormUrlEncoded
    Call<TokenResult> getToken(@FieldMap Map<String, Object> params);
}
