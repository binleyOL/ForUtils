package com.wewins.custom.forutils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 类说明：<br />
 * 
 * @author binley
 * <br />
 * E-mail: wangbl@we-wins.com
 * @version 1.0
 * <br />
 * Time create:2017年11月6日 上午9:25:43
 *
 */
public class BUTime {
	
	/** 获取消息发送的时间*/
	public static String getFormatedMsgTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss", Locale.CHINA);
		final Date date = new Date();
		return format.format(date);
	}

}
