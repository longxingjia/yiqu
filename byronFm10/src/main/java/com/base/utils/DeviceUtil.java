package com.base.utils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;


/**
 * 
 *@comments 与设备相关的一些处理
 */
public class DeviceUtil {
	/**
	 * Check if is OPhone
	 * @param context
	 * @return
	 */
	public static boolean isOphone(Context context) {
		String packageName = "oms.mms";

		PackageManager pm = context.getPackageManager();
		Intent filterIntent = new Intent();
		filterIntent.setPackage(packageName);
		List<ResolveInfo> activities = pm.queryIntentActivities(filterIntent, 0);

		boolean result = activities == null? false : activities.size() > 0;

		return result;
	}
	
	/**
	 * @comments 判断网络是否连接
	 * @param context
	 * @return
	 * @version 1.0
	 */
	public static boolean isNetWorkOpen(Context context) {
		// Check network information
		try{
			ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
			if (networkInfo != null) {
				return networkInfo.isAvailable();
			}
		}catch(Exception e){
			//出错了，有可能此手机系统不支持获取网络状态，但是并不代表之后的网络接口访问会失败。
			//所以还是返回真吧
			e.printStackTrace();
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @comments 获取屏幕的宽度
	 * @param context
	 * @return
	 * @version 1.0
	 */
	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * 
	 * @comments 获取屏幕的高度
	 * @param context
	 * @return
	 * @version 1.0
	 */
	public static int getScreenHeight(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
	
	/**
	 * Get device ID (IMEI)
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context c) {
		String deviceId = getIMEI(c);
		if(deviceId == null){
			try{
				TelephonyManager tm = (TelephonyManager)c.getSystemService(Context.TELEPHONY_SERVICE);
				deviceId = tm.getDeviceId();
			}catch(Exception e){
				e.printStackTrace();
			}
			if(deviceId == null){
				deviceId = UUID.randomUUID().toString();
			}
			if(deviceId != null && deviceId.contains("-")){
				deviceId = deviceId.replace("-", "_");
			}
			setIMEI(c, deviceId);
		}		
		return deviceId;
	}
	private static final String FILE_NAME = "device";
	private class Keys {
		private static final String IMEI = "imei";
	}
	
	public static void setIMEI(Context c, String imei){
		SharedPreferences p = c.getSharedPreferences(FILE_NAME, Context.MODE_APPEND);
		Editor e = p.edit();
		e.putString(Keys.IMEI, imei);
		e.commit();
	}

	public static String getIMEI(Context c){
		SharedPreferences p = c.getSharedPreferences(FILE_NAME,Context.MODE_APPEND);
		return p.getString(Keys.IMEI, null);
	}
	
	/**
	 * Get subscriber ID (IMSI)
	 * @param context
	 * @return
	 */
	public static String getSubscriberId(Context context) {
		String subscriberId = null;
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		subscriberId = telephonyManager.getSubscriberId();
		return subscriberId;
	}
	 /**
	 * Role:Telecom service providers>
	 * <uses-permission
	 * android:name="android.permission.READ_PHONE_STATE"/>
	 */
	public static String getProvidersName(Context context) {
	    String ProvidersName = "";
	    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	    ProvidersName = telephonyManager.getSimOperatorName();
	    return ProvidersName;
	}
	 /**
	 * Role:Telecom service providers>
	 * <uses-permission
	 * android:name="android.permission.READ_PHONE_STATE"/>
	 */
	public static String getProvidersCode(Context context) {
	    String ProvidersCode = "";
	    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	    ProvidersCode = telephonyManager.getSimOperator();
	    return ProvidersCode;
	}

	/**
	 * Get phone's SN number
	 * 
	 * @return
	 */
	public static String getSerialNo() {
		String serialnum = null;
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method get = c.getMethod("get", String.class, String.class);
			serialnum = (String) (get.invoke(c, "ro.serialno", "unknown"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serialnum;
	}
	
	/**
	 * Get phone's CID (carrier)
	 * @return
	 */
	public static String getCarrier() {
		String carrier = null;
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method get = c.getMethod("get", String.class, String.class);
			carrier = (String) (get.invoke(c, "ro.carrier", "unknown"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return carrier;
	}
	
	/**
	 * Get phone's Main board version
	 * @return
	 */
	public static String getHardware() {
		String hardware = null;
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method get = c.getMethod("get", String.class, String.class);
			hardware = (String) (get.invoke(c, "ro.hardware", "unknown"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hardware;
	}
	
	/**
	 * Get phone's SPL (Hboot) version number
	 * @return
	 */
	public static String getBootloader() {
		String bootloader = null;
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method get = c.getMethod("get", String.class, String.class);
			bootloader = (String) (get.invoke(c, "ro.bootloader", "unknown"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bootloader;
	}

	/**
	 * Get line1 number
	 * @param context
	 * @return
	 */
	public static String getLine1Number(Context context) {
		TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		String line1Number = tm.getLine1Number();
		return line1Number;
	}
	
	/**
	 * @param context
	 * @param file
	 */
	public static void installApk(Context context, File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), getMIMEType(file));
		context.startActivity(intent);
	}
	
	/**
	 * @param file
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static String getMIMEType(File file) {
        String type = "";
        String fName = file.getName();
		String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();

		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            type = "image";
        } else if (end.equals("apk")) {
            /* android.permission.INSTALL_PACKAGES */
            type = "application/vnd.android.package-archive";
        } else {
            type = "*";
        }
		
		//Could be opened directly, popup selection menu to user
        if (end.equals("apk")) {
        } else {
            type += "/*";
        }
        return type;
    }
	
	/**
	 * @param context
	 * @return
	 * @throws NameNotFoundException 
	 */
	public static String getVersionName(Context context){
		String result = null;
		try {
			result = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * @comments 获取当前应用的版本号
	 * @param context
	 * @return 版本号
	 * @version 1.0
	 */
	public static int getVersionCode(Context context) {
		int result = -1;
		try {
			result = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 
	 * @comments 获取SD可用空间
	 * @return -1: no sdcard; else 可用空间字节数
	 * @version 1.0
	 */
	public static  long getSDCardAvailMemory() {
	     String state = Environment.getExternalStorageState();
	     if(Environment.MEDIA_MOUNTED.equals(state)) {
	    	 File sdcardDir = Environment.getExternalStorageDirectory();
	      	StatFs sf = new StatFs(sdcardDir.getPath());
	      	long blockSize = sf.getBlockSize();
//	      	long blockCount = sf.getBlockCount();
	      	long availCount = sf.getAvailableBlocks();
//	        String totalsizeFormat = "";//Formatter.formatFileSize(this, blockSize*blockCount);
//	        totalsizeFormat = "";//Formatter.formatFileSize(this, blockSize*availCount);
	        return blockSize*availCount;
	     }  
	     return -1;
	}
	/**
	 * 
	 * @comments 获取系统data区可用空间
	 * @return
	 * @version 1.0
	 */
	public static  long  getSystemAvailMemory() {   
       File path = Environment.getDataDirectory();   
       StatFs sf = new  StatFs(path.getPath());   
       long  blockSize = sf.getBlockSize();   
//       long  blockCount = sf.getBlockCount();   
       long  availCount = sf.getAvailableBlocks();  
//       String totalsizeFormat = "";//Formatter.formatFileSize(this, blockSize*blockCount);
//       totalsizeFormat = "";//Formatter.formatFileSize(this, blockSize*availCount);
       return blockSize*availCount;
   }
	/**
	 * @comments 获取系统model 机型
	 * @return
	 * @version 1.0
	 */
	public static final String getModelName(){
		return Build.MODEL;
	}
	/**
	 * @comments 获取系统版本
	 * @return
	 * @version 1.0
	 */
	@SuppressWarnings("deprecation")
	public static final String getOsVersion(){
		return Build.VERSION.SDK;
	}
}
