package com.yiqu.iyijiayi.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class AppVersionCode {
	/**
	 * 获取软件当前版本好号 Add By long
	 * 
	 * @return
	 */
	public static int getVersionCode(Context context) {
		int versionCode = 1;

		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);

			versionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return versionCode;
	}


	/**
	 * 获取软件当前版本好号 Add By long
	 *
	 * @return
	 */
	public static String getVersionName(Context context) {
		String versionCode = "1";

		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);

			versionCode = info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return versionCode;
	}
}
