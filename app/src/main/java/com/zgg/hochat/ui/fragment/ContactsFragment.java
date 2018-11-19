package com.zgg.hochat.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zgg.hochat.App;
import com.zgg.hochat.R;
import com.zgg.hochat.adapter.FriendListAdapter;
import com.zgg.hochat.base.BaseFragment;
import com.zgg.hochat.bean.AllFriendsResult;
import com.zgg.hochat.bean.CreateGroupResult;
import com.zgg.hochat.bean.Friend;
import com.zgg.hochat.bean.GetGroupDetailResult;
import com.zgg.hochat.bean.GetGroupsResult;
import com.zgg.hochat.bean.MessageEvent;
import com.zgg.hochat.http.contract.AllFriendsContract;
import com.zgg.hochat.http.contract.GroupContract;
import com.zgg.hochat.http.model.FriendShipModel;
import com.zgg.hochat.http.model.GroupModel;
import com.zgg.hochat.http.presenter.AllFriendsPresenter;
import com.zgg.hochat.http.presenter.GroupPresenter;
import com.zgg.hochat.ui.activity.GroupListActivity;
import com.zgg.hochat.ui.activity.NewFriendListActivity;
import com.zgg.hochat.ui.activity.UserDetailActivity;
import com.zgg.hochat.utils.DataUtil;
import com.zgg.hochat.utils.PortraitUtil;
import com.zgg.hochat.widget.SelectableRoundedImageView;
import com.zgg.hochat.widget.pinyin.CharacterParser;
import com.zgg.hochat.widget.pinyin.PinyinComparator;
import com.zgg.hochat.widget.pinyin.SideBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;


/**
 * tab 2 通讯录的 Fragment
 * Created by Bob on 2015/1/25.
 */
public class ContactsFragment extends BaseFragment implements View.OnClickListener, AllFriendsContract.View, GroupContract.View {
    @BindView(R.id.list_view)
    ListView mListView;
    @BindView(R.id.sidebar)
    SideBar mSidBar;
    @BindView(R.id.tv_no_friend)
    TextView mNoFriends;
    /**
     * 中部展示的字母提示
     */
    @BindView(R.id.group_dialog)
    TextView mDialogTextView;

    TextView mUnreadTextView;

    public static final String TAG = ContactsFragment.class.getName();
    private SelectableRoundedImageView mSelectableRoundedImageView;
    private TextView mNameTextView;
    private View mHeadView;
    private PinyinComparator mPinyinComparator;


    private List<Friend> mFriendList;
    private List<Friend> mFilteredFriendList;
    /**
     * 好友列表的 mFriendListAdapter
     */
    private FriendListAdapter mFriendListAdapter;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser mCharacterParser;
    /**
     * 根据拼音来排列ListView里面的数据类
     */

    private String mId;
    private String mCacheName;

    private static final int CLICK_CONTACT_FRAGMENT_FRIEND = 2;

