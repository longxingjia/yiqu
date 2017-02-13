package com.yiqu.iyijiayi.net;

import com.fwrestnet.NetMethod;
import com.fwrestnet.base.BaseNetApi;

public class MyNetApi extends BaseNetApi {
	

	@Override
	public NetMethod getNetMethod() {
		return NetMethod.POST;
	}

	@Override
	public boolean isDemo() {
		return false;
	}

	@Override
	public boolean isLog() {
		return true;
	}

	@Override
	public int getMinTime() {
		return 0;
	}

	
	@Override
	public int getTimeOut() {
		return 30 * 1000;
	}

	@Override
	public boolean isOnlyDialogMinTimeControl() {
		return false;
	}
	
	
}
