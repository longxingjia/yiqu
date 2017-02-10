package com.fwrestnet;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import android.util.Log;


/**
 * 
 *@comments 网络访问请求对象
 */
public class NetRequest {
	/**
	 * 请求数据
	 */
	private Object data;
	/**
	 * 请求参数
	 */
	private Map<String, String> httpParams = new LinkedHashMap<String, String>();
	/**
	 * 请求被更换的URL地址
	 */
	private Map<String, String> urlParams = new LinkedHashMap<String, String>();
	/**
	 * Constructor
	 */
	public NetRequest() {
		
	}
	
//	/**
//	 * Constructor with attributes
//	 * @param data
//	 * @param httpParams
//	 */
//	public NetRequest(Context context, Object data, Map<String, String> httpParams) {
//		this.context = context;
//		this.data = data;
//		this.httpParams = httpParams;
//	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * Get HttpParams
	 * @return
	 */
	public Map<String, String> getHttpParams() {
		return httpParams;
	}

	/**
	 * Set HttpParams
	 * @param httpParams
	 */
	public void setHttpParams(Map<String, String> httpParams) {
		this.httpParams = httpParams;
	}
	
	/**
	 * Add a new HttpParam
	 * @param name
	 * @param value
	 */
	public void addHttpParam(String name, String value) {
		httpParams.put(name, value);
	}
	
	/**
	 * Clear HttpParams
	 */
	public void clearHttpParams() {
		httpParams.clear();
	}

	/**
	 * Add a new HttpParam encoded by default encoding
	 * @param name
	 * @param value
	 */
	public void addEncodedHttpParam(String name, String value) {
		try {
			String encoding = NetApi.KEY_DEFAULT_ENCODING;
			value = URLEncoder.encode(value, encoding);
		} catch (UnsupportedEncodingException ex) {
			Log.e("NetRequest", ex.toString());
		}
		httpParams.put(name, value);
	}

	/**
	 * Get urlParams
	 * @return
	 */
	public Map<String, String> getUrlParams() {
		return urlParams;
	}
	/**
	 * Add a new urlParams
	 * @param name
	 * @param value
	 */
	public void addUrlParam(String name, String value) {
		urlParams.put(name, value);
	}
}
