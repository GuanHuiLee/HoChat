package com.zgg.hochat.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.TextView;

import com.zgg.hochat.R;


/**
 * 创建时间：2017/8/27 0027 10:42
 * 修改人：
 * 修改时间：
 * 修改备注：
 */

public class MLoadingDialog {

    private static Dialog mDialog;

    /**
     * 显示加载框
     */
    public static void show(Activity context) {
        show(context, "", true);
    }

    /**
     * 显示加载框
     */
    public static void show(Activity context, String info) {
        show(context, info, true);
    }

    /**
     * @param context
     * @param info
     * @param listener
     */
    public static void show(Activity context, String info, DialogInterface.OnCancelListener listener) {
        show(context, info, true, listener);
    }

    /**
     * 显示加载框
     */
    public static void show(Activity context, String info, boolean cancelable) {
        show(context, info, cancelable, null);
    }

    /**
     * @param context
     * @param info
     * @param cancelable
     * @param listener
     */
    public static void show(Activity context, String info, boolean cancelable, DialogInterface.OnCancelListener listener) {
        if (mDialog != null && mDialog.isShowing()) {
            return;
        }
        mDialog = new Dialog(context, R.style.showDialog);
        mDialog.setContentView(context.getLayoutInflater().inflate(R.layout.dialog_loading_layout, null));
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(cancelable);
        mDialog.setOnCancelListener(listener);
        TextView mInfo = (TextView) mDialog.findViewById(R.id.tv_msg);
        if (!TextUtils.isEmpty(info)) {
            mInfo.setText(info);
        }
        mDialog.show();
    }

    /**
     * 对话框是否显示
     *
     * @return
     */
    public static boolean isShow() {
        return mDialog != null && mDialog.isShowing();
    }

    /**
     * 隐藏加载框
     */
    public static void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mDialog = null;
    }
}
