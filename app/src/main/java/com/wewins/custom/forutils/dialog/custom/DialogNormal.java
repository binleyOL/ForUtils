package com.wewins.custom.forutils.dialog.custom;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wewins.custom.forutils.R;
import com.wewins.custom.forutils.dialog.BLDialog;
import com.wewins.custom.forutils.screen.Density;

/**
 * <br/>
 * <b color="#008000">Created by Binley at 2019/2/20.<b/>
 */
public class DialogNormal extends BLDialog {

    private TextView tvTitle;
    private TextView tvMessage;
    private Button btnLeft;
    private Button btnRight;
    private Button btnCenter;
    private LinearLayout llBottom;

    private String sTitle;
    private String sMessage;
    private String sLeft;
    private String sRight;
    private String sCenter;

    private View.OnClickListener leftListener = null;
    private View.OnClickListener rightListener = null;

    public DialogNormal(Context context) {
        super(context, BLDialogTypeNormal);
        gravityTitle = -5;
    }

    /** 设置数据*/
    public void set(Object title, Object message, Object left, Object right, final View.OnClickListener leftListener, final View.OnClickListener rightListener) {
        sTitle = getString(title);
        sMessage = getString(message);
        sLeft = getString(left);
        sRight = getString(right);
        this.leftListener = leftListener;
        this.rightListener = rightListener;
    }

    /** 添加一个唯一中间的按键*/
    public void addCenterButton(String name, final View.OnClickListener listener) {
        float buttonTitleSize = 14;
        int buttonTitleColor = Color.GRAY;
        sCenter = name;
        // 按键
        if(btnCenter == null) {
            Button btn = new Button(context);
            btn.setText(name);
            btn.setTextSize(buttonTitleSize);
            btn.setTextColor(buttonTitleColor);
            btn.setBackgroundResource(R.drawable.bg_dialog_button_center);
            LinearLayout.LayoutParams lpBtnLeft = new LinearLayout.LayoutParams(0, -1);
            lpBtnLeft.weight = 1;
            btn.setLayoutParams(lpBtnLeft);
            llBottom.addView(btn, 1);
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(listener != null) {
                        dismiss();
                        listener.onClick(v);
                    }
                }
            });
            btnCenter = btn;
        } else {
            btnCenter.setText(name);
        }
    }

    /** 设置中间按键的文字 和 显示状态*/
    public void setCenterButton(String name, boolean isShow) {
        if(btnCenter != null) {
            btnCenter.setText(name);
            if(isShow) {
                btnCenter.setVisibility(View.VISIBLE);
            } else {
                btnCenter.setVisibility(View.GONE);
            }
        }
    }

    /** 标题的位置*/
    int gravityTitle;
    public void setTitleGravity(int gravity) {
        gravityTitle = gravity;
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
        int titleColor = context.getResources().getColor(R.color.dialog_button_title);
        int separatorLinesColor = Color.GRAY;
        int screenHeight = du.screenHeight;
        // 底层ViewGroup
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setBackgroundResource(R.drawable.bg_dialog_button_center);
        // 标题
        tvTitle = new TextView(context);
        LinearLayout.LayoutParams lpTitle = new LinearLayout.LayoutParams(-2, -2);
        tvTitle.setPadding(leftPadding, 0, rightPadding, 0);
        lpTitle.gravity = gravityTitle == -5 ? Gravity.CENTER_HORIZONTAL : gravityTitle;
        lpTitle.topMargin = topMargin;
        tvTitle.setText(sTitle);
        tvTitle.setTextSize(titleSize);
        tvTitle.setTextColor(titleColor);
        tvTitle.setLayoutParams(lpTitle);
        TextPaint tp = tvTitle.getPaint();
        tp.setFakeBoldText(true);//粗体
        // 消息内容
        tvMessage = new TextView(context);
        tvMessage.setPadding(leftPadding, 0, rightPadding, 0);
        LinearLayout.LayoutParams lpMessage = new LinearLayout.LayoutParams(-2, -2);
//		lpMessage.gravity = Gravity.CENTER_HORIZONTAL;
        lpMessage.topMargin = topMargin;
        tvMessage.setText(sMessage);
        tvMessage.setTextSize(messageSize);
        tvMessage.setTextColor(messageColor);
        tvMessage.setLayoutParams(lpMessage);
        tvMessage.setMaxHeight((int)(screenHeight / 5.0f * 3));
        //分割线
        View hgp = new View(context);
        hgp.setBackgroundColor(separatorLinesColor);
        LinearLayout.LayoutParams lpHgp = new LinearLayout.LayoutParams(-1, 1);
        lpHgp.topMargin = topMargin;
        hgp.setLayoutParams(lpHgp);
        // 按键的父ViewGroup
        llBottom = new LinearLayout(context);
        llBottom.setOrientation(LinearLayout.HORIZONTAL);
        llBottom.setLayoutParams(new  LinearLayout.LayoutParams(-1, (int)(46 * du.density)));
        // 左按键
        btnLeft = new Button(context);
        btnLeft.setText(sLeft);
        btnLeft.setTextSize(buttonTitleSize);
        btnLeft.setTextColor(buttonTitleColor);
        btnLeft.setBackgroundResource(R.drawable.bg_dialog_button_left);
        LinearLayout.LayoutParams lpBtnLeft = new LinearLayout.LayoutParams(0, -1);
        lpBtnLeft.weight = 1;
        btnLeft.setLayoutParams(lpBtnLeft);
        //分割线
        View vgp = new View(context);
        vgp.setBackgroundColor(separatorLinesColor);
        vgp.setLayoutParams(new LinearLayout.LayoutParams(1, -1));
        // 右按键
        btnRight = new Button(context);
        btnRight.setText(sRight);
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

        btnLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(leftListener != null) {
                    leftListener.onClick(v);
                } else {
                    dismiss();
                }
            }
        });
        btnRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(rightListener != null) {
                    dismiss();
                    rightListener.onClick(v);
                }
            }
        });
        return ll;
    }
}
