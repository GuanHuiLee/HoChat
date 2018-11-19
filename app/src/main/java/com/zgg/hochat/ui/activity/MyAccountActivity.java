package com.zgg.hochat.ui.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zgg.hochat.App;
import com.zgg.hochat.R;
import com.zgg.hochat.base.BaseToolbarActivity;
import com.zgg.hochat.utils.DataUtil;
import com.zgg.hochat.widget.SelectableRoundedImageView;

import io.rong.imageloader.core.ImageLoader;


public class MyAccountActivity extends BaseToolbarActivity implements View.OnClickListener {

    private static final int UP_LOAD_PORTRAIT = 8;
    private static final int GET_QI_NIU_TOKEN = 128;
    private SelectableRoundedImageView mImageView;
    private TextView mName;
    private String imageUrl;
    private Uri selectUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);
        initView();
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        toolbar.setTitle("个人账号");
    }

    private void initView() {
        TextView mPhone = (TextView) findViewById(R.id.tv_my_phone);
        RelativeLayout portraitItem = (RelativeLayout) findViewById(R.id.rl_my_portrait);
        RelativeLayout nameItem = (RelativeLayout) findViewById(R.id.rl_my_username);
        mImageView = (SelectableRoundedImageView) findViewById(R.id.img_my_portrait);
        mName = (TextView) findViewById(R.id.tv_my_username);
        portraitItem.setOnClickListener(this);
        nameItem.setOnClickListener(this);
        String cachePhone = DataUtil.getUser().getPhone();
        if (!TextUtils.isEmpty(cachePhone)) {
            mPhone.setText("+86 " + cachePhone);
        }
        String cacheName = DataUtil.getNickName();
        if (!TextUtils.isEmpty(cacheName)) {
            mName.setText(cacheName);
            ImageLoader.getInstance().displayImage(null, mImageView, App.getOptions());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_my_portrait:
                break;
            case R.id.rl_my_username:
                startActivityForResult(new Intent(this, UpdateNameActivity.class), 2);
                break;
        }
    }


    static public final int REQUEST_CODE_ASK_PERMISSIONS = 101;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            mName.setText(DataUtil.getNickName());
        }
    }


}
