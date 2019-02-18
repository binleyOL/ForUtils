package com.wewins.custom.forutils.toast;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


/**
 * <br/>
 * <b color="#008000">Created by Binley at 2019/1/22.<b/>
 */
public class ToastText {

    private static final String TAG = ToastText.class.getName();

    /** 圆*/
    public static final int TYPE_CIRCLE = 0;//圆圈
    /** 圆角矩形*/
    public static final int TYPE_ROUND_RECT = 1;//圆角矩形

    private static final int FILL_SHADOW_COLOR = 0x55000000;// 阴影填充色
    private static final int KEY_SHADOW_COLOR = 0x55000000;// 阴影填充色

    private static final float SHADOW_RADIUS = 4.2f;// 阴影半径
    private static final float X_OFFSET = 0f;
    private static final float Y_OFFSET = SHADOW_RADIUS / 2;

    private float density;
    private int mDiffWH;
    private int mShadowRadius;
    private int mShadowXOffset;
    private int mShadowYOffset;

    private int mBackgroundColor;

    private TextView mTextView;

    public ToastText(Context context) {
        TextView tv = new TextView(context) {
            @Override
            protected void onSizeChanged(int w, int h, int oldw, int oldh) {
                super.onSizeChanged(w, h, oldw, oldh);
                // 必须重写此内容，来重新匹配 Drawable 图
                refreshBackgroundDrawable(w, h);
            }
        };
        density = context.getResources().getDisplayMetrics().density;
        mTextView = tv;
        mBackgroundColor = Color.WHITE;//默认白色背景

        // 计算宽高差异
        float textHeight = tv.getTextSize();//Paint Size
        float textWidth = textHeight / 4;
        mDiffWH = (int) (Math.abs(textHeight - textWidth) / 2);

        // 设置 TextView 的 Padding
        mShadowRadius = (int) (density * SHADOW_RADIUS);// 阴影半径
        mShadowXOffset = (int) (density * X_OFFSET);
        mShadowYOffset = (int) (density * Y_OFFSET);
        int basePadding = mShadowRadius * 3;//调整高度
        int horizontalPadding = basePadding + mDiffWH;
        tv.setPadding(horizontalPadding, basePadding, horizontalPadding, basePadding);
    }

    public TextView getTextView() {
        return mTextView;
    }

    /** 更换文字控件的背景颜色*/
    public void setBackgroundColor(@ColorInt int backgroundColor) {
        mBackgroundColor = backgroundColor;
        refreshBackgroundDrawable(mTextView.getWidth(), mTextView.getHeight());
    }

    public void refreshBackgroundDrawable(int targetWidth, int targetHeight) {
        //如果文字数大于1
        if(mTextView.length() < 2) {
            refreshBackgroundDrawable(targetWidth, targetHeight, TYPE_CIRCLE);
        } else {
            refreshBackgroundDrawable(targetWidth, targetHeight, TYPE_ROUND_RECT);
        }
    }

    private void refreshBackgroundDrawable(int targetWidth, int targetHeight, int type) {
        if (targetWidth <= 0 || targetHeight <= 0) {
            return;
        }
        CharSequence text = mTextView.getText();
        if (text == null) {
            return;
        }

        if(type == TYPE_CIRCLE) {
            setCircleBackground(targetWidth, targetHeight);
        } else if(type == TYPE_ROUND_RECT) {
            setRectBackground();
        }
    }

