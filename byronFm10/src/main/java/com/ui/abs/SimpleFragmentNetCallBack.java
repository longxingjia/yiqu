package com.ui.abs;

import com.base.utils.ToastManager;
import com.byron.framework.R;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.ui.abs.AbsFragment;

public class SimpleFragmentNetCallBack implements NetCallBack {

	private AbsFragment mAbsFragment;

	public SimpleFragmentNetCallBack(AbsFragment c){
		mAbsFragment = c;
	}
	
	@Override
	public void onNetNoStart(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNetStart(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNetEnd(String id, int type, NetResponse netResponse) {
		// TODO Auto-generated method stub
		switch(type){
		case TYPE_SUCCESS:
			break;
		case TYPE_TOKEN_INVALID:
			
			break;
		case TYPE_ERROR:
			if(mAbsFragment == null || mAbsFragment.isVisible()){
			//	ToastManager.getInstance(mAbsFragment.getActivity()).showText(netResponse.statusDesc);
			}
			break;
		case TYPE_CANCEL:
			break;
		case TYPE_TIMEOUT:
			if(mAbsFragment == null || mAbsFragment.isVisible()){
				ToastManager.getInstance(mAbsFragment.getActivity()).showText(R.string.fm_net_call_timeout);
			}
			break;
		}
	}

}
