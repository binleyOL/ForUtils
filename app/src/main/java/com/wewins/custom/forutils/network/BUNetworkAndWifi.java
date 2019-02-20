package com.wewins.custom.forutils.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.wewins.custom.forutils.log.BULog;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * </br>
 * Created by Binley on 2018/3/15.
 */

public class BUNetworkAndWifi {

	private BUNetworkAndWifi() {
        throw new UnsupportedOperationException("不可以被初始化...");
	}

    /** 获取IP字符串*/
    public static String getIpAddress(Context context) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int iIP = mWifiManager.getConnectionInfo().getIpAddress();
        if(iIP == 0) return null;
        String sIP = (iIP & 0xFF) + "."
                + ((iIP >> 8) & 0xFF) + "."
                + ((iIP >> 16) & 0xFF) + "."
                + ((iIP >> 24) & 0xFF);
        BULog.d("self ip is : " + sIP);
        return sIP;
    }

    /**
     * 获得到当前的手机网络类型
     * @param context
     * @return 字符串"nil"-"wifi"-"2g"-"3g"-"4g"-""
     */
    public static final String getCurrentNetType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return getConnType(info);
    }

    public static final String getConnType(NetworkInfo info) {
        String connType = "";
        if (info == null) {
            connType = "nil";
        } else if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                connType = "wifi";
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                int subType = info.getSubtype();
                if (subType == TelephonyManager.NETWORK_TYPE_CDMA
                        || subType == TelephonyManager.NETWORK_TYPE_GPRS
                        || subType == TelephonyManager.NETWORK_TYPE_EDGE) {
                    connType = "2g";
                } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS
                        || subType == TelephonyManager.NETWORK_TYPE_HSDPA
                        || subType == TelephonyManager.NETWORK_TYPE_EVDO_A
                        || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
                        || subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
                    connType = "3g";
                } else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准
                    connType = "4g";
                } else {
                    connType = "nil";
                }
            }
        }
        BULog.d("网络模式：" + connType);
        return connType;
    }

    /**
     * get local ip address
     * @return
     */
    public static final String getInetIpAddress() {
        Enumeration<NetworkInterface> enumeration = null;
        try {
            enumeration = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            BULog.e(e.toString());
        }
        if (enumeration != null) {
            // 遍历所用的网络接口
            while (enumeration.hasMoreElements()) {
                NetworkInterface nif = enumeration.nextElement();// 得到每一个网络接口绑定的地址
                Enumeration<InetAddress> inetAddresses = nif.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                if (inetAddresses != null)
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress ip = inetAddresses.nextElement();
                        if (!ip.isLoopbackAddress() && isIPv4Address(ip.getHostAddress())) {
                            return ip.getHostAddress();
                        }
                    }
            }
        }
        return "";
    }

    /**
     * Ipv4 address check.
     */
    private static final Pattern IPV4_PATTERN = Pattern.compile("^(" +
            "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" +
            "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");

    /**
     * Check if valid IPV4 address.
     *
     * @param input the address string to check for validity.
     * @return True if the input parameter is a valid IPv4 address.
     */
    public static final boolean isIPv4Address(String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

    /**
     * check whether the wifiAp is Enable
     */
    public static final boolean isWifiApEnabled(Context context) {
        try {
            WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            Method method = manager.getClass().getMethod("isWifiApEnabled");
            return (Boolean) method.invoke(manager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }
}
