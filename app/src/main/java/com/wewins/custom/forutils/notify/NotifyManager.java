package com.wewins.custom.forutils.notify;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.wewins.custom.forutils.R;

/**
 * 控制NotifyCompatYc
 * <br/>
 * <b color="#008000">Created by Binley at 2019/1/2.<b/>
 */
public class NotifyManager {

    // 单例开始
    private volatile static NotifyManager INSTANCE;

    private NotifyManager(Context context, Class<?> c) {
        initNotifyManager(context, c);
    }

    public static NotifyManager getInstance(Context context, Class<?> c) {
        if (INSTANCE == null) {
            synchronized (NotifyManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NotifyManager(context, c);
                }
            }
        }
        return INSTANCE;
    }
    // 单例结束


    private NotificationManager manager;
    // NotificationManagerCompat
    private NotificationCompat.Builder builder;

    //初始化通知栏配置
    private void initNotifyManager(Context context, Class<?> c) {
        context = context.getApplicationContext();
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // 如果存在则清除上一个消息
//        manager.cancel(news_flag);
        builder = new NotificationCompat.Builder(context,NotifyCompatYc.QFMD_CHANNEL_ID);

        NotifyCompatYc.setONotifyChannel(manager,builder,NotifyCompatYc.QFMD_CHANNEL_ID,NotifyCompatYc.QFMD_CHANNEL_NAME);

        // 设置标题
        builder.setContentTitle("标题1");
        // 状态栏的动画提醒语句
        builder.setTicker("动画提醒语句1");
        // 什么时候提醒的
        builder.setWhen(System.currentTimeMillis());
        // 设置通知栏的优先级
        builder.setPriority(Notification.PRIORITY_DEFAULT);
        // 设置点击可消失
        builder.setAutoCancel(true);
        // 设置是否震动等
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        // 设置icon
        builder.setSmallIcon(R.mipmap.ic_launcher);
        // 设置点击意图
        Intent intent = new Intent(context, c);
        Bundle bundle = new Bundle();
        bundle.putBoolean("form_notification_in", true);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 230, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
    }

    /**
     * 显示祈福明灯过期通知
     */
    public void showQiFuLampOutOfDateNotify(Context context) {
        // 设置内容
        builder.setContentText("这里是消息1的内容");
        manager.notify(13251, builder.build());
    }

    public void showQiFuLampBlessNotify(Context context) {
        builder.setContentText("这里是消息2的内容");
        manager.notify(13255, builder.build());
    }
}
