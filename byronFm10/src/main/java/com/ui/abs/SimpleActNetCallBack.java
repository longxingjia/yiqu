package com.ui.abs;

import com.base.utils.ToastManager;
import com.byron.framework.R;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.ui.abs.AbsFragmentAct;

public class SimpleActNetCallBack implements NetCallBack {

	private AbsFragmentAct mAbsFragmentAct;

	public SimpleActNetCallBack(AbsFragmentAct c){
		mAbsFragmentAct = c;
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
			if(mAbsFragmentAct == null || mAbsFragmentAct.isActVisibile()){
			//	ToastManager.getInstance(mAbsFragmentAct).showText(netResponse.statusDesc);
			}
			break;
		case TYPE_CANCEL:
			break;
		case TYPE_TIMEOUT:
			if(mAbsFragmentAct == null || mAbsFragmentAct.isActVisibile()){
				ToastManager.getInstance(mAbsFragmentAct).showText(R.string.fm_net_call_timeout);
			}
			break;
		}
	}

}
