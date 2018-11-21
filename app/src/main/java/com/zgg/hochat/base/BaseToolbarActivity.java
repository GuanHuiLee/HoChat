package com.zgg.hochat.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;


import com.anthonycr.grant.PermissionsManager;
import com.zgg.hochat.R;
import com.zgg.hochat.utils.ToastUtils;
import com.zgg.hochat.widget.MLoadingDialog;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 类描述：Activity 基类
 * 创建人：mhl
 * 创建时间：2016/10/10 12:03
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public abstract class BaseToolbarActivity extends AppCompatActivity implements BaseView {

    protected String TAG;

    private Unbinder unbinder;
    public Context mContext;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    /**
     * 业务逻辑类
     */
    protected List<BasePresenter> presenterList = new LinkedList<BasePresenter>();

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);

        initToolbar(toolbar);
        setSupportActionBar(toolbar);

        initTAG(this);
        initUI();
        initData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        for (BasePresenter basePresenter : presenterList) {
            basePresenter.onCreate();
        }
    }


    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    @Override
    protected void onStop() {
        super.onStop();
        for (BasePresenter basePresenter : presenterList) {
            basePresenter.stop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (BasePresenter basePresenter : presenterList) {
            basePresenter.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (BasePresenter basePresenter : presenterList) {
            basePresenter.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (BasePresenter basePresenter : presenterList) {
            basePresenter.destroy();
        }
        presenterList.clear();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }

    /**
     * 日志TAG初始化
     *
     * @param context
     */
    protected void initTAG(Context context) {
        TAG = context.getClass().getSimpleName();
    }


    /**
     * 业务逻辑类加入生命周期管理
     *
     * @param BasePresenter
     */
    public void addPresenter(BasePresenter BasePresenter) {
        presenterList.add(BasePresenter);
    }

    /**
     * 业务逻辑类脱离生命周期管理
     *
     * @param BasePresenter
     */
    public void removePresenter(BasePresenter BasePresenter) {
        presenterList.remove(BasePresenter);
    }

    @Override
    public void showProgress(String message) {
        MLoadingDialog.show(this, message);
    }

    @Override
    public void hideProgress() {
        MLoadingDialog.dismiss();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void showError(String message) {
        ToastUtils.showShort(message);
        if (!isDestroyed()) {
            hideProgress();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    /**
     * 界面初始化
     */
    protected abstract void initUI();

    /**
     * 数据初始化
     */
    protected abstract void initData();

    abstract protected void initToolbar(Toolbar toolbar);

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
