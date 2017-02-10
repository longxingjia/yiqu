package com.fwimageload;

import android.graphics.Bitmap;
/**
 * 
 *@comments 对于{@link #ImageLoader }泛型的一种支持。
 *可以让非ImageView也可以作为图片加载器的使用者
 */
public interface ImageloadPhotoLoadListener{
	/**
	 * 
	 * @comments 取得图片，通知刷新。
	 * @param bitmap
	 * @version 1.0
	 */
	public void setSrcBitmap(Bitmap bitmap, String url);
}