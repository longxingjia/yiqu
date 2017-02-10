package com.base.utils;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;

/**
 *@comments 利用intent调用系统的各种方法
 */
public class IntentUtils {

	/**
	 * @comments 通过拍照，并裁剪图片
	 * @param tempFile 图片临时缓存地址
	 * @param outputX  裁剪要的像素宽度
	 * @param outputY  裁剪要的像素高度
	 * @return 启动intent
	 * @version 1.0
	 */
	public static Intent takePhotoCrop(File tempFile, int outputX, int outputY){
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");//启动照相
		intent.putExtra("output", Uri.fromFile(tempFile));//照相的缓存图片
		intent.putExtra("crop", "true");//是否裁剪
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);// 输出图片大小,如640像素
		intent.putExtra("outputY", outputY);
		return intent;
	}
	/**
	 * @comments 通过相册选择，并裁剪图片
	 * @param tempFile 图片临时缓存地址
	 * @param outputX  裁剪要的像素宽度
	 * @param outputY  裁剪要的像素高度
	 * @return 启动intent
	 * @version 1.0
	 */
	public static Intent photoAlbumCrop(File tempFile, int outputX, int outputY){
		Intent intent = new Intent("android.intent.action.PICK");//启动相册
		intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
		intent.putExtra("output", Uri.fromFile(tempFile));//裁剪缓存图片
		intent.putExtra("crop", "true");//是否裁剪
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);// 输出图片大小
		intent.putExtra("outputY", outputY);
		return intent;
	}
	/**
	 * @comments 模拟launcher启动应用
	 * @param context
	 * @param packageName 要启动的包名
	 * @version 1.0
	 */
	public static void openApp(Context context, String packageName) {
		PackageInfo pi = null;
		try {
			pi = context.getPackageManager().getPackageInfo(packageName, 0);
			Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
			resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			resolveIntent.setPackage(pi.packageName);
			//寻找满足上述条件的启动意图
			List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(resolveIntent, 0);
			ResolveInfo ri = null;
			try {
				ri = apps.iterator().next();
			} catch (NoSuchElementException e) {
				return;
			}
			if (ri != null) {
				String packageName1 = ri.activityInfo.packageName;
				String className = ri.activityInfo.name;

				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);

				ComponentName cn = new ComponentName(packageName1, className);
				
				intent.setComponent(cn);
				//启动
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
	                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				context.startActivity(intent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	//android获取一个用于打开文本文件的intent    
	public static Intent getTextFileIntent( String param, boolean paramBoolean)   
	{   
	    Intent intent = new Intent("android.intent.action.VIEW");   
	    intent.addCategory("android.intent.category.DEFAULT");   
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
	    if (paramBoolean)   
	    {   
	        Uri uri1 = Uri.parse(param );   
	        intent.setDataAndType(uri1, "text/plain");   
	    }   
	    else   
	    {   
	        Uri uri2 = Uri.fromFile(new File(param ));   
	        intent.setDataAndType(uri2, "text/plain");   
	    }   
	    return intent;   
	}   
	  
	  
	//android获取一个用于打开CHM文件的intent    
	  public static Intent getChmFileIntent( String param )   
	  {   
	    Intent intent = new Intent("android.intent.action.VIEW");   
	    intent.addCategory("android.intent.category.DEFAULT");   
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
	    Uri uri = Uri.fromFile(new File(param ));   
	    intent.setDataAndType(uri, "application/x-chm");   
	    return intent;   
	  }   
	    
	//android获取一个用于打开PDF文件的intent    
	  public static Intent getPdfFileIntent( String param )   
	  {   
	    Intent intent = new Intent("android.intent.action.VIEW");   
	    intent.addCategory("android.intent.category.DEFAULT");   
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
	    Uri uri = Uri.fromFile(new File(param ));   
	    intent.setDataAndType(uri, "application/pdf");   
	    return intent;   
	  }   
	  
	  
	//android获取一个用于打开Word文件的intent    
	  public static Intent getWordFileIntent( String param )   
	  {   
	    Intent intent = new Intent("android.intent.action.VIEW");   
	    intent.addCategory("android.intent.category.DEFAULT");   
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
	    Uri uri = Uri.fromFile(new File(param ));   
	    intent.setDataAndType(uri, "application/msword");   
	    return intent;   
	  }   
	    
	//android获取一个用于打开PPT文件的intent    
	  public static Intent getPptFileIntent( String param )   
	  {   
	    Intent intent = new Intent("android.intent.action.VIEW");   
	    intent.addCategory("android.intent.category.DEFAULT");   
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
	    Uri uri = Uri.fromFile(new File(param ));   
	    intent.setDataAndType(uri, "application/vnd.ms-powerpoint");   
	    return intent;   
	  }   
	  
	  
	//android获取一个用于打开Excel文件的intent    
	  public static Intent getExcelFileIntent( String param )   
	  {   
	    Intent intent = new Intent("android.intent.action.VIEW");   
	    intent.addCategory("android.intent.category.DEFAULT");   
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
	    Uri uri = Uri.fromFile(new File(param ));   
	    intent.setDataAndType(uri, "application/vnd.ms-excel");   
	    return intent;   
	  }   
}

