package com.yiqu.iyijiayi.fragment.menu;

import android.os.CountDownTimer;
import android.view.View;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.model.Constant;
import com.yiqu.iyijiayi.model.Model;

/**
 * Created by Administrator on 2017/2/9.
 */

public class SelectLoginFragment extends AbsAllFragment {

    public static final String APP_ID = Constant.APP_ID;
    private IWXAPI api;

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_back;
    }

    @Override
    protected int getTitleBarType() {
        return FLAG_TXT|FLAG_BACK;
    }

    @Override
    protected int getBodyView() {
        return R.layout.select_login_fragment;
    }


    @Override
    protected boolean onPageBack() {
        return false;
    }

    @Override
    protected boolean onPageNext() {
        return false;
    }

    @Override
    protected void initTitle() {
        setTitleText("登录");

    }

    private void regTowx() {
        api = WXAPIFactory.createWXAPI(getActivity(), APP_ID, true);
        api.registerApp(APP_ID);
    }

    @Override
    protected void initView(View v) {
        regTowx();
        v.findViewById(R.id.wechat_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wechat_sdk";
//                req.state = "wechat_sdk_demo_test";
                api.sendReq(req);


            }
        });

        v.findViewById(R.id.phone_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.startNextAct(getActivity(), RegisterFragment.class.getName());

            }
        });

    }


}
