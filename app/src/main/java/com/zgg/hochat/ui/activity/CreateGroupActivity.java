package com.zgg.hochat.ui.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.zgg.hochat.R;
import com.zgg.hochat.base.BaseToolbarActivity;
import com.zgg.hochat.bean.CreateCroupInput;
import com.zgg.hochat.bean.CreateGroupResult;
import com.zgg.hochat.bean.Friend;
import com.zgg.hochat.bean.GetGroupDetailResult;
import com.zgg.hochat.bean.GetGroupsResult;
import com.zgg.hochat.bean.MessageEvent;
import com.zgg.hochat.http.contract.GroupContract;
import com.zgg.hochat.http.model.GroupModel;
import com.zgg.hochat.http.presenter.GroupPresenter;
import com.zgg.hochat.utils.DataUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.rong.imkit.emoticon.AndroidEmoji;
import io.rong.imkit.widget.AsyncImageView;

/**
 * Created by AMing on 16/1/25.
 * Company RongCloud
 */
public class CreateGroupActivity extends BaseToolbarActivity implements View.OnClickListener, GroupContract.View {

    private static final int GET_QI_NIU_TOKEN = 131;
    private static final int CREATE_GROUP = 16;
    private static final int SET_GROUP_PORTRAIT_URI = 17;
    public static final String REFRESH_GROUP_UI = "REFRESH_GROUP_UI";
    private String mGroupName, mGroupId;
    private List<String> groupIds = new ArrayList<>();
    private Uri selectUri;
    private String imageUrl;
    private GroupPresenter groupsPresenter;

    @BindView(R.id.create_groupname)
    EditText mGroupNameEdit;
    @BindView(R.id.img_Group_portrait)
    AsyncImageView asyncImageView;
    @BindView(R.id.create_ok)
    Button mButton;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        setTitle(R.string.rc_item_create_group);
        List<Friend> memberList = (List<Friend>) getIntent().getSerializableExtra("GroupMember");
        initView();
        if (memberList != null && memberList.size() > 0) {
            groupIds.add(DataUtil.getUserId());
            for (Friend f : memberList) {
                groupIds.add(f.getUserId());
            }
        }
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initData() {
        groupsPresenter = new GroupPresenter(this, GroupModel.newInstance());
        addPresenter(groupsPresenter);
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {

    }


    private void initView() {
        asyncImageView.setOnClickListener(this);
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_Group_portrait://选择群头像
                break;
            case R.id.create_ok:
                mGroupName = mGroupNameEdit.getText().toString().trim();
                if (TextUtils.isEmpty(mGroupName)) {
                    showError(getString(R.string.group_name_not_is_null));
                    break;
                }
                if (mGroupName.length() == 1) {
                    showError(getString(R.string.group_name_size_is_one));
                    return;
                }
                if (AndroidEmoji.isEmoji(mGroupName)) {
                    if (mGroupName.length() <= 2) {
                        showError(getString(R.string.group_name_size_is_one));
                        return;
                    }
                }
                if (groupIds.size() > 1) {//创建群
                    showProgress("正在创建中");
                    groupsPresenter.createGroup(new CreateCroupInput(mGroupName, groupIds));
                }

                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        hintKbTwo();
        finish();
        return super.onOptionsItemSelected(item);
    }


    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void showCreateGroupResult(CreateGroupResult result) {
        showError("创建成功");
        finish();

        EventBus.getDefault().post(new MessageEvent("group"));
    }

    @Override
    public void showGetGroupsResult(List<GetGroupsResult> result) {

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
