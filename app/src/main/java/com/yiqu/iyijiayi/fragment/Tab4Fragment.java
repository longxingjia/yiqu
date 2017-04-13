package com.yiqu.iyijiayi.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.ui.views.LoadMoreView;
import com.ui.views.LoadMoreView.OnMoreListener;
import com.ui.views.RefreshList;
import com.ui.views.RefreshList.IRefreshListViewListener;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.adapter.Tab4Adapter;
import com.yiqu.iyijiayi.fragment.tab4.RenewFragment;
import com.yiqu.iyijiayi.model.Discovery;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.NSDictionary;
import com.yiqu.iyijiayi.model.Remen;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;
import com.yiqu.iyijiayi.utils.JsonUtils;
import com.yiqu.iyijiayi.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Tab4Fragment extends TabContentFragment {

    private String tag = "Tab4Fragment";
    private String uid;
    @BindView(R.id.item_renew)
    public TextView item_renew;
    @BindView(R.id.item_1)
    public TextView item_1;
    @BindView(R.id.item_2)
    public TextView item_2;


    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab4_fragment;
    }

    @Override
    protected void initView(View v) {
        ButterKnife.bind(this, v);


    }

    @Override
    protected boolean isTouchMaskForNetting() {

        return false;
    }

    @Override
    protected void init(Bundle savedInstanceState) {


    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("发现"); //统计页面，"MainScreen"为页面名称，可自定义


    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("发现");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getTitleBarType() {
        return FLAG_TXT;
    }

    @Override
    protected boolean onPageBack() {
        if (mOnFragmentListener != null) {
            mOnFragmentListener.onFragmentBack(this);
        }
        return true;
    }

    @Override
    protected boolean onPageNext() {
        pageNextComplete();
        return true;
    }

    @Override
    protected void initTitle() {
        setTitleText(getString(R.string.label_tab4));
    }

    @OnClick({R.id.item_renew, R.id.item_1, R.id.item_2})
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.item_renew:
                Model.startNextAct(getActivity(),
                        RenewFragment.class.getName());

                break;

            case R.id.item_1:

                break;
            case R.id.item_2:

                break;
        }

    }


}
