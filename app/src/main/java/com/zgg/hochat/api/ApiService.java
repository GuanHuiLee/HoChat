package com.zgg.hochat.api;

import com.zgg.hochat.base.BaseResult;
import com.zgg.hochat.bean.FindUserResult;
import com.zgg.hochat.bean.InviteInput;
import com.zgg.hochat.bean.InviteResult;
import com.zgg.hochat.bean.LoginInput;
import com.zgg.hochat.bean.LoginResult;
import com.zgg.hochat.bean.RegisterInput;
import com.zgg.hochat.bean.RegisterResult;
import com.zgg.hochat.bean.TokenResult;

import java.util.Map;

import io.rong.imlib.model.UserInfo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiService {
    @POST()
    @FormUrlEncoded
    Call<TokenResult> getToken(@Url String url, @FieldMap Map<String, Object> params);

    @POST("user/login")
    Call<BaseResult<LoginResult>> login(@Body LoginInput input);

    @POST("user/register")
    Call<BaseResult<RegisterResult>> register(@Body RegisterInput input);

    @GET("user/find/{region}/{phone}")
    Call<BaseResult<FindUserResult>> findUser(@Path("region") String region, @Path("phone") String phone);

    @POST("friendship/invite")
    Call<BaseResult<InviteResult>> invite(@Body InviteInput inviteInput);
}
