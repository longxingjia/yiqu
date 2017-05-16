package com.yiqu.iyijiayi.fragment.tab5;

import android.content.Intent;
import android.view.View;

import com.base.utils.ToastManager;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.model.Constant;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.utils.AppAvilibleUtils;
import com.yiqu.iyijiayi.wxapi.WXEntryActivity;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;

/**
 * Created by Administrator on 2017/2/9.
 */

public class SelectLoginFragment extends AbsAllFragment {



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

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("选择登陆方式"); //统计页面，"MainScreen"为页面名称，可自定义
        JAnalyticsInterface.onPageStart(getActivity(),"选择登陆方式");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("选择登陆方式");
        JAnalyticsInterface.onPageEnd(getActivity(),"选择登陆方式");
    }


    @Override
    protected void initView(View v) {

        v.findViewById(R.id.wechat_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppAvilibleUtils.isWeixinAvilible(getActivity())){
                    Intent intent = new Intent(getActivity(), WXEntryActivity.class);
                    intent.putExtra("data","login");
                    startActivity(intent);
                }else {
                    ToastManager.getInstance(getActivity()).showText("您还没有安装微信，请您先安装微信。");
                }


            }
        });

        v.findViewById(R.id.phone_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.startNextAct(getActivity(), RegisterFragment.class.getName());
                getActivity().finish();

            }
        });

    }


}
