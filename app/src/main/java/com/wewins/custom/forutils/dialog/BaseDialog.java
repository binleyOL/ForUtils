package com.wewins.custom.forutils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * 类说明：<br />
 * 可监控弹窗外部点击事件的弹窗控件
 * @author binley <br />
 *         E-mail: wangbl@we-wins.com
 * @version 1.0 <br />
 *          Time create:2017年9月14日 下午2:54:07
 *
 */
public class BaseDialog extends Dialog {

	private int slop;

	public BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init();
	}

	public BaseDialog(Context context, int theme) {
		super(context, theme);
		init();
	}

	public BaseDialog(Context context) {
		super(context);
		init();
	}

	private void init() {
		Context ctx = getContext();
		slop = ViewConfiguration.get(ctx).getScaledWindowTouchSlop();
	}

	public boolean onTouchEvent(MotionEvent event) {
		/* 触摸外部弹窗 */
		if(listener != null) {
			if (isOutOfBounds(getContext(), event)) {
				listener.onOutsideTouched();
			}
		}
		return super.onTouchEvent(event);
	}

	private boolean isOutOfBounds(Context context, MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		View decorView = getWindow().getDecorView();
		return (x < -slop) || (y < -slop)
				|| (x > (decorView.getWidth() + slop))
				|| (y > (decorView.getHeight() + slop));
	}
	
	private OnOutsideTouchedListener listener;
	
	public void setOnOutsideTouchedListener(OnOutsideTouchedListener listener) {
		this.listener = listener;
	}
	
	public interface OnOutsideTouchedListener {
		public void onOutsideTouched();
	}

}
