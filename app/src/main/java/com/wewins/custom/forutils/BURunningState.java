package com.wewins.custom.forutils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

import com.wewins.custom.forutils.log.BULog;

import java.util.List;

/**
 * 类说明：<br />
 * 判断应用是否处于后台运行
 * @author binley
 * <br />
 * E-mail: wangbl@we-wins.com
 * @version 1.0
 * <br />
 * Time create:2018年2月11日 上午11:14:29
 *
 */
public class BURunningState {

    private BURunningState() {
        throw new UnsupportedOperationException("不可以被初始化...");
    }

	
	/**
     *判断当前应用程序处于前台还是后台。需要权限<uses-permission android:name="android.permission.GET_TASKS" />  
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            BULog.v("topActivity包名="+topActivity.getPackageName()+"|本应用包名="+context.getPackageName());
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     *判断当前应用程序处于前台还是后台
     */
	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					BULog.v("后台"+appProcess.processName);
					return true;
				} else {
//					BULog.v("前台"+appProcess.processName);
					return false;
				}
			}
		}
		return false;
	}
	
	
	/**
     * 判断某个Activity 界面是否在前台</br>
     * 需要权限<uses-permission android:name="android.permission.GET_TASKS" />
     * @param context
     * @param className 某个界面名称
     * @return                  
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
		List<RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
