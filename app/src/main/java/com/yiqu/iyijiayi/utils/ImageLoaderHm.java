package com.yiqu.iyijiayi.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

import com.fwimageload.ImageLoaderEx;

import java.io.File;

/**
 *@comments 异步图片处理
 *@date 2016-8-31
 *@version 1.0
 */
public class ImageLoaderHm<T> extends ImageLoaderEx<T> {
	private Context mContext;
	private ImageLoaderFile mImageLoaderFile;
	public ImageLoaderHm(Context context) {
		this(context, 500);
	}
	public ImageLoaderHm(Context context, int maxPixel) {
		super(context, maxPixel);
		mContext = context;
		mImageLoaderFile = ImageLoaderFile.DEFAULT;
	}
	@Override
	protected File getFile(String url) {

		String fileName = getFileNameFromUrlFormat(url, "jpg");
		
		//存到data/data/pakege..下
		File fold = new File(mContext.getFilesDir()+mImageLoaderFile.getFolder());
		if (!fold.exists()) {
			fold.mkdirs();
		}
		File file = new File(fold, fileName);
		return file;
		
		//存到sd卡下
//		if (Environment.getExternalStorageState().equals(
//				Environment.MEDIA_MOUNTED)) {
//			
//			File localFile1 = Environment.getExternalStorageDirectory();
//			File localFile2 = new File(localFile1, FileConstant.FILE_PHOTO_CACHE_PATH + mImageLoaderFile.getFolder());
//			if (!localFile2.exists()) {
//				localFile2.mkdirs();
//			}
//			File f = new File(localFile2, fileName);
//			return f;
//		}
//		return null;
		
	}

	public static String getFileNameFromUrlFormat(String url, String suffix) {
		final int start = url.indexOf("context=");
		String fle = null;
		if(start != -1){
			fle = url.substring("context=".length() + start, url.length());
			fle = fle + "." + suffix;
		}else if(url.contains(".jpg")){
			return getFileName(url, suffix);
		}else{
			fle =  System.currentTimeMillis() + "." + suffix;
		}		
		return fle;
	}
	
	public static String getFileName(String url, String suffix) {
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
	
	@Override
	protected boolean isCallNet() {
		ConnectivityManager con = (ConnectivityManager)mContext.getSystemService(Activity.CONNECTIVITY_SERVICE);  
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();  
        boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();  
        if(wifi || internet){ 
			return true;
		}
		return false;
	}	

}