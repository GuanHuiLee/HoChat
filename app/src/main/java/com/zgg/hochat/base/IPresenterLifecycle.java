package com.zgg.hochat.base;


/**
 * 类描述：presenter 生命周期规范
 * 创建人：mhl
 * 创建时间：2017/9/15  上午 11:26
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public interface IPresenterLifecycle {


    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onResume() onCreate.
     */
    void onCreate();


    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onResume() method.
     */
    void resume();

    /**
     * Method that controls the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onPause() method.
     */
    void pause();

    /**
     * Method that controls the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onStop() method.
     */
    void stop();

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onDestroy() method.
     */
    void destroy();
}
