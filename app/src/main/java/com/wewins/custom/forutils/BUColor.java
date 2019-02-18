package com.wewins.custom.forutils;

import android.content.Context;
import android.graphics.Color;

import java.util.Locale;
import java.util.Random;

/**
 * 类说明：<br />
 * Color的扩展类
 * 
 * @author binley <br />
 *         E-mail: wangbl@we-wins.com
 * @version 1.0 <br />
 *          Time create:2017年9月8日 下午4:15:55
 * 
 */
public class BUColor extends Color {

	/**
	 * 获取十六进制的颜色代码.例如 "#6E36B4" , 不含透明度
	 * 
	 * @return String
	 */
	public static int randomColorInt() {
		return Color.parseColor(randomColor());
	}

	/**
	 * 获取十六进制的颜色代码.例如 "#6E36B4" , 不含透明度
	 * 
	 * @return String
	 */
	public static String randomColor() {
		String r, g, b;
		Random random = new Random();
		r = Integer.toHexString(random.nextInt(256))
				.toUpperCase(Locale.ENGLISH);
		g = Integer.toHexString(random.nextInt(256))
				.toUpperCase(Locale.ENGLISH);
		b = Integer.toHexString(random.nextInt(256))
				.toUpperCase(Locale.ENGLISH);
		r = r.length() == 1 ? "0" + r : r;
		g = g.length() == 1 ? "0" + g : g;
		b = b.length() == 1 ? "0" + b : b;
		return "#FF" + r + g + b;
	}

	/**
	 * 获取十六进制的颜色代码.例如 "0xff6E36B4" , 含透明度
	 * 
	 * @return String
	 */
	public static int randomAlphaColorInt() {
		return Color.parseColor(randomAlphaColor());
	}

	/**
	 * 获取十六进制的颜色代码.例如 "#FF6E36B4" , 含透明度
	 * 
	 * @return String
	 */
	public static String randomAlphaColor() {
		String r, g, b, a;
		Random random = new Random();
		a = Integer.toHexString(random.nextInt(256))
				.toUpperCase(Locale.ENGLISH);
		r = Integer.toHexString(random.nextInt(256))
				.toUpperCase(Locale.ENGLISH);
		g = Integer.toHexString(random.nextInt(256))
				.toUpperCase(Locale.ENGLISH);
		b = Integer.toHexString(random.nextInt(256))
				.toUpperCase(Locale.ENGLISH);
		a = a.length() == 1 ? "0" + a : a;
		r = r.length() == 1 ? "0" + r : r;
		g = g.length() == 1 ? "0" + g : g;
		b = b.length() == 1 ? "0" + b : b;
		return "#" + a + r + g + b;
	}

	/** 获取res资源文件中的颜色*/
	public static int getResourceColor(Context context, int id) {
		return context.getResources().getColor(id);
	}

}
