package com.fwrestnet;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @comments soap网络访问处理类。
 */
public class RestSyncTask extends AbsRestSyncTask {
    public static final String TAG = "RestSyncTask";
    /**
     * 网络访问对话框
     */
    protected ProgressDialog p;
    protected Context mContext;
    /**
     * 网络访问标识符
     */
    private String mID;
    /**
     * 访问网络接口回调
     */
    private NetCallBack mNetCallBack;
    /**
     * 是否显示网络访问对话框
     */
    private boolean mShowDialogEanble;
    /**
     * 接口访问中是否已经取消
     */
    private boolean isCanceled = false;

    public RestSyncTask(Context c,
                        NetApi api,
                        NetRequest netRequest,
                        String id,
                        NetCallBack back,
                        boolean showDialogEanble) {
        super(api, api.getNetMethod(), netRequest);
        mContext = c;
        mID = id;
        mNetCallBack = back;
        mShowDialogEanble = showDialogEanble;
    }

    @Override
    public void minTimeControl(long t) {

        //不显示对话框时不做最小时间控制
        if (mNetApi.isOnlyDialogMinTimeControl() && !mShowDialogEanble) {
            return;
        }
        if (t < mNetApi.getMinTime()) {
            //时间不足，需要控制时间
            try {
                Thread.sleep(mNetApi.getMinTime() - t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCancelled() {

        //设置接口已经取消了的标记
        isCanceled = true;
        //通知回调 接口结束
        if (mNetCallBack != null) {
            mNetCallBack.onNetEnd(mID, NetCallBack.TYPE_CANCEL, null);
            mNetCallBack = null;
        }
        super.onCancelled();
    }

    @Override
    protected void onPreExecute() {
        try {
            //通知回调接口开始
            if (mNetCallBack != null) {
                mNetCallBack.onNetStart(mID);
            }
            //重置接口取消标记
            isCanceled = false;
            //如果不显示对话框，下面的处理忽略
            if (!mShowDialogEanble) {
                return;
            }
            //重置对话框
            if (p != null && p.isShowing()) {
                p.dismiss();
            }
            //显示对话框
            p = ProgressDialog.show(mContext, null, null, false, false);
            //注册按键事件
            p.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    // TODO Auto-generated method stub
                    //当用户松开BACK按键时，对话框会取消
                    if (KeyEvent.ACTION_UP == event.getAction() && keyCode == KeyEvent.KEYCODE_BACK) {
                        p.dismiss();
                    }
                    return false;
                }
            });
            //监听对话框取消事件，把对话框取消视作为接口的取消
            p.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    if (isCanceled) {
                        return;
                    }
                    //设置接口已经取消了的标记
                    isCanceled = true;
                    //停止接口访问
                    cancel(true);
                    //通知回调 接口结束
                    if (mNetCallBack != null) {
                        mNetCallBack.onNetEnd(mID, NetCallBack.TYPE_CANCEL, null);
                        mNetCallBack = null;
                    }
                }
            });
            //监听对话框停止事件，把对话框停止视作为接口的取消
            p.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface arg0) {
                    if (isCanceled) {
                        return;
                    }
                    //设置接口已经取消了的标记
                    isCanceled = true;
                    //停止接口访问
                    cancel(true);
                    //通知回调 接口结束
                    if (mNetCallBack != null) {
                        mNetCallBack.onNetEnd(mID, NetCallBack.TYPE_CANCEL, null);
                        mNetCallBack = null;
                    }
                }
            });
            //设置对话框布局
            p.setContentView(mNetApi.getShowUIResource());
            p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            TextView t = (TextView) p.findViewById(mNetApi.getShowUITextID());
            //设置对话框描述文字为接口的语言配置
            t.setText(mNetApi.getMsg());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(int statusCode, String statusMsg, NetResponse netResponse, Object maps) throws JSONException {

        if (mNetApi.getResponseType() == ResponseType.JSON) {

            if (mNetApi.isLog()) {
                Log.d(TAG, maps.toString());
            }
            JSONObject json = new JSONObject((String) maps);
            if (json.has("data")) {
                netResponse.data = json.getString("data");
            }
            if (json.has("result")) {
                netResponse.result = json.getString("result");
            }
            if (json.has("bool")) {
                netResponse.bool = json.getInt("bool");
            }

//			if(result.statusCode == NetResponse.SUCCESS){
//				//接口处理成功之后，解析数据
//				if(json.has("data")){
//					String body = json.getString("data");
//					result.body = mNetApi.parseBody(body);
//				}
//
//			}
        } else {
            //	result.body = maps;
        }
    }

    //接口访问结束处理函数
    @Override
    protected void onPostExecute(NetResponse netResponse) {
        Log.i(TAG, "onPostExecute");
        try {
            //停止对话框
            if (p != null) {
                p.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //接口访问被用户取消时，忽略后续处理
            if (isCancelled() || isCanceled) {
                return;
            }
            int type = NetCallBack.TYPE_ERROR;
            if (netResponse == null) {
                type = NetCallBack.TYPE_CANCEL;
            } else {
                if (netResponse.bool == 1) {
                    type = NetCallBack.TYPE_SUCCESS;
                } else if (netResponse.bool == 0) {
                    type = NetCallBack.TYPE_NO_VALUE;
                }else {
                    type = NetCallBack.TYPE_ERROR;
                }
            }

//			if(result.e != null){
//				//接口访问出错
//				Log.e(TAG, result.e.toString());
//				if(mNetCallBack != null){
//					if(result.e instanceof SocketTimeoutException ||
//							result.e instanceof ConnectTimeoutException){
//						//接口访问出错为超时时，通知上层超时
//						type = NetCallBack.TYPE_TIMEOUT;
//					}else{
//						//接口访问出错时，通知上层，兵返回接口配置的错误提示语
//						if(result.statusDesc == null || result.statusDesc.trim().length() == 0){
//							result.statusDesc = mContext.getString(mNetApi.getErrorString());
//						}
//					}
//				}
//			}else{
//
//				if(result.statusCode == NetResponse.SUCCESS){
//					type = NetCallBack.TYPE_SUCCESS;
//				}
//
//				if(result.statusCode == NetResponse.TOKEN_INVALID){
//					type = NetCallBack.TYPE_TOKEN_INVALID;
//				}
//
//			}
            //接口调用结束，通知上层
            if (mNetCallBack != null) {
                mNetCallBack.onNetEnd(mID, type, netResponse);
                mNetCallBack = null;
            }
        } catch (Exception e) {
            //其他一些未知的错误异常
            Toast.makeText(mContext, mContext.getString(mNetApi.getErrorString()), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    @Override
    public boolean isDemo(NetResponse result) throws JSONException {
        // TODO Auto-generated method stub
        if (mNetApi.isDemo()) {
            process(200, null, result, mNetApi.mockData(mContext));
            return true;
        }
        return false;
    }

}

