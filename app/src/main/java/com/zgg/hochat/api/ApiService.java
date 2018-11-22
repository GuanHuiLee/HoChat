package com.zgg.hochat.api;

import com.zgg.hochat.base.BaseResult;
import com.zgg.hochat.bean.AddGroupMemberInput;
import com.zgg.hochat.bean.AgreeInput;
import com.zgg.hochat.bean.CreateCroupInput;
import com.zgg.hochat.bean.CreateGroupResult;
import com.zgg.hochat.bean.FindUserResult;
import com.zgg.hochat.bean.AllFriendsResult;
import com.zgg.hochat.bean.GetGroupDetailResult;
import com.zgg.hochat.bean.GetGroupMembersResult;
import com.zgg.hochat.bean.GetGroupsResult;
import com.zgg.hochat.bean.GetUserInfoByIdResult;
import com.zgg.hochat.bean.InviteInput;
import com.zgg.hochat.bean.ActionResult;
import com.zgg.hochat.bean.LoginInput;
import com.zgg.hochat.bean.LoginResult;
import com.zgg.hochat.bean.NickNameInput;
import com.zgg.hochat.bean.QuitGroupInput;
import com.zgg.hochat.bean.RegisterInput;
import com.zgg.hochat.bean.RegisterResult;
import com.zgg.hochat.bean.RegisterZggInput;
import com.zgg.hochat.bean.SetDisplayNameInput;
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
     * 删除好友
     */
    @POST("friendship/delete")
    Call<BaseResult<String>> deleteFriend(@Body AgreeInput input);

    /**
     * 获取好友列表
     *
     * @return
     */
    @GET("friendship/all")
    Call<BaseResult<List<AllFriendsResult>>> getAllFriends();

    /**
     * 设置备注名称
     */
    @POST("friendship/set_display_name")
    Call<BaseResult<String>> setDisplayName(@Body SetDisplayNameInput input);

    /**
     * 创建群组
     *
     * @return
     */
    @POST("group/create")
    Call<BaseResult<CreateGroupResult>> createCroup(@Body CreateCroupInput input);

    /**
     * 获取所有群
     *
     * @return
     */
    @GET("user/groups")
    Call<BaseResult<List<GetGroupsResult>>> getGroups();

    /**
     * 添加群员
     *
     * @return
     */
    @POST("group/add")
    Call<BaseResult<String>> addGroupMembers(@Body AddGroupMemberInput input);

    /**
     * 删除群员
     *
     * @return
     */
    @POST("group/kick")
    Call<BaseResult<String>> kickGroupMembers(@Body AddGroupMemberInput input);


    /**
     * 退群
     *
     * @return
     */
    @POST("group/quit")
    Call<BaseResult<String>> quitGroup(@Body QuitGroupInput input);

    /**
     * 解散群组
     *
     * @return
     */
    @POST("group/dismiss")
    Call<BaseResult<String>> dismissGroup(@Body QuitGroupInput input);


    /**
     * 获取群成员
     *
     * @return
     */
    @GET("group/{id}/members")
    Call<BaseResult<List<GetGroupMembersResult>>> getGroupMembers(@Path("id") String id);

    /**
     * 获取群信息
     */
    @GET("group/{id}")
    Call<BaseResult<GetGroupDetailResult>> getGroupDetail(@Path("id") String id);


    /**
     * 修改昵称
     */
    @POST("user/set_nickname")
    Call<BaseResult<String>> setNickName(@Body NickNameInput input);

    /**
     * 获取用户信息
     */
    @GET("user/{id}")
    Call<BaseResult<GetUserInfoByIdResult>> getUserInfoById(@Path("id") String id);

}
