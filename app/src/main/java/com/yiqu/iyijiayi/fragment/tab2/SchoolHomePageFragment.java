package com.yiqu.iyijiayi.fragment.tab2;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;

import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ui.views.LoadMoreView;
import com.ui.views.RefreshList;
import com.umeng.analytics.MobclickAgent;
import com.utils.L;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.Tab2ListFragmetAdapter;
import com.yiqu.iyijiayi.model.Tab2_groups;
import com.yiqu.iyijiayi.model.Teacher;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.JsonUtils;

import org.json.JSONException;

import java.util.ArrayList;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;

public class SchoolHomePageFragment extends AbsAllFragment {


    private String tag = "SchoolHomePageFragment";


    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab2_school_home_fragment;
    }

    @Override
    protected void initView(View v) {

    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("老师学生列表");
        JAnalyticsInterface.onPageStart(getActivity(), "老师学生列表");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("老师学生列表");
        JAnalyticsInterface.onPageEnd(getActivity(), "老师学生列表");
    }

    @Override
    protected boolean isTouchMaskForNetting() {
        return false;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected int getTitleBarType() {
        return FLAG_TXT | FLAG_BACK;
    }

    @Override
    protected boolean onPageBack() {

        return false;
    }

    @Override
    protected boolean onPageNext() {
        pageNextComplete();
        return true;
    }

    @Override
    protected void initTitle() {


    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {


        if (id.equals("get_follow_recommend_list")) {
            if (type == NetCallBack.TYPE_SUCCESS) {


            }
        }
    }
}


