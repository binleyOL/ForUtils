package com.wewins.custom.forutils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * <br/>
 * <b color="#008000">Created by Binley at 2019/2/19.<b/>
 */
public class BUApkVersionCode {

    private static String[] version = new String[2];

    private BUApkVersionCode() {
        throw new UnsupportedOperationException("不可以被初始化...");
    }

    /**
     * 获取当前本地apk的版本号
     * @param context
     * @return 版本号
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        if(version == null) {
            initVersion(context);
        }
        versionCode = Integer.valueOf(version[0]);
        return versionCode;
    }

    /**
     * 获取版本号名称
     * @param context
     * @return 版本号名称
     */
    public static String getVersionName(Context context) {
        if(version == null) {
            initVersion(context);
        }
        String verName = version[1];
        return verName;
    }

    /** 获取版本号 和 版本号名称*/
    private static void initVersion(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version[0] = String.valueOf(pi.versionCode);
            version[1] = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
