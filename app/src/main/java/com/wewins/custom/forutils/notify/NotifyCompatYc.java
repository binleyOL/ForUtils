package com.wewins.custom.forutils.notify;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.wewins.custom.forutils.R;

/**
 * 被NotifyManager控制
 * <br/>
 * 安卓8.0通知的兼容类哦
 * <br/>
 * 安卓8.0中，为了更好的管制通知的提醒，不想一些不重要的通知打扰用户，新增了通知渠道，用户可以根据渠道来屏蔽一些不想要的通知.
 * <b color="#008000">Created by Binley at 2019/1/2.<b/>
 */
public class NotifyCompatYc {
    private static final String TAG = NotifyCompatYc.class.getName();

    public static final String QFMD_CHANNEL_ID = "com.oms.mingdeng";
    public static final String QFMD_CHANNEL_NAME = "祈福明燈";
    public static final String LJMS_DEFAULT_CHANNEL_NAME = "靈機妙算";
    public static final String LJMS_CHANNEL_ID = "com.oms.mmcnotity";
    public static final String XYS_CHANNEL_ID = "com.oms.xuyuanshu";
    public static final String XYS_CHANNEL_NAME = "許願樹";

    public static void setONotifyChannel(NotificationManager manager, NotificationCompat.Builder builder, String channeId, String channelName) {
        if (TextUtils.isEmpty(channeId)||TextUtils.isEmpty(channelName)){
            Log.d(TAG, "NotifyCompatYc:  ".concat("安卓8.0的通知兼容库中 channeId 与 channelName 不能为empty"));
        }
        if (Build.VERSION.SDK_INT >= 26) {
            //第三个参数设置通知的优先级别
            NotificationChannel channel =
                    new NotificationChannel(channeId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.canBypassDnd();//是否可以绕过请勿打扰模式
            channel.canShowBadge();//是否可以显示icon角标
            channel.enableLights(true);//是否显示通知闪灯
            channel.enableVibration(true);//收到小时时震动提示
            channel.setBypassDnd(true);//设置绕过免打扰
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_SECRET);
            channel.setLightColor(Color.RED);//设置闪光灯颜色
            channel.getAudioAttributes();//获取设置铃声设置
            channel.setVibrationPattern(new long[]{100, 200, 100});//设置震动模式
            channel.shouldShowLights();//是否会闪光
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
            if (builder != null) {
                builder.setChannelId(channeId);//这个id参数要与上面channel构建的第一个参数对应
            }
        }
    }

    public static void setONotifyChannel(NotificationManager manager, String channeId, String channelName) {
        setONotifyChannel(manager,null,channeId,channelName);
    }

    public static Notification getNotification(Context context, String channelId) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        return notification;
    }

}
