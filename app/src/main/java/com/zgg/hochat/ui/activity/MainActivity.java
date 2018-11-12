package com.zgg.hochat.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.zgg.hochat.R;
import com.zgg.hochat.ui.base.BaseActivity;
import com.zgg.hochat.ui.fragment.ContactsFragment;
import com.zgg.hochat.ui.fragment.ConversationFragmentEx;
import com.zgg.hochat.ui.fragment.DiscoverFragment;
import com.zgg.hochat.ui.fragment.MineFragment;

import butterknife.BindView;
import butterknife.OnCheckedChanged;

public class MainActivity extends BaseActivity {
    @BindView(R.id.rb_chat)
    RadioButton rbChat;

    @BindView(R.id.rb_contacts)
    RadioButton rbContacts;

    @BindView(R.id.rb_find)
    RadioButton rbFind;

    @BindView(R.id.rb_mine)
    RadioButton rbMine;

    private FragmentManager fragmentManager;

    /**
     * 当前选中View
     */
    private CompoundButton selectView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initUI() {
        rbChat.setTag(ConversationFragmentEx.TAG);
        rbContacts.setTag(ContactsFragment.TAG);
        rbFind.setTag(DiscoverFragment.TAG);
        rbMine.setTag(MineFragment.TAG);
    }

    @Override
    protected void initData() {
        fragmentManager = getSupportFragmentManager();
    }

    private void showFragment(CompoundButton curView) {
        if (selectView != null) {
            selectView.setChecked(false);
        }
        selectView = curView;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String tag = (String) curView.getTag();
        if (fragmentManager.findFragmentByTag(tag) == null) {
            Fragment fragment = Fragment.instantiate(this, tag);
            fragmentTransaction.add(R.id.fl_content, fragment, tag);
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            boolean isHide = fragmentManager.findFragmentByTag(tag).isHidden();
            if (isHide) {
                fragmentTransaction.show(fragmentManager.findFragmentByTag(tag));
                fragmentTransaction.commitAllowingStateLoss();
                fragmentManager.findFragmentByTag(tag).onResume(); //切换Fragment，实时刷新数据
            }
        }
    }

    private void hideFragment(CompoundButton lastView) {

        if (lastView != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            String tag = (String) lastView.getTag();
            if (fragmentManager.findFragmentByTag(tag) != null) {
                boolean isHide = fragmentManager.findFragmentByTag(tag).isHidden();
                if (!isHide) {
                    fragmentTransaction.hide(fragmentManager.findFragmentByTag(tag));
                    fragmentTransaction.commitAllowingStateLoss();


                }
            }
        }
    }

    @OnCheckedChanged({R.id.rb_chat, R.id.rb_contacts, R.id.rb_find, R.id.rb_mine})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            showFragment(buttonView);
        } else {
            hideFragment(buttonView);
        }
    }

    @Override
    public void onBackPressed() {

    }
}
