package com.yiqu.iyijiayi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ui.views.LoadMoreView;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.adapter.Tab4Adapter;
import com.yiqu.iyijiayi.adapter.Tab4DiscoveryAdapter;
import com.yiqu.iyijiayi.fragment.tab4.EventFragment;
import com.yiqu.iyijiayi.fragment.tab4.RenewFragment;
import com.yiqu.iyijiayi.model.Events;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.LogUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Tab4Fragment extends TabContentFragment implements LoadMoreView.OnMoreListener {

    private String tag = "Tab4Fragment";
    private String uid;

    @BindView(R.id.listView)
    public ListView listView;

  //  private LoadMoreView mLoadMoreView;
    private int count = 0;
    private int rows = 10;
    private Tab4Adapter tab4Adapter;


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
        tab4Adapter = new Tab4Adapter(getActivity());
        listView.setAdapter(tab4Adapter);
        listView.setOnItemClickListener(tab4Adapter);
//
//        mLoadMoreView = (LoadMoreView) LayoutInflater.from(getActivity()).inflate(R.layout.list_footer, null);
//        mLoadMoreView.setOnMoreListener(this);
//        listView.addFooterView(mLoadMoreView);
//        listView.setOnScrollListener(mLoadMoreView);
//
//        listView.setFooterDividersEnabled(false);
//        listView.setHeaderDividersEnabled(false);

//        mLoadMoreView.end();
//        mLoadMoreView.setMoreAble(false);


        RestNetCallHelper.callNet(
                getActivity(),
                MyNetApiConfig.getEventList,
                MyNetRequestConfig.getEventList(getActivity(), 0, 10),
                "getEventList", Tab4Fragment.this);

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        super.onNetEnd(id, type, netResponse);
        if (id.equals("getEventList")) {
            if (type == TYPE_SUCCESS) {
                LogUtils.LOGE(tag, netResponse.toString());
                ArrayList<Events> events =
                        new Gson().fromJson(netResponse.data, new TypeToken<ArrayList<Events>>() {
                        }.getType());
                tab4Adapter.setData(events);

            }

        }

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

    @OnClick({R.id.item_renew})
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.item_renew:
                Model.startNextAct(getActivity(),
                        RenewFragment.class.getName());
                break;
        }

    }


    @Override
    public boolean onMore(AbsListView view) {
        return false;
    }
}
