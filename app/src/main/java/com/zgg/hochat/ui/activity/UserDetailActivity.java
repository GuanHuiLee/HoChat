package com.zgg.hochat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zgg.hochat.App;
import com.zgg.hochat.R;
import com.zgg.hochat.base.BaseActivity;
import com.zgg.hochat.base.BaseToolbarActivity;
import com.zgg.hochat.bean.Friend;
import com.zgg.hochat.utils.DataUtil;
import com.zgg.hochat.utils.PortraitUtil;

import java.util.Locale;

import butterknife.BindView;
import io.rong.callkit.RongCallAction;
import io.rong.callkit.RongVoIPIntent;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

//CallKit start 1
//CallKit end 1

/**
 * Created by tiankui on 16/11/2.
 */

public class UserDetailActivity extends BaseToolbarActivity implements View.OnClickListener {
    @BindView(R.id.contact_top)
    TextView mUserDisplayName;
    @BindView(R.id.contact_below)
    TextView mUserNickName;
    @BindView(R.id.contact_phone)
    TextView mUserPhone;
    @BindView(R.id.ac_bt_add_friend)
    Button mAddFriendButton;
    @BindView(R.id.ac_ll_chat_button_group)
    LinearLayout mChatButtonGroupLinearLayout;
    @BindView(R.id.user_online_status)
    TextView mUserLineStatus;
    @BindView(R.id.ac_iv_user_portrait)
    ImageView mUserPortrait;
    @BindView(R.id.ac_ll_note_name)
    LinearLayout mNoteNameLinearLayout;

    private static final int SYNC_FRIEND_INFO = 129;

    private static final int ADD_FRIEND = 10086;
    private static final int SYN_USER_INFO = 10087;
    private Friend mFriend;
    private String addMessage;
    private String mGroupName;
    private String mPhoneString;
    private boolean mIsFriendsRelationship;

    private int mType;
    private static final int CLICK_CONVERSATION_USER_PORTRAIT = 1;
    private static final int CLICK_CONTACT_FRAGMENT_FRIEND = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        initView();
    }

    private void initView() {
        setTitle(R.string.user_details);

        mAddFriendButton.setOnClickListener(this);
        mUserPhone.setOnClickListener(this);
    }

    protected void initData() {
        mType = getIntent().getIntExtra("type", 0);
        mGroupName = getIntent().getStringExtra("groupName");
        mFriend = getIntent().getParcelableExtra("friend");

        if (mFriend != null) {
            if (mFriend.isExitsDisplayName()) {
                mUserNickName.setVisibility(View.VISIBLE);
                mUserNickName.setText(getString(R.string.ac_contact_nick_name) + " " + mFriend.getName());
                mUserDisplayName.setText(mFriend.getDisplayName());
            } else {
                mUserDisplayName.setText(mFriend.getName());
            }
            UserInfo userInfo = new UserInfo(mFriend.getUserId(), mFriend.getName(), null);
            ImageLoader.getInstance().displayImage(PortraitUtil.generateDefaultAvatar(userInfo), mUserPortrait, App.getOptions());
        }
        mIsFriendsRelationship = true;

        if (!TextUtils.isEmpty(mFriend.getUserId())) {
            String mySelf = DataUtil.getUserId();
            if (mySelf.equals(mFriend.getUserId())) {
                mChatButtonGroupLinearLayout.setVisibility(View.VISIBLE);
                mAddFriendButton.setVisibility(View.GONE);
                return;
            }
            if (mIsFriendsRelationship) {
                mChatButtonGroupLinearLayout.setVisibility(View.VISIBLE);
                mAddFriendButton.setVisibility(View.GONE);
            } else {
                mAddFriendButton.setVisibility(View.VISIBLE);
                mChatButtonGroupLinearLayout.setVisibility(View.GONE);
                mNoteNameLinearLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {

    }


    public void startChat(View view) {
        String displayName = mFriend.getDisplayName();
        if (!TextUtils.isEmpty(displayName)) {
            RongIM.getInstance().startPrivateChat(mContext, mFriend.getUserId(), displayName);
        } else {
            RongIM.getInstance().startPrivateChat(mContext, mFriend.getUserId(), mFriend.getName());
        }
        finish();
    }

    //CallKit start 2
    public void startVoice(View view) {
        RongCallSession profile = RongCallClient.getInstance().getCallSession();
        if (profile != null && profile.getActiveTime() > 0) {
            Toast.makeText(mContext,
                    profile.getMediaType() == RongCallCommon.CallMediaType.AUDIO ?
                            getString(io.rong.callkit.R.string.rc_voip_call_audio_start_fail) :
                            getString(io.rong.callkit.R.string.rc_voip_call_video_start_fail),
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            Toast.makeText(mContext, getString(io.rong.callkit.R.string.rc_voip_call_network_error), Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEAUDIO);
        intent.putExtra("conversationType", Conversation.ConversationType.PRIVATE.getName().toLowerCase(Locale.US));
        intent.putExtra("targetId", mFriend.getUserId());
        intent.putExtra("callAction", RongCallAction.ACTION_OUTGOING_CALL.getName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage(getPackageName());
        getApplicationContext().startActivity(intent);
    }

    public void startVideo(View view) {
        RongCallSession profile = RongCallClient.getInstance().getCallSession();
        if (profile != null && profile.getActiveTime() > 0) {
            Toast.makeText(mContext,
                    profile.getMediaType() == RongCallCommon.CallMediaType.AUDIO ?
                            getString(io.rong.callkit.R.string.rc_voip_call_audio_start_fail) :
                            getString(io.rong.callkit.R.string.rc_voip_call_video_start_fail),
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
            Toast.makeText(mContext, getString(io.rong.callkit.R.string.rc_voip_call_network_error), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEVIDEO);
        intent.putExtra("conversationType", Conversation.ConversationType.PRIVATE.getName().toLowerCase(Locale.US));
        intent.putExtra("targetId", mFriend.getUserId());
        intent.putExtra("callAction", RongCallAction.ACTION_OUTGOING_CALL.getName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage(getPackageName());
        getApplicationContext().startActivity(intent);
    }
    //CallKit end 2


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contact_phone:
                if (!TextUtils.isEmpty(mPhoneString)) {
                    Uri telUri = Uri.parse("tel:" + mPhoneString);
                    Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;
            case R.id.ac_bt_add_friend:
                Intent intent = new Intent(mContext, VerifyUserActivity.class);
                intent.putExtra("userId", mFriend.getUserId());
                startActivity(intent);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void initUI() {

    }
}
