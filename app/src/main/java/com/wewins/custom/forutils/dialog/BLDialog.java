package com.wewins.custom.forutils.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wewins.custom.forutils.R;
import com.wewins.custom.forutils.screen.Density;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 类说明：<br />
 * 自定义弹窗的基类
 * 
 * @author binley
 * <br />
 * E-mail: wangbl@we-wins.com
 * @version 1.0
 * <br />
 * Time create:2017年8月26日 下午4:53:16
 *
 */
public abstract class BLDialog {
	
	/** 3s后可点击外部关闭*/
	public static final String BLDialogTypeCanDismissAfter3s = "3s";
	/** 可点击外部或返回键可关闭*/
	public static final String BLDialogTypeCanDismiss = "0s";
	/** 点击外部不可关闭，点击返回键不可关闭*/
	public static final String BLDialogTypeDefault = "nan";
	/** 点击外部不可关闭，点击返回键可关闭*/
	public static final String BLDialogTypeNormal = "nan,but";
	
	/** 上下文*/
	protected Context context = null;
	/** 弹窗本身*/
	private BaseDialog dlg = null;
	/** 模式*/
	private int type = 0;
    /** 弹窗宽*/
    private int width = 0;
    // 定时器
	private Timer timer = null;
	private TimerTask timerTask = null;
	

	/** 弹窗外部点击监听器*/
	private BaseDialog.OnOutsideTouchedListener onOutsideTouchedListener;;
	
	public BLDialog(Context context, String BLDialogType) {
		this.context = context;
		setDialogType(BLDialogType);
	}
	
	public Dialog getDialog() {
		return dlg;
	}
	
	public final void setDialogType(String BLDialogType) {
		if(BLDialogType.equals(BLDialogTypeDefault)) type = 0;
		else if(BLDialogType.equals(BLDialogTypeNormal)) type = 1;
		else if(BLDialogType.equals(BLDialogTypeCanDismiss)) type = 2;
		else if(BLDialogType.equals(BLDialogTypeCanDismissAfter3s)) type = 3;
		else {
			try {
				throw new Exception("BLDialogType is limited. The input can only be BLDialogTypeDefault,BLDialogTypeNormal,BLDialogTypeCanDismiss,BLDialogTypeCanDismissAfter3s. To view BLDialog.class for more infomation.");
			} catch (Exception e) { e.printStackTrace(); }
		}
	}

    /**
     * 反类型获取字符串。当前支持resId和string
     * @param obj
     * @return
     */
    protected String getString(Object obj) {
        String s = null;
        if(obj != null) {
            if(obj instanceof Integer) {
                s = context.getString((Integer) obj);
            } else if(obj instanceof String) {
                s = (String) obj;
            }
        }
        return s;
    }
	
	/** 设置弹窗宽*/
	public void setWidth(int width) {
		this.width = width;
	}
	
	/** 展示*/
	public void show() {
		if(dlg == null) {
			Density du = Density.getInstance(context);
			dlg = new BaseDialog(context, R.style.Dialog);
			if(width == 0) {
				width = Density.round(du.screenWidth / 4.0f * 3);
			}
			View view = getContentView();
            if(view == null) {
                return;
            }
            dlg.addContentView(view, new ViewGroup.LayoutParams(width, -2));
			switch (type) {
			case 0: ;
			case 3: dlg.setCancelable(false); break;
			case 1: dlg.setCanceledOnTouchOutside(false); break;
			case 2: break;
			default: break;
			}
			dlg.setOnOutsideTouchedListener(onOutsideTouchedListener);
		}
		dlg.show();
		if(type == 3) {
			stopTimer();
			startTimer();
		}
	}

    /** 获取主视图*/
    public abstract View getContentView();
	
	/** 关闭*/
	public void dismiss() {
		stopTimer();
		if(dlg != null) {
			if(dlg.isShowing()) dlg.dismiss();
		}
		dlg = null;
	}

	/** 设置，弹窗外部点击监听*/
	public void setOnOutsideTouchedListener(BaseDialog.OnOutsideTouchedListener listener) {
        this.onOutsideTouchedListener = listener;
        if(dlg != null) {
			dlg.setOnOutsideTouchedListener(onOutsideTouchedListener);
		}
	}
	
	/** 启动计时器*/
	protected void startTimer() {
		timer = new Timer();
		timerTask = new TimerTask() {
			public void run() {
				((Activity) context).runOnUiThread(new Runnable() {
					public void run() {
						dlg.setCancelable(true);
						dlg.setCanceledOnTouchOutside(true);
					}
				});
			}
		};
		timer.schedule(timerTask, 3 * 1000);
	}
	
