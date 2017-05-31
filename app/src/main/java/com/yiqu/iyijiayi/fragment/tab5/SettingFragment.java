package com.yiqu.iyijiayi.fragment.tab5;

import android.content.Intent;
import android.view.View;

import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.model.Model;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;

/**
 * Created by Administrator on 2017/2/9.
 */

public class SettingFragment extends AbsAllFragment implements View.OnClickListener {

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getTitleBarType() {
        return FLAG_TXT | FLAG_BACK;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab5_setting_fragment;
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
        setTitleText("设置");

    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("设置"); //统计页面，"MainScreen"为页面名称，可自定义
        JAnalyticsInterface.onPageStart(getActivity(),"设置");
    }

    @Override
    public void onPause() {
        super.onPause();
        JAnalyticsInterface.onPageEnd(getActivity(),"设置");
        MobclickAgent.onPageEnd("设置");
    }


    @Override
    protected void initView(View v) {
        v.findViewById(R.id.agreement).setOnClickListener(this);
        v.findViewById(R.id.publish_rules).setOnClickListener(this);
        v.findViewById(R.id.help).setOnClickListener(this);
        v.findViewById(R.id.about).setOnClickListener(this);
        v.findViewById(R.id.advices).setOnClickListener(this);
        v.findViewById(R.id.clear_cach).setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), StubActivity.class);
        switch (v.getId()) {
            case R.id.agreement:
                intent.putExtra("fragment", ShowSettingFragment.class.getName());
                intent.putExtra("data", "agreement");
                getActivity().startActivity(intent);
                break;
            case R.id.publish_rules:
                intent.putExtra("fragment", ShowSettingFragment.class.getName());
                intent.putExtra("data", "publish_rules");
                getActivity().startActivity(intent);
                break;
            case R.id.help:
                intent.putExtra("fragment", ShowSettingFragment.class.getName());
                intent.putExtra("data", "help");
                getActivity().startActivity(intent);
                break;
            case R.id.about:
                Model.startNextAct(getActivity(),
                        Tab5AboutFragment.class.getName());
                break;
            case R.id.advices:
                intent.putExtra("fragment", EditInfoFragment.class.getName());
                intent.putExtra("data", "advices");
                getActivity().startActivity(intent);
                break;
            case R.id.clear_cach:
                Model.startNextAct(getActivity(),
                        ClearCacheFragment.class.getName());
                break;
        }

    }
}
