package com.wewins.custom.forutils;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import java.util.HashMap;
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
		int color = context.getResources().getColor(id);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			color = context.getResources().getColor(id, null);
		}
		return color;
	}

    private static final HashMap<String, Integer> sColorNameMap;
    static {
        sColorNameMap = new HashMap<>();
        sColorNameMap.put("black", BLACK);
        sColorNameMap.put("darkgray", DKGRAY);
        sColorNameMap.put("gray", GRAY);
        sColorNameMap.put("lightgray", LTGRAY);
        sColorNameMap.put("white", WHITE);
        sColorNameMap.put("red", RED);
        sColorNameMap.put("green", GREEN);
        sColorNameMap.put("blue", BLUE);
        sColorNameMap.put("yellow", YELLOW);
        sColorNameMap.put("cyan", CYAN);
        sColorNameMap.put("magenta", MAGENTA);
        sColorNameMap.put("aqua", 0xFF00FFFF);
        sColorNameMap.put("fuchsia", 0xFFFF00FF);
        sColorNameMap.put("darkgrey", DKGRAY);
        sColorNameMap.put("grey", GRAY);
        sColorNameMap.put("lightgrey", LTGRAY);
        sColorNameMap.put("lime", 0xFF00FF00);
        sColorNameMap.put("maroon", 0xFF800000);
        sColorNameMap.put("navy", 0xFF000080);
        sColorNameMap.put("olive", 0xFF808000);
        sColorNameMap.put("purple", 0xFF800080);
        sColorNameMap.put("silver", 0xFFC0C0C0);
        sColorNameMap.put("teal", 0xFF008080);

    }

	public static int parseColor(String colorString) {
        if (colorString.charAt(0) == '#') {
            // 兼容输入的格式"#XXX"
            if(colorString.length() == 4) {
                String s1 = colorString.substring(1,2);
                String s2 = colorString.substring(2,3);
                String s3 = colorString.substring(3,4);
                colorString = String.format("#FF%s%s%s%s%s%s", s1, s1, s2, s2, s3, s3);
            }
            // Use a long to avoid rollovers on #ffXXXXXX
            long color = Long.parseLong(colorString.substring(1), 16);
            if (colorString.length() == 7) {
                // Set the alpha value
                color |= 0x00000000ff000000;
            } else if (colorString.length() != 9) {
                throw new IllegalArgumentException("Unknown color");
            }
            return (int)color;
        } else {
            Integer color = sColorNameMap.get(colorString.toLowerCase(Locale.ROOT));
            if (color != null) {
                return color;
            }
        }
        throw new IllegalArgumentException("Unknown color");
    }


}
