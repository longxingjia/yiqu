package com.yiqu.iyijiayi.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.base.utils.ToastManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class Tools {

	public static String SD_PATH = null;
	public static final String PACKAGE_NAME = "com.yiqu.iyijiayi";
	public static String DB_PATH = null;


//	public static boolean isNetworkConnected(Context context) {
//		if (context != null) {
//			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
//					.getSystemService(Context.CONNECTIVITY_SERVICE);
//			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
//			if (mNetworkInfo != null) {
//				return mNetworkInfo.isAvailable();
//			}
//		}
//		return false;
//	}
	
	public static boolean isSDCardAvailable(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// sd card 可用
			
			File sdDir = Environment.getExternalStorageDirectory();
			
			if(sdDir.exists())
			{
				SD_PATH = sdDir.toString();
			}
			else
			{
				Dev_MountInfo dev = Dev_MountInfo.getInstance();
				Dev_MountInfo.DevInfo info = dev.getExternalInfo();// External SD Card
														// Informations

				if (null == info) {
					info = dev.getInternalInfo();// Internal SD Card Informations
				}

				if (null == info) {
					Toast.makeText(context, "SD卡不存在，暂时无法下载数据！请插入SD卡之后进行下载。",
							Toast.LENGTH_LONG).show();
					return false;
				}
				// Methods:
				SD_PATH = info.getPath(); // SD 卡路径
			}

			Log.d("sd path", SD_PATH);
			return true;
		} else {
			// 当前不可用
			Toast.makeText(context, "SD卡不存在，暂时无法下载数据！请插入SD卡之后进行下载。",
					Toast.LENGTH_LONG).show();
			return false;
		}
	}

	/**
	 * 拷贝Assets下文件到sd卡并解压
	 * 
	 * @param context
	 * @param fileName
	 */
	public static boolean copyBigDataToSD(Context context, String strOutPath,
			String fileName) {
		File path = new File(strOutPath
				+ fileName.substring(0, fileName.length() - 4));
		if (path.exists()) {
			// 路径不存在创建路径
			delete(path);
		}

		InputStream myInput;
		OutputStream myOutput;
		try {
			myOutput = new FileOutputStream(strOutPath + "/" + fileName);

			myInput = context.getAssets().open(fileName);
			byte[] buffer = new byte[1024];
			int length = myInput.read(buffer);
			while (length > 0) {
				myOutput.write(buffer, 0, length);
				length = myInput.read(buffer);
			}

			myOutput.flush();
			myInput.close();
			myOutput.close();

			File zipFile = new File(strOutPath + "/" + fileName);
			unzip(zipFile, fileName);
			zipFile.delete();

			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void delete(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
			file.delete();
		}
	}

	@SuppressWarnings("unchecked")
	public static long unzip(File mInput, String gamePackage) {
		long extractedSize = 0L;
		Enumeration<ZipEntry> entries;
		ZipFile zip = null;
		try {
			zip = new ZipFile(mInput);

			entries = (Enumeration<ZipEntry>) zip.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				if (entry.isDirectory()) {
					continue;
				}
				File destination = new File(new File(DB_PATH),
						entry.getName().replace(entry.getName().substring(0, entry.getName().indexOf("/")),
								gamePackage)
						);
				if (!destination.getParentFile().exists()) {
					destination.getParentFile().mkdirs();
				}
				FileOutputStream outStream = new FileOutputStream(destination);
				extractedSize += copy(zip.getInputStream(entry), outStream);
				outStream.close();
				
			}
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(null != zip)
					zip.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return extractedSize;
	}

	public static int copy(InputStream input, OutputStream output) {
		byte[] buffer = new byte[1024 * 8];
		BufferedInputStream in = new BufferedInputStream(input, 1024 * 8);
		BufferedOutputStream out = new BufferedOutputStream(output, 1024 * 8);
		int count = 0, n = 0;
		try {
			while ((n = in.read(buffer, 0, 1024 * 8)) != -1) {
				out.write(buffer, 0, n);
				count += n;
			}
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return count;
	}

	/**
	 * 加载本地图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
     * 检查当前网络是否可用
     * 
     * @return
     */
    
    public static boolean isNetworkAvailable(Activity activity)
    {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (connectivityManager == null)
        {
			ToastManager.getInstance(activity).showText("网络不可用，请检查网络状态");
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            
            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
		ToastManager.getInstance(activity).showText("网络不可用，请检查网络状态");
        return false;
    }

	/**
	 * 获取应用专属缓存目录
	 * android 4.4及以上系统不需要申请SD卡读写权限
	 * 因此也不用考虑6.0系统动态申请SD卡读写权限问题，切随应用被卸载后自动清空 不会污染用户存储空间
	 * @param context 上下文
	 * @param type 文件夹类型 可以为空，为空则返回API得到的一级目录
	 * @return 缓存文件夹 如果没有SD卡或SD卡有问题则返回内存缓存目录，否则优先返回SD卡缓存目录
	 */
	public static File getCacheDirectory(Context context,String type) {
		File appCacheDir = getExternalCacheDirectory(context,type);
		if (appCacheDir == null){
			appCacheDir = getInternalCacheDirectory(context,type);
		}

		if (appCacheDir == null){
			Log.e("getCacheDirectory","getCacheDirectory fail ,the reason is mobile phone unknown exception !");
		}else {
			if (!appCacheDir.exists()&&!appCacheDir.mkdirs()){
				Log.e("getCacheDirectory","getCacheDirectory fail ,the reason is make directory fail !");
			}
		}
		return appCacheDir;
	}

	/**
	 * 获取SD卡缓存目录
	 * @param context 上下文
	 * @param type 文件夹类型 如果为空则返回 /storage/emulated/0/Android/data/app_package_name/cache
	 *             否则返回对应类型的文件夹如Environment.DIRECTORY_PICTURES 对应的文件夹为 .../data/app_package_name/files/Pictures
	 * {@link android.os.Environment#DIRECTORY_MUSIC},
	 * {@link android.os.Environment#DIRECTORY_PODCASTS},
	 * {@link android.os.Environment#DIRECTORY_RINGTONES},
	 * {@link android.os.Environment#DIRECTORY_ALARMS},
	 * {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
	 * {@link android.os.Environment#DIRECTORY_PICTURES}, or
	 * {@link android.os.Environment#DIRECTORY_MOVIES}.or 自定义文件夹名称
	 * @return 缓存目录文件夹 或 null（无SD卡或SD卡挂载失败）
	 */
	public static File getExternalCacheDirectory(Context context,String type) {
		File appCacheDir = null;
		if( Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			if (TextUtils.isEmpty(type)){
				appCacheDir = context.getExternalCacheDir();
			}else {
				appCacheDir = context.getExternalFilesDir(type);
			}

			if (appCacheDir == null){// 有些手机需要通过自定义目录
				appCacheDir = new File(Environment.getExternalStorageDirectory(),"Android/data/"+context.getPackageName()+"/cache/"+type);
			}

			if (appCacheDir == null){
				Log.e("getExternalDirectory","getExternalDirectory fail ,the reason is sdCard unknown exception !");
			}else {
				if (!appCacheDir.exists()&&!appCacheDir.mkdirs()){
					Log.e("getExternalDirectory","getExternalDirectory fail ,the reason is make directory fail !");
				}
			}
		}else {
			Log.e("getExternalDirectory","getExternalDirectory fail ,the reason is sdCard nonexistence or sdCard mount fail !");
		}
		return appCacheDir;
	}

	/**
	 * 获取内存缓存目录
	 * @param type 子目录，可以为空，为空直接返回一级目录
	 * @return 缓存目录文件夹 或 null（创建目录文件失败）
	 * 注：该方法获取的目录是能供当前应用自己使用，外部应用没有读写权限，如 系统相机应用
	 */
	public static File getInternalCacheDirectory(Context context,String type) {
		File appCacheDir = null;
		if (TextUtils.isEmpty(type)){
			appCacheDir = context.getCacheDir();// /data/data/app_package_name/cache
		}else {
			appCacheDir = new File(context.getFilesDir(),type);// /data/data/app_package_name/files/type
		}

		if (!appCacheDir.exists()&&!appCacheDir.mkdirs()){
			Log.e("getInternalDirectory","getInternalDirectory fail ,the reason is make directory fail !");
		}
		return appCacheDir;
	}
}
