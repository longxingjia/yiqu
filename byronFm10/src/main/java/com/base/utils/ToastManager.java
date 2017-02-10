package com.base.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 
 *@comments 管理应用中所有的提示框
 */
public class ToastManager {

	/**toast*/
	private Toast mToast;
	/**ToastManager*/
	private static ToastManager mToastManager;
	
	private ToastManager(Context c){
		mToast = Toast.makeText(c, null, Toast.LENGTH_SHORT);
		mToast.setGravity(Gravity.CENTER, 0, 0);	
	}
	/**
	 * 
	 * @comments  单例模式 获取引用
	 * @param c
	 * @return
	 * @version 1.0
	 */
	public static synchronized ToastManager getInstance(Context c){
		if(mToastManager == null){
			mToastManager = new ToastManager(c);
		}
		return mToastManager;
	}
	/**
	 * 
	 * @comments 纯文字显示 
	 * @param text
	 * @version 1.0
	 */
	public void showText(String text){
		mToast.setText(text);
		mToast.show();		
	}
	/**
	 * 
	 * @comments 纯文字显示 
	 * @param text
	 * @version 1.0
	 */
	public void showText(int text){
		mToast.setText(text);
		mToast.show();		
	}
}

