package com.ui.abs;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.base.utils.ToastManager;
import com.byron.framework.R;
/**
 * 
 *@comments 被继承的组
 */
@SuppressWarnings("deprecation")
public abstract class AbsBaseActGroup extends ActivityGroup{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try{
			setContentView(getContentView());
			initView();
			init(savedInstanceState);
		}catch(Exception e){
			ToastManager.getInstance(this).showText(R.string.fm_indeterminism_error);
			e.printStackTrace();
		}
	}
	
	/**
	 * @comments 界面的布局文件
	 * @return
	 * @version 1.0
	 */
	protected abstract int getContentView();
	/**
	 * @comments 界面初始化数据的地方
	 * @param savedInstanceState
	 * @version 1.0
	 */
	protected abstract void init(Bundle savedInstanceState);
	/**
	 * @comments 界面初始化组件的地方
	 * @version 1.0
	 */
	protected abstract void initView();	
	
	protected View changeTabView(String tag, Class<?> c, ViewGroup content){
		View wd = null;
		try{
			LocalActivityManager localActivityManager = getLocalActivityManager();
			Intent i = new Intent(this, c);
			final Window w = localActivityManager.startActivity(
					 tag, i);
	        wd = w != null ? w.getDecorView() : null;
	        if(content != null){
	        	content.removeAllViews();
	        	content.addView(wd);
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
		return wd;
	}

}

