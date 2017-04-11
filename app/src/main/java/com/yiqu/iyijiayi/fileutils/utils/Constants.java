package com.yiqu.iyijiayi.fileutils.utils;

import android.os.Environment;

import com.yiqu.Tool.Global.Variable;

public class Constants {
	/** SD卡路径 */
	public static final String SD_PATH = Environment.getExternalStorageDirectory().getPath() + "/";
	/** 缓存保存路径 */
	public static final String DOWNLOAD_PATH = Variable.StorageQandAPath;
	/** SD卡预留最小值 */
	public static final int SD_REMAIN_SIZE = 50 * 1024 * 1024;
	/** 单次缓存文件最大值 */
	public static final int AUDIO_BUFFER_MAX_LENGTH = 2 * 1024 * 1024;
	/** 缓存文件个数最大值 */
	public static final int CACHE_FILE_NUMBER = 10000;
	/** 预缓存文件大小 */
	public static final int PRECACHE_SIZE = 300*1000;
	// Http Header Name
	public final static String CONTENT_RANGE = "Content-Range";
	public final static String CONTENT_LENGTH = "Content-Length";
	public final static String RANGE = "Range";
	public final static String HOST = "Host";
	public final static String USER_AGENT = "User-Agent";
	// Http Header Value Parts
	public final static String RANGE_PARAMS = "bytes=";
	public final static String RANGE_PARAMS_0 = "bytes=0-";
	public final static String CONTENT_RANGE_PARAMS = "bytes ";

	public final static String LINE_BREAK = "\r\n";
	public final static String HTTP_END = LINE_BREAK + LINE_BREAK;
}