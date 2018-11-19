package com.zgg.hochat.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.zgg.hochat.App;
import com.zgg.hochat.BuildConfig;
import com.zgg.hochat.R;
import com.zgg.hochat.base.BaseActivity;
import com.zgg.hochat.base.BaseToolbarActivity;
import com.zgg.hochat.bean.AddGroupMemberInput;
import com.zgg.hochat.bean.AllFriendsResult;
import com.zgg.hochat.bean.Friend;
import com.zgg.hochat.bean.GetGroupMembersResult;
import com.zgg.hochat.bean.GroupMember;
import com.zgg.hochat.bean.QuitGroupInput;
import com.zgg.hochat.http.contract.AllFriendsContract;
import com.zgg.hochat.http.contract.GroupMemberContract;
import com.zgg.hochat.http.model.FriendShipModel;
import com.zgg.hochat.http.model.GroupModel;
import com.zgg.hochat.http.presenter.AllFriendsPresenter;
import com.zgg.hochat.http.presenter.GroupMemberPresenter;
import com.zgg.hochat.utils.DataUtil;
import com.zgg.hochat.widget.SelectableRoundedImageView;
import com.zgg.hochat.widget.pinyin.CharacterParser;
import com.zgg.hochat.widget.pinyin.PinyinComparator;
import com.zgg.hochat.widget.pinyin.SideBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import butterknife.BindView;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import retrofit2.HttpException;

/**
 * Created by AMing on 16/1/21.
 * Company RongCloud
 */
public class SelectFriendsActivity extends BaseActivity implements AllFriendsContract.View, GroupMemberContract.View {

    private static final int ADD_GROUP_MEMBER = 21;
    private static final int DELETE_GROUP_MEMBER = 23;
    public static final String DISCUSSION_UPDATE = "DISCUSSION_UPDATE";

    /**
     * 发起讨论组的 adapter
     */
    private StartDiscussionAdapter adapter;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser mCharacterParser;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private List<Friend> data_list = new ArrayList<>();
    private List<Friend> sourceDataList = new ArrayList<>();
    private LinearLayout mSelectedFriendsLinearLayout;
    private boolean isCrateGroup;
    private boolean isConversationActivityStartDiscussion;
    private boolean isConversationActivityStartPrivate;
    private List<GroupMember> addGroupMemberList;
    private List<GroupMember> deleteGroupMemberList;
    private String groupId;
    private String conversationStartId;
    private String conversationStartType = "null";
    private ArrayList<String> discListMember;
    private ArrayList<UserInfo> addDisList, deleDisList;
    private boolean isStartPrivateChat;
    private List<Friend> mSelectedFriend;
    private boolean isAddGroupMember;
    private boolean isDeleteGroupMember;
    private String groupName;

    @BindView(R.id.dis_show_no_friend)
    TextView mNoFriends;
    /**
     * 好友列表的 ListView
     */
    @BindView(R.id.dis_friendlistview)
    ListView mListView;

