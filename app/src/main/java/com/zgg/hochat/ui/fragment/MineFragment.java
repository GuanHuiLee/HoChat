package com.zgg.hochat.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.zgg.hochat.App;
import com.zgg.hochat.R;
import com.zgg.hochat.base.BaseFragment;
import com.zgg.hochat.ui.activity.LoginActivity;
import com.zgg.hochat.ui.activity.MyAccountActivity;
import com.zgg.hochat.utils.DataUtil;
import com.zgg.hochat.utils.PortraitUtil;
import com.zgg.hochat.widget.SelectableRoundedImageView;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;


/**
 * Created by AMing on 16/6/21.
 * Company RongCloud
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.mine_header)
    SelectableRoundedImageView mSelectableRoundedImageView;
    @BindView(R.id.mine_name)
    TextView tv_name;

    public static final String TAG = MineFragment.class.getName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_mine, container, false);
        return mView;
    }

    @Override
    protected void initUI() {
        String phone = DataUtil.getNickName();
        UserInfo userInfo = new UserInfo(DataUtil.getUserId(), phone, null);
        ImageLoader.getInstance().displayImage(PortraitUtil.generateDefaultAvatar(userInfo), mSelectableRoundedImageView, App.getOptions());
        tv_name.setText(phone);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View view) {

    }

    @OnClick({R.id.mine_exit, R.id.start_user_profile})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.mine_exit:
                DataUtil.setToken("");
                RongIM.getInstance().logout();
                Intent loginActivityIntent = new Intent();
                loginActivityIntent.setClass(mContext, LoginActivity.class);
                loginActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(loginActivityIntent);
                break;
            case R.id.start_user_profile:
                startActivity(new Intent(mContext, MyAccountActivity.class));
                break;
        }
    }
}
