package com.zgg.hochat.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zgg.hochat.R;
import com.zgg.hochat.adapter.NewFriendListAdapter;
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

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.HttpException;


public class NewFriendListActivity extends BaseToolbarActivity implements
        NewFriendListAdapter.OnItemButtonClick, View.OnClickListener, AllFriendsContract.View, FriendRequestContract.View {

    private static final int GET_ALL = 11;
    private static final int AGREE_FRIENDS = 12;
    public static final int FRIEND_LIST_REQUEST_CODE = 1001;
    private ListView shipListView;
    private NewFriendListAdapter adapter;
    private String friendId;
    private TextView isData;
    private AllFriendsPresenter presenter;
    private FriendRequestPresenter friendRequestPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friendlist);
        initView();

        adapter = new NewFriendListAdapter(mContext);
        shipListView.setAdapter(adapter);
    }

    protected void initView() {
        setTitle(R.string.new_friends);
        shipListView = (ListView) findViewById(R.id.shiplistview);
        isData = (TextView) findViewById(R.id.isData);
    }


    @Override
    protected void onDestroy() {
        if (adapter != null) {
            adapter = null;
        }
        super.onDestroy();
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initData() {
        presenter = new AllFriendsPresenter(this, FriendShipModel.newInstance());
        addPresenter(presenter);
        presenter.getAllFriends();

        friendRequestPresenter = new FriendRequestPresenter(this, FriendShipModel.newInstance());
        addPresenter(friendRequestPresenter);
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        toolbar.setTitle("新的朋友");
    }

    private int index;

    @Override
    public boolean onButtonClick(int position, View view, int status) {
        index = position;
        switch (status) {
            case 11: //收到了好友邀请
                AllFriendsResult item = (AllFriendsResult) adapter.getItem(position);
                friendId = item.getUser().getId();
                friendRequestPresenter.agree(new AgreeInput(friendId));
                break;
            case 10: // 发出了好友邀请
                break;
            case 21: // 忽略好友邀请
                break;
            case 20: // 已是好友
                break;
            case 30: // 删除了好友关系
                break;
        }
        return false;
    }

    private Date stringToDate(AllFriendsResult resultEntity) {
        String updatedAt = resultEntity.getUpdatedAt();
        String updatedAtDateStr = updatedAt.substring(0, 10) + " " + updatedAt.substring(11, 16);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date updateAtDate = null;
        try {
            updateAtDate = simpleDateFormat.parse(updatedAtDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return updateAtDate;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(NewFriendListActivity.this, SearchFriendActivity.class);
        startActivityForResult(intent, FRIEND_LIST_REQUEST_CODE);
    }

    @Override
    public void showAllFriendsResult(List<AllFriendsResult> result) {

        if (result.size() == 0) {
            isData.setVisibility(View.VISIBLE);
            return;
        }

        Collections.sort(result, new Comparator<AllFriendsResult>() {

            @Override
            public int compare(AllFriendsResult lhs, AllFriendsResult rhs) {
                Date date1 = stringToDate(lhs);
                Date date2 = stringToDate(rhs);
                if (date1.before(date2)) {
                    return 1;
                }
                return -1;
            }
        });

        adapter.removeAll();
        adapter.addData(result);

        adapter.notifyDataSetChanged();
        adapter.setOnItemButtonClick(this);
    }

    @Override
    public void showInviteResult(ActionResult result) {

    }

    @Override
    public void showAgreeResult(ActionResult result) {
        showError("已同意");
        presenter.getAllFriends();
        EventBus.getDefault().post(new MessageEvent("agree"));
    }
}