    @BindView(R.id.dis_sidrbar)
    SideBar mSidBar;
    /**
     * 中部展示的字母提示
     */
    @BindView(R.id.dis_dialog)
    TextView dialog;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private TextView tv_ok;
    private List<String> startDisList;
    private List<Friend> createGroupList;
    private AllFriendsPresenter allFriendsPresenter;
    private GroupMemberPresenter groupMemberPresenter;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_disc);
        initView();
        mSelectedFriend = new ArrayList<>();
        mSelectedFriendsLinearLayout = (LinearLayout) findViewById(R.id.ll_selected_friends);
        isCrateGroup = getIntent().getBooleanExtra("createGroup", false);
        isConversationActivityStartDiscussion = getIntent().getBooleanExtra("CONVERSATION_DISCUSSION", false);
        isConversationActivityStartPrivate = getIntent().getBooleanExtra("CONVERSATION_PRIVATE", false);
        groupId = getIntent().getStringExtra("GroupId");
        groupName = getIntent().getStringExtra("GroupName");
        isAddGroupMember = getIntent().getBooleanExtra("isAddGroupMember", false);
        isDeleteGroupMember = getIntent().getBooleanExtra("isDeleteGroupMember", false);
        if (isAddGroupMember || isDeleteGroupMember) {
            initGroupMemberList();
        }
        addDisList = (ArrayList<UserInfo>) getIntent().getSerializableExtra("AddDiscuMember");
        deleDisList = (ArrayList<UserInfo>) getIntent().getSerializableExtra("DeleteDiscuMember");
        Logger.d("onCreate");

        if (deleDisList != null && deleDisList.size() > 0) {
            for (int i = 0; i < deleDisList.size(); i++) {
                if (deleDisList.get(i).getUserId().contains(DataUtil.getUserId())) {
                    continue;
                }
                data_list.add(new Friend(deleDisList.get(i).getUserId(),
                        deleDisList.get(i).getName(),
                        deleDisList.get(i).getPortraitUri(),
                        null //TODO displayName 需要处理 暂为 null
                ));
            }
            /**
             * 以下3步是标准流程
             * 1.填充数据sourceDataList
             * 2.过滤数据,邀请新成员时需要过滤掉已经是成员的用户,但做删除操作时不需要这一步
             * 3.设置adapter显示
             */
            fillSourceDataList();
            filterSourceDataList();
            updateAdapter();
        } else if (!isDeleteGroupMember && !isAddGroupMember) {
            fillSourceDataListWithFriendsInfo();
        }

        setTitle();
        initToolbar();
        setSupportActionBar(toolbar);
    }

    /**
     * 删除，添加成员
     */
    private void initGroupMemberList() {
        groupMemberPresenter.getGroupMembers(groupId);
    }

    private void setTitle() {
        if (isConversationActivityStartPrivate) {
            conversationStartType = "PRIVATE";
            conversationStartId = getIntent().getStringExtra("DEMO_FRIEND_TARGETID");
            toolbar.setTitle("选择讨论组成员");
        } else if (isConversationActivityStartDiscussion) {
            conversationStartType = "DISCUSSION";
            conversationStartId = getIntent().getStringExtra("DEMO_FRIEND_TARGETID");
            discListMember = getIntent().getStringArrayListExtra("DISCUSSIONMEMBER");
            toolbar.setTitle("选择讨论组成员");
        } else if (isDeleteGroupMember) {
            toolbar.setTitle(getString(R.string.remove_group_member));
        } else if (isAddGroupMember) {
            toolbar.setTitle(getString(R.string.add_group_member));
        } else if (isCrateGroup) {
            toolbar.setTitle(getString(R.string.select_group_member));
        } else if (addDisList != null) {
            toolbar.setTitle("增加讨论组成员");
        } else if (deleDisList != null) {
            toolbar.setTitle("移除讨论组成员");
        } else {
            toolbar.setTitle(getString(R.string.select_contact));
            isStartPrivateChat = true;
        }
    }

    private void initView() {
        //实例化汉字转拼音类
        mCharacterParser = CharacterParser.getInstance();
        pinyinComparator = PinyinComparator.getInstance();
        mSidBar.setTextView(dialog);
        //设置右侧触摸监听
        mSidBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }
            }
        });

        adapter = new StartDiscussionAdapter(mContext, sourceDataList);
        mListView.setAdapter(adapter);
    }

    /**
     * 根据进行的操作初始化数据,添加删除群成员和获取好友信息是异步操作,所以做了很多额外的处理
     * 数据添加后还需要过滤已经是群成员,讨论组成员的用户
     * 最后设置adapter显示
     * 后两个操作全都根据异步操作推后
     */
    @Override
    protected void initData() {
        Logger.d("initData");

        allFriendsPresenter = new AllFriendsPresenter(this, FriendShipModel.newInstance());
        addPresenter(allFriendsPresenter);
        groupMemberPresenter = new GroupMemberPresenter(this, GroupModel.newInstance());
        addPresenter(groupMemberPresenter);
    }

    protected void initToolbar() {
        tv_ok = toolbar.findViewById(R.id.toolbar_other);
        tv_ok.setText("确定");
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOk();
            }
        });
    }

    private void clickOk() {
        if (mCBFlag != null && sourceDataList != null && sourceDataList.size() > 0) {
            startDisList = new ArrayList<>();
            List<String> disNameList = new ArrayList<>();
            createGroupList = new ArrayList<>();
            for (int i = 0; i < sourceDataList.size(); i++) {
                if (mCBFlag.get(i)) {
                    startDisList.add(sourceDataList.get(i).getUserId());
                    disNameList.add(sourceDataList.get(i).getName());
                    createGroupList.add(sourceDataList.get(i));
                }
            }

            if (isConversationActivityStartDiscussion) {//添加群员
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().addMemberToDiscussion(conversationStartId, startDisList, new RongIMClient.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            showError(getString(R.string.add_successful));
                            finish();
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                }
            } else if (isConversationActivityStartPrivate) {//添加讨论组成员
                if (RongIM.getInstance() != null) { // 没有被调用 二人讨论组时候
                    RongIM.getInstance().addMemberToDiscussion(conversationStartId, startDisList, new RongIMClient.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            showError(getString(R.string.add_successful));
                            finish();
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                }
            } else if (deleteGroupMemberList != null && startDisList != null && sourceDataList.size() > 0) {//删除群成员
                tv_ok.setClickable(true);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("确认移除群成员?")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                groupMemberPresenter.kickGroupMembers(new AddGroupMemberInput(groupId, startDisList));
                            }
                        }).show();

            } else if (deleDisList != null && startDisList != null && startDisList.size() > 0) {//添加删除成员
                Intent intent = new Intent();
                intent.putExtra("deleteDiscuMember", (Serializable) startDisList);
                setResult(RESULT_OK, intent);
                finish();

            } else if (addGroupMemberList != null && startDisList != null && startDisList.size() > 0) {//添加群成员
                //TODO 选中添加成员的数据添加到服务端数据库  返回本地也需要更改
                groupMemberPresenter.addGroupMembers(new AddGroupMemberInput(groupId, startDisList));

            } else if (addDisList != null && startDisList != null && startDisList.size() > 0) {//添加讨论组成员
                Intent intent = new Intent();
                intent.putExtra("addDiscuMember", (Serializable) startDisList);
                setResult(RESULT_OK, intent);
                finish();
            } else if (isCrateGroup) {//创建群
                if (createGroupList.size() > 0) {
                    tv_ok.setClickable(true);
                    Intent intent = new Intent(SelectFriendsActivity.this, CreateGroupActivity.class);
                    intent.putExtra("GroupMember", (Serializable) createGroupList);
                    startActivity(intent);
                    finish();
                } else {
                    showError("请至少邀请一位好友创建群组");
                    tv_ok.setClickable(true);
                }
            } else {//讨论组一个好友，直接单聊

                if (startDisList != null && startDisList.size() == 1) {
                    RongIM.getInstance().startPrivateChat(mContext, startDisList.get(0), "");
                } else if (startDisList.size() > 1) {

                    String disName;
                    if (disNameList.size() < 2) {
                        disName = disNameList.get(0) + "和我的讨论组";
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (String s : disNameList) {
                            sb.append(s);
                            sb.append(",");
                        }
                        String str = sb.toString();
                        disName = str.substring(0, str.length() - 1);
                        disName = disName + "和我的讨论组";
                    }
                    RongIM.getInstance().createDiscussion(disName, startDisList, new RongIMClient.CreateDiscussionCallback() {
                        @Override
                        public void onSuccess(String s) {
                            Logger.d("onSuccess" + s);
                            RongIM.getInstance().startDiscussionChat(SelectFriendsActivity.this, s, "");
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                            Logger.d(errorCode.getValue());
                        }
                    });
                } else {
                    tv_ok.setClickable(true);
                    showError(getString(R.string.least_one_friend));
                }
            }
        } else {
            Toast.makeText(SelectFriendsActivity.this, "无数据", Toast.LENGTH_SHORT).show();
        }
    }

    private void fillSourceDataList() {
        if (data_list != null && data_list.size() > 0) {
            sourceDataList = filledData(data_list); //过滤数据为有字母的字段  现在有字母 别的数据没有
        } else {
            mNoFriends.setVisibility(View.VISIBLE);
        }

        //还原除了带字母字段的其他数据
        for (int i = 0; i < data_list.size(); i++) {
            sourceDataList.get(i).setName(data_list.get(i).getName());
            sourceDataList.get(i).setUserId(data_list.get(i).getUserId());
            sourceDataList.get(i).setPortraitUri(data_list.get(i).getPortraitUri());
            sourceDataList.get(i).setDisplayName(data_list.get(i).getDisplayName());
        }
        // 根据a-z进行排序源数据
        Collections.sort(sourceDataList, pinyinComparator);
    }

    //讨论组群组邀请新成员时需要过滤掉已经是成员的用户
    private void filterSourceDataList() {
        if (addDisList != null && addDisList.size() > 0) {
            for (UserInfo u : addDisList) {
                for (int i = 0; i < sourceDataList.size(); i++) {
                    if (sourceDataList.get(i).getUserId().contains(u.getUserId())) {
                        sourceDataList.remove(sourceDataList.get(i));
                    }
                }
            }
        } else if (addGroupMemberList != null && addGroupMemberList.size() > 0) {
            for (GroupMember addMember : addGroupMemberList) {
                for (int i = 0; i < sourceDataList.size(); i++) {
                    if (sourceDataList.get(i).getUserId().contains(addMember.getUserId())) {
                        sourceDataList.remove(sourceDataList.get(i));
                    }
                }
            }
        } else if (conversationStartType.equals("DISCUSSION")) {
            if (discListMember != null && discListMember.size() > 1) {
                for (String s : discListMember) {
                    for (int i = 0; i < sourceDataList.size(); i++) {
                        if (sourceDataList.get(i).getUserId().contains(s)) {
                            sourceDataList.remove(sourceDataList.get(i));
                        }
                    }
                }
            }
        } else if (conversationStartType.equals("PRIVATE")) {
            for (int i = 0; i < sourceDataList.size(); i++) {
                if (sourceDataList.get(i).getUserId().contains(conversationStartId)) {
                    sourceDataList.remove(sourceDataList.get(i));
                }
            }
        }
    }

    private void updateAdapter() {
        adapter.setData(sourceDataList);
        adapter.notifyDataSetChanged();
    }

    /**
     * 获取好友列表
     */
    private void fillSourceDataListWithFriendsInfo() {
        allFriendsPresenter.getAllFriends();
    }

    private void fillSourceDataListForDeleteGroupMember() {
        if (deleteGroupMemberList != null && deleteGroupMemberList.size() > 0) {
            for (GroupMember deleteMember : deleteGroupMemberList) {
                if (deleteMember.getUserId().contains(DataUtil.getUserId())) {
                    continue;
                }
                data_list.add(new Friend(deleteMember.getUserId(),
                        deleteMember.getName(), deleteMember.getPortraitUri(),
                        null //TODO displayName 需要处理 暂为 null
                ));
            }
            fillSourceDataList();
            updateAdapter();
        }
    }


    //用于存储CheckBox选中状态
    public Map<Integer, Boolean> mCBFlag;

    public List<Friend> adapterList;

    @Override
    public void showAllFriendsResult(List<AllFriendsResult> result) {
        List<Friend> friendList = addFriends(result);
        if (mListView != null) {
            if (friendList != null && friendList.size() > 0) {
                for (Friend friend : friendList) {
                    data_list.add(new Friend(friend.getUserId(), friend.getName(), friend.getPortraitUri(), friend.getDisplayName(), null, null));
                }
                if (isAddGroupMember) {
                    for (GroupMember groupMember : addGroupMemberList) {
                        for (int i = 0; i < data_list.size(); i++) {
                            if (groupMember.getUserId().equals(data_list.get(i).getUserId())) {
                                data_list.remove(i);
                            }
                        }
                    }
                }
                fillSourceDataList();
                filterSourceDataList();
                updateAdapter();
            }
        }
    }

    @Override
    public void showGetGroupMembersResult(List<GetGroupMembersResult> result) {
        if (isAddGroupMember) {//添加群员
            addGroupMemberList = addGroupMembers(result);
            fillSourceDataListWithFriendsInfo();
        } else {//删除群员
            deleteGroupMemberList = addGroupMembers(result);
            fillSourceDataListForDeleteGroupMember();
        }
    }

    private List<GroupMember> addGroupMembers(List<GetGroupMembersResult> groupMember) {
        List<GroupMember> newList = new ArrayList<>();
        GroupMember created = null;
        for (GetGroupMembersResult group : groupMember) {

            GroupMember newMember = new GroupMember(groupId,
                    group.getUser().getId(),
                    group.getUser().getNickname(),
                    Uri.parse(group.getUser().getPortraitUri()),
                    group.getDisplayName(),
                    CharacterParser.getInstance().getSpelling(group.getUser().getNickname()),
                    CharacterParser.getInstance().getSpelling(group.getDisplayName()),
                    groupName,
                    CharacterParser.getInstance().getSpelling(groupName),
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
        showError(result);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showKickGroupMembersResult(String result) {
        showError(result);
        setResult(RESULT_OK);
        finish();
    }


    class StartDiscussionAdapter extends BaseAdapter implements SectionIndexer {

        private Context context;
        private ArrayList<CheckBox> checkBoxList = new ArrayList<>();

        public StartDiscussionAdapter(Context context, List<Friend> list) {
            this.context = context;
            adapterList = list;
            mCBFlag = new HashMap<>();
            init();
        }

        public void setData(List<Friend> friends) {
            adapterList = friends;
            init();
        }

        void init() {
            for (int i = 0; i < adapterList.size(); i++) {
                mCBFlag.put(i, false);
            }
        }

        /**
         * 传入新的数据 刷新UI的方法
         */
        public void updateListView(List<Friend> list) {
            adapterList = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return adapterList.size();
        }

        @Override
        public Object getItem(int position) {
            return adapterList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder viewHolder;
            final Friend friend = adapterList.get(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_start_discussion, parent, false);
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.dis_friendname);
                viewHolder.tvLetter = (TextView) convertView.findViewById(R.id.dis_catalog);
                viewHolder.mImageView = (SelectableRoundedImageView) convertView.findViewById(R.id.dis_frienduri);
                viewHolder.isSelect = (CheckBox) convertView.findViewById(R.id.dis_select);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //根据position获取分类的首字母的Char ascii值
            int section = getSectionForPosition(position);
            //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(section)) {
                viewHolder.tvLetter.setVisibility(View.VISIBLE);
                viewHolder.tvLetter.setText(friend.getLetters());
            } else {
                viewHolder.tvLetter.setVisibility(View.GONE);
            }

            if (isStartPrivateChat) {
                viewHolder.isSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        if (cb != null) {
                            if (cb.isChecked()) {
                                for (CheckBox c : checkBoxList) {
                                    c.setChecked(false);
                                }
                                checkBoxList.clear();
                                checkBoxList.add(cb);
                            } else {
                                checkBoxList.clear();
                            }
                        }
                    }
                });
                viewHolder.isSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mCBFlag.put(position, viewHolder.isSelect.isChecked());
                    }
                });
            } else {
                viewHolder.isSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCBFlag.put(position, viewHolder.isSelect.isChecked());
                        updateSelectedSizeView(mCBFlag);
                        if (mSelectedFriend.contains(friend)) {
                            int index = mSelectedFriend.indexOf(friend);
                            if (index > -1) {
                                mSelectedFriendsLinearLayout.removeViewAt(index);
                            }
                            mSelectedFriend.remove(friend);
                        } else {
                            mSelectedFriend.add(friend);
                            LinearLayout view = (LinearLayout) View.inflate(SelectFriendsActivity.this, R.layout.item_selected_friends, null);
                            SelectableRoundedImageView asyncImageView = (SelectableRoundedImageView) view.findViewById(R.id.iv_selected_friends);
                            ImageLoader.getInstance().displayImage(null, asyncImageView);
                            view.removeView(asyncImageView);
                            mSelectedFriendsLinearLayout.addView(asyncImageView);
                        }
                    }
                });
            }
            viewHolder.isSelect.setChecked(mCBFlag.get(position));

            if (TextUtils.isEmpty(adapterList.get(position).getDisplayName())) {
                viewHolder.tvTitle.setText(adapterList.get(position).getName());
            } else {
                viewHolder.tvTitle.setText(adapterList.get(position).getDisplayName());
            }

            ImageLoader.getInstance().displayImage(null, viewHolder.mImageView, App.getOptions());
            return convertView;
        }

        private void updateSelectedSizeView(Map<Integer, Boolean> mCBFlag) {
            if (!isStartPrivateChat && mCBFlag != null) {
                int size = 0;
                for (int i = 0; i < mCBFlag.size(); i++) {
                    if (mCBFlag.get(i)) {
                        size++;
                    }
                }
                if (size == 0) {
                    tv_ok.setText("确定");
                    mSelectedFriendsLinearLayout.setVisibility(View.GONE);
                } else {
                    tv_ok.setText("确定(" + size + ")");
                    List<Friend> selectedList = new ArrayList<>();
                    for (int i = 0; i < sourceDataList.size(); i++) {
                        if (mCBFlag.get(i)) {
                            selectedList.add(sourceDataList.get(i));
                        }
                    }
                    mSelectedFriendsLinearLayout.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public Object[] getSections() {
            return new Object[0];
        }

        /**
         * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
         */
        @Override
        public int getPositionForSection(int sectionIndex) {
            for (int i = 0; i < getCount(); i++) {
                String sortStr = adapterList.get(i).getLetters();
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == sectionIndex) {
                    return i;
                }
            }

            return -1;
        }

        /**
         * 根据ListView的当前位置获取分类的首字母的Char ascii值
         */
        @Override
        public int getSectionForPosition(int position) {
            return adapterList.get(position).getLetters().charAt(0);
        }


        final class ViewHolder {
            /**
             * 首字母
             */
            TextView tvLetter;
            /**
             * 昵称
             */
            TextView tvTitle;
            /**
             * 头像
             */
            SelectableRoundedImageView mImageView;
            /**
             * userid
             */
//            TextView tvUserId;
            /**
             * 是否被选中的checkbox
             */
            CheckBox isSelect;
        }

    }


    /**
     * 为ListView填充数据
     */
    private List<Friend> filledData(List<Friend> list) {
        List<Friend> mFriendList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Friend friendModel = new Friend(list.get(i).getUserId(), list.get(i).getName(), list.get(i).getPortraitUri());
            //汉字转换成拼音
            String pinyin = null;
            if (!TextUtils.isEmpty(list.get(i).getDisplayName())) {
                pinyin = mCharacterParser.getSpelling(list.get(i).getDisplayName());
            } else if (!TextUtils.isEmpty(list.get(i).getName())) {
                pinyin = mCharacterParser.getSpelling(list.get(i).getName());
            } else {
                UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(list.get(i).getUserId());
                if (userInfo != null) {
                    pinyin = mCharacterParser.getSpelling(userInfo.getName());
                }
            }
            String sortString;
            if (!TextUtils.isEmpty(pinyin)) {
                sortString = pinyin.substring(0, 1).toUpperCase();
            } else {
                sortString = "#";
            }

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                friendModel.setLetters(sortString);
            } else {
                friendModel.setLetters("#");
            }

            mFriendList.add(friendModel);
        }
        return mFriendList;

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mListView = null;
        adapter = null;
    }

    @Override
    protected void initUI() {

    }

