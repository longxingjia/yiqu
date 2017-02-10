/**
 * @Title: ImageLoaderFile.java
 * @Package com.ginwa.model
 * @Description: TODO 
 * @author byron 249892049@qq.com
 * @date 2014-12-18 下午04:39:28
 * @version V1.0
 */

package com.yiqu.iyijiayi.utils;



/**
 * @ClassName: ImageLoaderFile
 * @Description: TODO
 * @date 2016-12-18 下午04:39:28
 *
 */

public enum ImageLoaderFile {
	DEFAULT {
		@Override
		public String getFolder() {
			// TODO Auto-generated method stub
			return "img/";
		}
	};
	
	
	
	public abstract String getFolder();
}
