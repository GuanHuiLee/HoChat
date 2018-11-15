package com.zgg.hochat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zgg.hochat.App;
import com.zgg.hochat.R;
import com.zgg.hochat.base.BaseActivity;
import com.zgg.hochat.base.BaseToolbarActivity;
import com.zgg.hochat.bean.FindUserResult;
import com.zgg.hochat.bean.Friend;
import com.zgg.hochat.http.contract.FindUserContract;
import com.zgg.hochat.http.model.FriendShipModel;
import com.zgg.hochat.http.presenter.FindUserPresenter;
import com.zgg.hochat.utils.AMUtils;
import com.zgg.hochat.utils.DataUtil;
import com.zgg.hochat.widget.SelectableRoundedImageView;

import butterknife.BindView;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imlib.model.UserInfo;
import retrofit2.HttpException;

public class SearchFriendActivity extends BaseToolbarActivity implements FindUserContract.View {

    private static final int CLICK_CONVERSATION_USER_PORTRAIT = 1;
    private static final int SEARCH_PHONE = 10;
    private static final int ADD_FRIEND = 11;

    @BindView(R.id.search_edit)
    EditText mEtSearch;

    @BindView(R.id.search_result)
    LinearLayout searchItem;
    @BindView(R.id.search_name)
    TextView searchName;

    @BindView(R.id.search_header)
    SelectableRoundedImageView searchImage;

    private String mPhone;
    private String addFriendMessage;
    private String mFriendId;

    private Friend mFriend;
    private FindUserPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle((R.string.search_friend));

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    mPhone = s.toString().trim();
                    if (!AMUtils.isMobile(mPhone)) {
                        showError("非法手机号");
                        return;
                    }
                    hintKbTwo();
                    presenter.findUserByPhone(mPhone);
                } else {
                    searchItem.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initData() {
        presenter = new FindUserPresenter(this, FriendShipModel.newInstance());
        addPresenter(presenter);
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {

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

    private boolean isFriendOrSelf(String id) {
        String inputPhoneNumber = mEtSearch.getText().toString().trim();
        String selfPhoneNumber = DataUtil.getUser().getPhone();
        if (inputPhoneNumber != null) {
            if (inputPhoneNumber.equals(selfPhoneNumber)) {
                mFriend = new Friend(DataUtil.getUserId(), selfPhoneNumber, Uri.parse(DataUtil.getUserPortrait()));
                return true;
            } else {
//                mFriend = SealUserInfoManager.getInstance().getFriendByID(id);
                if (mFriend != null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void showFindUserResult(FindUserResult result) {
        ImageLoader.getInstance().displayImage(result.getPortraitUri(), searchImage);
        searchName.setText(result.getNickname());
        mFriendId = result.getId();
        searchItem.setVisibility(View.VISIBLE);
        searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFriendOrSelf(mFriendId)) {
                    Intent intent = new Intent(SearchFriendActivity.this, UserDetailActivity.class);
                    intent.putExtra("friend", mFriend);
                    intent.putExtra("type", CLICK_CONVERSATION_USER_PORTRAIT);
                    startActivity(intent);
                } else {

                }
            }
        });
    }
}
