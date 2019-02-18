package com.wewins.custom.forutils.blur;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

/**cao
 * 高斯模糊工具类
 * Created by loongggdroid on 2016/5/12.
 */
public class BlurUtil {

    private Context mContext = null;
    private Bitmap mBitmap = null;
    private float mRadius = 4;// 模糊半径
    private float mScale = .4f;// 缩放比
    private BlurPolicy mPolicy = BlurPolicy.RENDER_SCRIPT;//默认为 新 的模糊方式

    /** 模糊方式*/
    public enum BlurPolicy {
        RENDER_SCRIPT,      // 新
        FAST_BLUR           // 旧
    }

    /** 载入引用*/
    public BlurUtil with(Context context) {
        this.mContext = context;
        return this;
    }

    /** 设置源图片*/
    public BlurUtil bitmap(Bitmap source) {
        this.mBitmap = source;
        return this;
    }

    /** 设置模糊半径*/
    public BlurUtil radius(float radius) {
        if(radius > 25) {
            radius = 25;
        } else if(radius < 1) {
            radius = 1;
        }
        this.mRadius = radius;
        return this;
    }

    /** 设置缩放比*/
    public BlurUtil scale(float scale) {
        if(scale > 1) {
            scale = 1;
        } else if(scale < 0) {
            scale = 0;
        }
        this.mScale = scale;
        return this;
    }

    /** 载入模糊模式*/
    public BlurUtil policy(BlurPolicy policy) {
        this.mPolicy = policy;
        return this;
    }

    /** 执行模糊*/
    public Bitmap blur() {
        if(mContext == null || mBitmap == null) {
            return null;
        }
        Bitmap bmp = null;
        if(Build.VERSION.SDK_INT < 17) {
            mPolicy = BlurPolicy.FAST_BLUR;
        }
        switch (mPolicy) {
            case RENDER_SCRIPT:
                bmp = rsBlur(mContext, mBitmap, mScale, mRadius);
                break;
            case FAST_BLUR:
                bmp = fastBlur(mBitmap, mScale, (int)(mRadius + .5f));
                break;
        }
        return bmp;
    }

    //////////////////////////////////////////////////////////////
    //模糊 方法1
    //////////////////////////////////////////////////////////////

    /**
     * API 17(含17)以上才能使用
     * @param context 上下文
     * @param source 源Bitmap图片
     * @param scale 缩放比：0~1之间的小数
     * @param radius 模糊半径：0~25
     * @return 高斯模糊后的图片
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static final Bitmap rsBlur(Context context, Bitmap source, float scale, float radius) {
        if(radius <= 0) {
            return source;
        } else if(radius > 25) {
            radius = 25;
        }
        // 计算图片缩小后的长宽
        int width = Math.round(source.getWidth() * scale);
        int height = Math.round(source.getHeight() * scale);

        // 将缩小后的图片做为预渲染的图片。
        Bitmap inputBitmap = Bitmap.createScaledBitmap(source, width, height, false);
        // 创建一张渲染后的输出图片。
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        // 创建RenderScript内核对象
        RenderScript rs = RenderScript.create(context);
        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间。
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去。
        Allocation tmpIn = Allocation.createFromBitmap(rs, outputBitmap);
//        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        Allocation tmpOut = Allocation.createTyped(rs, tmpIn.getType());

        // 创建一个模糊效果的RenderScript的工具对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        // 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setRadius(radius);
        // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn);
        // 将输出数据保存到输出内存中
        blurScript.forEach(tmpOut);

        // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    //////////////////////////////////////////////////////////////
    //模糊 方法2
    //////////////////////////////////////////////////////////////

    /**
     * 直接在Java层做图片的模糊处理。对每个像素点应用高斯模糊计算、最后在合成Bitmap
     * @param source 源Bitmap图片
     * @param scale 缩放比：0~1之间的小数
     * @param radius 模糊半径：0~25
     * @return 高斯模糊后的图片
     */
    private static final Bitmap fastBlur(Bitmap source, float scale, int radius) {

        int width = Math.round(source.getWidth() * scale);
        int height = Math.round(source.getHeight() * scale);
        source = Bitmap.createScaledBitmap(source, width, height, false);

        Bitmap bitmap = source.copy(source.getConfig(), true);

        if (radius < 1) {
            return (source);
        } else if(radius > 25) {
            radius = 25;
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }
}
