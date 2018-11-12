package com.zgg.hochat;

import android.app.Application;

public class App extends Application {
    private static App mApp = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }

    public static final App getApplication() {
        return mApp;
    }
}
