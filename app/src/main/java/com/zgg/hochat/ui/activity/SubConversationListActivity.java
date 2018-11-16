package com.zgg.hochat.ui.activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.zgg.hochat.R;
import com.zgg.hochat.adapter.SubConversationListAdapterEx;
import com.zgg.hochat.base.BaseToolbarActivity;

import io.rong.imkit.RongContext;
import io.rong.imkit.fragment.SubConversationListFragment;

public class SubConversationListActivity extends BaseToolbarActivity {

    private String type;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subconversationlist);

        SubConversationListFragment fragment = new SubConversationListFragment();
        fragment.setAdapter(new SubConversationListAdapterEx(RongContext.getInstance()));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rong_content, fragment);
        transaction.commit();

        Intent intent = getIntent();
        if (intent.getData() == null) {
            return;
        }
        //聚合会话参数
        type = intent.getData().getQueryParameter("type");

        if (type == null)
            return;

        if (type.equals("group")) {
            toolbar.setTitle(R.string.de_actionbar_sub_group);
        } else if (type.equals("private")) {
            toolbar.setTitle(R.string.de_actionbar_sub_private);
        } else if (type.equals("discussion")) {
            toolbar.setTitle(R.string.de_actionbar_sub_discussion);
        } else if (type.equals("system")) {
            toolbar.setTitle(R.string.de_actionbar_sub_system);
        } else {
            toolbar.setTitle(R.string.de_actionbar_sub_defult);
        }
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }
}
