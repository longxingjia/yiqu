package com.fwrestnet.base;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import android.content.Context;

import com.byron.framework.R;
import com.fwrestnet.NetApi;
import com.fwrestnet.NetMethod;

/**
 *@comments 极信的接口统一配置
 */
public abstract class BaseNetApi extends NetApi{

	@Override
	public Object parseBody(String body) throws JSONException {
		// TODO Auto-generated method stub
		return body;
	}
	
	@Override
	public int getErrorString() {
		// TODO Auto-generated method stub
		return R.string.fm_net_call_error;
	}


	@Override
	public int getTimeOut() {
		// TODO Auto-generated method stub
		return 30*1000;
	}

	@Override
	public int getMsg() {
		// TODO Auto-generated method stub
		return R.string.fm_net_call_msg_default;
	}

	@Override
	public String mockData(Context c) {
		// TODO Auto-generated method stub
		return MockData.getMock(c, "success.txt");
	}

	@Override
	public int getShowUIResource() {
		// TODO Auto-generated method stub
		return R.layout.progress_overlay;
	}

	@Override
	public int getShowUITextID() {
		// TODO Auto-generated method stub
		return R.id.text;
	}

	@Override
	public boolean isDemo() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLog() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public NetMethod getNetMethod() {
		// TODO Auto-generated method stub
		return super.getNetMethod();
	}

	@Override
	public Map<String, String> getDefaultHeaders() {
		// TODO Auto-generated method stub
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		return headers;
	}

	@Override
	public int getMinTime() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

}

