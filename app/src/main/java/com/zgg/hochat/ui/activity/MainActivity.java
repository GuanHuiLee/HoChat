package com.zgg.hochat.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.zgg.hochat.BuildConfig;
import com.zgg.hochat.R;
import com.zgg.hochat.adapter.ConversationListAdapterEx;
import com.zgg.hochat.base.BaseActivity;
import com.zgg.hochat.ui.fragment.ContactsFragment;
import com.zgg.hochat.ui.fragment.ConversationFragmentEx;
import com.zgg.hochat.ui.fragment.DiscoverFragment;
import com.zgg.hochat.ui.fragment.MineFragment;
import com.zgg.hochat.utils.Constant;
import com.zgg.hochat.utils.DataUtil;
import com.zgg.hochat.widget.DragPointView;
import com.zgg.hochat.widget.MorePopWindow;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.ContactNotificationMessage;

public class MainActivity extends BaseActivity implements IUnReadMessageObserver {
    @BindView(R.id.rb_chat)
    RadioButton rbChat;

    @BindView(R.id.rb_contacts)
    RadioButton rbContacts;

    @BindView(R.id.rb_find)
    RadioButton rbFind;

    @BindView(R.id.rb_mine)
    RadioButton rbMine;

    @BindView(R.id.iv_more)
    ImageView iv_more;
    @BindView(R.id.iv_search)
    ImageView iv_search;

    @BindView(R.id.seal_num)
     DragPointView mUnreadNumView;

    private FragmentManager fragmentManager;

    /**
     * 当前选中View
     */
    private CompoundButton selectView;
    /**
     * 会话列表的fragment
     */
    private ConversationListFragment mConversationListFragment = null;
    private Conversation.ConversationType[] mConversationsTypes = null;
    private Fragment conversationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initUI() {
        rbChat.setTag(Constant.CONVERSATION);
        rbContacts.setTag(ContactsFragment.TAG);
//        rbFind.setTag(DiscoverFragment.TAG);
        rbMine.setTag(MineFragment.TAG);
    }

    @Override
    protected void initData() {
        fragmentManager = getSupportFragmentManager();
        conversationList = initConversationList();

        rbContacts.setChecked(true);

        final Conversation.ConversationType[] conversationTypes = {
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE
        };

        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);
        getConversationPush();// 获取 push 的 id 和 target
        getPushMessage();
    }

    /**
     * 得到不落地 push 消息
     */
    private void getPushMessage() {
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {
            String path = intent.getData().getPath();
            if (path.contains("push_message")) {
                String cacheToken = DataUtil.getToken();
                if (TextUtils.isEmpty(cacheToken)) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else {
                    if (!RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                        RongIM.connect(cacheToken, new RongIMClient.ConnectCallback() {
                            @Override
                            public void onTokenIncorrect() {
                            }

                            @Override
                            public void onSuccess(String s) {
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode e) {
                                showError(e.getMessage());
                            }
                        });
                    }
                }
            }
        }
    }

    private void getConversationPush() {
        if (getIntent() != null && getIntent().hasExtra("PUSH_CONVERSATIONTYPE") && getIntent().hasExtra("PUSH_TARGETID")) {

            final String conversationType = getIntent().getStringExtra("PUSH_CONVERSATIONTYPE");
            final String targetId = getIntent().getStringExtra("PUSH_TARGETID");


            RongIM.getInstance().getConversation(Conversation.ConversationType.valueOf(conversationType), targetId, new RongIMClient.ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {

                    if (conversation != null) {

                        if (conversation.getLatestMessage() instanceof ContactNotificationMessage) { //好友消息的push
                            startActivity(new Intent(MainActivity.this, NewFriendListActivity.class));
                        } else {
                            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon().appendPath("conversation")
                                    .appendPath(conversationType).appendQueryParameter("targetId", targetId).build();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) {

                }
            });
        }
    }

    private void showFragment(CompoundButton curView) {
        if (selectView != null) {
            selectView.setChecked(false);
        }
        selectView = curView;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String tag = (String) curView.getTag();


        if (fragmentManager.findFragmentByTag(tag) == null) {
            Fragment fragment;
            if (tag.equals(Constant.CONVERSATION)) {
                fragment = conversationList;
            } else {
                fragment = Fragment.instantiate(this, tag);
            }
            fragmentTransaction.add(R.id.fl_content, fragment, tag);
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            boolean isHide = fragmentManager.findFragmentByTag(tag).isHidden();
            if (isHide) {
                fragmentTransaction.show(fragmentManager.findFragmentByTag(tag));
                fragmentTransaction.commitAllowingStateLoss();
                fragmentManager.findFragmentByTag(tag).onResume(); //切换Fragment，实时刷新数据
            }
        }
    }

    private void hideFragment(CompoundButton lastView) {

        if (lastView != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            String tag = (String) lastView.getTag();
            if (fragmentManager.findFragmentByTag(tag) != null) {
                boolean isHide = fragmentManager.findFragmentByTag(tag).isHidden();
                if (!isHide) {
                    fragmentTransaction.hide(fragmentManager.findFragmentByTag(tag));
                    fragmentTransaction.commitAllowingStateLoss();
                }
            }
        }
    }

    @OnCheckedChanged({R.id.rb_chat, R.id.rb_contacts, R.id.rb_find, R.id.rb_mine})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            showFragment(buttonView);
        } else {
            hideFragment(buttonView);
        }
    }

    @OnClick({R.id.iv_more})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.iv_more:
                MorePopWindow morePopWindow = new MorePopWindow(MainActivity.this);
                morePopWindow.showPopupWindow(iv_more);
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }

    private Fragment initConversationList() {
        if (mConversationListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
            Uri uri;
            if (BuildConfig.DEBUG) {
                uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                        .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
                        .build();
                mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                        Conversation.ConversationType.GROUP,
                        Conversation.ConversationType.PUBLIC_SERVICE,
                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
                        Conversation.ConversationType.SYSTEM,
                        Conversation.ConversationType.DISCUSSION
                };

            } else {
                uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                        .build();
                mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                        Conversation.ConversationType.GROUP,
                        Conversation.ConversationType.PUBLIC_SERVICE,
                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
                        Conversation.ConversationType.SYSTEM
                };
            }
            listFragment.setUri(uri);
            mConversationListFragment = listFragment;
            return listFragment;
        } else {
            return mConversationListFragment;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCountChanged(int count) {
        if (count == 0) {
            mUnreadNumView.setVisibility(View.GONE);
        } else if (count > 0 && count < 100) {
            mUnreadNumView.setVisibility(View.VISIBLE);
            mUnreadNumView.setText(String.valueOf(count));
        } else {
            mUnreadNumView.setVisibility(View.VISIBLE);
            mUnreadNumView.setText(R.string.no_read_message);
        }
    }


}
