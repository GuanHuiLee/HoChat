package com.zgg.hochat.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.zgg.hochat.api.ApiFactory;
import com.zgg.hochat.base.BaseResult;
import com.zgg.hochat.bean.ContactNotificationMessageData;
import com.zgg.hochat.bean.Friend;
import com.zgg.hochat.bean.GetGroupDetailResult;
import com.zgg.hochat.bean.GetGroupMembersResult;
import com.zgg.hochat.bean.GroupMember;
import com.zgg.hochat.bean.MessageEvent;
import com.zgg.hochat.ui.activity.LoginActivity;
import com.zgg.hochat.ui.activity.NewFriendListActivity;
import com.zgg.hochat.ui.activity.UserDetailActivity;
import com.zgg.hochat.utils.DataUtil;
import com.zgg.hochat.utils.PortraitUtil;
import com.zgg.hochat.widget.pinyin.CharacterParser;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallSession;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.GroupNotificationMessageData;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.GroupNotificationMessage;
import io.rong.message.ImageMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * 融云相关监听 事件集合类
 * Created by AMing on 16/1/7.
 * Company RongCloud
 */
public class AppContext implements RongIM.ConversationListBehaviorListener,
        RongIMClient.OnReceiveMessageListener,
        RongIM.UserInfoProvider,
        RongIM.GroupInfoProvider,
        RongIM.GroupUserInfoProvider,
        RongIM.LocationProvider,
        RongIMClient.ConnectionStatusListener,
        RongIM.ConversationBehaviorListener,
        RongIM.IGroupMembersProvider {

    private static final int CLICK_CONVERSATION_USER_PORTRAIT = 1;


    private final static String TAG = "SealAppContext";
    public static final String UPDATE_FRIEND = "update_friend";
    public static final String UPDATE_RED_DOT = "update_red_dot";
    public static final String UPDATE_GROUP_NAME = "update_group_name";
    public static final String UPDATE_GROUP_MEMBER = "update_group_member";
    public static final String GROUP_DISMISS = "group_dismiss";

    private Context mContext;

    private static AppContext mRongCloudInstance;

    private RongIM.LocationProvider.LocationCallback mLastLocationCallback;

    private static ArrayList<Activity> mActivities;

    public AppContext(Context mContext) {
        this.mContext = mContext;
        initListener();
        mActivities = new ArrayList<>();
    }

    /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {

        if (mRongCloudInstance == null) {
            synchronized (AppContext.class) {

                if (mRongCloudInstance == null) {
                    mRongCloudInstance = new AppContext(context);
                }
            }
        }

    }

    /**
     * 获取RongCloud 实例。
     *
     * @return RongCloud。
     */
    public static AppContext getInstance() {
        return mRongCloudInstance;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * init 后就能设置的监听
     */
    private void initListener() {
        RongIM.setConversationBehaviorListener(this);//设置会话界面操作的监听器。
        RongIM.setConversationListBehaviorListener(this);
        RongIM.setConnectionStatusListener(this);
        RongIM.setUserInfoProvider(this, true);
        RongIM.setGroupInfoProvider(this, true);
        RongIM.setLocationProvider(this);//设置地理位置提供者,不用位置的同学可以注掉此行代码
        setInputProvider();
        //setUserInfoEngineListener();//移到SealUserInfoManager
        setReadReceiptConversationType();
        RongIM.getInstance().enableNewComingMessageIcon(true);
        RongIM.getInstance().enableUnreadMessageIcon(true);
        RongIM.getInstance().setGroupMembersProvider(this);
//        RongIM.setGroupUserInfoProvider(this, true);//seal app暂时未使用这种方式,目前使用UserInfoProvider
    }


    private void setReadReceiptConversationType() {
        Conversation.ConversationType[] types = new Conversation.ConversationType[]{
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP,
                Conversation.ConversationType.DISCUSSION
        };
        RongIM.getInstance().setReadReceiptConversationTypeList(types);
    }

    private void setInputProvider() {
        RongIM.setOnReceiveMessageListener(this);

        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }
            if (defaultModule != null) {
//                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
//                RongExtensionManager.getInstance().registerExtensionModule(new SealExtensionModule());
            }
        }
    }

    @Override
    public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    @Override
    public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    @Override
    public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
        return false;
    }

    @Override
    public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
        MessageContent messageContent = uiConversation.getMessageContent();
        if (messageContent instanceof ContactNotificationMessage) {
            ContactNotificationMessage contactNotificationMessage = (ContactNotificationMessage) messageContent;
            if (contactNotificationMessage.getOperation().equals("AcceptResponse")) {
                // 被加方同意请求后
                if (contactNotificationMessage.getExtra() != null) {
                    ContactNotificationMessageData bean = null;
                    try {
                        bean = new Gson().fromJson(contactNotificationMessage.getExtra(), ContactNotificationMessageData.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    RongIM.getInstance().startPrivateChat(context, uiConversation.getConversationSenderId(), bean.getSourceUserNickname());

                }
            } else {
                context.startActivity(new Intent(context, NewFriendListActivity.class));
            }
            return true;
        }
        return false;
    }

    /**
     * @param message 收到的消息实体。
     * @param i
     * @return
     */
    @Override
    public boolean onReceived(Message message, int i) {
        MessageContent messageContent = message.getContent();
        Logger.d("onReceived msg");
        if (messageContent instanceof ContactNotificationMessage) {
            ContactNotificationMessage contactNotificationMessage = (ContactNotificationMessage) messageContent;
            Logger.d(contactNotificationMessage.getOperation());
            if (contactNotificationMessage.getOperation().equals("Request")) {
                //对方发来好友邀请
                EventBus.getDefault().post(new MessageEvent("new_request"));
            } else if (contactNotificationMessage.getOperation().equals("AcceptResponse")) {
                //对方同意我的好友请求
                ContactNotificationMessageData contactNotificationMessageData;
                try {
                    contactNotificationMessageData = new Gson().fromJson(contactNotificationMessage.getExtra(), ContactNotificationMessageData.class);
                } catch (HttpException e) {
                    e.printStackTrace();
                    return false;
                }
                if (contactNotificationMessageData != null) {
//                    Toast.makeText(getContext(), "好友已接受您的请求", Toast.LENGTH_LONG).show();
                    EventBus.getDefault().post(new MessageEvent(""));
                }
            }
            /*// 发广播通知更新好友列表
            BroadcastManager.getInstance(mContext).sendBroadcast(UPDATE_RED_DOT);
            }*/
        } else if (messageContent instanceof GroupNotificationMessage) {
            GroupNotificationMessage groupNotificationMessage = (GroupNotificationMessage) messageContent;
            Logger.d("onReceived:" + groupNotificationMessage.getMessage());
            String groupID = message.getTargetId();
            GroupNotificationMessageData data = null;
            try {
                String currentID = RongIM.getInstance().getCurrentUserId();
                try {
                    data = new Gson().fromJson(groupNotificationMessage.getData(), GroupNotificationMessageData.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (groupNotificationMessage.getOperation().equals("Create")) {    //创建群组
                    EventBus.getDefault().post(new MessageEvent(""));
                } else if (groupNotificationMessage.getOperation().equals("Dismiss")) {    //解散群组
                    hangUpWhenQuitGroup();      //挂断电话
                    handleGroupDismiss(groupID);
                } else if (groupNotificationMessage.getOperation().equals("Kicked")) {       //群组踢人

                    if (data != null) {
                        List<String> memberIdList = data.getTargetUserIds();
                        if (memberIdList != null) {
                            for (String userId : memberIdList) {
                                if (currentID.equals(userId)) {
                                    hangUpWhenQuitGroup();
                                    RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, message.getTargetId(), new RongIMClient.ResultCallback<Boolean>() {
                                        @Override
                                        public void onSuccess(Boolean aBoolean) {
                                            Log.e("SealAppContext", "Conversation remove successfully.");
                                        }

                                        @Override
                                        public void onError(RongIMClient.ErrorCode e) {

                                        }
                                    });
                                }
                            }
                        }

                        List<String> kickedUserIDs = data.getTargetUserIds();
                    }
                } else if (groupNotificationMessage.getOperation().equals("Add")) {   //群组添加人员

                } else if (groupNotificationMessage.getOperation().equals("Quit")) {     //退出群组

                    if (data != null) {
                        List<String> quitUserIDs = data.getTargetUserIds();
                        if (quitUserIDs.contains(currentID)) {
                            hangUpWhenQuitGroup();
                        }
                    }
                } else if (groupNotificationMessage.getOperation().equals("Rename")) {  //群组重命名

                    if (data != null) {
                        String targetGroupName = data.getTargetGroupName();
                        List<String> groupNameList = new ArrayList<>();
                        groupNameList.add(groupID);
                        groupNameList.add(data.getTargetGroupName());
                        groupNameList.add(data.getOperatorNickname());
                        Group group = new Group(groupID, data.getTargetGroupName(),
                                Uri.parse(PortraitUtil.generateDefaultAvatar(new UserInfo(groupID, targetGroupName, null))));
                        RongIM.getInstance().refreshGroupInfoCache(group);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else if (messageContent instanceof ImageMessage)

        {
            //ImageMessage imageMessage = (ImageMessage) messageContent;
        }
        return false;
    }

    private void handleGroupDismiss(final String groupID) {
        RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP, groupID, new RongIMClient.ResultCallback<Conversation>() {
            @Override
            public void onSuccess(Conversation conversation) {
                RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP, groupID, new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, groupID, null);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode e) {

                    }
                });
            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {

            }
        });
    }

    /**
     * 用户信息提供者的逻辑移到SealUserInfoManager
     * 先从数据库读,没有数据时从网络获取
     */
    @Override
    public UserInfo getUserInfo(String s) {
        //UserInfoEngine.getInstance(mContext).startEngine(s);
        return null;
    }

    @Override
    public Group getGroupInfo(String s) {
        //return GroupInfoEngine.getInstance(mContext).startEngine(s);
        return null;
    }

    @Override
    public GroupUserInfo getGroupUserInfo(String groupId, String userId) {
        //return GroupUserInfoEngine.getInstance(mContext).startEngine(groupId, userId);
        return null;
    }


    @Override
    public void onStartLocation(Context context, LocationCallback locationCallback) {
        /**
         * demo 代码  开发者需替换成自己的代码。
         */
    }

    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        if (conversationType == Conversation.ConversationType.CUSTOMER_SERVICE || conversationType == Conversation.ConversationType.PUBLIC_SERVICE || conversationType == Conversation.ConversationType.APP_PUBLIC_SERVICE) {
            return false;
        }
        //开发测试时,发送系统消息的userInfo只有id不为空
        if (userInfo != null && userInfo.getName() != null && userInfo.getPortraitUri() != null) {
            Intent intent = new Intent(context, UserDetailActivity.class);
            intent.putExtra("conversationType", conversationType.getValue());
            Friend friend = CharacterParser.getInstance().generateFriendFromUserInfo(userInfo);
            intent.putExtra("friend", friend);
            intent.putExtra("type", CLICK_CONVERSATION_USER_PORTRAIT);
            context.startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onMessageClick(final Context context, final View view, final Message message) {
        //real-time location message end
        /**
         * demo 代码  开发者需替换成自己的代码。
         */
        if (message.getContent() instanceof ImageMessage) {
            /*Intent intent = new Intent(context, PhotoActivity.class);
            intent.putExtra("message", message);
            context.startActivity(intent);*/
        }

        return false;
    }


    private void startRealTimeLocation(Context context, Conversation.ConversationType conversationType, String targetId) {

    }

    private void joinRealTimeLocation(Context context, Conversation.ConversationType conversationType, String targetId) {

    }

    @Override
    public boolean onMessageLinkClick(Context context, String s) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        return false;
    }


    public RongIM.LocationProvider.LocationCallback getLastLocationCallback() {
        return mLastLocationCallback;
    }

    public void setLastLocationCallback(RongIM.LocationProvider.LocationCallback lastLocationCallback) {
        this.mLastLocationCallback = lastLocationCallback;
    }

    @Override
    public void onChanged(ConnectionStatus connectionStatus) {
        Logger.d("ConnectionStatus onChanged = " + connectionStatus.getMessage());
        if (connectionStatus.equals(ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
            quit();
        }
    }


    private void quit() {
        DataUtil.setToken("");
        DataUtil.setCookie("");
        RongIM.getInstance().logout();
        Intent loginActivityIntent = new Intent();
        loginActivityIntent.setClass(mContext, LoginActivity.class);
        loginActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(loginActivityIntent);
    }

    @Override
    public void getGroupMembers(final String groupId, final RongIM.IGroupMemberCallback callback) {
        Logger.d("getGroupMembers");
        callback.onGetGroupMembersResult(null);
    }


    private void hangUpWhenQuitGroup() {
        RongCallSession session = RongCallClient.getInstance().getCallSession();
        if (session != null) {
            RongCallClient.getInstance().hangUpCall(session.getCallId());
        }
    }
}
