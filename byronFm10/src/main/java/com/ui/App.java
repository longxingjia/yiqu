package com.ui;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 *@comments
 */
public class App extends Application  {
	/***
	 * 前后台标记
	 */
	public static boolean isActive;
	public static Context mContext; // 应用全局context
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		try{
//	        AppShare.resetUrl(this);
		}catch(Exception e){
			e.printStackTrace();
		}
		Log.i("byron", "fm-App-onCreate");
		mContext = this.getApplicationContext();
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(myReceiver, filter );
		super.onCreate();
	}
	private BroadcastReceiver myReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {

			String action  = intent.getAction();
			if(Intent.ACTION_SCREEN_OFF.equals(action)){
				App.isActive = false;
			}
		}
	};
	
	@Override
	public void onTerminate() {

		try{
			unregisterReceiver(myReceiver);
		}catch(Exception e){
			e.printStackTrace();
		}
		super.onTerminate();
	}

}