    /** 圆形背景*/
    private void setCircleBackground(int width, int height) {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShadow(width, height, mShadowRadius));
        //设置 图层样式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mTextView.setLayerType(View.LAYER_TYPE_SOFTWARE, shapeDrawable.getPaint());
        } else {
            ViewCompat.setLayerType(mTextView, ViewCompat.LAYER_TYPE_SOFTWARE, shapeDrawable.getPaint());
        }
        shapeDrawable.getPaint().setShadowLayer(mShadowRadius, mShadowXOffset, mShadowYOffset, KEY_SHADOW_COLOR);//阴影
        //填充 颜色
        shapeDrawable.getPaint().setColor(mBackgroundColor);
        shapeDrawable.getPaint().setAntiAlias(true);
        //填充 背景
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mTextView.setBackground(shapeDrawable);
        } else {
            mTextView.setBackgroundDrawable(shapeDrawable);
        }
    }

    /** 圆角矩形的背景*/
    private void setRectBackground() {
        SemiCircleRectDrawable sr = new SemiCircleRectDrawable();
        sr.set(mDiffWH, mShadowRadius);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mTextView.setLayerType(TextView.LAYER_TYPE_SOFTWARE, sr.getPaint());
        } else {
            ViewCompat.setLayerType(mTextView, ViewCompat.LAYER_TYPE_SOFTWARE, sr.getPaint());
        }
        sr.getPaint().setShadowLayer(mShadowRadius, mShadowXOffset, mShadowYOffset, KEY_SHADOW_COLOR);//阴影
        sr.getPaint().setColor(mBackgroundColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mTextView.setBackground(sr);
        } else {
            mTextView.setBackgroundDrawable(sr);
        }
    }

    /** 默认 Type 为 TYPE_ROUND_RECT， 白色背景*/
    public static TextView getText(Context context) {
        ToastText shapeDrawable = new ToastText(context);
        return shapeDrawable.getTextView();
    }

    /** 圆圈*/
    private static class OvalShadow extends OvalShape {
        private RadialGradient mRadialGradient;
        private Paint mShadowPaint;
        private int mCircleDiameter;//圆 直径
        private int mShadowRadius;//阴影 半径
        private int mViewWidth;
        private int mViewHeight;

        /**
         * @param width 控件宽
         * @param height 控件高
         * @param shadowRadius 阴影半径
         */
        public OvalShadow(int width, int height, int shadowRadius) {
            super();
            int max = Math.max(width, height);
            int circleDiameter = max - (2 * shadowRadius);

            this.mViewWidth = width;
            this.mViewHeight = height;
            this.mShadowRadius = shadowRadius;
            this.mCircleDiameter = circleDiameter;
            this.mRadialGradient = new RadialGradient(mCircleDiameter / 2, mCircleDiameter / 2,
                    mShadowRadius,
                    new int[]{ FILL_SHADOW_COLOR, Color.TRANSPARENT },
                    null, Shader.TileMode.CLAMP);
            this.mShadowPaint = new Paint();
            mShadowPaint.setShader(mRadialGradient);
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {
            final int viewWidth = mViewWidth;
            final int viewHeight = mViewHeight;
            int cx = viewWidth / 2;// x轴中心
            int cy = viewHeight / 2;// y轴中心
            int circleRadius = mCircleDiameter / 2;// 圆 半径
            int shadowRadius = circleRadius + mShadowRadius;//阴影 半径
            canvas.drawCircle(cx, cy, shadowRadius, mShadowPaint);//画 阴影
            canvas.drawCircle(viewWidth / 2, viewHeight / 2, circleRadius, paint);//画 背景
        }
    }

    /** 半挂 圆 矩形 可拉的*/
    static class SemiCircleRectDrawable extends Drawable {
        private final Paint mPaint;
        private RectF rectF;
        private int mDiffWH;
        private int mShadowRadius;

        public SemiCircleRectDrawable() {
            this.mPaint = new Paint();
            mPaint.setAntiAlias(true);
        }

        public Paint getPaint() {
            return mPaint;
        }

        public void set(int diffWH, int shadowRadius) {
            this.mDiffWH = diffWH;
            this.mShadowRadius = shadowRadius;
        }

        @Override
        public void setBounds(int left, int top, int right, int bottom) {
            super.setBounds(left, top, right, bottom);
            Log.d(TAG, "left="+left+",top="+top+",right="+right+",bottom="+bottom);
            // 上下padding=4px预留阴影位置
            top += mShadowRadius + 4;
            bottom -= mShadowRadius + 4;
            // 左右预留
            left += mDiffWH;
            right -= mDiffWH;
            if (rectF == null) {
                rectF = new RectF(left, top, right, bottom );
            } else {
                rectF.set(left, top, right, bottom);
            }
            Log.d(TAG, "set bounds rectF="+rectF.toString());
        }

        @Override
        public void draw(Canvas canvas) {
            float R = Math.min(rectF.bottom, rectF.right) * .4f;
            Log.d(TAG, "draw R=" + R);
            canvas.drawRoundRect(rectF, R, R, mPaint);
        }

        @Override
        public void setAlpha(int alpha) {
            mPaint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {
            mPaint.setColorFilter(colorFilter);
        }

        @Override
        public int getOpacity() {
            //不透明度
            return PixelFormat.TRANSPARENT;
        }

    }


}
