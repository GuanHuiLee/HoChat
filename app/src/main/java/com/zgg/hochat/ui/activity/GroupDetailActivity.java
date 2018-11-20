package com.zgg.hochat.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zgg.hochat.App;
import com.zgg.hochat.R;
import com.zgg.hochat.base.BaseToolbarActivity;
import com.zgg.hochat.bean.AllFriendsResult;
import com.zgg.hochat.bean.CreateGroupResult;
import com.zgg.hochat.bean.Friend;
import com.zgg.hochat.bean.GetGroupDetailResult;
import com.zgg.hochat.bean.GetGroupMembersResult;
import com.zgg.hochat.bean.GetGroupsResult;
import com.zgg.hochat.bean.GroupMember;
import com.zgg.hochat.bean.Groups;
import com.zgg.hochat.bean.QuitGroupInput;
import com.zgg.hochat.http.contract.GroupMemberContract;
import com.zgg.hochat.http.contract.GroupContract;
import com.zgg.hochat.http.model.GroupModel;
import com.zgg.hochat.http.presenter.GroupMemberPresenter;
import com.zgg.hochat.http.presenter.GroupPresenter;
import com.zgg.hochat.utils.DataUtil;
import com.zgg.hochat.utils.OperationRong;
import com.zgg.hochat.utils.PortraitUtil;
import com.zgg.hochat.widget.MyGridView;
import com.zgg.hochat.widget.SelectableRoundedImageView;
import com.zgg.hochat.widget.pinyin.CharacterParser;
import com.zgg.hochat.widget.switchbutton.SwitchButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.AndroidEmoji;
import io.rong.imkit.utilities.PromptPopupDialog;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * Created by AMing on 16/1/27.
 * Company RongCloud
 */
public class GroupDetailActivity extends BaseToolbarActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, GroupMemberContract.View, GroupContract.View {

    private static final int CLICK_CONVERSATION_USER_PORTRAIT = 1;

    private static final int DISMISS_GROUP = 26;
    private static final int QUIT_GROUP = 27;
    private static final int SET_GROUP_NAME = 29;
    private static final int GET_GROUP_INFO = 30;
    private static final int UPDATE_GROUP_NAME = 32;
    private static final int GET_QI_NIU_TOKEN = 133;
    private static final int UPDATE_GROUP_HEADER = 25;
    private static final int SEARCH_TYPE_FLAG = 1;
    private static final int CHECKGROUPURL = 39;


    private boolean isCreated = false;
    private MyGridView mGridView;
    private List<GroupMember> mGroupMember;
    private TextView mTextViewMemberSize, mGroupDisplayNameText;
    private SelectableRoundedImageView mGroupHeader;
    private SwitchButton messageTop, messageNotification;
    private String fromConversationId;
    private Conversation.ConversationType mConversationType;
    private boolean isFromConversation;
    private LinearLayout mGroupAnnouncementDividerLinearLayout;
    private TextView mGroupName;
    private String imageUrl;
    private Uri selectUri;
    private String newGroupName;
    private LinearLayout mGroupNotice;
    private LinearLayout mSearchMessagesLinearLayout;
    private Button mDismissBtn;
    private Button mQuitBtn;

