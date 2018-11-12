package com.zgg.hochat.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zgg.hochat.App;
import com.zgg.hochat.R;
import com.zgg.hochat.ui.base.BaseFragment;


/**
 * Created by AMing on 16/6/21.
 * Company RongCloud
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = MineFragment.class.getName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_mine, container, false);
        return mView;
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View view) {

    }
}
