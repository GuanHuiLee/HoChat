package com.zgg.hochat.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zgg.hochat.R;
import com.zgg.hochat.base.BaseFragment;


public class DiscoverFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = DiscoverFragment.class.getName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatroom_list, container, false);
        return view;
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
