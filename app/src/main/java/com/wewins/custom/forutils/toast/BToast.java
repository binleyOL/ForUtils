package com.wewins.custom.forutils.toast;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
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

    private static BToast instance; //单例的
    private View mToastView;//自定义toast view
    private TextView mTextView;
    private Boolean mIsShow;//记录状态 是否在显示
    private Timer mTimer;//定时器
    private WindowManager.LayoutParams mParams;//布局参数
    private int mShowTime = Toast.LENGTH_SHORT;//显示延时时间
    private int[] mGravity = new int[3];//显示位置改变
    private String mText;

    public WindowManager mWdm;

    public synchronized static BToast getInstance(Context context) {
        if (instance == null)
            instance = new BToast(context);
        return instance;
    }

    private BToast(Context context) {
        mIsShow = false;// 记录当前Toast的内容是否已经在显示
        // WindowManager
        mWdm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // 这里初始化toast view
        mToastView = LayoutInflater.from(context).inflate(R.layout.common_toast, null);
        LinearLayout ll = mToastView.findViewById(R.id.rootView);
        mTextView = ToastText.getText(context);//用来提示的文字
        mTextView.setTextSize(16);
        ll.addView(mTextView);
        // 初始化计数器
        mTimer = new Timer();
        // 设置布局参数
        setParams();
    }

    /** 设置布局参数*/
    private void setParams() {
        mParams = new WindowManager.LayoutParams();//初始化
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;  //高
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;   //宽
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.windowAnimations = R.style.custom_animation_toast_aplha;// 设置进入退出动画效果
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        setGravity(Gravity.BOTTOM, 0, 0);        //对其方式
        mParams.y = 45;      //下间距
    }

    /** 获取定时器可执行线程*/
    private TimerTask getTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
//                BaseAppliaction.mWdm.removeView(mToastView);
                mWdm.removeView(mToastView);
                mIsShow = false;
            }
        };
    }

    /** 自定义View。展示没用到*/
    private View customView(Context context, CharSequence text){
        LinearLayout toastView = new LinearLayout(context);
        toastView.setOrientation(LinearLayout.VERTICAL);
        toastView.setBackgroundResource(R.drawable.bg_common_toast);
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextColor(Color.parseColor("#ff000000"));
        textView.setTextSize(14);
        textView.setPadding(20,5,20,5);
        toastView.addView(textView, 0);
        mToastView = toastView;
        mTextView = textView;
        return toastView;
    }

    /** 设置布局位置*/
    public final void setGravity(int gravity, int xOffset, int yOffset) {
        mParams.gravity = gravity;
        mParams.x = xOffset;
        mParams.y = yOffset;
    }

    /**
     * 设置延时时间
     * @param duration
     */
    public final void setShowTime(int duration) {
        if(duration < 0) {
            mShowTime = Toast.LENGTH_SHORT;
        } else if(duration <= 1){
        } else if(duration < 500) {
            mShowTime = Toast.LENGTH_SHORT;
        }
        mShowTime = duration;
    }

    public void dismiss() {
        // 取消计时器
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = new Timer();
        }
//        BaseAppliaction.mWdm.removeView(mToastView);
        mWdm.removeView(mToastView);
        mIsShow = false;
    }

    /** 显示*/
    public void show(String text) {
        if (mToastView == null) {
            // 未初始化，终止后续内容执行
            return;
        }
        if (mIsShow) {
            // 取消计时器
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = new Timer();
            }
            // 如果Toast已经在显示，但是Gravity相关参数改变
            if(mGravity[0] != mParams.gravity || mGravity[1] != mParams.x || mGravity[2] != mParams.y) {
//                BaseAppliaction.mWdm.removeView(mToastView);
                mWdm.removeView(mToastView);
                // 记录Gravity相关信息
                mGravity[0] = mParams.gravity;
                mGravity[1] = mParams.x;
                mGravity[2] = mParams.y;
                // 将其加载到windowManager上
//                BaseAppliaction.mWdm.addView(mToastView, mParams);
                mWdm.addView(mToastView, mParams);
            }
            // 设置显示内容
            if(!mText.equals(text)) {
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
            mTextView.setText(text);
            // 设置显示状态
            mIsShow = true;
            // 将其加载到windowManager上
//            BaseAppliaction.mWdm.addView(mToastView, mParams);
            mWdm.addView(mToastView, mParams);
        }
        // 设置计时器
        mTimer.schedule(getTimerTask(), (long) (mShowTime == Toast.LENGTH_LONG ? 3400 : ( mShowTime == Toast.LENGTH_SHORT ? 2000 : mShowTime)));
    }
}
