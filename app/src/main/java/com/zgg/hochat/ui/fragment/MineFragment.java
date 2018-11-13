package com.zgg.hochat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.zgg.hochat.R;
import com.zgg.hochat.base.BaseFragment;


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
