package com.zgg.hochat.ui.activity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zgg.hochat.R;
import com.zgg.hochat.base.BaseActivity;
import com.zgg.hochat.base.BaseToolbarActivity;
import com.zgg.hochat.bean.GetUserInfoByIdResult;
import com.zgg.hochat.bean.MessageEvent;
import com.zgg.hochat.bean.NickNameInput;
import com.zgg.hochat.http.contract.AccountContract;
import com.zgg.hochat.http.model.AccountModel;
import com.zgg.hochat.http.presenter.AccountPresenter;
import com.zgg.hochat.utils.ClearEditTextView;
import com.zgg.hochat.utils.DataUtil;

import org.greenrobot.eventbus.EventBus;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import retrofit2.HttpException;

/**
 * Created by AMing on 16/6/23.
 * Company RongCloud
 */
public class UpdateNameActivity extends BaseToolbarActivity implements View.OnClickListener, AccountContract.View {

    private static final int UPDATE_NAME = 7;
    private ClearEditTextView mNameEditText;
    private String newName;
    private AccountPresenter accountPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);
        mNameEditText = (ClearEditTextView) findViewById(R.id.update_name);
        String nickName = DataUtil.getNickName();
        mNameEditText.setText(nickName);
        mNameEditText.setSelection(nickName.length());

    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initData() {
        accountPresenter = new AccountPresenter(this, AccountModel.newInstance());
        addPresenter(accountPresenter);
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        toolbar.setTitle("昵称更改");
        TextView tv_other = toolbar.findViewById(R.id.toolbar_other);
        tv_other.setText("确认");
        tv_other.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        newName = mNameEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(newName)) {
            showProgress("更改中");
            accountPresenter.setNickName(new NickNameInput(newName));
        } else {
            showError("昵称不能为空");
        }
    }

    @Override
    public void showSetNickNameResult(String result) {
        String userId = DataUtil.getUserId();
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(userId, newName,
                null));
        RongIM.getInstance().setCurrentUserInfo(new UserInfo(
                userId, newName, null));

        DataUtil.setNickName(newName);

        showError(result);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showUserInfoResult(GetUserInfoByIdResult result) {

    }
}
