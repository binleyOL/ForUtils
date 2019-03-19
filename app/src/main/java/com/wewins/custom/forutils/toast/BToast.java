package com.wewins.custom.forutils.toast;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wewins.custom.forutils.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Toast控制
 * <br/>
 * <b color="#008000">Created by Binley at 2019/1/7.<b/>
 */
public class BToast {

    /** 自定义toast view的根View*/
    private View mToastView;
    /** 文本*/
    private TextView mTextView;
    /** 定时器：显示后开始计时，时间到了后，隐藏Toast*/
    private Timer mTimer;
    /** Toast的布局参数*/
    private WindowManager.LayoutParams mParams;
    private WindowManager mWdm;
    private Handler mHandle;
    private ToastText mToastText;

    /** 记录状态 是否在显示。true=已显示*/
    private Boolean mIsShowing;
    /** 显示延时时间*/
    private int mShowTime = Toast.LENGTH_SHORT;
    private String mText;
    private float textSize;
    private int textColor;
    private int textBackgroundColor;
    /** 显示位置*/
    private int[] mGravity = new int[3];

    public BToast(Context context) {
        mIsShowing = false;// 记录当前Toast的内容是否已经在显示
        // WindowManager
        mWdm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // 这里初始化toast view
        mToastView = LayoutInflater.from(context).inflate(R.layout.common_toast, null);
        LinearLayout ll = mToastView.findViewById(R.id.rootView);
        mToastText = new ToastText(context);
        mTextView = mToastText.getTextView();
        textSize = 16;
        setDefaultType();
        ll.addView(mTextView);
        // 初始化计数器
        mTimer = new Timer();
        // 设置布局参数
        setParams();
        // 同步到主线程
        mHandle = new Handler();
    }

    /** 设置布局参数*/
    private void setParams() {
        mParams = new WindowManager.LayoutParams();//初始化
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;  //高
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;   //宽
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.windowAnimations = com.wewins.custom.forutils.R.style.custom_animation_toast_aplha;// 设置进入退出动画效果
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        gravity(Gravity.BOTTOM, 0, 45);        //对其方式
    }

    /** 获取定时器可执行线程*/
    private TimerTask getTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                mHandle.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mWdm.removeView(mToastView);
                            mIsShowing = false;
                        } catch (Exception e) {}
                    }
                });
            }
        };
    }

    public BToast errorType() {
        setErrorType();
        return this;
    }

    /** 设置Error样式*/
    public void setErrorType() {
        textColor = Color.WHITE;
        textBackgroundColor = Color.RED;
    }

    public BToast warningType() {
        setWarningType();
        return this;
    }

    /** 设置Warning样式*/
    public void setWarningType() {
        textColor = Color.YELLOW;
        textBackgroundColor = Color.BLUE;
    }

    public BToast defaultType() {
        setDefaultType();
        return this;
    }

    /** 设置普通样式*/
    public void setDefaultType() {
        textColor = Color.BLACK;
        textBackgroundColor = Color.WHITE;
    }

    /**
     * 设置布局位置
     * @param gravity
     * @param xOffset
     * @param yOffset
     */
    public BToast gravity(int gravity, int xOffset, int yOffset) {
        mParams.gravity = gravity;
        mParams.x = xOffset;
        mParams.y = yOffset;//下间距
        return this;
    }

    /**
     * 文字大小
     * @param textSize 单位sp
     */
    public BToast textSize(float textSize) {
        this.textSize = textSize;
        return this;
    }

    /**
     * 文字颜色
     * @param textColor
     */
    public BToast textColor(@ColorInt int textColor) {
        this.textColor = textColor;
        return this;
    }

    /**
     * 背景颜色
     * @param backgroundColor
     */
    public BToast backgroundColor(@ColorInt int backgroundColor) {
        this.textBackgroundColor = backgroundColor;
        return this;
    }

    /**
     * 设置延时时间
     * @param duration
     */
    public BToast showTime(int duration) {
        if(duration < 0) {
            mShowTime = Toast.LENGTH_SHORT;
        } else if(duration <= 1){
        } else if(duration < 500) {
            mShowTime = Toast.LENGTH_SHORT;
        }
        mShowTime = duration;
        return this;
    }

    /**
     * 隐藏
     */
    public void dismiss() {
        // 取消计时器
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = new Timer();
        }
        mWdm.removeView(mToastView);
        mIsShowing = false;
    }

    /** 显示*/
    public void show(String text) {
        if (mToastView == null) {
            // 未初始化，终止后续内容执行
            return;
        }
        if (mIsShowing) {
            // 取消计时器
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = new Timer();
            }
            // 如果Toast已经在显示，但是Gravity相关参数改变
            if(mGravity[0] != mParams.gravity || mGravity[1] != mParams.x || mGravity[2] != mParams.y) {
                mWdm.removeView(mToastView);
                // 记录Gravity相关信息
                mGravity[0] = mParams.gravity;
                mGravity[1] = mParams.x;
                mGravity[2] = mParams.y;
                // 将其加载到windowManager上
                mWdm.addView(mToastView, mParams);
            }

            mToastText.setBackgroundColor(textBackgroundColor);
            mToastText.setTextColor(textColor);
            mToastText.setTextSize(textSize);
            mToastText.commit();
            if(!mText.equals(text)) {
                // 设置显示内容
                mTextView.setText(text);
            }
            mText = text;
        } else {
            // 记录Gravity相关信息
            mGravity[0] = mParams.gravity;
            mGravity[1] = mParams.x;
            mGravity[2] = mParams.y;
            // 设置显示内容
            mText = text;
            mToastText.setBackgroundColor(textBackgroundColor);
            mToastText.setTextColor(textColor);
            mToastText.setTextSize(textSize);
            mToastText.commit();
            mTextView.setText(text);

            // 设置显示状态
            mIsShowing = true;
            // 将其加载到windowManager上
            mWdm.addView(mToastView, mParams);
        }
        // 设置计时器
        mTimer.schedule(getTimerTask(), (long) (mShowTime == Toast.LENGTH_LONG ? 3400 : ( mShowTime == Toast.LENGTH_SHORT ? 2000 : mShowTime)));
    }
}
