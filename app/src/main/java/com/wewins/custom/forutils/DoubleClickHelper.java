package com.wewins.custom.forutils;

import android.os.SystemClock;

/**
 * 双击判断工具类
 * <br/>
 * <b color="#008000">Created by Binley at 2019/1/2.<b/>
 */
public class DoubleClickHelper {

    private static final long[] TIME_ARRAY = new long[2]; // 数组的长度为2代表只记录双击操作

    /**
     * 是否在短时间内进行了双击操作
     */
    public static boolean isOnDoubleClick() {
        // 默认间隔时长
        return isOnDoubleClick(1500);
    }

    /**
     * 是否在短时间内进行了双击操作
     * @param time 间隔时间，单位：ms
     * @return
     */
    public static boolean isOnDoubleClick(int time) {
        System.arraycopy(TIME_ARRAY, 1, TIME_ARRAY, 0, TIME_ARRAY.length - 1);
        TIME_ARRAY[TIME_ARRAY.length - 1] = SystemClock.uptimeMillis();// 从开机到现在的毫秒数（手机睡眠的时间不包括在内）；
        return TIME_ARRAY[0] >= (SystemClock.uptimeMillis() - time);
    }

}
