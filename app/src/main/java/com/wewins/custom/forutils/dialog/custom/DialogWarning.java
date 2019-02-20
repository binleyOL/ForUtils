package com.wewins.custom.forutils.dialog.custom;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wewins.custom.forutils.R;
import com.wewins.custom.forutils.dialog.BLDialog;
import com.wewins.custom.forutils.screen.Density;

/**
 * 类说明：
 * 消息提醒---只有1个按键
 * <br />
 * 
 * @author binley
 * <br />
 * E-mail: wangbl@we-wins.com
 * @version 1.0
 * <br />
 * Time create:2018年7月16日 下午3:14:24
 *
 */
public class DialogWarning extends BLDialog {
	
	private TextView tvTitle;
	private TextView tvMessage;
	private Button btn;
	
	private String sTitle;
	private String sMessage;
	private String sBtn;

	private View.OnClickListener btnClickListener = null;
	
	public DialogWarning(Context context) {
		super(context, BLDialogTypeDefault);
	}
	
	/** 设置数据*/
	public void set(Object title, Object message, Object btnName, final View.OnClickListener btnClickListener) {
		sTitle = getString(title);
		sMessage = getString(message);
		sBtn = getString(btnName);
		this.btnClickListener = btnClickListener;
	}
	
	@Override
	public View getContentView() {
		Density du = Density.getInstance(context);
		float titleSize = 16;
		float messageSize = 14;
		float buttonTitleSize = 14;
		int topMargin = dip2px(context, 10);
		int leftPadding, rightPadding;
		leftPadding = rightPadding = dip2px(context, 20);
		int buttonTitleColor = Color.GRAY;
		int messageColor = Color.GRAY;
		int titleColor = Color.BLACK;
		int separatorLinesColor = Color.GRAY;

		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setBackgroundResource(R.drawable.bg_dialog);
		
		tvTitle = new TextView(context);
		LinearLayout.LayoutParams lpTitle = new LinearLayout.LayoutParams(-2, -2);
		lpTitle.gravity = Gravity.CENTER_HORIZONTAL;
		lpTitle.topMargin = topMargin;
		tvTitle.setText(sTitle);
		tvTitle.setTextSize(titleSize);
		tvTitle.setTextColor(titleColor);
		tvTitle.setLayoutParams(lpTitle);
		TextPaint tp = tvTitle.getPaint();
		tp.setFakeBoldText(true);//粗体
		
		tvMessage = new TextView(context);
		tvMessage.setPadding(leftPadding, 0, rightPadding, 0);
		LinearLayout.LayoutParams lpMessage = new LinearLayout.LayoutParams(-2, -2);
		lpMessage.gravity = Gravity.CENTER_HORIZONTAL;
		lpMessage.topMargin = topMargin;
		tvMessage.setText(sMessage);
		tvMessage.setTextSize(messageSize);
		tvMessage.setTextColor(messageColor);
		tvMessage.setLayoutParams(lpMessage);
		
		View hgp = new View(context);
		hgp.setBackgroundColor(separatorLinesColor);
		LinearLayout.LayoutParams lpHgp = new LinearLayout.LayoutParams(-1, 1);
		lpHgp.topMargin = topMargin;
		hgp.setLayoutParams(lpHgp);
		
		btn = new Button(context);
		btn.setText(sBtn);
		btn.setTextSize(buttonTitleSize);
		btn.setTextColor(buttonTitleColor);
		btn.setBackgroundResource(R.drawable.bg_dialog_button);
		LinearLayout.LayoutParams lpBtnLeft = new LinearLayout.LayoutParams(-1, (int)(46 * du.density));
		btn.setLayoutParams(lpBtnLeft);
		
		if(!TextUtils.isEmpty(sTitle)) ll.addView(tvTitle);
		ll.addView(tvMessage);
		ll.addView(hgp);
		ll.addView(btn);
		
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dismiss();
				if(btnClickListener != null) {
					btnClickListener.onClick(v);
				}
			}
		});
		return ll;
	}

}
