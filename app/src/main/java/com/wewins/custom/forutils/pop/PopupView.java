package com.wewins.custom.forutils.pop;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.wewins.custom.forutils.KeyboardHelper;
import com.wewins.custom.forutils.blur.BlurUtil;
import com.wewins.custom.forutils.log.BULog;

import java.lang.reflect.InvocationTargetException;

/**
 * 类说明：<br />
 * PopupView的控制类，控制生命周期：显示，隐藏，添加，删除。
 * 
 * @author binley
 * <br />
 * E-mail: wangbl@we-wins.com
 * @version 1.0
 * <br />
 * Time create:2018年7月5日 上午9:27:44
 *
 */
public abstract class PopupView {

    public static final int ANIM_TYPE_ALPHA = 0;
    public static final int ANIM_TYPE_BLUR = 1;

	protected Context mContext;
	protected Handler mHandler;
	protected PopupWindow mPopupWindow = null;
    private OnWindowChangedListener listener = null;

	public PopupView(Context context) {
		this.mContext = context;
		mHandler = new Handler();
		Activity activity = (Activity) context;
		// 解决变暗权限问题
        final int flags = activity.getWindow().getAttributes().flags;
        if((flags & WindowManager.LayoutParams.FLAG_DIM_BEHIND) == 0) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
	}

	/** 显示*/
	public abstract void show();
	
	/** 取消显示*/
	public abstract void dismiss();

    /**
     * 获取字符传
     * @param obj 传入的数据类型仅可以是String类型或者Integer类型。Integer类型的数据是资源ID
     * @return
     */
    protected String getString(Object obj) {
        String str;
        if(obj instanceof String) {
            str = (String) obj;
        } else if(obj instanceof Integer) {
            str = mContext.getResources().getString((Integer) obj);
        } else {
            str = obj.toString();
        }
        return str;
    }

    /** 设置屏幕高度变化的监听器*/
    public void setOnWindowChangedListener(OnWindowChangedListener listener) {
        this.listener = listener;
    }
	
