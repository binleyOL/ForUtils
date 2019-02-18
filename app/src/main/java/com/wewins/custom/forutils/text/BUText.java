package com.wewins.custom.forutils.text;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wewins.custom.forutils.BUBase;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class BUText extends BUBase {

    ////////////////////////////////////////////////////////////////////////////////
    // 加载工具
    ////////////////////////////////////////////////////////////////////////////////

    /** 从raw文件夹中获取文件并读取数据*/
    public static String getFromRaw(Context context, int rawId){
        String result = "";
        try {
            InputStream in = context.getResources().openRawResource(rawId);//R.raw.test1
            //获取文件的字节数
            int length = in.available();
            //创建byte数组
            byte[]  buffer = new byte[length];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /** 从assets 文件夹中获取文件并读取数据*/
    public String getFromAssets(Context context, String fileName){
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            //获取文件的字节数
            int len = in.available();
            //创建byte数组
            byte[]  buffer = new byte[len];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    ////////////////////////////////////////////////////////////////////////////////
    // 字体工具
    ////////////////////////////////////////////////////////////////////////////////
    private static Map<String, Typeface> typefaces;

    /** 添加字体到静态存储区*/
    public static final String addTypeface(Activity context, String filePathInAsset) {
        String key = filePathInAsset.substring(filePathInAsset.lastIndexOf("/") + 1);
        Typeface typeface = createTypeface(context, filePathInAsset);
        if(typeface != null) {
            addTypeface(key, typeface);
        } else {
            key = null;
        }
        return key;
    }

    /** 添加字体到静态存储区*/
    public static final void addTypeface(String key, Typeface typeface) {
        if(typefaces == null) {
            typefaces = new HashMap<>();
        }
        typefaces.put(key, typeface);
    }

    /**
     * 从静态存储区获取字体
     * @param key
     * @return
     */
    public static final Typeface getTypeface(String key) {
        if(typefaces != null) {
            return typefaces.get(key);
        }
        return null;
    }

    public static final void setTypeface(Typeface tf, TextView...tvs) {
        for(int i=0;i<tvs.length;i++) tvs[i].setTypeface(tf);
    }

    /**
     * 从Activity 获取 rootView 根节点
     * @param context
     * @return 当前activity布局的根节点
     */
    public static final View getRootView(Activity context) {
        return ((ViewGroup)context.findViewById(android.R.id.content)).getChildAt(0);
    }

    /*
     * Create a Typeface instance with your font file
     */
    public static final Typeface createTypeface(Context context, String fontPath) {
        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }

    public static void replaceFont(View v, Typeface tf) {
        if (v == null) {
            return;
        }
        if (v instanceof TextView) { // If view is TextView or it's subclass, replace it's font
            TextView textView = (TextView) v;
            int style = Typeface.NORMAL;
            if (textView.getTypeface() != null) {
                style = textView.getTypeface().getStyle();
            }
            textView.setTypeface(tf, style);
        } else if (v instanceof ViewGroup) { // If view is ViewGroup, apply this method on it's child views
            ViewGroup viewGroup = (ViewGroup) v;
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                replaceFont(viewGroup.getChildAt(i), tf);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    // 基础工具
    ////////////////////////////////////////////////////////////////////////////////

    /**
     * 在宽度为 textWidthAtMost 的水平空间，如果 mText 能放的下就原样返回 mText，否则按照 where 的方式进行打点并返回打点后的字符串。
     * @param mText
     * @param p
     * @param textWidthAtMost
     * @param where TruncateAt.START，TruncateAt.MIDDLE，TruncateAt.END，TruncateAt.MARQUEE，TruncateAt.END_SMALL
     * @return
     */
    public static CharSequence ellipsize(CharSequence mText, TextPaint p, float textWidthAtMost, TextUtils.TruncateAt where) {
        return TextUtils.ellipsize(mText, p, textWidthAtMost, where);
    }

    /** 计算这个 TextView 中文本的宽度。type不同使用的计算方法不同*/
    public static float getWidth(TextView textView, int type) {
        // 该值不包含 textView 的 margin 或 padding 值，是裸文本的长度；textView 的文本中不能含有换行符
        // 该值是 textView 的文本放在一行显示时的长度；
        // 该长度可能大于屏幕宽度（此时表示实际文本会折行显示，该长度等于折行后的各行文本的长度之和）；
        float width;
        if(type == 0) {
            width = android.text.Layout.getDesiredWidth(textView.getText(), textView.getPaint());
        } else {
            width = textView.getPaint().measureText(textView.getText().toString());
        }
        return width;
    }

    /**
     * 获取宽度最宽的文字
     * @param mPaint 画笔
     * @param strs 比较用的String类型字符串队列
     * @return 宽度最宽的文字
     */
    public static String getMaxWidthString(Paint mPaint, String...strs) {
        if(strs == null) return null;
        String temp = strs[0];
        for(int i=0;i+1<strs.length;i++) {
            temp = mPaint.measureText(strs[i+1]) > mPaint.measureText(strs[i]) ? strs[i+1] : strs[i];
        }
        return temp;
    }

    /** 设置斜体文字*/
    public static void italy(TextView tv) {
        if(tv == null) {
            return;
        }
        String str = tv.getText().toString().trim() + " ";//字体倾斜的边缘显示不完整问题
        if(!TextUtils.isEmpty(str)) {
            SpannableString sp = new SpannableString(str);
            sp.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 0, sp.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            tv.setText(sp);
        }
    }

    /**判断字符串是否为数字----方式2,正则表达式*/
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 为文字设置风格（粗体、斜体）
     */
    private void italyAndBlod(TextView tv, int start, int end){
        if(tv == null) {
            return;
        }
        if(start > 0 && start <= end && end < tv.length()) {
            String str = tv.getText().toString().trim() + " ";//字体倾斜的边缘显示不完整问题
            SpannableString spannableString = new SpannableString(str);
            //            StyleSpan colorSpan = new StyleSpan(Typeface.BOLD);
            //            StyleSpan colorSpanit = new StyleSpan(Typeface.ITALIC);
            //            spannableString.setSpan(colorSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            //            spannableString.setSpan(colorSpanit, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            //            tv.setHighlightColor(Color.parseColor("#36969696"));
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tv.setText(spannableString);
        }
    }

    /**
     * 设置部分字体颜色
     * @param tv 文字控件
     * @param start 开始位置。含首不含尾
     * @param end 结束位置
     * @param colorString 颜色
     */
    public static void setColor(TextView tv, int start, int end, String colorString){
        int color = Color.parseColor(colorString);
        setColor(tv, start, end, color);
    }

    /**
     * 设置部分字体颜色
     * @param tv 文字控件
     * @param start 开始位置。含首不含尾
     * @param end 结束位置
     * @param colorInt 颜色
     */
    public static void setColor(TextView tv, int start, int end, int colorInt){
        if(tv == null) {
            return;
        }
        if(start > 0 && start <= end && end < tv.length()) {
            SpannableString spannableString = new SpannableString(tv.getText().toString().trim());
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(colorInt);
            spannableString.setSpan(colorSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tv.setText(spannableString);
        }
    }

    /**
     * 设置部分字体背景颜色
     * @param tv 文字控件
     * @param start 开始位置。含首不含尾
     * @param end 结束位置
     * @param colorString 颜色
     */
    public static void backgroundColor(TextView tv, int start, int end, String colorString){
        int color = Color.parseColor(colorString);
        backgroundColor(tv, start, end, color);
    }

    /**
     * 设置部分字体背景颜色
     * @param tv 文字控件
     * @param start 开始位置。含首不含尾
     * @param end 结束位置
     * @param colorInt 颜色
     */
    public static void backgroundColor(TextView tv, int start, int end, int colorInt){
        if(tv == null) {
            return;
        }
        if(start > 0 && start <= end && end < tv.length()) {
            SpannableString spannableString = new SpannableString(tv.getText().toString().trim());
            BackgroundColorSpan colorSpan = new BackgroundColorSpan(colorInt);
            spannableString.setSpan(colorSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tv.setText(spannableString);
        }
    }

    /**
     * 为文本设置中划线，也就是常说的删除线
     * @param tv 文字控件
     */
    private static void strikeThrough(TextView tv) {
        strikeThrough(tv, 0, tv.getText().toString().trim().length());
    }

    /**
     * 为文本设置中划线，也就是常说的删除线
     * @param tv 文字控件
     * @param start 开始位置
     * @param end 结束位置
     */
    private static void strikeThrough(TextView tv, int start, int end){
        if(tv == null) {
            return;
        }
        if(start > 0 && start <= end && end < tv.length()) {
            SpannableString spannableString = new SpannableString(tv.getText().toString().trim());
            StrikethroughSpan colorSpan = new StrikethroughSpan();
            spannableString.setSpan(colorSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tv.setText(spannableString);
        }
    }

    /**
     * 设置文本中添加图片表情
     */
    public void addImage(TextView tv, int drawableId, int drawableSize){
        Context ctx = tv.getContext();
        Drawable drawable = ctx.getResources().getDrawable(drawableId);
        addImage(tv, 0, tv.getText().toString().trim().length(), drawable, drawableSize, drawableSize);
    }

    /**
     * 设置文本中添加图片表情
     */
    public void addImage(TextView tv, int start, int end, int drawableId, int drawableWidth, int drawableHeight){
        Context ctx = tv.getContext();
        Drawable drawable = ctx.getResources().getDrawable(drawableId);
        addImage(tv, start, end, drawable, drawableWidth, drawableHeight);
    }

    /**
     * 设置文本中添加图片表情
     */
    public void addImage(TextView tv, int start, int end, Drawable drawable, int drawableWidth, int drawableHeight){
        if(tv == null) {
            return;
        }
        if(start > 0 && start <= end && end < tv.length() && drawableWidth > 0 && drawableHeight > 0) {
            SpannableString spannableString = new SpannableString(tv.getText().toString().trim());
            drawable.setBounds(0, 0, drawableWidth, drawableHeight);
            ImageSpan imageSpan = new ImageSpan(drawable);
            spannableString.setSpan(imageSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tv.setText(spannableString);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    // 加载工具
    ////////////////////////////////////////////////////////////////////////////////
    
	/**
	 * 启动线程。根据限制的宽度为文本控件获取合适的字体大小。限制为单行
	 * @param txt 需要计算的文字内容
	 * @param limited 限制的宽度、高度。[宽度, 高度]。单位px
	 * @param listener 计算结果监听
	 */
	public static void callSuitableFont(String txt, final float[] limited, final OnSizeRecountListener listener) {
		final String str = txt;
		new Thread(new Runnable() {
			public void run() {
                askFitSize(str, limited, listener);
			}
		}).start();
	}

    /**
     * 根据文本内容 和 限制的宽高，计算合适的字体大小
     * @param txt 文本
     * @param limited 限制的宽高。[宽, 高]。单位px
     * @param listener 结果回调
     */
	public static void askFitSize(String txt, float[] limited, OnSizeRecountListener listener) {
        float width = limited[0];
        float height = limited[1];
        float size = 1;
        if(width == 0 || height == 0) {
            if(listener != null) listener.onSizeResult(size);
            return;
        }
        String text = txt;
        Paint paint = new Paint();
        int zoomType = 0;//0=缩小，1=放大
        int zoomDirection = 0;//0=x轴，1=y轴
        //计算是缩小还是放大
        //计算缩放倍数
        paint.setTextSize(size);
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        //计算限制的宽度是实际字体宽度的几倍：X轴缩放比=限制宽度/实际宽度
        float scaleW = width / rect.width();
        //计算限制的高度是实际字体宽度的几倍：Y轴缩放比=限制高度/实际高度
        float scaleH = height / rect.height();
        float scale = 1;
        float minScale = Math.min(scaleW, scaleH);
        float maxScale = Math.max(scaleW, scaleH);
        if((maxScale < 1) || (minScale > 1)) {
            boolean isScaleWidthBiggerThanHeight = scaleW > scaleH;
            if(scaleW > 1 && scaleH > 1) {
                //限制宽>实际宽，限制高>实际高
                zoomType = 1;
            }
            //取缩小最多的方向，放大最小的方向
            zoomDirection = isScaleWidthBiggerThanHeight ? 1 : 0;
            //取最小倍数
            scale = isScaleWidthBiggerThanHeight ? scaleH : scaleW;
        } else {
            //限制的宽高中有任意一项小于实际字体宽高，字体应缩小
            if(scaleW < 1) {
                scale = scaleW;//取缩小倍数
            } else {
                scale = scaleH;//取缩小倍数
                zoomDirection = 1;
            }
        }
        size *= scale;
        //计算缩放后的Size
        if(zoomDirection == 0) {
            //X轴
            //缩放字体
            while(true) {
                paint.setTextSize(size);
                float wid = paint.measureText(text);
                if(zoomType == 0) {
                    if(wid > width) {
                        size -= 1;
                    } else break;
                } else {
                    if(wid < width) {
                        size += 1;
                    } else {
                        size -= 1;
                        break;
                    }
                }
            }
        } else {
            //Y轴
            //缩放字体
            while(true) {
                paint.setTextSize(size);
                paint.getTextBounds(text, 0, text.length(), rect);
                float hei = rect.height();
                if(zoomType == 0) {
                    if(hei > height) {
                        size -= 1;
                    } else break;
                } else {
                    if(hei < height) {
                        size += 1;
                    } else {
                        size -= 1;
                        break;
                    }
                }
            }
        }
        if(listener != null) listener.onSizeResult(size);
    }
	
	/**
	 * 计算一个合适大小的字体，使其刚好填充宽度。限制为单行
	 * @param txt 需要计算的文字内容
	 * @param textSize 原始字体大小。单位px
	 * @param widthLimited 限制的宽度。单位px
	 * @param listener 计算结果监听
	 */
	public static void askFitWidth(String txt, float textSize, float widthLimited, OnSizeRecountListener listener) {
        float width = widthLimited;
        float size = textSize;
        if(width == 0) {
            if(listener != null) listener.onSizeResult(size);
            return;
        }
        String text = txt;
        Paint paint = new Paint();
        int zoomType = 0;//0=缩小，1=放大
        //计算是缩小还是放大
        //计算缩放倍数
        paint.setTextSize(size);
        float tempWidth = paint.measureText(text);
        float scale = width / tempWidth;
        if(scale > 1) {
            zoomType = 1;
        }
        size *= scale;
        //缩放字体
        while(true) {
            paint.setTextSize(size);
            float wid = paint.measureText(text);
            if(zoomType == 0) {
                if(wid > width) {
                    size -= 1;
                } else break;
            } else {
                if(wid < width) {
                    size += 1;
                } else {
                    size -= 1;
                    break;
                }
            }
        }
        if(listener != null) listener.onSizeResult(size);
	}

	//限制为单行
	public static void askFitHeight(String txt, float textSize, float heightLimited, OnSizeRecountListener listener) {
		float height = heightLimited;
        float size = textSize;
        if(height == 0) {
            if(listener != null) listener.onSizeResult(size);
            return;
        }
        String text = txt;
        Paint paint = new Paint();
        int zoomType = 0;//0=缩小，1=放大
        //计算是缩小还是放大
        //计算缩放倍数
        paint.setTextSize(size);
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        float scale = height / rect.height();
        if(scale > 1) {//需要放大
            zoomType = 1;
        }
        size *= scale;
        //缩放字体
        while(true) {
            paint.setTextSize(size);
            paint.getTextBounds(text, 0, text.length(), rect);
            float hei = rect.height();
            if(zoomType == 0) {
                if(hei > height) {
                    size -= 1;
                } else break;
            } else {
                if(hei < height) {
                    size += 1;
                } else {
                    size -= 1;
                    break;
                }
            }
        }
        if(listener != null) listener.onSizeResult(size);
	}
	
	/** 计算结果回调*/
	public interface OnSizeRecountListener {
		/**
		 * @param size 输入的单位为px，如果使用TextView.setTextSize()设置字体大小，需要先转化为sp
		 */
		public void onSizeResult(float size);
	}


}