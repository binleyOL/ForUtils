package com.wewins.custom.forutils.screen;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wewins.custom.forutils.BUBase;

/**
 * 类说明：<br />
 * 
 * @author binley
 * <br />
 * E-mail: wangbl@we-wins.com
 * @version 1.0
 * <br />
 * Time create:2018年9月6日 下午5:21:20
 *
 */
public class Relayout extends BUBase {
	
	/** 没有改变*/
	public static final int NOT_CHANGE = -3;
	
	/**
	 * 重新设置字体大小
	 * @param tv TextView自身
	 * @param pxValue 字体大小 单位px
	 */
	public final void setTextSizeByPx(TextView tv, int pxValue) {
		Context ctx = tv.getContext();
		Density du = Density.getInstance(ctx);
		tv.setTextSize(px2sp(ctx, pxValue * du.density / Density.getTestDensity()));
	}
	
	/**
	 * 重设的View的左padding、上padding，右padding，下padding
	 * @param v View自身
	 * @param left 左margin 单位px
	 * @param top 上margin 单位px
	 * @param right 右margin 单位px
	 * @param bottom 下margin 单位px
	 */
	public final void setPaddingByPx(View v, int left, int top, int right, int bottom) {
		Context ctx = v.getContext();
		Density dt = Density.getInstance(ctx);
		float dsd = dt.density / Density.getTestDensity();
		v.setPadding((int) (left * dsd), (int) (top * dsd), (int) (right * dsd), (int) (bottom * dsd));
	}
	
	/**
	 * 重设父View为RelativeLayout的View的宽、高、左margin、上margin，右margin，下margin
	 * @param v View自身
	 * @param width View的宽
	 * @param height View的高
	 * @param left 左margin 单位px
	 * @param top 上margin 单位px
	 * @param right 右margin 单位px
	 * @param bottom 下margin 单位px
	 */
	public final void setRelativeParams(View v, int width, int height, int left, int top, int right, int bottom) {
		Context ctx = v.getContext();
		Density du = Density.getInstance(ctx);
		float dsd = du.density / Density.getTestDensity();
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
		if(width != NOT_CHANGE)params.width = (int) (width * dsd);
		if(height != NOT_CHANGE)params.height = (int) (height * dsd);
		if(left != NOT_CHANGE)params.leftMargin = (int) (left * dsd);
		if(right != NOT_CHANGE)params.rightMargin = (int) (right * dsd);
		if(top != NOT_CHANGE)params.topMargin = (int) (top * dsd);
		if(bottom != NOT_CHANGE)params.bottomMargin = (int) (bottom * dsd);
		v.setLayoutParams(params);
	}
	
	
	// setLinearParams
	
	public void setLinearParams(View v, int left, int top, int right, int bottom) {
		setLinearParams(v, NOT_CHANGE, NOT_CHANGE, left, top, right, bottom, NOT_CHANGE);
	}
	
	public void setLinearParams(View v, int width, int height, int left, int top, int right, int bottom) {
		setLinearParams(v, width, height, left, top, right, bottom, NOT_CHANGE);
	}
	
	public void setLinearParams(View v, int width, int height) {
		setLinearParams(v, width, height, NOT_CHANGE, NOT_CHANGE, NOT_CHANGE, NOT_CHANGE, NOT_CHANGE);
	}
	
	
	/**
	 * 重设父View为RelativeLayout的View的左margin、上margin，右margin，下margin
	 * @param v View自身
	 * @param left 左margin 单位px
	 * @param top 上margin 单位px
	 * @param right 右margin 单位px
	 * @param bottom 下margin 单位px
	 */
	public void setLinearParams(View v, int width, int height, int left, int top, int right, int bottom, int gravity) {
		Context ctx = v.getContext();
		Density du = Density.getInstance(ctx);
		float dsd = du.density / Density.getTestDensity();
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v.getLayoutParams();
		if(width != NOT_CHANGE)params.width = (int) (width * dsd);
		if(height != NOT_CHANGE)params.height = (int) (height * dsd);
		if(left != NOT_CHANGE)params.leftMargin = (int) (left * dsd);
		if(right != NOT_CHANGE)params.rightMargin = (int) (right * dsd);
		if(top != NOT_CHANGE)params.topMargin = (int) (top * dsd);
		if(bottom != NOT_CHANGE)params.bottomMargin = (int) (bottom * dsd);
		if(gravity != NOT_CHANGE)params.gravity = gravity;
		v.setLayoutParams(params);
	}
	

}
