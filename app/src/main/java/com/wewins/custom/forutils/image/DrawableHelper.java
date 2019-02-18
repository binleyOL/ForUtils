package com.wewins.custom.forutils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.res.ResourcesCompat;

/**
 * <br/>
 * <b color="#008000">Created by Binley at 2019/2/15.<b/>
 */
public class DrawableHelper {

    ////////////////////////////////////////////////////////////////////////////////
    // getColorChangeDrawable
    ////////////////////////////////////////////////////////////////////////////////

    /** 改变原图颜色。res加载的图片颜色被改变后所有复用的地方的颜色都会改变，所以改变了颜色的图需要单独使用*/
    public static final android.graphics.drawable.Drawable getColorChangeDrawable(Context ctx, int drawableResId, int colorId) {
        android.graphics.drawable.Drawable drawable = getDrawableFromResources(ctx, drawableResId);
        return getColorChangeDrawable(drawable, ctx.getResources().getColor(colorId), PorterDuff.Mode.SRC_IN);
    }

    /** 改变原图颜色。res加载的图片颜色被改变后所有复用的地方的颜色都会改变，所以改变了颜色的图需要单独使用*/
    public static final android.graphics.drawable.Drawable getColorChangeDrawable(android.graphics.drawable.Drawable drawable, int color, PorterDuff.Mode PorterDuffMode) {
        drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuffMode));
        return drawable;
    }

    /** 从资源文件中取出Drawable图*/
    public static final android.graphics.drawable.Drawable getDrawableFromResources(Context context, int drawableResId) {
        return ResourcesCompat.getDrawable(context.getResources(), drawableResId, null);
    }

    ///////////////////////////////////////////
    // Drawable 与 Bitmap 转换
    ///////////////////////////////////////////

    /** bitmap转drawable*/
    public static android.graphics.drawable.Drawable bitmapToDrawable(Context context, Bitmap bitmap) {
        android.graphics.drawable.Drawable drawable = null;
        if(context == null) {
            drawable = new BitmapDrawable(null, bitmap);
        } else {
            drawable = new BitmapDrawable(context.getResources(), bitmap);
        }
        return drawable;
    }

}
