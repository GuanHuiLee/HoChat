package com.zgg.hochat.common;

import android.content.Context;

import com.orhanobut.logger.Logger;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;


public class NotificationReceiver extends PushMessageReceiver {

    /**
     * 用来接收服务器发来的通知栏消息(消息到达客户端时触发)
     * 默认return false，通知消息会以融云 SDK 的默认形式展现
     * 如果需要自定义通知栏的展示，在这里实现⾃己的通知栏展现代码，只要return true即可
     */

    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
        Logger.d(message.getPushContent());
        return false;
    }

    /**
     * ⽤户点击通知栏消息时触发 (注意:如果⾃定义了通知栏的展现，则不会触发)
     * 默认 return false
     * 如果需要自定义点击通知时的跳转，return true即可
     */
    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
        return false;
    }

}
