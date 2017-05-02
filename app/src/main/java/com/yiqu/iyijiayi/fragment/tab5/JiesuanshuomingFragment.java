package com.yiqu.iyijiayi.fragment.tab5;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.base.utils.ToastManager;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.model.YzmKey;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppAvilibleUtils;
import com.yiqu.iyijiayi.utils.AppShare;
import com.utils.LogUtils;
import com.yiqu.iyijiayi.wxapi.WXEntryActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/2/15.
 */

public class JiesuanshuomingFragment extends AbsAllFragment {


    private String key;
    private String openid;
    private EditText code;
    private UserInfo userInfo;

    @Override
    protected int getTitleBarType() {
        return FLAG_BACK | FLAG_TXT;
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
        setTitleText("结算说明");
    }

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab5_jiesuanshuoming_fragment;
    }

    @Override
    protected void initView(View v) {
        code = (EditText) v.findViewById(R.id.code);


        v.findViewById(R.id.band).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppShare.getIsLogin(getActivity())){
                    userInfo = AppShare.getUserInfo(getActivity());
                    if (TextUtils.isEmpty(key)) {
                        RestNetCallHelper.callNet(getActivity(),
                                MyNetApiConfig.getLoginMessageCode, MyNetRequestConfig
                                        .getLoginMessageCode(getActivity(), userInfo.phone),
                                "getLoginMessageCode", JiesuanshuomingFragment.this);
                        code.setVisibility(View.VISIBLE);
                    } else {
                        if (!TextUtils.isEmpty(code.getText().toString().trim())) {
                            if (AppAvilibleUtils.isWeixinAvilible(getActivity())) {
                                Intent intent = new Intent(getActivity(), WXEntryActivity.class);
                                intent.putExtra("data", "login");
                                intent.putExtra("band", "band");
                                startActivityForResult(intent, 0);
                            } else {
                                ToastManager.getInstance(getActivity()).showText("您还没有安装微信，请您先安装微信。");
                            }
                        } else {
                            ToastManager.getInstance(getActivity()).showText("请填写验证码");
                        }

                    }
                }else {
                    Model.startNextAct(getActivity(),
                            SelectLoginFragment.class.getName());
                    ToastManager.getInstance(getActivity()).showText("请登录后再试");
                }

            }
        });


    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        super.onNetEnd(id, type, netResponse);
        if (id.equals("getLoginMessageCode")) {
            if (type == TYPE_SUCCESS) {
                Gson gson = new Gson();
                YzmKey yzmKey = gson.fromJson(netResponse.data, YzmKey.class);
                key = yzmKey.key;
                ToastManager.getInstance(getActivity()).showText("验证码发送成功");
            }

        }else if (id.equals("bindPhoneCheck")){

            LogUtils.LOGE("1",netResponse.toString());
            if (type == TYPE_SUCCESS) {
//                ToastManager.getInstance(getActivity()).showText();
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                openid = data.getStringExtra("openid");
                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.bindPhoneCheck, MyNetRequestConfig
                                .bindPhoneCheck(getActivity(),userInfo.uid,userInfo.phone,code.getText().toString().trim(),key,openid ),
                        "bindPhoneCheck", JiesuanshuomingFragment.this);

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
    }

}
