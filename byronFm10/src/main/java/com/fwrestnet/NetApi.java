package com.fwrestnet;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHost;
import org.json.JSONException;

import android.content.Context;


/**
 * 
 *@comments 网络接口
 */
public abstract class NetApi {
	/**超时时间*/
	private static final int TIME_OUT = 30 * 1000;
	/**最小访问网络时间。 单位毫秒*/
	private static final int MIN_TIME = 100;
	
	/**
	 * HTTP headers
	 */
	private Map<String, String> headers = new HashMap<String, String>();
	public static final String KEY_DEFAULT_ENCODING = "UTF-8";

	/**
	 * Default proxy
	 */
	private HttpHost proxy;
	/**
	 * 接口地址
	 */
	private String path;
	
	public NetApi() {
		//Set common headers
		//headers.put("user-agent", getUserAgent());
//		headers.put("accept-encoding", "gzip");
		headers.put("accept-charset", KEY_DEFAULT_ENCODING);
		//headers.put("x-agent", "Android [RS]");
		//headers.put("accept", "text/xml,text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		headers.put("connection", "close");

		headers.putAll(getDefaultHeaders());
		
//		proxy = new HttpHost(Sys.getValue(Sys.KEY_PROXY_HOST), Sys.getValueAsInt(Sys.KEY_PROXY_PORT), "http");
	}
	
	/**
	 * Should be overrided by subclass. Return URL path, exclude basic IP and port
	 * @return
	 */
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * Should be overrided by subclass. Default is JSON.
	 * @return
	 */
	public ResponseType getResponseType() {
		return ResponseType.JSON;
	}

	/**
	 * Return proxy which is defined in string resource file
	 * @return
	 */
	public HttpHost getProxy() {
		return proxy;
	}

	/**
	 * Retrieve all defined HTTP headers.
	 * @return
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * Define default headers here, it is basic HTTP header.
	 * @return
	 */
	public Map<String, String> getDefaultHeaders() {
		return new HashMap<String, String>();
	}

	/**
	 * Add more extra headers besides default headers defined in HttpApi.
	 * @param name
	 * @param value
	 */
	public void addExtraHeader(String name, String value) {
		headers.put(name, value);
	}
	
//	/**
//	 * @return http agent
//	 */
//	private String getUserAgent() {
//		return System.getProperty("http.agent");
//	}
	/**
	 * @comments JSON解析
	 * @param body JSON字符窜
	 * @return
	 * @throws JSONException
	 * @version 1.0
	 */
	public abstract Object parseBody(String body) throws JSONException;
	/**
	 * @comments 接口访问错误提示语 
	 * @return
	 * @version 1.0
	 */
	public abstract int getErrorString();
	/**
	 * @comments 接口访问中，提示语
	 * @return
	 * @version 1.0
	 */
	public abstract int getMsg();
	/**
	 * @comments 模拟数据
	 * @param c
	 * @return
	 * @version 1.0
	 */
	public abstract String mockData(Context c);
	
	/**
	 * @comments 获取接口的超时时间配置
	 * @return
	 * @version 1.0
	 */
	public int getTimeOut(){
		return TIME_OUT;
	}
	/**
	 * @comments 获取接口的最小访问网络时间配置，用于优化用户体验
	 * @return
	 * @version 1.0
	 */
	public int getMinTime(){
		return MIN_TIME;
	}
	/**
	 * @comments 获取接口的最小访问网络时间配置，用于优化用户体验
	 * @return
	 * @version 1.0
	 */
	public boolean isOnlyDialogMinTimeControl(){
		return true;
	}
	/**
	 * @comments 接口调用时，用于提示用户的布局文件
	 * @return
	 * @version 1.0
	 */
	public abstract int getShowUIResource();
	/**
	 * @comments 接口调用时，用于显示文字的组件ID
	 * @return
	 * @version 1.0
	 */
	public abstract int getShowUITextID();

	/**
	 * @comments 当前接口是否打印LOG
	 * @return
	 * @version 1.0
	 */
	public abstract boolean isLog();
	
	/**
	 * @comments 当前接口是否是演示数据
	 * @return
	 * @version 1.0
	 */
	public abstract boolean isDemo();
	
	public NetMethod getNetMethod(){
		return NetMethod.POST;
	};
}