    public static boolean showKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        return true;
    }
	
	public static boolean showKeyboard(View v) {
//		BULog.i("弹出软键盘");
		return showKeyboard(v.getContext());
	}

    public static boolean closeKeybord(Context ctx) {
        return closeKeybord((Activity) ctx);
    }

	public static boolean closeKeybord(Activity act) {
//		BULog.i("关闭软键盘");
		InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
		View v = act.getCurrentFocus();
		if (imm.isActive() && v != null) {
			if (v.getWindowToken() != null) {
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);//强制隐藏键盘
                return true;
			}
		}
        return false;
	}
	
	/** 添加UI改变监听。监听键盘弹出和缩回事件*/
	protected void addWindowChangedListener() {
		Activity act = ((Activity) mContext);
		final View decorView = act.getWindow().getDecorView();
		KeyboardHelper.registerSoftInputChangedListener(act, decorView, new KeyboardHelper.OnSoftInputChangedListener() {
			@Override
			public void onSoftInputChanged(int height) {
				if(null != listener) listener.onWindowChanged(height);
			}
		});
//		ViewTreeObserver vto = decorView.getViewTreeObserver();
//		layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
//			boolean visible = false;
//			public void onGlobalLayout() {
//				Rect rect = new Rect();
//				decorView.getWindowVisibleDisplayFrame(rect);
//				//获得屏幕整体的高度
//				int height = decorView.getRootView().getHeight();
//				//计算出可见屏幕的高度
//				int displayHight = rect.bottom;
//				//获得键盘高度
//				int heightDifference = height - displayHight;
//				double temp = heightDifference / height;
//				boolean visible = temp < 0.8;//表示键盘弹出来了
//				//检测在Pop弹窗弹出时，Navigation Bar弹出和缩回时间
//				boolean isDiff = recordDiffHeight != heightDifference;
//				recordDiffHeight = heightDifference;
//				if((visible && this.visible) || (!visible && !this.visible)) {
//					if(visible && this.visible && isDiff) {
//						//变化的高度
//						if(null != listener) listener.onWindowChanged(heightDifference);
//					}
//				} else {
//					this.visible = visible;
//					//变化的高度
//					if(null != listener) listener.onWindowChanged(heightDifference);
//				}
//			}
//		};
//		vto.addOnGlobalLayoutListener(layoutListener);
	}

    /** 移除UI改变监听*/
    protected void removeWindowChangedListener() {
        Activity act = ((Activity) mContext);
        final View decorView = act.getWindow().getDecorView();
        KeyboardHelper.removeLayoutChangeListener(decorView);
    }

    protected void startPopAmin(boolean isShow, int animType) {
        switch (animType) {
            case ANIM_TYPE_ALPHA:
                startPopAlphaAnim(isShow, .6f, 250);
                break;
            case ANIM_TYPE_BLUR:
                startPopBlurAnim(isShow, 8, 250);
                break;
        }
    }

    /**
     * alpha动画
     * @param isShow true=显示=动画由dimValue -> 1, 否则画由1 -> dimValue
     * @param dimValue alpha值
     * @param duration 动画执行时间，单位毫秒(ms)
     */
    private void startPopAlphaAnim(boolean isShow, float dimValue, int duration) {
        float start;
        float end;
        if(isShow) {
            start = 1;
            end = dimValue;
        } else {
            start = dimValue;
            end = 1;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, end);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (Float) animation.getAnimatedValue();
                Window window = ((Activity) mContext).getWindow();
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.alpha = alpha;
                window.setAttributes(lp);
            }
        });
        valueAnimator.start();
    }

    /** 模糊前，载入的图片空间*/
    private ImageView ivWindowBlur;
    /** 模糊前，截取的Window的图*/
    private Bitmap bitmapWindowBlur;
    /**
     * alpha动画
     * @param isShow true=显示=动画由dimValue -> 1, 否则画由1 -> dimValue
     * @param blurValue 模糊值
     * @param duration 动画执行时间，单位毫秒(ms)
     */
    private void startPopBlurAnim(final boolean isShow, int blurValue, int duration) {
        if(isShow) {
            //将屏幕截图，加入到decorView里，覆盖住当前显示的内容
            bitmapWindowBlur = getWindowCache();
            if(ivWindowBlur == null) ivWindowBlur = new ImageView(mContext);
            ((Activity) mContext).addContentView(ivWindowBlur, new ViewGroup.LayoutParams(-1, -1));
        }

        int start;
        int end;
        if(isShow) {
            start = 0;
            end = blurValue;
        } else {
            start = blurValue;
            end = 0;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, end);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (Float) animation.getAnimatedValue();
                int radius = Math.round(alpha);
                BULog.d("blur="+ radius);
                //                View rootView = ((Activity) mContext).getWindow().getDecorView().findViewById(android.R.id.content);
                //                rootView.setBackground(new BitmapDrawable(mContext.getResources(), getIerceptionScreen(radius)));
                if(isShow) {
                    setBackgroundBitmap(ivWindowBlur, getIerceptionScreen(bitmapWindowBlur, radius));
                } else {
                    if(ivWindowBlur == null) return;
                    Bitmap bm = getIerceptionScreen(bitmapWindowBlur, radius);
                    setBackgroundBitmap(ivWindowBlur, bm);
                    if(radius == 0) {
                        setBackgroundDrawable(ivWindowBlur, new ColorDrawable(Color.TRANSPARENT));
                        ((ViewGroup) ivWindowBlur.getParent()).removeView(ivWindowBlur);
                        ivWindowBlur = null;
                    }
                }
            }
        });
        valueAnimator.start();
    }

    private void setBackgroundBitmap(View v, Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(mContext.getResources(), bitmap);
        setBackgroundDrawable(v, drawable);
    }

    private void setBackgroundDrawable(View v, Drawable drawable) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackground(drawable);
        } else {
            v.setBackgroundDrawable(drawable);
        }
    }


    /** 取得当前APP屏幕的视图。仅能截取显示当前处于屏幕区域的内容*/
    private Bitmap getWindowCache() {
        Activity activity = (Activity) mContext;
        // View是你需要截图的View
        View decorView  = activity.getWindow().getDecorView();
        decorView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);//图像质量
        decorView.setDrawingCacheEnabled(true);//允许抓图
        decorView.buildDrawingCache();//抓图
        Bitmap tmp = decorView.getDrawingCache();//取得抓图
        Bitmap src = Bitmap.createBitmap(tmp);//转存
        decorView .destroyDrawingCache();//销毁
        return src;
    }

    /** 图像模糊 */
    private Bitmap getIerceptionScreen(Bitmap tmp, int radius) {
        Activity activity = (Activity) mContext;

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        // 获取屏幕长和高
        Point point = getRealScreenSize(activity);
        int width = point.x;
        int height = point.y;

        // 图像模糊
        Bitmap bitmap = Bitmap.createBitmap(tmp, 0, statusBarHeight, width, height - statusBarHeight);//减去状态栏的高度
        bitmap = new BlurUtil().with(activity).bitmap(bitmap).radius(radius).blur();
        if (bitmap != null) {
            return bitmap;
        } else {
            return null;
        }
    }

    /** 获取屏幕的实际大小*/
    public static Point getRealScreenSize(Context context) {
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
            DisplayMetrics dm = new DisplayMetrics();
            display.getMetrics(dm);
            size.x = dm.widthPixels;
            size.y = dm.heightPixels;
        }
        BULog.d("屏幕宽、高："+size.x+","+size.y);
        return size;
    }





    ////////////////////////////////////////////////////////////
    // 监听器
    ////////////////////////////////////////////////////////////

	public interface OnWindowChangedListener {
        /**
         * 屏幕显示区域发生变化时调用。当前用在软键盘弹出和缩回时。
         * @param heightDifference 软键盘高
         */
		void onWindowChanged(int heightDifference);
	}
}
