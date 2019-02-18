package com.wewins.custom.forutils;

import android.content.Context;

/**
 * <br/>
 * <b color="#008000">Created by Binley at 2019/2/14.<b/>
 */
public class BUBase {

    /**
     * 四舍五入
     * @return
     */
    public static final int round(float value) {
        return Math.round(value);
    }

    /**
     * 根据手机的分辨率PX(像素)转成DIP
     * @return
     */
    public static float px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return pxValue / scale;
    }

    /**
     * 根据手机分辨率从DIP转成PX(像素)
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * @return
     */
    public static float px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return pxValue / fontScale;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
