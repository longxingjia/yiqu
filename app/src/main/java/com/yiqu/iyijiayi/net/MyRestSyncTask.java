package com.yiqu.iyijiayi.net;

import android.content.Context;

import com.fwrestnet.NetApi;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetRequest;
import com.fwrestnet.RestSyncTask;

/**
 *@comments 接口任务
 */
public class MyRestSyncTask extends RestSyncTask {


	public MyRestSyncTask(Context c, NetApi api, NetRequest netRequest,
                          String id, NetCallBack back, boolean showDialogEanble) {
		super(c, api, netRequest, id, back, showDialogEanble);
		// TODO Auto-generated constructor stub
	}
	
}

