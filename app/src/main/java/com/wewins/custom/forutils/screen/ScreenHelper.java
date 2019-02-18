package com.wewins.custom.forutils.screen;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import com.wewins.custom.forutils.log.BULog;

import java.lang.reflect.InvocationTargetException;

/**
 * <br/>
 * <b color="#008000">Created by Binley at 2019/2/14.<b/>
 */
public class ScreenHelper {
    private static final String TAG = ScreenHelper.class.getName();

    /**
     * Get the screen height.
     *
     * @param context
     * @return the screen height
     */
    public static int height(Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            return size.y;
        }
        return display.getHeight();
    }

    /**
     * Get the screen width.
     *
     * @param context
     * @return the screen width
     */
    public static int width(Activity context) {

        Display display = context.getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            return size.x;
        }
        return display.getWidth();
    }

    /**
     * 获取屏幕参数
     * @param context
     * @return 参数0=屏幕总宽度<br/>参数1=屏幕总高度
     */
    public static int[] frame(Activity context) {
        int[] frame = new int[2];
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        frame[0] = dm.widthPixels;
        frame[1] = dm.heightPixels;
        return frame;
    }

    /** 获取屏幕的宽px和高px：[宽度, 高度]*/
    public static Point realSize(Activity context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (IllegalAccessException e) {} catch (InvocationTargetException e) {} catch (NoSuchMethodException e) {}
        } else {
            DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
            display.getMetrics(outMetrics);// 给白纸设置宽高
            size.x = outMetrics.widthPixels;
            size.y = outMetrics.heightPixels;
        }
        BULog.d("getRealScreenSize屏幕宽、高："+size.x+","+size.y);
        return size;
    }

    /**
     * 顶部状态栏高度
     * <br/>
     * 获取状态栏高度*/
    public static int statusBarHeight(Activity context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 标题栏
     * <br/>
     * 获取标题栏高度。
     * 在页面没有启动好之前调用的话titleBarHeight的值一直是0*/
    public static int titleBarHeightAfterDisplayed(Activity context) {
        Rect frame = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        // 状态栏高度
        int statusBarHeight = frame.top;
        View v = context.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        int contentTop = v.getTop();
        // statusBarHeight是上面所求的状态栏的高度
        int titleBarHeight = contentTop - statusBarHeight;
        return titleBarHeight;
    }

    /** 获取状态栏高度＋标题栏(ActionBar)高度*/
    public static int topBarHeight(Activity activity) {
        return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
    }

    /**
     * 导航栏
     * <br/>
     * 获取导航栏高度*/
    public static int navigationBarHeight(Context context) {
        boolean isHasNavigationBar = false;
        if(Build.VERSION.SDK_INT > 13) {
            boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            isHasNavigationBar = !hasMenuKey && !hasBackKey;
        } else {
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            isHasNavigationBar = !hasBackKey;
        }
        Log.v(TAG, "isHasNavigationBar="+isHasNavigationBar);
        if (isHasNavigationBar) {
            Resources resources = context.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            // 获取NavigationBar的高度
            int height = resources.getDimensionPixelSize(resourceId);
            return height;
        } else {
            return 0;
        }
    }

    public static Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        BULog.d("getAppUsableScreenSize屏幕宽、高："+size.x+","+size.y);
        return size;
    }

    /** 全网唯一能兼容所有手机（包括全面屏）判断是否有导航栏的方法 */
    public static boolean hasNavigationBar(Activity context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = realSize(context);
        int statusBarHeight = statusBarHeight(context);
        BULog.d("statusBarHeight="+statusBarHeight);
        return appUsableSize.y + statusBarHeight < realScreenSize.y;
    }
}
