package com.zgg.hochat.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zgg.hochat.R;
import com.zgg.hochat.base.BaseToolbarActivity;
import com.zgg.hochat.bean.InviteInput;
import com.zgg.hochat.bean.ActionResult;
import com.zgg.hochat.http.contract.FriendRequestContract;
import com.zgg.hochat.http.model.FriendShipModel;
import com.zgg.hochat.http.presenter.FriendRequestPresenter;
import com.zgg.hochat.utils.ClearEditTextView;
import com.zgg.hochat.utils.DataUtil;

import butterknife.BindView;

public class VerifyUserActivity extends BaseToolbarActivity implements FriendRequestContract.View {
    @BindView(R.id.ct_mark)
    ClearEditTextView ct_mark;

    private FriendRequestPresenter presenter;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_user);
    }

    @Override
    protected void initUI() {
        ct_mark.setText("你好，我是" + DataUtil.getUser().getPhone());
        userId = getIntent().getStringExtra("userId");
    }

    @Override
    protected void initData() {
        presenter = new FriendRequestPresenter(this, FriendShipModel.newInstance());
        addPresenter(presenter);
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        TextView tv_other = toolbar.findViewById(R.id.toolbar_other);
        tv_other.setText("发送");
        toolbar.setTitle("验证申请");
        tv_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.invite(new InviteInput(userId, ct_mark.getText().toString()));
            }
        });
    }

    @Override
    public void showInviteResult(ActionResult result) {
        showError(result.getAction());
        finish();
    }

    @Override
    public void showAgreeResult(ActionResult result) {

    }
}