// case ADD_GROUP_MEMBER:
//    AddGroupMemberResponse res = (AddGroupMemberResponse) result;
//                    if(res.getCode()==200)
//
//    {
//        Intent data = new Intent();
//        data.putExtra("newAddMember", (Serializable) createGroupList);
//        setResult(101, data);
//        LoadDialog.dismiss(mContext);
//        NToast.shortToast(mContext, getString(R.string.add_successful));
//        finish();
//    }
//                    break;
//                case DELETE_GROUP_MEMBER:
//    DeleteGroupMemberResponse response = (DeleteGroupMemberResponse) result;
//                    if(response.getCode()==200)
//
//    {
//        Intent intent = new Intent();
//        intent.putExtra("deleteMember", (Serializable) createGroupList);
//        setResult(102, intent);
//        LoadDialog.dismiss(mContext);
//        NToast.shortToast(mContext, getString(R.string.remove_successful));
//        finish();
//    } else if(response.getCode()==400)
//
//    {
//        LoadDialog.dismiss(mContext);
//        NToast.shortToast(mContext, "创建者不能将自己移除");
//    }
//                    break;


    private List<Friend> addFriends(final List<AllFriendsResult> list) {
        if (list != null && list.size() > 0) {
            List<Friend> friendsList = new ArrayList<>();
            for (AllFriendsResult resultEntity : list) {
                if (resultEntity.getUser().getId().contains(DataUtil.getUserId())) {
                    continue;
                }

                String id = resultEntity.getUser().getId();
                String nickname = resultEntity.getUser().getNickname();
                if (resultEntity.getStatus() == 20) {
                    Friend friend = new Friend(
                            id,
                            nickname,
                            Uri.parse(resultEntity.getUser().getPortraitUri()),
                            resultEntity.getDisplayName(),
                            null, null, null, null,
                            CharacterParser.getInstance().getSpelling(nickname),
                            CharacterParser.getInstance().getSpelling(resultEntity.getDisplayName()));
                    if (friend.getPortraitUri() == null || TextUtils.isEmpty(friend.getPortraitUri().toString())) {
                    }
                    friendsList.add(friend);
                }

                RongIM.getInstance().refreshUserInfoCache(new UserInfo(id, nickname, null));
            }

            return friendsList;
        } else {
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
