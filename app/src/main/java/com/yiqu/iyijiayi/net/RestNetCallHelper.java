package com.yiqu.iyijiayi.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.base.utils.ToastManager;
import com.fwrestnet.NetApi;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetRequest;
import com.fwrestnet.RestSyncTask;
import com.yiqu.iyijiayi.R;

/**
 *@comments 接口总入口。所有界面调用接口的辅助类
 */
public class RestNetCallHelper {
	
	/**
	 * @comments 
	 * @param c
	 * @param api
	 * @param netRequest
	 * @param id
	 * @param callback
	 * @return
	 * @version 1.0
	 */
	public static RestSyncTask callNet(Context c, NetApi api,
                                       NetRequest netRequest, String id, NetCallBack callback) {
		return callNet(c, api, netRequest, id, callback, true, true);
	}
	/**
	 * @comments 采用soap方式访问网络接口
	 * @param c
	 *            Context
	 * @param api
	 *            接口
	 * @param netRequest
	 *            参数
	 * @param id
	 *            本次接口调用的ID
	 * @param callback
	 *            回调
	 * @param isDialog
	 *            是否显示对话框 true:显示:false:不显示
	 * @param isShowNoNet
	 *            没有网络时，是否提示      
	 *            
	 * @return
	 * @version 1.0
	 */
	public static RestSyncTask callNet(Context c, NetApi api,
                                       NetRequest netRequest, String id, NetCallBack callback,
                                       boolean isDialog, boolean isShowNoNet) {
		if (isNetWorkOpen(c)) {
			MyRestSyncTask soap = new MyRestSyncTask(c, api, netRequest, id,
					callback, isDialog);
			try{
				soap.execute();
			}catch(Exception e){
				e.printStackTrace();
			}
			return soap;
		} else {
			if(callback != null){
				callback.onNetNoStart(id);
			}
			if (isShowNoNet) {
				ToastManager.getInstance(c).showText(
						(R.string.fm_net_call_no_network));
			}
			
		}
		return null;
	}


	/**
	 * @comments 判断网络是否连接
	 * @param context
	 * @return
	 * @version 1.0
	 */
	public static boolean isNetWorkOpen(Context context) {
		// Check network information
		try{
			ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
			if (networkInfo != null) {
				return networkInfo.isAvailable();
			}
		}catch(Exception e){
			//出错了，有可能此手机系统不支持获取网络状态，但是并不代表之后的网络接口访问会失败。
			//所以还是返回真吧
			e.printStackTrace();
			return true;
		}
		return false;
	}
	
}
