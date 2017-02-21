package com.fwrestnet;
/**
 *@comments API回调
 */
public interface NetCallBack {
	/**错误*/
	public static final int TYPE_ERROR = 0;
	/**成功*/
	public static final int TYPE_SUCCESS = 1;
//	/**成功*/
	public static final int TYPE_TOKEN_INVALID = 2;
	/**超时*/
	public static final int TYPE_TIMEOUT = 3;
	/**取消*/
	public static final int TYPE_CANCEL = 4;
	
	/**
	 * @comments 开始
	 * @param id

	 * @version 1.0
	 */
	public void onNetNoStart(String id);
	
	/**
	 * @comments 开始
	 * @param id

	 * @version 1.0
	 */
	public void onNetStart(String id);
	
	/**
	 * 
	 * @comments  网络访问结束，当一次网络访问结束，不管是取消了还是成功失败了，都会调用此方法
	 * @param id 本次访问id,自己设置，用来判断本次是谁调用的
	 * @param type 状态值（TYPE_SUCCESS，TYPE_TIMEOUT，TYPE_ERROR，TYPE_CANCEL...）
	 * @param netResponse 网络访问结果对象
	 * @version 1.0
	 */
	
	public void onNetEnd(String id, int type, NetResponse netResponse);
}

