package com.wewins.custom.forutils.log;
import android.text.TextUtils;
import android.util.Log;

/**
 * 类说明：<br />
 * LOG工具类。使用该类中的log方法，能快捷的在项目中添加log信息。同时，通过更改DefinePlcae.class类中的LOG_SHOW字段的数值，
 * 可以快速、便捷的显示和隐藏项目中的Log。
 * 
 * @author binley <br />
 *         E-mail: wangbl@we-wins.com
 * @version 1.0 <br />
 *          Time create:2017年9月8日 下午3:33:21
 * 
 */
public class BULog {
	private static String logTag = "ADDC";
	private static LogType logType = LogType.show;

	public enum LogType {
		show, // 显示全部
		ignore, // 忽略全部
		only_d, // 只显示d标志的
		only_i, // 只显示i标志的
		only_v, // 只显示v标志的
		only_e, // 只显示e标志的
		except_d, // 除d标志的都显示
		except_i, // 除i标志的都显示
		except_v, // 除v标志的都显示
		except_e // 除e标志的都显示
	};

	public static void setLogTag(String tag) {
		if(TextUtils.isEmpty(tag))
			try {
				throw new Exception("You had put in a wrong tag, tag can not be empty!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		logTag = tag;
	}


	/**
	 * 设置Log的显隐状态
	 * @throws Exception
	 *             did not find this type
	 */
	public static void setLogType(LogType logType) {
//		if (logType.ordinal() > LogType.except_e.ordinal() || logType.ordinal() < LogType.show.ordinal())
//			throw new Exception("the log is limited in [" + LogType.show.ordinal()
//					+ "-" + LogType.except_e.ordinal() + "].");
		BULog.logType = logType;
	}

	public static final void d(String msg) {
		if (packageLog(1))
			Log.d(logTag, msg);
	}

	public static final void i(String msg) {
		if (packageLog(2))
			Log.i(logTag, msg);
	}

	public static final void v(String msg) {
		if (packageLog(3))
			Log.v(logTag, msg);
	}

	public static final void e(String msg) {
		if (packageLog(4))
			Log.e(logTag, msg);
	}

	private static final boolean packageLog(int tp) {
		boolean showOrNot = true;
		int theSwitch = logType.ordinal();
		if (theSwitch == LogType.show.ordinal()) {
		} else if (theSwitch == LogType.ignore.ordinal()) {
			showOrNot = false;
		} else {
			switch (tp) {
			case 1:
				if (theSwitch == LogType.except_d.ordinal()
						|| theSwitch == LogType.only_i.ordinal()
						|| theSwitch == LogType.only_v.ordinal()
						|| theSwitch == LogType.only_e.ordinal())
					showOrNot = false;
				break;
			case 2:
				if (theSwitch == LogType.except_i.ordinal()
						|| theSwitch == LogType.only_d.ordinal()
						|| theSwitch == LogType.only_v.ordinal()
						|| theSwitch == LogType.only_e.ordinal())
					showOrNot = false;
				break;
			case 3:
				if (theSwitch == LogType.except_v.ordinal()
						|| theSwitch == LogType.only_d.ordinal()
						|| theSwitch == LogType.only_i.ordinal()
						|| theSwitch == LogType.only_e.ordinal())
					showOrNot = false;
				break;
			case 4:
				if (theSwitch == LogType.except_e.ordinal()
						|| theSwitch == LogType.only_d.ordinal()
						|| theSwitch == LogType.only_i.ordinal()
						|| theSwitch == LogType.only_v.ordinal())
					showOrNot = false;
				break;
			default:
				break;
			}
		}
		return showOrNot;
	}
}