	/** 关闭计时器*/
	protected void stopTimer() {
		if(timer == null) return;
		timer.cancel();
		timer = null;
		timerTask = null;
	}

	////////////////////////////////////////////////////////////////////////////////////////
	//
	////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 根据手机分辨率从DIP转成PX(像素)
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return Math.round(dipValue * scale);
    }

    /**
     * 获取一个设定好的View
     * @param context
     * @return view[标题, 消息内容, 左按键, 右按键, 消息与按键的分割线, 按键与按键的分割线, 按键的Viewgroup, 最底层的ViewGroup]
     */
    public static View[] getDefaultView(Context context) {
        View[] vs = new View[8];
        Density du = Density.getInstance(context);
        float titleSize = 16;
        float messageSize = 14;
        float buttonTitleSize = 14;
        int topMargin = dip2px(context, 10);
        int buttonTitleColor = Color.GRAY;
        int messageColor = Color.GRAY;
        int titleColor = Color.BLACK;
        int separatorLinesColor = Color.GRAY;
        // 底层ViewGroup
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setBackgroundResource(R.drawable.bg_dialog);
        // 标题
        TextView tvTitle = new TextView(context);
        LinearLayout.LayoutParams lpTitle = new LinearLayout.LayoutParams(-2, -2);
        lpTitle.gravity = Gravity.CENTER_HORIZONTAL;
        lpTitle.topMargin = topMargin;
        tvTitle.setText("Title");
        tvTitle.setTextSize(titleSize);
        tvTitle.setTextColor(titleColor);
        tvTitle.setLayoutParams(lpTitle);
        TextPaint tp = tvTitle.getPaint();
        tp.setFakeBoldText(true);//粗体
        // 消息内容
        TextView tvMessage = new TextView(context);
        LinearLayout.LayoutParams lpMessage = new LinearLayout.LayoutParams(-2, -2);
        lpMessage.gravity = Gravity.CENTER_HORIZONTAL;
        lpMessage.topMargin = topMargin;
        tvMessage.setText("XXX");
        tvMessage.setTextSize(messageSize);
        tvMessage.setTextColor(messageColor);
        tvMessage.setLayoutParams(lpMessage);
        // 分割线
        View hgp = new View(context);
        hgp.setBackgroundColor(separatorLinesColor);
        LinearLayout.LayoutParams lpHgp = new LinearLayout.LayoutParams(-1, 1);
        lpHgp.topMargin = topMargin;
        hgp.setLayoutParams(lpHgp);
        // 按键的父ViewGroup
        LinearLayout llBottom = new LinearLayout(context);
        llBottom.setOrientation(LinearLayout.HORIZONTAL);
        llBottom.setLayoutParams(new  LinearLayout.LayoutParams(-1, (int)(46 * du.density)));
        // 左按键
        Button btnLeft = new Button(context);
        btnLeft.setText(R.string.cancel);
        btnLeft.setTextSize(buttonTitleSize);
        btnLeft.setTextColor(buttonTitleColor);
        btnLeft.setBackgroundResource(R.drawable.bg_dialog_button_left);
        LinearLayout.LayoutParams lpBtnLeft = new LinearLayout.LayoutParams(0, -1);
        lpBtnLeft.weight = 1;
        btnLeft.setLayoutParams(lpBtnLeft);
        // 按键们间的分割线
        View vgp = new View(context);
        vgp.setBackgroundColor(separatorLinesColor);
        vgp.setLayoutParams(new LinearLayout.LayoutParams(1, -1));
        // 右按键
        Button btnRight = new Button(context);
        btnRight.setText(R.string.ok);
        btnRight.setTextSize(buttonTitleSize);
        btnRight.setTextColor(buttonTitleColor);
        btnRight.setBackgroundResource(R.drawable.bg_dialog_button_right);
        LinearLayout.LayoutParams lpBtnRight = new LinearLayout.LayoutParams(0, -2);
        lpBtnRight.weight = 1;
        btnRight.setLayoutParams(lpBtnRight);

        llBottom.addView(btnLeft);
        llBottom.addView(vgp);
        llBottom.addView(btnRight);
        ll.addView(tvTitle);
        ll.addView(tvMessage);
        ll.addView(hgp);
        ll.addView(llBottom);
        vs[0] = tvTitle;
        vs[1] = tvMessage;
        vs[2] = btnLeft;
        vs[3] = btnRight;
        vs[4] = vgp;
        vs[5] = hgp;
        vs[6] = llBottom;
        vs[7] = ll;
        return vs;
    }

}
