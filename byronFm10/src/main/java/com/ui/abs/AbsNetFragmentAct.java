package com.ui.abs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.base.utils.DeviceUtil;
import com.base.utils.ToastManager;
import com.byron.framework.R;


public abstract class AbsNetFragmentAct extends AbsFragmentAct {

	/** 网络是否连接 */
	private boolean mIsNetOpen = false;
	
	private BroadcastReceiver netReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent
					.getAction())) {
				final boolean isNetOpen = DeviceUtil.isNetWorkOpen(context);
				if (mIsNetOpen != isNetOpen) {
					mIsNetOpen = isNetOpen;
					onNetworkChange(mIsNetOpen);
				}
			}
		}
	};
	
	protected boolean isNetOpen(){
		return mIsNetOpen;
	}
	
	protected abstract void onNetworkChange(boolean isNetOpen);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			initNetWork();
		} catch (Exception e) {
			ToastManager.getInstance(this).showText(
					R.string.fm_indeterminism_error);
			e.printStackTrace();
		}
		
		
	}

	private void initNetWork() {
		mIsNetOpen = DeviceUtil.isNetWorkOpen(this);
		onNetworkChange(mIsNetOpen);
		IntentFilter netFilter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(netReceiver, netFilter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		try {
			unregisterReceiver(netReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}
	
	
}
