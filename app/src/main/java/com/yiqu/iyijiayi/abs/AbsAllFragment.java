package com.yiqu.iyijiayi.abs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.base.utils.ToastManager;
import com.fwrestnet.NetResponse;

import com.ui.abs.AbsTitleNetFragment;
import com.yiqu.iyijiayi.model.Constant;


public abstract class AbsAllFragment extends AbsTitleNetFragment {

	private BroadcastReceiver loginOutReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constant.ACTION_LOG_OUT.equals(intent
					.getAction())) {
				getActivity().finish();
			}
		}
	};
	
	private void initLoginOutBroadcast() {
		IntentFilter netFilter = new IntentFilter(
				Constant.ACTION_LOG_OUT);
		getActivity().registerReceiver(loginOutReceiver, netFilter);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		try {
			getActivity().unregisterReceiver(loginOutReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initLoginOutBroadcast();
	}



	@Override
	public void onNetEnd(String id, int type, NetResponse netResponse) {
		// TODO Auto-generated method stub
		if(type == TYPE_TOKEN_INVALID){
			//如果token失效
			Intent intent = new Intent();  
			intent.setAction(Constant.ACTION_LOG_OUT);  
			getActivity().sendBroadcast(intent);
			//AppShare.LoginOut(getActivity());
			
			ToastManager.getInstance(getActivity()).showText("身份失效，需要重新登录");
//			((MyApp)(getActivity().getApplication())).stopPush();
//
//			Model.startNextAct(getActivity(), LoginFragment.class.getName());
			getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			
		}
		super.onNetEnd(id, type, netResponse);
	}



	

	
}
