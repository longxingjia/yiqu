package com.base.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 
 * @comments 工具类
 */
public class Utils {

	/**
	 * @comments 获取数据库路径
	 * @param c
	 * @return
	 * @version 1.0
	 */
	public static String getDateBasePath(Context c) {
		return "data/data/" + c.getPackageName() + "/databases/";
	}

	/**
	 * @comments 获取缓存路径
	 * @param c
	 * @return
	 * @version 1.0
	 */
	public static String getSharedsBasePath(Context c) {
		return "data/data/" + c.getPackageName() + "/shared_prefs/";
	}

	/**
	 * 
	 * @comments 程序是否在前台运行
	 * @param c
	 * @return
	 * @version 1.0
	 */
	public static boolean isAppOnForeground(Context c) {
		// Returns a list of application processes that are running on the
		// device
		ActivityManager activityManager = (ActivityManager) c
				.getApplicationContext().getSystemService(
						Context.ACTIVITY_SERVICE);
		String packageName = c.getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	
	/**
	 * @comments 文件拷贝
	 * @param fromFile 源文件
	 * @param toFile 目标文件
	 * @return
	 * @throws Exception 异常
	 * @version 1.0
	 */
	public static boolean CopyFile(String fromFile, String toFile)
			throws Exception {
		InputStream fosfrom = null;
		OutputStream fosto = null;
		try {
			fosfrom = new FileInputStream(fromFile);
			fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c);
			}
			fosfrom.close();
			fosto.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fosfrom != null) {
					fosfrom.close();
				}
				if (fosto != null) {
					fosto.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	/**
	 * @comments 文件拷贝
	 * @param fosfrom 源文件
	 * @param toFile  目标文件
	 * @return
	 * @version 1.0
	 */
	public static boolean CopyFile(InputStream fosfrom, String toFile) {
		OutputStream fosto = null;
		try {
			fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c);
			}
			fosfrom.close();
			fosto.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fosfrom != null) {
					fosfrom.close();
				}
				if (fosto != null) {
					fosto.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	/**
	 * @comments 时间处理。根据系统当前时间，按规则换算出相当时间
	 * @param c
	 * @param t 时间
	 * @return
	 * @version 1.0
	 */
	public static String stringTime(Calendar c, long t) {
		if(t != 0){
			c.setTimeInMillis(System.currentTimeMillis());
			int cyear = c.get(Calendar.YEAR);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			long ctTodayAm = c.getTimeInMillis();
			c.add(Calendar.DAY_OF_MONTH, 1);
			long ctTodayPm = c.getTimeInMillis();
			c.add(Calendar.DAY_OF_MONTH, -2);
			long ctYesterdayAm = c.getTimeInMillis();

			c.setTimeInMillis(t);
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int day = c.get(Calendar.DAY_OF_MONTH);
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			if (ctYesterdayAm <= t && ctTodayPm > t) {
				if (ctTodayAm < t) {// 今日
					return "今天 " + getTwoDigital(hour) + ":"
							+ getTwoDigital(minute);
				} else {// 昨日
					return "昨天 " + getTwoDigital(hour) + ":"
							+ getTwoDigital(minute);
				}
			}
			if (cyear == year) {
				return getTwoDigital(month) + "/" + getTwoDigital(day) + " "
						+ getTwoDigital(hour) + ":" + getTwoDigital(minute);
			}
			return getTwoDigital(year) + "/" + getTwoDigital(month) + "/"
					+ getTwoDigital(day) + " " + getTwoDigital(hour) + ":"
					+ getTwoDigital(minute);
		}
		return null;
	}
	/**
	 * @comments 处理数字，使得1～9===> 01~09 
	 * @param one 被处理数字
	 * @return 处理结果
	 * @version 1.0
	 */
	public static String getTwoDigital(int one) {
		if (one < 10) {
			return "0" + one;
		} else {
			return String.valueOf(one);
		}
	}

	/**
	 * @comments 隐藏输入法
	 * @param c
	 * @param edit
	 * @version 1.0
	 */
	public static void hiddenInput(Context c, EditText edit) {
		try {
			InputMethodManager imm = (InputMethodManager) c
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(edit.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @comments 显示输入法
	 * @param c
	 * @param edit
	 * @version 1.0
	 */
	public static void showInput(Context c, EditText edit) {
		try {
			edit.requestFocus();
			InputMethodManager imm = (InputMethodManager) c
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(edit, InputMethodManager.SHOW_IMPLICIT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @comments 判断时间是否今天
	 * @param c
	 * @param lastTime
	 * @return
	 * @version 1.0
	 */
	public static boolean isToday(Calendar c, long lastTime) {
		c.setTimeInMillis(lastTime);
		int lyear = c.get(Calendar.YEAR);
		int lmonth = c.get(Calendar.MONTH);
		int lday = c.get(Calendar.DAY_OF_MONTH);
		c.setTimeInMillis(System.currentTimeMillis());
		int cyear = c.get(Calendar.YEAR);
		int cmonth = c.get(Calendar.MONTH);
		int cday = c.get(Calendar.DAY_OF_MONTH);
		if (lyear != cyear || lmonth != cmonth || lday != cday) {
			return false;
		}
		return true;
	}

	/**
	 * @comments 获取渠道ID
	 * @param context
	 * @return
	 * @version 1.0
	 */
	public static String getChannelCode(Context context) {
		String code = getMetaData(context, "CHANNEL");
		if (code != null) {
			return code;
		}
		return "c_000";
	}

	/**
	 * @comments 获取AndroidManifest.xml metaData信息
	 * @param context
	 * @param key
	 * @return
	 * @version 1.0
	 */
	private static String getMetaData(Context context, String key) {
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			Object value = ai.metaData.get(key);
			if (value != null) {
				return value.toString();
			}
		} catch (Exception e) {
			//
		}
		return null;
	}

	/**
	 * @comments 通过URL地址取得文件名字
	 * @param url
	 * @param suffix
	 * @return
	 * @version 1.0
	 */
	public static String getFileNameFromUrlFormat(String url, String suffix) {
		final int end = url.lastIndexOf(".");
		if (end == -1) {
			return url.substring(url.lastIndexOf("/") + 1) + "." + suffix;
		} else {
			String fle = url.substring(url.lastIndexOf("/") + 1, end);
			int i = fle.lastIndexOf(".");
			if (i != -1 && i < fle.length() - 1) {
				fle = fle.substring(i + 1);
			}
			return fle + "." + suffix;
		}
	}
	/**
	 * @comments 获取指定天的 最后一秒时间
	 * @param time
	 * @return
	 * @version 1.0
	 */
	public static long getMaxDayTime(int year, int month, int day){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 0);
		c.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		return c.getTimeInMillis();
	}
	
	/**
	 * @comments 获取指定天 最早一秒时间
	 * @param time
	 * @return
	 * @version 1.0
	 */
	public static long getMinDayTime(int year, int month, int day){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		return c.getTimeInMillis();
	}


}
