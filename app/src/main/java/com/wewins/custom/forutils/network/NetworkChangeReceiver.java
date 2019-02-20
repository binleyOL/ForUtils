package com.wewins.custom.forutils.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;

import com.wewins.custom.forutils.log.BULog;

/**
 * 手机网络变化监听器
 * <br/>
 * 引入权限android.permission.ACCESS_NETWORK_STATE
 * <br/>
 * 引入权限android.permission.ACCESS_WIFI_STATE
 * <br/>
 * <b color="#008000">Created by Binley at 2019/2/19.<b/>
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    public static final String BROADCAST_ACTION_NETWORK_MODE_CHANGED = "broadcast action network mode changed";
    public static final String BROADCAST_ACTION_WIFI_DISCONNECTED = "broadcast action wifi disconnected";
    public static final String BROADCAST_ACTION_WIFI_CONNECTED = "broadcast action wifi connected";
    public static final String EXTRA_KEY_NETWORK_MODE = "key network mode";

    private int wifiState;//WiFi状态
    private String networkActionName;//网络名字
    private int wifiConnectionState;//路由有效还是无效

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            BULog.i("<获取手机的联网模式>");
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            String connType = BUNetworkAndWifi.getConnType(info);
            networkActionName = connType;
        } else if(action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            BULog.v("<Wi-Fi变化了>");
            //拿到wifi的状态值
            int wifiState =  intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLING:
                    BULog.v("-正在关Wi-Fi");
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    BULog.v("-已关Wi-Fi");
                    sendWifiDisconnected(context);
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    BULog.v("-正在打开Wi-Fi");
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    BULog.v("-已开Wi-Fi");
                    break;
                default:
                    BULog.v("其他未知情况");
                    break;
            }
            this.wifiState = wifiState;
        } else if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            BULog.v("<监听wifi的连接状态即是否连接的一个有效的无线路由>");
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (parcelableExtra != null){
                // 获取联网状态的NetWorkInfo对象
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                //获取的State对象则代表着连接成功与否等状态
                NetworkInfo.State state = networkInfo.getState();
                //判断网络是否已经连接
                boolean isConnected = state == NetworkInfo.State.CONNECTED;
                if (isConnected) {
                    wifiConnectionState = 2;
                    BULog.v("<<有效");
                } else {
                    BULog.v("<<无效");
                    wifiConnectionState = 1;
                }
            }
        }
    }

    private static void sendConnType(Context context, String connType) {
        BULog.d("------BLNetworkChangeReceiver发出广播------sendConnType()------网络状态改变");
        Intent intent = new Intent();
        intent.setAction(BROADCAST_ACTION_NETWORK_MODE_CHANGED);
        intent.putExtra(EXTRA_KEY_NETWORK_MODE, connType);
        context.sendBroadcast(intent);
    }

    /** WiFi断开了*/
    private static void sendWifiDisconnected(Context context) {
        BULog.d("------BLNetworkChangeReceiver发出广播------sendWifiDisconnected()------WiFi掉线");
        Intent intent = new Intent();
        intent.setAction(BROADCAST_ACTION_WIFI_DISCONNECTED);
        context.sendBroadcast(intent);
    }

    /** WiFi连上了*/
    private static void sendWifiConnected(Context context) {
        BULog.d("------BLNetworkChangeReceiver发出广播------sendWifiConnected()------WiFi已连上");
        Intent intent = new Intent();
        intent.setAction(BROADCAST_ACTION_WIFI_CONNECTED);
        context.sendBroadcast(intent);
    }


    //
    //
    //
    //			处理收到的系统广播  1018 08 16
    //
    //


    //经个人观察 发现
    //断开WiFi的时候，依次会执行"<<无效"，"<<无效"，"-正在关Wi-Fi"，"-已关Wi-Fi"，手机联网模式为'空'，手机联网模式为'3G'
    //移动网络未打开情况下，打开wifi的时候，依次会执行"-正在打开Wi-Fi"，"-已开Wi-Fi"，<<无效，<<无效，<<无效，<<有效，网络模式：wifi
    //Wi-Fi已连接的情况下，打开手机网络，依次会执行"手机联网模式为'空'"，"网络模式：wifi"
    //移动网络打开情况下，打开wifi的时候，依次会执行"-正在打开Wi-Fi"，"-已开Wi-Fi"，<<无效，<<无效，<<无效，<<有效，网络模式：wifi，手机联网模式为'空'，网络模式：wifi



}
