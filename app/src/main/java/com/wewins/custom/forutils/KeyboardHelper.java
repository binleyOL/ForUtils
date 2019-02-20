package com.wewins.custom.forutils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.wewins.custom.forutils.log.BULog;

/**
 * 键盘操控工具类
 * <br/>
 * <b color="#008000">Created by Binley at 2019/1/15.<b/>
 */
public class KeyboardHelper {

    private KeyboardHelper() {
        throw new UnsupportedOperationException("不可以被初始化...");
    }

    /** 记录未被软键盘遮挡的屏幕的高*/
    private static int iDecorViewInvisibleHeightPre;
    private static ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;
    private static OnSoftInputChangedListener onSoftInputChangedListener;
    /** */
    private static int sDecorViewDelta = 0;

    private static int getDecorViewInvisibleHeight(final Activity activity) {
        // APP显示区域屏幕总高
        final View decorView = activity.getWindow().getDecorView();
        if (decorView == null) return iDecorViewInvisibleHeightPre;
        // 可视的屏幕高
        final Rect outRect = new Rect();
        decorView.getWindowVisibleDisplayFrame(outRect);
        BULog.d("decorView.getBottom()="+decorView.getBottom()+",outRect.bottom="+outRect.bottom);
        // 被遮挡的高 = APP显示区域屏幕总高 - 可视的屏幕高
        int delta = Math.abs(decorView.getBottom() - outRect.bottom);
        if (delta <= getNavBarHeight()) {
            // 被遮挡的高 <= 底部导航高
            sDecorViewDelta = delta;
            return 0;
        }
        return delta - sDecorViewDelta;
    }

    /** 获取导航栏高度*/
    private static int getNavBarHeight() {
        Resources res = Resources.getSystem();
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId != 0) {
            return res.getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    /** 获取Activity底层View*/
    private static final FrameLayout getActivityContentView(Activity activity) {
        return activity.findViewById(android.R.id.content);
    }

    /**
     * 注册软键盘改变监听器
     * @param activity The activity.
     * @param listener The soft input changed listener.
     */
    public static void registerSoftInputChangedListener(final Activity activity, final OnSoftInputChangedListener listener) {
        final int flags = activity.getWindow().getAttributes().flags;
        if ((flags & WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS) != 0) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        final FrameLayout contentView = getActivityContentView(activity);
        iDecorViewInvisibleHeightPre = getDecorViewInvisibleHeight(activity);
        BULog.d("初始iDecorViewInvisibleHeightPre="+iDecorViewInvisibleHeightPre);
        onSoftInputChangedListener = listener;
        onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (onSoftInputChangedListener != null) {
                    int height = getDecorViewInvisibleHeight(activity);
                    BULog.d("height="+height);
                    if (iDecorViewInvisibleHeightPre != height) {
                        onSoftInputChangedListener.onSoftInputChanged(height);
                        iDecorViewInvisibleHeightPre = height;
                    }
                    BULog.d("监控iDecorViewInvisibleHeightPre="+iDecorViewInvisibleHeightPre);
                    BULog.d("键盘监控被调用");
                }
            }
        };
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        BULog.d("添加键盘监控");
    }

    /** 移除监测键盘*/
    public static void removeLayoutChangeListener(View decorView){
        BULog.d("移除键盘监控");
        View contentView = decorView.findViewById(android.R.id.content);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            contentView.getViewTreeObserver().removeGlobalOnLayoutListener(onGlobalLayoutListener);
        } else {
            contentView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
        onGlobalLayoutListener = null;
    }

    /** 展示软键盘*/
    public static boolean showKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        BULog.v("展示键盘");
        return true;
    }

    /** 关闭软键盘*/
    public static void closeKeybord(Activity activity) {
        View v = activity.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && v != null) {
            if (v.getWindowToken() != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);//强制隐藏键盘
            }
        }
        BULog.v("关闭键盘");
    }

    /** 键盘改变回调*/
    public interface OnSoftInputChangedListener {
        void onSoftInputChanged(int height);
    }
}
