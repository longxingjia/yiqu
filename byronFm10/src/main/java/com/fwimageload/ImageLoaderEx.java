package com.fwimageload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

/**
 * 
 *@comments 图片异步加载器
 *需要继承此类，实现{@link #getFile(String) #isCallNet()}方法
 */
public abstract class ImageLoaderEx<T> {
	private static final String TAG = "MemoryCache";
	/**图片内存缓冲池*/
	private static MemoryCache memoryCache = new MemoryCache();
	/**图片地址与组件对应队列*/
	private Map<T, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<T, String>());
	/**线程池*/
	private ExecutorService executorService;
	/**请求图片的像素大小*/
	private int REQUIRED_SIZE = 100;
	protected Context mContext;

	public static void clear(){
		try{
			memoryCache.clear();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public ImageLoaderEx(Context context) {
		executorService = Executors.newFixedThreadPool(5);
		mContext = context;
	}
	/**
	 * @param context
	 * @param size 请求图片的像素大小
	 */
	public ImageLoaderEx(Context context, int size) {
		this(context);
		REQUIRED_SIZE = size;
	}
	/**
	 * 
	 * @comments 主要方法。提供图片异步加载显示处理。
	 * @param url 图片的网络地址。
	 * @param view 要显示图片的组件
	 * @return true:已经取得图片，false:没有
	 * @version 1.0
	 */
	public boolean DisplayImage(String url, T view) {
		//判空处理
		if(url == null || url.trim().length() == 0){
			return false;
		}
		//加入到队列
		imageViews.put(view, url);
		//从内存区获取图片
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null){
			//获取到图片直接反馈给组件
			if(view instanceof ImageView) {
    			((ImageView)view).setImageBitmap(bitmap);
    		} else if(view instanceof ImageloadPhotoLoadListener) {
    			((ImageloadPhotoLoadListener)view).setSrcBitmap(bitmap, url);
    		}
			return true;
		}else {	
			//内存没有该图片，去加载。
			queuePhoto(url, view);
		}
		return false;
	}
	public void reLoad() {
		
	}	
	private void queuePhoto(String url, T view) {
		//构造图片组件结合对象，执行任务
		PhotoToLoad p = new PhotoToLoad(url, view);
		executorService.submit(new PhotosLoader(p));
	}
	/**
	 * 
	 * @comments 获取图片的存储文件
	 * 作为工具类，并不制定图片的保持路径，需要具体实现类制定存储地址。
	 * @param url 图片的网络地址。
	 * @return 图片文件
	 * @version 1.0
	 */
	protected abstract File getFile(String url);
	/**
	 * 
	 * @comments 是否从网络获取图片
	 * 对外提供一种开关机制。实现诸如用户设置不从网络获取图片的功能。
	 * @return
	 * @version 1.0
	 */
	protected abstract boolean isCallNet();
	
	/**
	 * 
	 * @comments 获取图片
	 * 先从本地获取，没有则从网络获取，并且保存到本地。
	 * @param url
	 * @return 返回图片指针
	 * @version 1.0
	 */
	private Bitmap getBitmap(String url) {
		//从本地SD卡获取
		File f = getFile(url);//StorageUtil.getStorageFile(mContext, Conf.BASE_DIR + File.separator + Conf.CACHE_DIR, fileName);
		Bitmap b = null;
		if (f != null && f.exists()){
			//判断文件存在，从SD读取图片
			b = decodeFile(f);
		}
		if (b != null){
			return b;
		}
		//图片为空，证明SD卡尚未缓存，需要从网络下载
		
		if(!isCallNet()){
			//无网络或各种网络条件限制，不下载
			return null;
		}
		try {
			//从网络下载
			Bitmap bitmap = null;

			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpCall(httpGet);	
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if(statusCode != 200){
				return null;
			}
			
			HttpEntity entity = httpResponse.getEntity();
			InputStream is = entity.getContent();
			
			//本地SD卡缓存
			OutputStream os = new FileOutputStream(f);
			//文件拷贝形式缓存到本地
			boolean isSuccess = CopyStream(is, os);
			os.close();
			if(!isSuccess){
				f.delete();
			}
			//从文件读取图片
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
    /**
     * 
     * @comments 从文件获取图片 
     * @param 图片文件
     * @return 图片指针
     * @version 1.0
     */
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
//			if(REQUIRED_SIZE > 0){
//				//根据请求的像素大小，加载图片时进行缩放处理
//				while (true) {
//					if (width_tmp / 2 < REQUIRED_SIZE 
//							|| height_tmp / 2 < REQUIRED_SIZE)
//						break;
//					width_tmp /= 2;
//					height_tmp /= 2;
//					scale *= 2;
//				}
//			}
			if(REQUIRED_SIZE > 0){
				//根据请求的像素大小，加载图片时进行缩放处理
				while (width_tmp  > REQUIRED_SIZE 
						|| height_tmp > REQUIRED_SIZE) {
					width_tmp /= 2;
					height_tmp /= 2;
					scale *= 2;
				}
			}
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	/**
	 *@comments 图片地址、组件结合对象
	 *@author byron(linbochuan@hopsun.cn)
	 *@date 2014-3-24
	 *@version 1.0
	 */
	private class PhotoToLoad {
		/**图片远程地址*/
		public String url;
		/**用于显示图片的组件*/
		public T view;

		public PhotoToLoad(String u, T i) {
			url = u;
			view = i;
		}
	}
	/**
	 *@comments 图片异步加载器
	 *@author byron(linbochuan@hopsun.cn)
	 *@date 2014-3-24
	 *@version 1.0
	 */
	private class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			//判断该图片是否继续下载
			if (imageViewReused(photoToLoad)){
				return;
			}
			//加载拖
			Bitmap bmp = getBitmap(photoToLoad.url);
			//存储入内存缓存
			memoryCache.put(photoToLoad.url, bmp);
			if(bmp == null){
				return;
			}
			if (imageViewReused(photoToLoad)){
				return;
			}		
			//通知UI组件显示图片
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			Activity a = (Activity) mContext;
			a.runOnUiThread(bd);
		}
	}
	/**
	 * 
	 * @comments 检测是否暂停获取图片
	 * @param photoToLoad
	 * @return
	 * @version 1.0
	 */
	protected boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.view);
		//由于组件是复用的，而图片下载是异步处理，所以需要同步组件此时此刻的URL地址
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}
	/**
	 *@comments 图片显示器。UI线程。用于把后台线程下载好的图片呈现出来
	 *@author byron(linbochuan@hopsun.cn)
	 *@date 2014-3-24
	 *@version 1.0
	 */
	private class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			//判断该图片是否继续显示
			if (imageViewReused(photoToLoad)){
				return;
			}
			if (bitmap != null){
				T t = photoToLoad.view;
				if(t instanceof ImageView) {
	    			((ImageView)t).setImageBitmap(bitmap);
	    		} else if(t instanceof ImageloadPhotoLoadListener) {
	    			((ImageloadPhotoLoadListener)t).setSrcBitmap(bitmap, photoToLoad.url);
	    		}
			}
		}
	}
	/**
	 * @comments 停止异步图片加载
	 * @version 1.0
	 */
	public void stop(){
		//clearCache();
		if(!executorService.isShutdown()){
			try{
				executorService.shutdownNow();
				clear();
			}catch(Exception e){
				e.printStackTrace();
			}			
		}
	}
//	/**
//	 * @comments 清除图片内存
//	 * @version 1.0
//	 */
//	private void clearCache() {
//		memoryCache.clear();
//	}
	
	public void clearCache(String url){
		memoryCache.clear(url);
	}
	/**
	 * @comments 文件拷贝
	 * @param is 输入流
	 * @param os 输出流
	 * @version 1.0
	 */
	public static boolean CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		long total = 0;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
				total += count;
			}
			Log.d(TAG, "file " + total / 1024. / 1024. + "MB");
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
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
		int timeout = 30 * 1000;
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