    private GroupMemberPresenter groupMemberPresenter;
    private GroupPresenter groupsPresenter;
    private GetGroupDetailResult groupDetail;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_group);
        initViews();

        //群组会话界面点进群组详情
        fromConversationId = getIntent().getStringExtra("TargetId");
        mConversationType = (Conversation.ConversationType) getIntent().getSerializableExtra("conversationType");

        if (!TextUtils.isEmpty(fromConversationId)) {
            isFromConversation = true;
        }

        if (isFromConversation) {//群组会话页进入
            groupsPresenter.getGroupDetail(fromConversationId);

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
    protected void initData() {
        groupMemberPresenter = new GroupMemberPresenter(this, GroupModel.newInstance());
        groupsPresenter = new GroupPresenter(this, GroupModel.newInstance());

        addPresenter(groupsPresenter);
        addPresenter(groupMemberPresenter);

    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
        toolbar.setTitle(R.string.group_info);
    }

    private void initGroupData() {
        ImageLoader.getInstance().displayImage(null, mGroupHeader, App.getOptions());
        mGroupName.setText(groupDetail.getName());

        if (RongIM.getInstance() != null) {
            RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP, groupDetail.getId(), new RongIMClient.ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {
                    if (conversation == null) {
                        return;
                    }
                    if (conversation.isTop()) {
                        messageTop.setChecked(true);
                    } else {
                        messageTop.setChecked(false);
                    }

                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });

            RongIM.getInstance().getConversationNotificationStatus(Conversation.ConversationType.GROUP,
                    groupDetail.getId(), new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                        @Override
                        public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {

                            if (conversationNotificationStatus == Conversation.ConversationNotificationStatus.DO_NOT_DISTURB) {
                                messageNotification.setChecked(true);
                            } else {
                                messageNotification.setChecked(false);
                            }
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
        }

        if (groupDetail.getCreatorId().equals(DataUtil.getUserId()))//创建者
            isCreated = true;
        if (!isCreated) {
            mGroupAnnouncementDividerLinearLayout.setVisibility(View.VISIBLE);
            mGroupNotice.setVisibility(View.VISIBLE);
        } else {
            mGroupAnnouncementDividerLinearLayout.setVisibility(View.VISIBLE);
            mDismissBtn.setVisibility(View.VISIBLE);
            mQuitBtn.setVisibility(View.GONE);
            mGroupNotice.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_quit:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("是否确认退出群组")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                groupsPresenter.quitGroup(new QuitGroupInput(fromConversationId));
                            }
                        }).show();
                break;
            case R.id.group_dismiss:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("是否确认解散群组")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                groupsPresenter.dismissGroup(new QuitGroupInput(fromConversationId));
                            }
                        }).show();
                break;
            case R.id.ac_ll_search_chatting_records:

                break;
            case R.id.group_clean:
                PromptPopupDialog.newInstance(mContext,
                        getString(R.string.clean_group_chat_history)).setLayoutRes(io.rong.imkit.R.layout.rc_dialog_popup_prompt_warning)
                        .setPromptButtonClickedListener(new PromptPopupDialog.OnPromptButtonClickedListener() {
                            @Override
                            public void onPositiveButtonClicked() {
                                if (RongIM.getInstance() != null) {
                                    if (groupDetail != null) {
                                        RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP,
                                                groupDetail.getId(), new RongIMClient.ResultCallback<Boolean>() {
                                                    @Override
                                                    public void onSuccess(Boolean aBoolean) {
                                                        showError(getString(R.string.clear_success));
                                                    }

                                                    @Override
                                                    public void onError(RongIMClient.ErrorCode errorCode) {
                                                        showError(getString(R.string.clear_failure));
                                                    }
                                                });
                                        RongIMClient.getInstance().cleanRemoteHistoryMessages(Conversation.ConversationType.GROUP,
                                                groupDetail.getId(), System.currentTimeMillis(), null);
                                    }
                                }
                            }
                        }).show();

                break;
            case R.id.group_member_size_item:
//                Intent intent = new Intent(mContext, TotalGroupMemberActivity.class);
//                intent.putExtra("targetId", fromConversationId);
//                startActivity(intent);
                break;
            case R.id.group_member_online_status:
//                intent = new Intent(mContext, MembersOnlineStatusActivity.class);
//                intent.putExtra("targetId", fromConversationId);
//                startActivity(intent);
                break;
            case R.id.ll_group_port:
                break;
            case R.id.ll_group_name:
                if (isCreated) {

                }
                break;
            case R.id.group_announcement:
