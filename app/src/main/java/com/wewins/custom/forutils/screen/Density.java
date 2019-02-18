package com.wewins.custom.forutils.screen;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.wewins.custom.forutils.BUBase;
import com.wewins.custom.forutils.log.BULog;

import java.lang.reflect.InvocationTargetException;

/**
 * 类说明：单例创建
 * 
 * @author binley <br />
 *         E-mail: wangbl@we-wins.com
 * @version 1.0 <br />
 *          Time create:2016年1月19日 下午3:16:07
 *
 */
public class Density extends BUBase {
	
	/** 指定设计出图的手机屏幕的density为3*/
	private static float TestDensity = 3f;

	private Context ctx;
	private volatile static Density INSTANCE = null;

	public int screenWidth = 0;
	public int screenHeight = 0;
	public float dpi = 0;
	public float density = 0;

	/**
	 * 宽度 百分比的像素。该数组长度101。xPersPx[0]==0，xPersPx[1]=1% * 屏幕宽度，xPersPx[2]=2% *
	 * 屏幕宽度，以此类推，xPersPx[100]=100% * 屏幕宽度
	 */
	private int[] xPersPx = null;
	/**
	 * 高度 百分比的像素。该数组长度101。yPersPx[0]==0，yPersPx[1]=1% * 屏幕高度，yPersPx[2]=2% *
	 * 屏幕高度，以此类推，yPersPx[100]=100% * 屏幕高度
	 */
	private int[] yPersPx = null;

    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////

	/**
	 * 获取实例
	 *
	 * @param ctx
	 * @return
	 */
	public static final Density getInstance(Context ctx) {
		if (INSTANCE == null) {
			synchronized (Density.class) {
				if (INSTANCE == null) {
					INSTANCE = new Density(ctx);
				}
			}
		}
		return INSTANCE;
	}

	private Density(Context ctx) {
		this.ctx = ctx;
		DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		dpi = dm.densityDpi;
		density = dm.density;

		setXYPers(screenWidth, screenHeight);
	}

	/** 初始化百分比 */
	private void setXYPers(int x, int y) {
		int count = 101;
		xPersPx = new int[count];
		yPersPx = new int[count];
		float xPer = x / 100.0f;
		float yPer = y / 100.0f;
		for (int i = 0; i < count; i++) {
			if (i == 0) {
				xPersPx[i] = 0;
				yPersPx[i] = 0;
			} else if (i < 100) {
				xPersPx[i] = round(xPer * i);
				yPersPx[i] = round(yPer * i);
			} else {
				xPersPx[i] = x;
				yPersPx[i] = y;
			}
		}
	}

    /** 获取 宽度 百分比数组 */
    public int[] getXPersPx() {
        return xPersPx;
    }

    /** 获取 高度 百分比数组 */
    public int[] getYPersPx() {
        return yPersPx;
    }

    /** 设置 测试用的手机的屏幕dip比率*/
    public void setTestDensity(float density) {
        if(density < 0) density = 1;
        TestDensity = density;
    }

    /** 获取测试屏比例系数*/
    public static final float getTestDensity() {
        return TestDensity;
    }

    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////

    /** 测试，查看本机屏幕属性*/
	public void showDP() {
		Log.v("ADDC", "宽：" + screenWidth + ",高：" + screenHeight + ",density："
				+ density);
		Log.v("ADDC",
				"像素比例dp:"
						+ (Math.sqrt(screenWidth * screenWidth + screenHeight
								* screenHeight) / 5.0f));
	}

    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////     屏幕属性      /////////////////////////////
    ///////////////////////////////////////////////////////////////////////////

	/** onCreate中强行获取View的宽高：[宽度, 高度]*/
	public static Point getSizeForce(View view) {
		int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(widthMeasureSpec, heightMeasureSpec);
		return new Point(view.getMeasuredWidth(), view.getMeasuredHeight());
	}

	/** 获取屏幕的宽px和高px：[宽度, 高度]*/
	public static int[] getWindowSize(Context context) {
		Point point = getRealScreenSize(context);
		return new int[] { point.x, point.y };
	}

	/** 获取屏幕的宽px和高px：[宽度, 高度]*/
	private static Point getRealScreenSize(Context context) {
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


}