    private AllFriendsPresenter presenter;
    private GroupPresenter groupsPresenter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_address, container, false);
        updateUI();
        refreshUIListener();
        return view;
    }

    private void startFriendDetailsPage(Friend friend) {
        Intent intent = new Intent(getActivity(), UserDetailActivity.class);
        intent.putExtra("type", CLICK_CONTACT_FRAGMENT_FRIEND);
        intent.putExtra("friend", friend);
        startActivity(intent);
    }


    @Override
    protected void initData() {
        mFriendList = new ArrayList<>();
        FriendListAdapter adapter = new FriendListAdapter(getActivity(), mFriendList);
        mListView.setAdapter(adapter);
        mFilteredFriendList = new ArrayList<>();
        //实例化汉字转拼音类
        mCharacterParser = CharacterParser.getInstance();
        mPinyinComparator = PinyinComparator.getInstance();

        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = org.greenrobot.eventbus.ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getMessage().equals("group")) {
            groupsPresenter.getGroups();
        } else
            presenter.getAllFriends();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mDialogTextView != null) {
            mDialogTextView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr 需要过滤的 String
     */
    private void filterData(String filterStr) {
        List<Friend> filterDateList = new ArrayList<>();

        try {
            if (TextUtils.isEmpty(filterStr)) {
                filterDateList = mFriendList;
            } else {
                filterDateList.clear();
                for (Friend friendModel : mFriendList) {
                    String name = friendModel.getName();
                    String displayName = friendModel.getDisplayName();
                    if (!TextUtils.isEmpty(displayName)) {
//                        if (name.contains(filterStr) || mCharacterParser.getSpelling(name).startsWith(filterStr) || displayName.contains(filterStr) || mCharacterParser.getSpelling(displayName).startsWith(filterStr)) {
//                            filterDateList.add(friendModel);
//                        }
                    } else {
//                        if (name.contains(filterStr) || mCharacterParser.getSpelling(name).startsWith(filterStr)) {
//                            filterDateList.add(friendModel);
//                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 根据a-z进行排序
//        Collections.sort(filterDateList, mPinyinComparator);
        mFilteredFriendList = filterDateList;
        mFriendListAdapter.updateListView(filterDateList);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_newfriends:
                mUnreadTextView.setVisibility(View.GONE);
                Intent intent = new Intent(getActivity(), NewFriendListActivity.class);
                startActivity(intent);
                break;
            case R.id.re_chatroom:
                startActivity(new Intent(getActivity(), GroupListActivity.class));
                break;
            case R.id.publicservice:
//                Intent intentPublic = new Intent(getActivity(), PublicServiceActivity.class);
//                startActivity(intentPublic);
                break;
            case R.id.contact_me_item:
                RongIM.getInstance().startPrivateChat(getActivity(), mId, mCacheName);
                break;
        }
    }

    private void refreshUIListener() {
//        BroadcastManager.getInstance(getActivity()).addAction(SealAppContext.UPDATE_FRIEND, new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String command = intent.getAction();
//                if (!TextUtils.isEmpty(command)) {
//                    updateUI();
//                }
//            }
//        });
//
//        BroadcastManager.getInstance(getActivity()).addAction(SealAppContext.UPDATE_RED_DOT, new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String command = intent.getAction();
//                if (!TextUtils.isEmpty(command)) {
//                    mUnreadTextView.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
//        BroadcastManager.getInstance(getActivity()).addAction(SealConst.CHANGEINFO, new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                updatePersonalUI();
//            }
//        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        try {
//            BroadcastManager.getInstance(getActivity()).destroy(SealAppContext.UPDATE_FRIEND);
//            BroadcastManager.getInstance(getActivity()).destroy(SealAppContext.UPDATE_RED_DOT);
//            BroadcastManager.getInstance(getActivity()).destroy(SealConst.CHANGEINFO);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initUI() {
        mSidBar.setTextView(mDialogTextView);
        LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
        mHeadView = mLayoutInflater.inflate(R.layout.item_contact_list_header, null);
        mUnreadTextView = mHeadView.findViewById(R.id.tv_unread);
        RelativeLayout newFriendsLayout = (RelativeLayout) mHeadView.findViewById(R.id.re_newfriends);
        RelativeLayout groupLayout = (RelativeLayout) mHeadView.findViewById(R.id.re_chatroom);
        RelativeLayout publicServiceLayout = (RelativeLayout) mHeadView.findViewById(R.id.publicservice);
        RelativeLayout selfLayout = (RelativeLayout) mHeadView.findViewById(R.id.contact_me_item);
        mSelectableRoundedImageView = (SelectableRoundedImageView) mHeadView.findViewById(R.id.contact_me_img);
        mNameTextView = (TextView) mHeadView.findViewById(R.id.contact_me_name);
        updatePersonalUI();
        UserInfo userInfo = new UserInfo(mId, mCacheName, null);
        ImageLoader.getInstance().displayImage(PortraitUtil.generateDefaultAvatar(userInfo), mSelectableRoundedImageView, App.getOptions());
        mListView.addHeaderView(mHeadView);
        mNoFriends.setVisibility(View.VISIBLE);

        selfLayout.setOnClickListener(this);
        groupLayout.setOnClickListener(this);
        newFriendsLayout.setOnClickListener(this);
        publicServiceLayout.setOnClickListener(this);
        //设置右侧触摸监听
        mSidBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mFriendListAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }

            }
        });
    }

    private void updateUI() {
//        SealUserInfoManager.getInstance().getFriends(new SealUserInfoManager.ResultCallback<List<Friend>>() {
//            @Override
//            public void onSuccess(List<Friend> friendsList) {
//                updateFriendsList(friendsList);
//            }
//
//            @Override
//            public void onError(String errString) {
//                updateFriendsList(null);
//            }
//        });
        presenter = new AllFriendsPresenter(this, FriendShipModel.newInstance());
        addPresenter(presenter);
        presenter.getAllFriends();

        groupsPresenter = new GroupPresenter(this, GroupModel.newInstance());
        addPresenter(groupsPresenter);
        groupsPresenter.getGroups();
    }


    private void updatePersonalUI() {
        mId = DataUtil.getUser().getPhone();
        mCacheName = mId;
        mNameTextView.setText(mCacheName);
        if (!TextUtils.isEmpty(mId)) {
//            UserInfo userInfo = new UserInfo(mId, mCacheName, Uri.parse(header));
//            String portraitUri = SealUserInfoManager.getInstance().getPortraitUri(userInfo);
//            ImageLoader.getInstance().displayImage(portraitUri, mSelectableRoundedImageView, App.getOptions());
        }
    }

    private void handleFriendDataForSort() {
        for (Friend friend : mFriendList) {
            if (friend.isExitsDisplayName()) {
                String letters = replaceFirstCharacterWithUppercase(friend.getDisplayNameSpelling());
                friend.setLetters(letters);
            } else {
                String letters = replaceFirstCharacterWithUppercase(friend.getNameSpelling());
                friend.setLetters(letters);
            }
        }
    }

    private String replaceFirstCharacterWithUppercase(String spelling) {
        if (!TextUtils.isEmpty(spelling)) {
            char first = spelling.charAt(0);
            char newFirst = first;
            if (first >= 'a' && first <= 'z') {
                newFirst -= 32;
            }
            StringBuilder builder = new StringBuilder(String.valueOf(newFirst));
            if (spelling.length() > 1) {
                builder.append(spelling.substring(1, spelling.length()));
            }
            return builder.toString();
        } else {
            return "#";
        }
    }

    @Override
    public void showAllFriendsResult(List<AllFriendsResult> result) {
        List<Friend> friends = addFriends(result);
        updateFriendsList(friends);
    }

    private void updateFriendsList(List<Friend> friendsList) {
        if (friendsList == null) return;

        //updateUI fragment初始化和好友信息更新时都会调用,isReloadList表示是否是好友更新时调用
        boolean isReloadList = false;
        if (mFriendList != null && mFriendList.size() > 0) {
            mFriendList.clear();
            isReloadList = true;
        }
        mFriendList = friendsList;
        if (mFriendList != null && mFriendList.size() > 0) {
            handleFriendDataForSort();
            mNoFriends.setVisibility(View.GONE);
        } else {
            mNoFriends.setVisibility(View.VISIBLE);
        }

        // 根据a-z进行排序源数据
        Collections.sort(mFriendList, mPinyinComparator);
        if (isReloadList) {
            mSidBar.setVisibility(View.VISIBLE);
            mFriendListAdapter.updateListView(mFriendList);
        } else {
            mSidBar.setVisibility(View.VISIBLE);
            mFriendListAdapter = new FriendListAdapter(getActivity(), mFriendList);

            mListView.setAdapter(mFriendListAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mListView.getHeaderViewsCount() > 0) {
                        startFriendDetailsPage(mFriendList.get(position - 1));
                    } else {
                        startFriendDetailsPage(mFilteredFriendList.get(position));
                    }
                }
            });


            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    Friend bean = mFriendList.get(position - 1);
                    startFriendDetailsPage(bean);
                    return true;
                }
            });
        }
    }

    private List<Friend> addFriends(final List<AllFriendsResult> list) {
        if (list != null && list.size() > 0) {
            List<Friend> friendsList = new ArrayList<>();
            for (AllFriendsResult resultEntity : list) {
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
    public void showCreateGroupResult(CreateGroupResult result) {

    }

    @Override
    public void showGetGroupsResult(List<GetGroupsResult> result) {

        for (GetGroupsResult getGroupsResult : result) {
            GetGroupsResult.GroupBean group = getGroupsResult.getGroup();
            Group groupInfo = new Group(group.getId(), group.getName(), null);
            RongIM.getInstance().refreshGroupInfoCache(groupInfo);
        }
    }

    @Override
    public void showQuitGroupResult(String result) {

    }

    @Override
    public void showDismissGroupResult(String result) {

    }

    @Override
    public void showGetGroupDetailResult(GetGroupDetailResult groupDetail) {

    }
}
