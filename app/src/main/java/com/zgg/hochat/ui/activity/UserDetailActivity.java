package com.zgg.hochat.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zgg.hochat.App;
import com.zgg.hochat.R;
import com.zgg.hochat.base.BaseToolbarActivity;
import com.zgg.hochat.bean.ActionResult;
import com.zgg.hochat.bean.AgreeInput;
import com.zgg.hochat.bean.AllFriendsResult;
import com.zgg.hochat.bean.Friend;
import com.zgg.hochat.bean.MessageEvent;
import com.zgg.hochat.http.contract.AllFriendsContract;
import com.zgg.hochat.http.contract.FriendRequestContract;
import com.zgg.hochat.http.model.FriendShipModel;
import com.zgg.hochat.http.presenter.AllFriendsPresenter;
import com.zgg.hochat.http.presenter.FriendRequestPresenter;
import com.zgg.hochat.utils.DataUtil;
import com.zgg.hochat.utils.PortraitUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imkit.utilities.PromptPopupDialog;
import io.rong.imlib.model.UserInfo;

//CallKit start 1
//CallKit end 1

/**
 * Created by tiankui on 16/11/2.
 */

public class UserDetailActivity extends BaseToolbarActivity implements View.OnClickListener, AllFriendsContract.View, FriendRequestContract.View {
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
    @BindView(R.id.btn_delete_friend)
    Button btn_delete_friend;

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

    private AllFriendsPresenter presenter;
    private FriendRequestPresenter friendRequestPresenter;

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
        presenter = new AllFriendsPresenter(this, FriendShipModel.newInstance());
        addPresenter(presenter);
        friendRequestPresenter = new FriendRequestPresenter(this, FriendShipModel.newInstance());
        addPresenter(friendRequestPresenter);

        mType = getIntent().getIntExtra("type", 0);
        mGroupName = getIntent().getStringExtra("groupName");
        mFriend = getIntent().getParcelableExtra("friend");

        if (mFriend != null) {
            presenter.getAllFriends();
            if (!mFriend.getUserId().equals(DataUtil.getUserId()) && mType != CLICK_CONVERSATION_USER_PORTRAIT) {
                btn_delete_friend.setVisibility(View.VISIBLE);
            }
            if (mFriend.isExitsDisplayName()) {
                mUserNickName.setVisibility(View.VISIBLE);
                mUserNickName.setText(getString(R.string.ac_contact_nick_name) + "：" + mFriend.getName());
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
        showError("正在开发中，敬请期待！");


//        RongCallSession profile = RongCallClient.getInstance().getCallSession();
//        if (profile != null && profile.getActiveTime() > 0) {
//            Toast.makeText(mContext,
//                    profile.getMediaType() == RongCallCommon.CallMediaType.AUDIO ?
//                            getString(io.rong.callkit.R.string.rc_voip_call_audio_start_fail) :
//                            getString(io.rong.callkit.R.string.rc_voip_call_video_start_fail),
//                    Toast.LENGTH_SHORT)
//                    .show();
//            return;
//        }
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
//        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
//            Toast.makeText(mContext, getString(io.rong.callkit.R.string.rc_voip_call_network_error), Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Intent intent = new Intent(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEAUDIO);
//        intent.putExtra("conversationType", Conversation.ConversationType.PRIVATE.getName().toLowerCase(Locale.US));
//        intent.putExtra("targetId", mFriend.getUserId());
//        intent.putExtra("callAction", RongCallAction.ACTION_OUTGOING_CALL.getName());
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setPackage(getPackageName());
//        getApplicationContext().startActivity(intent);
    }

    public void startVideo(View view) {
        showError("正在开发中，敬请期待！");

//        RongCallSession profile = RongCallClient.getInstance().getCallSession();
//        if (profile != null && profile.getActiveTime() > 0) {
//            Toast.makeText(mContext,
//                    profile.getMediaType() == RongCallCommon.CallMediaType.AUDIO ?
//                            getString(io.rong.callkit.R.string.rc_voip_call_audio_start_fail) :
//                            getString(io.rong.callkit.R.string.rc_voip_call_video_start_fail),
//                    Toast.LENGTH_SHORT)
//                    .show();
//            return;
//        }
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
//        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
//            Toast.makeText(mContext, getString(io.rong.callkit.R.string.rc_voip_call_network_error), Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Intent intent = new Intent(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEVIDEO);
//        intent.putExtra("conversationType", Conversation.ConversationType.PRIVATE.getName().toLowerCase(Locale.US));
//        intent.putExtra("targetId", mFriend.getUserId());
//        intent.putExtra("callAction", RongCallAction.ACTION_OUTGOING_CALL.getName());
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setPackage(getPackageName());
//        getApplicationContext().startActivity(intent);
    }
    //CallKit end 2

    public void setDisplayName(View view) {
        Intent intent = new Intent(mContext, NoteInformationActivity.class);
        intent.putExtra("friend", mFriend);
        startActivityForResult(intent, 99);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contact_phone:
                mPhoneString = mUserPhone.getText().toString();
                if (!TextUtils.isEmpty(mPhoneString)) {
                    Uri telUri = Uri.parse("tel:" + mPhoneString);
                    Intent intent = new Intent(Intent.ACTION_DIAL, telUri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        showError("拨号失败");
                    }
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
        if (resultCode == RESULT_OK) {
            EventBus.getDefault().post(new MessageEvent(""));

            String displayName = data.getStringExtra("displayName");
            if (!TextUtils.isEmpty(displayName)) {
                mUserNickName.setVisibility(View.VISIBLE);
                mUserNickName.setText(getString(R.string.ac_contact_nick_name) + "：" + mFriend.getName());
                mUserDisplayName.setText(displayName);
                mFriend.setDisplayName(displayName);
            } else {
                mUserNickName.setVisibility(View.GONE);
                mUserDisplayName.setText(mFriend.getName());
                mUserDisplayName.setVisibility(View.VISIBLE);
                mFriend.setDisplayName("");
            }
        }
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void initUI() {

    }

    @Override
    public void showAllFriendsResult(List<AllFriendsResult> result) {
        for (AllFriendsResult friendsResult : result) {
            AllFriendsResult.UserBean user = friendsResult.getUser();
            if (user.getId().equals(mFriend.getUserId())) {
                mUserPhone.setText("手机号:" + user.getPhone());
                break;
            }
        }
    }

    @Override
    public void showSetDisplayNameResult(String str) {

    }

    public void deleteFriend(View view) {
        PromptPopupDialog.newInstance(mContext,
                "是否确认删除好友")
                .setLayoutRes(io.rong.imkit.R.layout.rc_dialog_popup_prompt)
                .setPromptButtonClickedListener(new PromptPopupDialog.OnPromptButtonClickedListener() {
                    @Override
                    public void onPositiveButtonClicked() {
                        showProgress("删除中");
                        friendRequestPresenter.deleteFriend(new AgreeInput(mFriend.getUserId()));
                    }
                }).show();

    }

    @Override
    public void showInviteResult(ActionResult result) {

    }

    @Override
    public void showAgreeResult(ActionResult result) {

    }

    @Override
    public void showDeleteFriendResult(String string) {
        showError(string);
        finish();
        EventBus.getDefault().post(new MessageEvent(""));
    }
}
