package com.fwimageload;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.util.Log;
/**
 * 
 *@comments 图片内存缓存处理
 */
public class MemoryCache {

	private static final String TAG = "MemoryCache";
	private Map<String, Bitmap> cache = Collections
			.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));
	private long size = 0;// current allocated size
	private long bsize = 0;
	private long limit = 1000000;// max memory in bytes

	public MemoryCache() {
		// use 25% of available heap size
		setLimit(Runtime.getRuntime().maxMemory() / 4);
	}

	/**
	 * 
	 * @comments 设置最大存储字节。
	 * @param new_limit
	 * @version 1.0
	 */
	public void setLimit(long new_limit) {
		limit = new_limit;
		Log.d(TAG, "MemoryCache will use up to " + limit / 1024. / 1024. + "MB");
	}
	/**
	 * 
	 * @comments 通过key，获取图片
	 * @param id 存储图片时的key.如 url
	 * @return Bitmap对象
	 * @version 1.0
	 */
	public Bitmap get(String id) {
		try {
			if (!cache.containsKey(id))
				return null;
			return cache.get(id);
		} catch (NullPointerException ex) {
			return null;
		}
	}
	/**
	 * @comments 插入图片内存区
	 * @param id
	 * @param bitmap
	 * @version 1.0
	 */
	public void put(String id, Bitmap bitmap) {
		try {
			if (cache.containsKey(id))
				size -= getSizeInBytes(cache.get(id));
			cache.put(id, bitmap);
			bsize = getSizeInBytes(bitmap);
			Log.d(TAG, "mem will use up to " + bsize / 1024. / 1024. + "MB");
			size += bsize;
			checkSize();
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}
	/**
	 * @comments 检测缓存池的大小，是否溢出
	 * @version 1.0
	 */
	private void checkSize() {
		if (size > limit) {
			Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Bitmap> entry = iter.next();
				size -= getSizeInBytes(entry.getValue());
				iter.remove();
				if (size <= limit)
					break;
			}
			Log.w(TAG, "Clean cache. New size " + cache.size());
		}
	}
	/**
	 * @comments 清除缓存
	 * @version 1.0
	 */
	public void clear() {
		cache.clear();
		size = 0;
	}
	
	/**
	 * @comments 清除缓存
	 * @version 1.0
	 */
	public void clear(String key) {
		Bitmap b = cache.remove(key);
		if(b != null){
			size -= getSizeInBytes(b);
			Log.i(TAG, "clear to " + size / 1024. / 1024. + "MB");
			b.recycle();
		}
	}

	/**
	 * @comments 获取图片的字节数
	 * @param bitmap
	 * @return
	 * @version 1.0
	 */
	private long getSizeInBytes(Bitmap bitmap) {
		if (bitmap == null){
			return 0;
		}
		return bitmap.getRowBytes() * bitmap.getHeight();
	}
}
