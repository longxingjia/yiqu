package com.yiqu.iyijiayi.wxapi;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.model.Constant;
import com.yiqu.iyijiayi.model.PayInfo;
import com.yiqu.iyijiayi.model.Wx_arr;
import com.yiqu.iyijiayi.net.NetworkRestClient;
import com.yiqu.iyijiayi.utils.LogUtils;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;
    public static final String APP_ID = Constant.APP_ID;
    private Wx_arr wx_arr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_overlay);

        api = WXAPIFactory.createWXAPI(this, APP_ID);
        api.registerApp(APP_ID);

        init();

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(getIntent(), this);
    }

    private void init() {
        Intent intent = getIntent();
        wx_arr = (Wx_arr) intent.getSerializableExtra("data");

        if (wx_arr==null){
            finish();
        }else {
            PayReq req = new PayReq();
            //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
            req.appId = APP_ID;
            req.partnerId = wx_arr.partnerid;
            req.prepayId = wx_arr.prepayid;
            req.nonceStr = wx_arr.noncestr;
            req.timeStamp = wx_arr.timestamp;
            req.packageValue = "Sign=WXPay";
            req.sign = wx_arr.sign;
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            api.sendReq(req);
        }
    }


    @Override
    public void onResp(BaseResp resp) {
          LogUtils.LOGE(TAG, "errCode:" + resp.errCode);
        Intent intent = new Intent();
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //即为所需的code
                if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
                    ToastManager.getInstance(this).showText("支付成功");

                    setResult(RESULT_OK, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
                    finish();//此处一定要调用finish()方法
                }

                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                ToastManager.getInstance(this).showText("支付被拒绝");
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                ToastManager.getInstance(this).showText("支付被取消");
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
        }

    }



    @Override
    public void onReq(BaseReq req) {
    }
}