//                Intent tempIntent = new Intent(mContext, GroupNoticeActivity.class);
//                tempIntent.putExtra("conversationType", Conversation.ConversationType.GROUP.getValue());
//                tempIntent.putExtra("targetId", fromConversationId);
//                startActivity(tempIntent);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sw_group_top:
                if (isChecked) {
                    if (groupDetail != null) {
                        OperationRong.setConversationTop(mContext, Conversation.ConversationType.GROUP, groupDetail.getId(), true);
                    }
                } else {
                    if (groupDetail != null) {
                        OperationRong.setConversationTop(mContext, Conversation.ConversationType.GROUP, groupDetail.getId(), false);
                    }
                }
                break;
            case R.id.sw_group_notfaction:
                if (isChecked) {
                    if (groupDetail != null) {
                        OperationRong.setConverstionNotif(mContext, Conversation.ConversationType.GROUP, groupDetail.getId(), true);
                    }
                } else {
                    if (groupDetail != null) {
                        OperationRong.setConverstionNotif(mContext, Conversation.ConversationType.GROUP, groupDetail.getId(), false);
                    }
                }

                break;
        }
    }

    @Override
    public void showGetGroupMembersResult(List<GetGroupMembersResult> list) {
        if (list != null && list.size() > 0) {
            mGroupMember = addGroupMembers(list);
            initGroupMemberData();
        }

    }

    private void initGroupMemberData() {
        if (mGroupMember != null && mGroupMember.size() > 0) {
            toolbar.setTitle(getString(R.string.group_info) + "(" + mGroupMember.size() + ")");
            mTextViewMemberSize.setText(getString(R.string.group_member_size) + "(" + mGroupMember.size() + ")");
            mGridView.setAdapter(new GridAdapter(mContext, mGroupMember));
        } else {
            return;
        }

        for (GroupMember member : mGroupMember) {
            if (member.getUserId().equals(DataUtil.getUserId())) {
                if (!TextUtils.isEmpty(member.getDisplayName())) {
                    mGroupDisplayNameText.setText(member.getDisplayName());
                } else {
                    mGroupDisplayNameText.setText("无");
                }
            }
        }
    }


    private List<GroupMember> addGroupMembers(List<GetGroupMembersResult> groupMember) {
        List<GroupMember> newList = new ArrayList<>();
        GroupMember created = null;
        for (GetGroupMembersResult group : groupMember) {

            GroupMember newMember = new GroupMember(groupDetail.getId(),
                    group.getUser().getId(),
                    group.getUser().getNickname(),
                    Uri.parse(group.getUser().getPortraitUri()),
                    group.getDisplayName(),
                    CharacterParser.getInstance().getSpelling(group.getUser().getNickname()),
                    CharacterParser.getInstance().getSpelling(group.getDisplayName()),
                    groupDetail.getName(),
                    CharacterParser.getInstance().getSpelling(groupDetail.getName()),
                    null);
            if (group.getRole() == 0) {
                created = newMember;
            } else {
                newList.add(newMember);
            }
        }
        if (created != null) {
            newList.add(created);
        }
        Collections.reverse(newList);
        return newList;
    }

    @Override
    public void showAddGroupMembersResult(String result) {

    }

    @Override
    public void showKickGroupMembersResult(String result) {

    }

    @Override
    public void showCreateGroupResult(CreateGroupResult result) {

    }

    @Override
    public void showGetGroupsResult(List<GetGroupsResult> result) {

    }

    @Override
    public void showQuitGroupResult(String result) {

        RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP, fromConversationId, new RongIMClient.ResultCallback<Conversation>() {
            @Override
            public void onSuccess(Conversation conversation) {
                RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP, fromConversationId, new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, fromConversationId, null);
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
        showError(getString(R.string.quit_success));
        finish();
    }

    @Override
    public void showDismissGroupResult(String result) {
        RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP, fromConversationId, new RongIMClient.ResultCallback<Conversation>() {
            @Override
            public void onSuccess(Conversation conversation) {
                RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP, fromConversationId, new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, fromConversationId, null);
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

    @Override
    public void showGetGroupDetailResult(GetGroupDetailResult groupDetail) {
        this.groupDetail = groupDetail;
        initGroupData();

        groupMemberPresenter.getGroupMembers(fromConversationId);
    }


    private class GridAdapter extends BaseAdapter {

        private List<GroupMember> list;
        Context context;


        public GridAdapter(Context context, List<GroupMember> list) {
            if (list.size() >= 31) {
                this.list = list.subList(0, 30);
            } else {
                this.list = list;
            }

            this.context = context;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.social_chatsetting_gridview_item, parent, false);
            }
            SelectableRoundedImageView iv_avatar = (SelectableRoundedImageView) convertView.findViewById(R.id.iv_avatar);
            TextView tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            ImageView badge_delete = (ImageView) convertView.findViewById(R.id.badge_delete);

            // 最后一个item，减人按钮
            if (position == getCount() - 1 && isCreated) {
                tv_username.setText("");
                badge_delete.setVisibility(View.GONE);
                iv_avatar.setImageResource(R.mipmap.icon_btn_deleteperson);

                iv_avatar.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GroupDetailActivity.this, SelectFriendsActivity.class);
                        intent.putExtra("isDeleteGroupMember", true);
                        intent.putExtra("GroupId", groupDetail.getId());
                        intent.putExtra("GroupName", groupDetail.getName());
                        startActivityForResult(intent, 101);
                    }

                });
            } else if ((isCreated && position == getCount() - 2) || (!isCreated && position == getCount() - 1)) {
                tv_username.setText("");
                badge_delete.setVisibility(View.GONE);
                iv_avatar.setImageResource(R.mipmap.jy_drltsz_btn_addperson);

                iv_avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GroupDetailActivity.this, SelectFriendsActivity.class);
                        intent.putExtra("isAddGroupMember", true);
                        intent.putExtra("GroupId", groupDetail.getId());
                        intent.putExtra("GroupName", groupDetail.getName());
                        startActivityForResult(intent, 100);

                    }
                });
            } else { // 普通成员
                final GroupMember bean = list.get(position);
                tv_username.setText(bean.getName());

                ImageLoader.getInstance().displayImage(Uri.decode(PortraitUtil.
                        generateDefaultAvatar(new UserInfo(bean.getUserId(), bean.getName(), null))), iv_avatar, App.getOptions());
                iv_avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfo userInfo = new UserInfo(bean.getUserId(), bean.getName(), Uri.parse(PortraitUtil.generateDefaultAvatar(bean.getName(), bean.getUserId())));
                        Intent intent = new Intent(context, UserDetailActivity.class);
                        Friend friend = CharacterParser.getInstance().generateFriendFromUserInfo(userInfo);
                        intent.putExtra("friend", friend);
                        intent.putExtra("conversationType", Conversation.ConversationType.GROUP.getValue());
                        //Groups not Serializable,just need group name
                        intent.putExtra("groupName", groupDetail.getName());
                        intent.putExtra("type", CLICK_CONVERSATION_USER_PORTRAIT);
                        context.startActivity(intent);
                    }

                });

            }

            return convertView;
        }

        @Override
        public int getCount() {
            if (isCreated) {
                return list.size() + 2;
            } else {
                return list.size() + 1;
            }
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 传入新的数据 刷新UI的方法
         */
        public void updateListView(List<GroupMember> list) {
            this.list = list;
            notifyDataSetChanged();
        }

    }


    // 拿到新增的成员刷新adapter
    @Override
    @SuppressWarnings("unchecked")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            groupMemberPresenter.getGroupMembers(fromConversationId);
        }

    }


    private void backAsGroupDismiss() {
        this.setResult(501, new Intent());
        finish();
    }


    private void initViews() {
        messageTop = (SwitchButton) findViewById(R.id.sw_group_top);
        messageNotification = (SwitchButton) findViewById(R.id.sw_group_notfaction);
        messageTop.setOnCheckedChangeListener(this);
        messageNotification.setOnCheckedChangeListener(this);
        LinearLayout groupClean = (LinearLayout) findViewById(R.id.group_clean);
        mGridView = (MyGridView) findViewById(R.id.gridview);
        mTextViewMemberSize = (TextView) findViewById(R.id.group_member_size);
        mGroupHeader = (SelectableRoundedImageView) findViewById(R.id.group_header);
        LinearLayout mGroupDisplayName = (LinearLayout) findViewById(R.id.group_displayname);
        mGroupDisplayNameText = (TextView) findViewById(R.id.group_displayname_text);
        mGroupName = (TextView) findViewById(R.id.group_name);
        mQuitBtn = (Button) findViewById(R.id.group_quit);
        mDismissBtn = (Button) findViewById(R.id.group_dismiss);
        RelativeLayout totalGroupMember = (RelativeLayout) findViewById(R.id.group_member_size_item);
        RelativeLayout memberOnlineStatus = (RelativeLayout) findViewById(R.id.group_member_online_status);
        LinearLayout mGroupPortL = (LinearLayout) findViewById(R.id.ll_group_port);
        LinearLayout mGroupNameL = (LinearLayout) findViewById(R.id.ll_group_name);
        mGroupAnnouncementDividerLinearLayout = (LinearLayout) findViewById(R.id.ac_ll_group_announcement_divider);
        mGroupNotice = (LinearLayout) findViewById(R.id.group_announcement);
        mSearchMessagesLinearLayout = (LinearLayout) findViewById(R.id.ac_ll_search_chatting_records);
        mGroupPortL.setOnClickListener(this);
        mGroupNameL.setOnClickListener(this);
        totalGroupMember.setOnClickListener(this);
        mGroupDisplayName.setOnClickListener(this);
        memberOnlineStatus.setOnClickListener(this);
        if (getSharedPreferences("config", Context.MODE_PRIVATE).getBoolean("isDebug", false)) {
            memberOnlineStatus.setVisibility(View.VISIBLE);
        }
        mQuitBtn.setOnClickListener(this);
        mDismissBtn.setOnClickListener(this);
        groupClean.setOnClickListener(this);
        mGroupNotice.setOnClickListener(this);
        mSearchMessagesLinearLayout.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
