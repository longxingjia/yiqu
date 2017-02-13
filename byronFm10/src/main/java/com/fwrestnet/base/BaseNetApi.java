package com.fwrestnet.base;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import android.content.Context;
import android.util.Log;

import com.byron.framework.R;
import com.fwrestnet.NetApi;
import com.fwrestnet.NetMethod;

/**
 *@comments 极信的接口统一配置
 */
public abstract class BaseNetApi extends NetApi{

	@Override
	public Object parseBody(String body) throws JSONException {
		return body;
	}
	
	@Override
	public int getErrorString() {
		return R.string.fm_net_call_error;
	}


	@Override
	public int getTimeOut() {
		return 30*1000;
	}

	@Override
	public int getMsg() {
		return R.string.fm_net_call_msg_default;
	}

	@Override
	public String mockData(Context c) {
		return MockData.getMock(c, "success.txt");
	}

	@Override
	public int getShowUIResource() {
		return R.layout.progress_overlay;
	}

	@Override
	public int getShowUITextID() {
		return R.id.text;
	}

	@Override
	public boolean isDemo() {
		return false;
	}

	@Override
	public boolean isLog() {
		return false;
	}

	@Override
	public NetMethod getNetMethod() {
		//Log.e("tab",super.getNetMethod().toString());

		return super.getNetMethod();
	}

	@Override
	public Map<String, String> getDefaultHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		return headers;
	}

	@Override
	public int getMinTime() {

		return 0;
	}
	
	

}

