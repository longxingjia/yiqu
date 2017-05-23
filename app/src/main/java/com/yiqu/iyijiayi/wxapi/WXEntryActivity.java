package com.yiqu.iyijiayi.wxapi;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.utils.L;
import com.yiqu.iyijiayi.MainActivity;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.adapter.DialogHelper;
import com.yiqu.iyijiayi.fragment.Tab5Fragment;
import com.yiqu.iyijiayi.fragment.tab5.SetPhoneFragment;
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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import cn.sharesdk.wechat.utils.WechatHandlerActivity;


public class WXEntryActivity extends WechatHandlerActivity implements IWXAPIEventHandler {

    private Context mContext;
    private String code;
    public static final String APP_ID = Constant.APP_ID;
    private IWXAPI api;
    private String band;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.progress_overlay);
        mContext = this;
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
        api.handleIntent(getIntent(), this);

        String login = getIntent().getStringExtra("data");
        band = getIntent().getStringExtra("band");

        if (!TextUtils.isEmpty(login)) {
            regTowx();
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk";
            api.sendReq(req);
        }

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

    private void regTowx() {
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        api.registerApp(APP_ID);
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
                ToastManager.getInstance(this).showText("操作被取消");
                finish();
                break;
        }
    }

    class LoginTask extends AsyncTask<String, Void, String> {

//        private DialogHelper dialogHelper;

        @Override
        protected void onPreExecute() {
//            dialogHelper = new DialogHelper(mContext, this);
//            dialogHelper.showProgressDialog();
        }

        @Override
        protected String doInBackground(String... voids) {
            String wechatUrl = String.format(MyNetApiConfig.wechatUrl, Constant.APP_ID, Constant.AppSecret, code);

            String result = NetworkRestClient.get(wechatUrl);


            return result;
        }

        @Override
        protected void onPostExecute(final String result) {
//
//            if (dialogHelper != null) {
//                dialogHelper.dismissProgressDialog();
//            }
            try {
                if (result.contains("errmsg")) {
                    ToastManager.getInstance(mContext).showText("登录失败");
                    finish();
                } else {
                    WechatAccessToken wechatAccessToken = new Gson().fromJson(result, WechatAccessToken.class);
                    if (!TextUtils.isEmpty(band)){
                        Intent intent = new Intent();
                        intent.putExtra("openid",wechatAccessToken.openid);
                        setResult(RESULT_OK,intent);

                        finish();
                        return;
                    }

                    if (NetworkRestClient.isNetworkAvailable(mContext)) {
                        GetWechatUserInfoTask getWechatUserInfoTask = new GetWechatUserInfoTask(mContext, wechatAccessToken.access_token, wechatAccessToken.openid);
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
//                    L.e(result);
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
                AppShare.setWechatUserInfo(mContext, vo);
                L.e(vo.headimgurl);

                RestNetCallHelper.callNet(mContext,
                        MyNetApiConfig.loginFromWechat, MyNetRequestConfig
                                .loginFromWechat(mContext, vo.openid, vo.nickname, vo.headimgurl, vo.sex),
                        "loginFromWechat", new NetCallBack() {
                            @Override
                            public void onNetNoStart(String id) {

                            }

                            @Override
                            public void onNetStart(String id) {

                            }

                            @Override
                            public void onNetEnd(String id, int type, NetResponse netResponse) {

                                if (netResponse.bool == 0) {
                                    //新用户
                                    Model.startNextAct(mContext, SetPhoneFragment.class.getName());
                                } else {

                                    Gson gson = new Gson();
                                    L.e(netResponse.toString());
                                    UserInfo userInfo = gson.fromJson(netResponse.data.toString(), UserInfo.class);
                                    AppShare.setIsLogin(mContext, true);
                                    AppShare.setUserInfo(mContext, userInfo);
                                    Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
                                    intent.putExtra("fragmentName", Tab5Fragment.class.getName());
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