package com.zgg.hochat.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.zgg.hochat.App;

/**
 * ================================================
 * 作    者：loujingying@aliyun.com
 * 版    本：1.0.0
 * 创建日期：2017/8/4
 * 描    述：Toast工具类
 * 修订历史：
 * ================================================
 */

public abstract class ToastUtils {

    private static final int CHAR_MSG = 1;
    private static final int RES_MSG = 2;

    private ToastUtils(){}

    private static Handler mDelivery = new Handler(Looper.getMainLooper(), new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            int arg1;

            switch (msg.what) {
                case CHAR_MSG:
                    String message = (String) msg.obj;
                    arg1 = msg.arg1;
                    showTextMessage(message,arg1);
                    break;
                case RES_MSG:
                    int resId = (int) msg.obj;
                    arg1 = msg.arg1;
                    showResMessage(resId,arg1);
                    break;
                default:
                    break;
            }

            return false;
        }
    });

    private static boolean isOnUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    private static Context getAppContext() {
        return App.getApplication();
    }

    public static void showShort(CharSequence message) {
        show(message, Toast.LENGTH_SHORT);
    }

    public static void showShort(int resId) {
        show(resId, Toast.LENGTH_SHORT);
    }

    public static void showLong(String message) {
        show(message, Toast.LENGTH_LONG);
    }

    public static void showLong(int resId) {
        show(resId, Toast.LENGTH_LONG);
    }

    public static void show(CharSequence message, int duration) {
        if (isOnUiThread()) {
            showTextMessage(message, duration);
            return;
        }

        Message msg = Message.obtain();
        msg.obj = message;
        msg.arg1 = duration;
        msg.what = CHAR_MSG;
        mDelivery.sendMessage(msg);
    }

    private static void showTextMessage(CharSequence message, int duration) {
        Toast.makeText(getAppContext(), message, duration).show();
    }

    public static final void show(int resId, int duration) {
        if (isOnUiThread()) {
            showResMessage(resId, duration);
            return;
        }

        Message msg = Message.obtain();
        msg.obj = resId;
        msg.arg1 = duration;
        msg.what = RES_MSG;
        mDelivery.sendMessage(msg);

    }

    private static void showResMessage(int resId, int duration) {
        Toast.makeText(getAppContext(), resId, duration).show();
    }


}
