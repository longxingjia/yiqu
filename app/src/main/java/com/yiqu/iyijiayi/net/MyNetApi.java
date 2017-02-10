package com.yiqu.iyijiayi.net;

import com.fwrestnet.NetMethod;
import com.fwrestnet.base.BaseNetApi;

public class MyNetApi extends BaseNetApi {
	

	@Override
	public NetMethod getNetMethod() {
		// TODO Auto-generated method stub
		return NetMethod.POST;
	}

	@Override
	public boolean isDemo() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLog() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getMinTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	@Override
	public int getTimeOut() {
		// TODO Auto-generated method stub
		return 30 * 1000;
	}

	@Override
	public boolean isOnlyDialogMinTimeControl() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
