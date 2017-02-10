package com.fwrestnet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.security.KeyStore;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.accounts.NetworkErrorException;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 
 *@comments webservice 访问任务类
 *根据NetApiConfig的接口配置，自动获取所需信息，使用异步的方式实现网络访问，别回调结果和异常信息。
 */
public abstract class AbsRestSyncTask extends AsyncTask<Object, Object, NetResponse> {
	private static final String TAG = "RestNet";
	
	/**
	 * ASCII code for < symbol
	 */
	public static final int ASCII_LESS = 60;
	/**
	 * 接口信息。
	 */
	protected NetApi mNetApi;
	/**HTTP 访问方法： GET POST*/
	protected NetMethod netMethod;
	/**REST API 访问参数*/
	protected NetRequest netRequest;
	/**HTTP 访问网络对象*/
	private HttpUriRequest httpUriRequest;
	
//	private Handler mHandler = new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			super.handleMessage(msg);
//		}
//	};
	
	public AbsRestSyncTask(NetApi api, NetMethod netMethod, NetRequest netRequest ) {
		this.mNetApi = api;
		this.netMethod = netMethod;
		this.netRequest = netRequest;
	}
	
	@Override
	protected NetResponse doInBackground(Object... params) {
		try {
			long time = System.currentTimeMillis();
			NetResponse result = doBackground();
			long t = System.currentTimeMillis() - time;
			minTimeControl(t);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  new NetResponse();
	}
	
	
	@Override
	protected void onPostExecute(NetResponse result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	/**
	 * @comments 接口后台处理
	 * @return
	 * @version 1.0
	 */
	private NetResponse doBackground(){
		NetResponse result = new NetResponse();
		try {
			httpUriRequest = netMethod.getHttpUriRequest(mNetApi, netRequest);
				
			try{
				if(mNetApi.isLog()){
					log();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			//判断是否为模拟接口调用
			if(isDemo(result)){
				//真实调用的情况
				return result;
			}
			
			HttpResponse httpResponse = httpCall(httpUriRequest);			
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			String statusMsg = httpResponse.getStatusLine().getReasonPhrase();
			Object object = null;
			object = parseResponse(httpResponse);
			if (HttpStatus.SC_OK == statusCode || 
					HttpStatus.SC_CREATED == statusCode ||
					HttpStatus.SC_ACCEPTED == statusCode) {								
				process(statusCode, statusMsg, result, object);
			}else{
				//result.e = new Exception(statusMsg);
			}
		} catch(Exception e){
			//result.e = e;
			if(e != null){
				e.printStackTrace();
			}
		}
		return result;
	}
	private void log() throws MalformedURLException{
		Log.d(TAG, httpUriRequest.getURI().toURL().toString());
		Log.d(TAG, mNetApi.getNetMethod().toString());
		if(netRequest.getData() != null){
			Log.d(TAG, netRequest.getData().toString());
		}
		StringBuilder sb = new StringBuilder("?");
		for (Entry<String, String> entry: netRequest.getHttpParams().entrySet()) {
			sb.append(entry.getKey() + "=" + entry.getValue() + "&");
		};
		Log.d(TAG, sb.toString());
//			
//		Log.d(TAG, mNetApi.getPath() + sb.toString());
		//Log.d(TAG,"Server request: \nURL=" + mNetApi.getPath() + sb.toString());
				//+ "\nURI=" + httpUriRequest.getURI().toURL().getFile()
				//+ "\nProxy=" + mNetApi.getProxy());


//			for (Entry<String, String> entry: mNetApi.getHeaders().entrySet()) {
//				sb.append("\n" + entry.getKey() + "=" + entry.getValue());
//			};
//			Log.d(TAG,"HTTP Headers: " + sb.toString());
		
//			sb = new StringBuilder();
//			for (Entry<String, String> entry: netRequest.getHttpParams().entrySet()) {
//				sb.append("\n" + entry.getKey() + "=" + entry.getValue());
//			};
//			Log.d(TAG,"HTTP Parameters: " + sb.toString());
	}
	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		try{
			if(httpUriRequest != null){
				httpUriRequest.abort();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		super.onCancelled();
	}
	/**
	 * @comments 网络访问结果处理。
	 * 交由子类处理，针对接口返回处理JSON数据解析，接口访问结果处理
	 * @param result HTTP返回结果
	 * @param maps JSON数据
	 * @throws JSONException JSON解析异常
	 * @version 1.0
	 */
	public abstract void process(int statusCode, String statusMsg ,NetResponse result, Object maps) throws JSONException;
	/**
	 * @comments 子类处理函数
	 * @param result HTTP返回结果
	 * @return true: 子类已经处理，父类无需处理。
	 * @throws JSONException
	 * @version 1.0
	 */
	public abstract boolean isDemo(NetResponse result) throws JSONException;
	/**
	 * @comments 时间控制，控制最小时间
	 * @param time 毫秒。
	 * @version 1.0
	 */
	public abstract void minTimeControl(long time);
	
	/**
	 * @comments 对结果进行处理。处理为流、JSON、XML
	 * @param response
	 * @return
	 * @throws Exception
	 * @version 1.0
	 */
	private Object parseResponse(HttpResponse response) throws Exception {
		Object result = null;

		HttpEntity entity = response.getEntity();
		InputStream in = entity.getContent();
		
		BufferedInputStream bufIn = new BufferedInputStream(in);
		// Header contentTypeHeader = entity.getContentType();
		
		BufferedReader readerIn = null;
		String line = null;
		StringBuilder sb = null;
		String responseStr = "";
		
		switch (mNetApi.getResponseType()) {
		case STREAM:
			result = bufIn;
			break;
			
		case JSON:
			readerIn = new BufferedReader(new InputStreamReader(in));
			sb = new StringBuilder();
			while ((line = readerIn.readLine()) != null) {
				sb.append(line);
			}
			responseStr = sb.toString();
			result = responseStr;
			break;

		case XML:
			int k = -1;
			long skip = 0;
			int f = ASCII_LESS;
			while ((k = in.read()) != f && k != -1) {
				skip++;
			}
			bufIn.skip(skip);
			// Response body
			Reader content = new BufferedReader(new InputStreamReader(bufIn));
			result = XmlPullParserFactory.newInstance().newPullParser();
			((XmlPullParser) result).setInput(content);
			break;
		}

		return result;
	}
	/**
	 * 
	 * @comments 网络访问
	 * @param httpUriRequest
	 * @return
	 * @throws Exception
	 * @version 1.0
	 */
	private HttpResponse httpCall(HttpUriRequest httpUriRequest) throws Exception {
		// Timeout setup
		HttpParams httpParams = new BasicHttpParams();
		int timeout = mNetApi.getTimeOut();
		HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
		HttpConnectionParams.setSoTimeout(httpParams, timeout);

		//DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
		HttpClient  httpClient = getNewHttpClient(httpParams);
		// If cmwap, proxy is needed
//		if (RsConst.isCmWap) {
//			log.debug("GlobalVars.isCmWap (network call): " + RsConst.isCmWap);
//			HttpParams params = httpClient.getParams();
//			params.setParameter(ConnRoutePNames.DEFAULT_PROXY, netApi.getProxy());
//			params.setParameter(ConnManagerPNames.TIMEOUT, timeout);
//		}
		// Network call
		HttpResponse httpResponse = httpClient.execute(httpUriRequest);
		// Process result
		if (httpResponse == null) {
			throw new NetworkErrorException("Network invocation error, HttpResponse is null.");
		}

		return httpResponse;
	}
	
	public static HttpClient getNewHttpClient(HttpParams params) {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}
}

