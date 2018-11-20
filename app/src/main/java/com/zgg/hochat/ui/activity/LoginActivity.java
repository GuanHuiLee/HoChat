package com.zgg.hochat.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.zgg.hochat.R;
import com.zgg.hochat.base.BaseToolbarActivity;
import com.zgg.hochat.bean.FindUserResult;
import com.zgg.hochat.bean.GetUserInfoByIdResult;
import com.zgg.hochat.bean.LoginInput;
import com.zgg.hochat.bean.LoginResult;
import com.zgg.hochat.bean.RegisterInput;
import com.zgg.hochat.bean.TokenResult;
import com.zgg.hochat.http.contract.AccountContract;
import com.zgg.hochat.http.contract.FindUserContract;
import com.zgg.hochat.http.contract.LoginContract;
import com.zgg.hochat.base.BaseActivity;
import com.zgg.hochat.http.model.AccountModel;
import com.zgg.hochat.http.model.FriendShipModel;
import com.zgg.hochat.http.presenter.AccountPresenter;
import com.zgg.hochat.http.presenter.FindUserPresenter;
import com.zgg.hochat.http.presenter.LoginPresenter;
import com.zgg.hochat.utils.AMUtils;
import com.zgg.hochat.utils.ClearEditTextView;
import com.zgg.hochat.utils.Constant;
import com.zgg.hochat.utils.DataUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

public class LoginActivity extends BaseToolbarActivity implements LoginContract.View, FindUserContract.View {
    @BindView(R.id.et_phone)
    ClearEditTextView etPhone;
    @BindView(R.id.et_pwd)
    ClearEditTextView etPwd;
    private LoginPresenter presenter;
    private FindUserPresenter accountPresenter;
    private String phone;
    private String pwd;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initUI() {
        presenter = new LoginPresenter(this, AccountModel.newInstance());
        addPresenter(presenter);
        accountPresenter = new FindUserPresenter(this, FriendShipModel.newInstance());
        addPresenter(accountPresenter);
    }

    @Override
    protected void initData() {
        RegisterInput user = DataUtil.getUser();
        String phone = user.getPhone();
        etPhone.setText(phone);
        String password = user.getPassword();
        etPwd.setText(password);

        if (!TextUtils.isEmpty(phone)) {
            etPhone.setSelection(phone.length());
        }
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        toolbar.setTitle("登录");
    }

    @OnClick({R.id.btn_login, R.id.tv_register})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_register:
                startActivityForResult(new Intent(mContext, RegisterActivity.class), Constant.REQUEST_CODE_REGISTER);
                break;
        }
    }

    private void login() {
        phone = etPhone.getText().toString();
        pwd = etPwd.getText().toString();

        if (!AMUtils.isMobile(phone)) {
            showError("请输入正确手机号");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            showError("请输入密码");
            return;
        }

        showProgress("登录中");
        presenter.login(new LoginInput("86", phone, pwd));
    }

    @Override
    public void showToken(TokenResult data) {

    }

    @Override
    public void showLoginResult(LoginResult data) {
        /**
         * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 {@link #init(Context)} 之后调用。</p>
         * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
         * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
         *
         * @param token    从服务端获取的用户身份令牌（Token）。
         * @param callback 连接回调。
         * @return RongIM  客户端核心类的实例。
         */
        final String token = data.getToken();
        DataUtil.setToken(token);
        DataUtil.setUser(phone, pwd);
        id = data.getId();
        DataUtil.setUserId(id);

        RongIM.connect(token, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                showError("token错误");
            }

            /**
             * 连接融云成功
             * @param s 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String s) {
                accountPresenter.findUserById(id);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                showError("IM连接失败：" + errorCode);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.REQUEST_CODE_REGISTER) {
                RegisterInput input = (RegisterInput) data.getSerializableExtra("data");
                etPhone.setText(input.getPhone());
                etPwd.setText(input.getPassword());
            }
        }
    }


    @Override
    public void showFindUserByPhoneResult(FindUserResult result) {

    }

    @Override
    public void showFindUserInfoByIdResult(GetUserInfoByIdResult result) {
        hideProgress();

        String nickName = result.getNickname();
        String portraitUri = result.getPortraitUri();
        DataUtil.setNickName(nickName);
        DataUtil.setUserPortrait(portraitUri);
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(id, nickName, Uri.parse(portraitUri)));

        showError("登录成功");
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }
}
