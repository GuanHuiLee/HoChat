package com.zgg.hochat.ui.base;



/**
 * Presenter 基类
 */
public abstract class BasePresenter<V extends BaseView, M extends IModel> implements IPresenterLifecycle {

    /**
     * 关联View
     */
    protected V view;

    /**
     * 业务处理类
     */
    protected M model;

    /**
     * View 生命周期标志
     */
    protected boolean isAttach;

    public BasePresenter(V view, M model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void resume() {
        isAttach = true;
    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {
        isAttach = false;
        view = null;
        model = null;
    }
}
