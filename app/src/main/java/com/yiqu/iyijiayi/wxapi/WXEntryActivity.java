package com.yiqu.iyijiayi.wxapi;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yiqu.iyijiayi.MainActivity;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.adapter.DialogHelper;
import com.yiqu.iyijiayi.fragment.Tab5Fragment;
import com.yiqu.iyijiayi.fragment.menu.RegisterFragment;
import com.yiqu.iyijiayi.fragment.menu.SetPhoneFragment;
import com.yiqu.iyijiayi.model.Constant;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.model.WechatAccessToken;
import com.yiqu.iyijiayi.model.WechatUserInfo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.NetworkRestClient;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    private Context mContext;
    private String code;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_wxentry);
        mContext = this;
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //即为所需的code
                code = ((SendAuth.Resp) resp).code;


                if (NetworkRestClient.isNetworkAvailable(mContext)) {
                    LoginTask loginTask = new LoginTask();
                    loginTask.execute();
                }



                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                ToastManager.getInstance(this).showText("登录被拒绝");
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                ToastManager.getInstance(this).showText("登录被取消");
                finish();
                break;
        }
    }

    class LoginTask extends AsyncTask<String, Void, String> {

        private DialogHelper dialogHelper;

        @Override
        protected void onPreExecute() {
            dialogHelper = new DialogHelper(mContext, this);
            dialogHelper.showProgressDialog();
        }

        @Override
        protected String doInBackground(String... voids) {
            String wechatUrl = String.format(MyNetApiConfig.wechatUrl, Constant.APP_ID, Constant.AppSecret, code);

            String result = NetworkRestClient.get(wechatUrl);


            return result;
        }

        @Override
        protected void onPostExecute(final String result) {

            if (dialogHelper != null) {
                dialogHelper.dismissProgressDialog();
            }
            try {
                if (result.contains("errmsg")) {
                    ToastManager.getInstance(mContext).showText("登录失败");
                    finish();
                } else {
                    WechatAccessToken wechatAccessToken = new Gson().fromJson(result, WechatAccessToken.class);

                    if (NetworkRestClient.isNetworkAvailable(mContext)) {
                        GetWechatUserInfoTask getWechatUserInfoTask = new GetWechatUserInfoTask(mContext,wechatAccessToken.access_token,wechatAccessToken.openid);
                        getWechatUserInfoTask.execute();
                    }



                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    class GetWechatUserInfoTask extends AsyncTask<Void, Void, WechatUserInfo> {

        private String openid;
        private String access_token;
        private Context mContext;
        private DialogHelper dialog;

        public GetWechatUserInfoTask(Context context, String access_token, String openid) {
            this.openid = openid;
            this.access_token = access_token;
            this.mContext = context;
        }

        @Override
        protected void onPreExecute() {
            dialog = new DialogHelper(mContext, this);
            dialog.showProgressDialog();
        }

        @Override
        protected WechatUserInfo doInBackground(Void... params) {
            if (!NetworkRestClient.isNetworkAvailable(mContext)) {
                return null;
            }
            try {
                String result = NetworkRestClient.get(String.format(MyNetApiConfig.getWechatUserInfo, access_token, openid));
                WechatUserInfo vo = null;

                if (result.length() > 0) {
                    Gson gson = new Gson();
                    vo = gson.fromJson(result, WechatUserInfo.class);
                }
                return vo;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(final WechatUserInfo vo) {
            if (dialog != null) {
                dialog.dismissProgressDialog();
            }


            if (vo != null) {
                 //   LogUtils.LOGE(vo.toString());
                AppShare.setWechatUserInfo(mContext,vo);
                RestNetCallHelper.callNet(mContext,
                        MyNetApiConfig.loginFromWechat, MyNetRequestConfig
                                .loginFromWechat(mContext,vo.openid,vo.nickname,vo.headimgurl,vo.sex ),
                        "loginFromWechat", new NetCallBack() {
                            @Override
                            public void onNetNoStart(String id) {

                            }

                            @Override
                            public void onNetStart(String id) {

                            }

                            @Override
                            public void onNetEnd(String id, int type, NetResponse netResponse) {
                                LogUtils.LOGE(netResponse.toString());
                                if (netResponse.bool==0){
                                    //新用户
                                    Model.startNextAct(mContext, SetPhoneFragment.class.getName());

                                }else {
                                    AppShare.setIsLogin(mContext, true);
                                    Gson gson = new Gson();
                                    UserInfo userInfo = gson.fromJson(netResponse.data.toString(), UserInfo.class);
                                    AppShare.setUserInfo(mContext, userInfo);
//                                    Model.startNextAct(mContext, Tab5Fragment.class.getName());
//                                    finish();

                                    Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
                                    intent.putExtra("fragmentName",Tab5Fragment.class.getName());
                                    startActivity(intent);


                                }
                                finish();
                            }
                        });
            }


        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

        }
    }
}