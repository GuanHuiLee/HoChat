package com.zgg.hochat;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.tencent.mmkv.MMKV;

import io.rong.imageloader.core.DisplayImageOptions;
import io.rong.imageloader.core.display.FadeInBitmapDisplayer;
import io.rong.imkit.RongIM;

public class App extends Application {
    private static App mApp = null;
    private static DisplayImageOptions options;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName
                .equals(getCurProcessName(getApplicationContext())) || "io.rong.push"
                .equals(getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(this);
        }
        MMKV.initialize(this);

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.de_default_portrait)
                .showImageOnFail(R.drawable.de_default_portrait)
                .showImageOnLoading(R.drawable.de_default_portrait)
                .displayer(new FadeInBitmapDisplayer(300))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static DisplayImageOptions getOptions() {
        return options;
    }

    public static final App getApplication() {
        return mApp;
    }
}
