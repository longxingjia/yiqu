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

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		try{
//	        AppShare.resetUrl(this);
		}catch(Exception e){
			e.printStackTrace();
		}
		Log.i("byron", "fm-App-onCreate");
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(myReceiver, filter );
		super.onCreate();
	}
	private BroadcastReceiver myReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action  = intent.getAction();
			if(Intent.ACTION_SCREEN_OFF.equals(action)){
				App.isActive = false;
			}
		}
	};
	
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		try{
			unregisterReceiver(myReceiver);
		}catch(Exception e){
			e.printStackTrace();
		}
		super.onTerminate();
	}

}

