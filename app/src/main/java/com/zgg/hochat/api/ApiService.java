package com.zgg.hochat.api;

import com.zgg.hochat.base.BaseResult;
import com.zgg.hochat.bean.AgreeInput;
import com.zgg.hochat.bean.FindUserResult;
import com.zgg.hochat.bean.AllFriendsResult;
import com.zgg.hochat.bean.InviteInput;
import com.zgg.hochat.bean.ActionResult;
import com.zgg.hochat.bean.LoginInput;
import com.zgg.hochat.bean.LoginResult;
import com.zgg.hochat.bean.RegisterInput;
import com.zgg.hochat.bean.RegisterResult;
import com.zgg.hochat.bean.RegisterZggInput;
import com.zgg.hochat.bean.TokenResult;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiService {
    /**
     * 获取融云聊天 token
     *
     * @param url
     * @param params
     * @return
     */
    @POST()
    @FormUrlEncoded
    Call<TokenResult> getToken(@Url String url, @FieldMap Map<String, Object> params);

    /**
     * 登录
     *
     * @param input
     * @return
     */
    @POST("user/login")
    Call<BaseResult<LoginResult>> login(@Body LoginInput input);

    /**
     * 注册
     *
     * @param input
     * @return
     */
    @POST("user/register")
    Call<BaseResult<RegisterResult>> register(@Body RegisterInput input);

    /**
     * 注册智呱呱系统
     *
     * @param url
     * @param input
     * @return
     */
    @POST()
    Call<BaseResult<RegisterResult>> registerZgg(@Url String url, @Body RegisterZggInput input);

    /**
     * 查找用户
     *
     * @param region
     * @param phone
     * @return
     */
    @GET("user/find/{region}/{phone}")
    Call<BaseResult<FindUserResult>> findUser(@Path("region") String region, @Path("phone") String phone);

    /**
     * 添加好友
     *
     * @param inviteInput
     * @return
     */
    @POST("friendship/invite")
    Call<BaseResult<ActionResult>> invite(@Body InviteInput inviteInput);

    /**
     * 同意申请
     *
     * @param inviteInput
     * @return
     */
    @POST("friendship/agree")
    Call<BaseResult<ActionResult>> agree(@Body AgreeInput inviteInput);

    /**
     * 获取好友列表
     *
     * @return
     */
    @GET("friendship/all")
    Call<BaseResult<List<AllFriendsResult>>> getAllFriends();
}
