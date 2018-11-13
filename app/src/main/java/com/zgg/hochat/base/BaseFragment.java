package com.zgg.hochat.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zgg.hochat.utils.ToastUtils;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 类描述：Fragment基类
 * 创建人：毛华磊
 * 创建时间：2016/10/10 12:03
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public abstract class BaseFragment extends Fragment implements BaseView {

    protected String TAG;

    protected Context mContext;

    private Unbinder unbinder;

    /**
     * 业务逻辑类
     */
    protected List<BasePresenter> presenterList = new LinkedList<BasePresenter>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this.getActivity();
        initTAG(this);
        Log.d(TAG, "into onCreate()");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "into onCreateView()");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "into onActivityCreated()");
        initUI();
        initData();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        Log.d(TAG, "into onViewCreated()");
    }


    @Override
    public void onResume() {
        super.onResume();
        for (BasePresenter basePresenter : presenterList) {
            basePresenter.resume();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        for (BasePresenter basePresenter : presenterList) {
            basePresenter.stop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        for (BasePresenter basePresenter : presenterList) {
            basePresenter.pause();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        for (BasePresenter basePresenter : presenterList) {
            basePresenter.destroy();
        }
        presenterList.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != unbinder) {
            unbinder.unbind();
        }
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


    /**
     * 日志TAG初始化
     *
     * @param fragment
     */
    protected void initTAG(Fragment fragment) {
        TAG = fragment.getClass().getSimpleName();
    }


    @Override
    public void showProgress(String message) {
    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void showError(String message) {
        ToastUtils.showShort(message);
    }

    /**
     * 界面初始化
     */
    protected abstract void initUI();

    /**
     * 数据初始化
     */
    protected abstract void initData();
}
