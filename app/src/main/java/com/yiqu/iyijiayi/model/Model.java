package com.yiqu.iyijiayi.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;

import com.ui.abs.AbsFragment;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.StubActivityNoSoft;

public class Model {

	public static AbsFragment creatFragment(String className){
		try {
			Class<?> c =  Class.forName(className);
			return (AbsFragment) c.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Fragment creatFragment2(String className){
		try {
			Class<?> c =  Class.forName(className);
			return (Fragment) c.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void startNextAct(Context c, String className){
		Intent i = new Intent(c, StubActivity.class);
		i.putExtra("fragment", className);
		c.startActivity(i);
	}
	
	public static void startNextActNoSoft(Context c, String className){
		Intent i = new Intent(c, StubActivityNoSoft.class);
		i.putExtra("fragment", className);
		c.startActivity(i);
	}
	
	public static void startNextAct(Activity a, String className, int requestCode){
		Intent i = new Intent(a, StubActivity.class);
		i.putExtra("fragment", className);
		a.startActivityForResult(i, requestCode);
	}
	
	/** 
	 * make true current connect service is wifi 
	 * @param mContext 
	 * @return 
	 */  
	public static boolean isWifi(Context mContext) {  
	    ConnectivityManager connectivityManager = (ConnectivityManager) mContext  
	            .getSystemService(Context.CONNECTIVITY_SERVICE);  
	    NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();  
	    if (activeNetInfo != null  
	            && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {  
	        return true;  
	    }  
	    return false;  
	}  
	
	
}
