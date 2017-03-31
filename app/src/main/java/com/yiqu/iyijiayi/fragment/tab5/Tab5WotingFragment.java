package com.yiqu.iyijiayi.fragment.tab5;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ui.views.LoadMoreView;
import com.ui.views.LoadMoreView.OnMoreListener;
import com.ui.views.RefreshList;
import com.ui.views.RefreshList.IRefreshListViewListener;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.Tab4Adapter;
import com.yiqu.iyijiayi.adapter.Tab5WotingAdapter;
import com.yiqu.iyijiayi.fragment.TabContentFragment;
import com.yiqu.iyijiayi.model.Discovery;
import com.yiqu.iyijiayi.model.Listened;
import com.yiqu.iyijiayi.model.NSDictionary;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.JsonUtils;
import com.yiqu.iyijiayi.utils.LogUtils;

import org.json.JSONException;

import java.util.ArrayList;

public class Tab5WotingFragment extends AbsAllFragment implements OnMoreListener, IRefreshListViewListener {

    //分页
    private LoadMoreView mLoadMoreView;
    private int count = 0;
    private int rows = 10;
    private String tag = "Tab5WotingFragment";

    //刷新
    private RefreshList listView;
    private Tab5WotingAdapter tab5WotingAdapter;
    private String uid;
    private ArrayList<Listened> listeneds;


    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab1_fragment;
    }

    @Override
    protected void initView(View v) {
        listView = (RefreshList) v.findViewById(R.id.lv_sound);

        tab5WotingAdapter = new Tab5WotingAdapter(getActivity());
        listView.setAdapter(tab5WotingAdapter);
        listView.setOnItemClickListener(tab5WotingAdapter);
        listView.setRefreshListListener(this);
//
        mLoadMoreView = (LoadMoreView) LayoutInflater.from(getActivity()).inflate(R.layout.list_footer, null);
        mLoadMoreView.setOnMoreListener(this);
        listView.addFooterView(mLoadMoreView);
        listView.setOnScrollListener(mLoadMoreView);

        listView.setFooterDividersEnabled(false);
        listView.setHeaderDividersEnabled(false);

        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);


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
        MobclickAgent.onPageStart("我听"); //统计页面，"MainScreen"为页面名称，可自定义


        uid = AppShare.getUserInfo(getActivity()).uid;
        count = 0;
        RestNetCallHelper.callNet(
                getActivity(),
                MyNetApiConfig.getHistory,
                MyNetRequestConfig.getHistory(getActivity(), uid,count,rows),
                "getHistory", Tab5WotingFragment.this, true, true);
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我听");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getTitleBarType() {
        return FLAG_TXT|FLAG_BACK;
    }

    @Override
    protected boolean onPageBack() {
//        if (mOnFragmentListener != null) {
//            mOnFragmentListener.onFragmentBack(this);
//        }
        return false;
    }

    @Override
    protected boolean onPageNext() {
        pageNextComplete();
        return true;
    }

    @Override
    protected void initTitle() {
        setTitleText("我听");
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {


        if (id.equals("getHistory")) {
            if (type == NetCallBack.TYPE_SUCCESS) {

                listeneds = new Gson().fromJson(netResponse.data, new TypeToken<ArrayList<Listened>>() {
                }.getType());
//                    discoveries = JsonUtils.parseDiscoveryList(netResponse.data);
                tab5WotingAdapter.setData(listeneds);
                    if (listeneds.size() == rows) {
                        mLoadMoreView.setMoreAble(true);
                    }
                count += rows;
                resfreshOk();

            } else {

                resfreshFail();

            }
        } else if ("getHistory_more".equals(id)) {
            if (TYPE_SUCCESS == type) {

                    listeneds = new Gson().fromJson(netResponse.data, new TypeToken<ArrayList<Listened>>() {
                    }.getType());
                    tab5WotingAdapter.addData(listeneds);
                    if (listeneds.size() < rows) {
                        mLoadMoreView.setMoreAble(false);
                    }
                    count += rows;
                    mLoadMoreView.end();

            } else {
                mLoadMoreView.setMoreAble(false);
                mLoadMoreView.end();
            }
        }
        super.onNetEnd(id, type, netResponse);
    }

    @Override
    public boolean onMore(AbsListView view) {

        if (mLoadMoreView.getMoreAble()) {
            if (mLoadMoreView.isloading()) {
                // 正在加载中
            } else {
                mLoadMoreView.loading();

                RestNetCallHelper.callNet(
                        getActivity(),
                        MyNetApiConfig.getHistory,
                        MyNetRequestConfig.getHistory(getActivity(), uid,count,rows),
                        "getHistory_more", Tab5WotingFragment.this, true, true);

            }
        }

        return false;
    }

    @Override
    public void onRefresh() {
        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);

        count = 0;
        RestNetCallHelper.callNet(
                getActivity(),
                MyNetApiConfig.getHistory,
                MyNetRequestConfig.getHistory(getActivity(), uid,count,rows),
                "getHistory", Tab5WotingFragment.this, true, true);


    }

    public void resfreshOk() {
        listView.refreshOk();
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                listView.stopRefresh();
            }


        }.execute();

    }

    public void resfreshFail() {
        listView.refreshFail();
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                listView.stopRefresh();
            }


        }.execute();
    }


}
