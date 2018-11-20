package com.zgg.hochat.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zgg.hochat.R;
import com.zgg.hochat.base.BaseActivity;
import com.zgg.hochat.base.BaseToolbarActivity;
import com.zgg.hochat.bean.AllFriendsResult;
import com.zgg.hochat.bean.Friend;
import com.zgg.hochat.bean.SetDisplayNameInput;
import com.zgg.hochat.http.contract.AllFriendsContract;
import com.zgg.hochat.http.model.FriendShipModel;
import com.zgg.hochat.http.presenter.AllFriendsPresenter;
import com.zgg.hochat.utils.ClearEditTextView;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import retrofit2.HttpException;

/**
 * Created by AMing on 16/8/10.
 * Company RongCloud
 */
@SuppressWarnings("deprecation")
public class NoteInformationActivity extends BaseToolbarActivity implements AllFriendsContract.View {

    private static final int SET_DISPLAYNAME = 12;
    private Friend mFriend;
    private ClearEditTextView mNoteEdit;
    private static final int CLICK_CONTACT_FRAGMENT_FRIEND = 2;
    private TextView tv_ok;

    private AllFriendsPresenter presenter;
    private String displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noteinfo);
        mNoteEdit = (ClearEditTextView) findViewById(R.id.notetext);
        mFriend = getIntent().getParcelableExtra("friend");

        if (mFriend != null) {
            tv_ok.setClickable(false);
            mNoteEdit.setText(mFriend.getDisplayName());
            mNoteEdit.setSelection(mNoteEdit.getText().length());
            mNoteEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!TextUtils.isEmpty(mFriend.getDisplayName())) {
                        tv_ok.setClickable(true);
                        tv_ok.setTextColor(getResources().getColor(R.color.color_white));
                    } else {
                        if (TextUtils.isEmpty(s.toString())) {
                            tv_ok.setClickable(false);
                            tv_ok.setTextColor(Color.parseColor("#9fcdfd"));
                        } else if (s.toString().equals(mFriend.getDisplayName())) {
                            tv_ok.setClickable(false);
                            tv_ok.setTextColor(Color.parseColor("#9fcdfd"));
                        } else {
                            tv_ok.setClickable(true);
                            tv_ok.setTextColor(getResources().getColor(R.color.color_white));
                        }
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initData() {
        presenter = new AllFriendsPresenter(this, FriendShipModel.newInstance());
        addPresenter(presenter);
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        toolbar.setTitle("备注信息");
        tv_ok = toolbar.findViewById(R.id.toolbar_other);
        tv_ok.setText("确定");
        tv_ok.setTextColor(Color.parseColor("#9fcdfd"));
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress("修改中");
                displayName = mNoteEdit.getText().toString();
                presenter.setDisplayName(new SetDisplayNameInput(mFriend.getUserId(), displayName));
            }
        });
    }


    public void finishPage(View view) {
        this.finish();
    }

    @Override
    public void showAllFriendsResult(List<AllFriendsResult> result) {

    }

    @Override
    public void showSetDisplayNameResult(String str) {
        showError(str);
        if (TextUtils.isEmpty(displayName)) {
            RongIM.getInstance().refreshUserInfoCache(new UserInfo(mFriend.getUserId(),
                    mFriend.getName(), mFriend.getPortraitUri()));
        } else {
            RongIM.getInstance().refreshUserInfoCache(new UserInfo(mFriend.getUserId(),
                    displayName, mFriend.getPortraitUri()));
        }
        Intent intent = new Intent(mContext, UserDetailActivity.class);
        intent.putExtra("type", CLICK_CONTACT_FRAGMENT_FRIEND);
        intent.putExtra("displayName", displayName);
        setResult(RESULT_OK, intent);
        finish();
    }
}
