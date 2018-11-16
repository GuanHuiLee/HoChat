package com.zgg.hochat.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.zgg.hochat.R;
import com.zgg.hochat.base.BaseActivity;
import com.zgg.hochat.base.BaseToolbarActivity;
import com.zgg.hochat.bean.RegisterInput;
import com.zgg.hochat.bean.RegisterResult;
import com.zgg.hochat.bean.RegisterZggInput;
import com.zgg.hochat.http.contract.RegisterContract;
import com.zgg.hochat.http.model.AccountModel;
import com.zgg.hochat.http.presenter.RegisterPresenter;
import com.zgg.hochat.utils.AMUtils;
import com.zgg.hochat.utils.ClearEditTextView;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.http.POST;

/**
 * 注册页面
 */
public class RegisterActivity extends BaseToolbarActivity implements RegisterContract.View {
    @BindView(R.id.et_phone)
    ClearEditTextView et_phone;
    @BindView(R.id.et_pwd)
    ClearEditTextView et_pwd;

    private RegisterPresenter presenter;
    private RegisterInput registerInput;
    private String phone;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initData() {
        presenter = new RegisterPresenter(this, AccountModel.newInstance());
        addPresenter(presenter);
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {

    }

    @OnClick({R.id.btn_register})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                register();
                break;
        }
    }

    private void register() {
        phone = et_phone.getText().toString();
        password = et_pwd.getText().toString();
        if (!AMUtils.isMobile(phone)) {
            showError("请输入正确手机号");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showError("请输入密码");
            return;
        }

        registerInput = new RegisterInput(phone, password);
        presenter.register(registerInput);
    }

    @Override
    public void showRegisterResult(RegisterResult result) {
        presenter.registerZgg(new RegisterZggInput(phone, password));
    }

    @Override
    public void showRegisterZggResult(String result) {
        showError("注册成功");
        Intent intent = new Intent();
        intent.putExtra("data", registerInput);
        setResult(RESULT_OK, intent);
        finish();
    }
}
