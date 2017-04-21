package com.yiqu.iyijiayi.fragment.tab4;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.ui.views.LoadMoreView;
import com.ui.views.RefreshList;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.Tab4DiscoveryAdapter;
import com.yiqu.iyijiayi.model.Discovery;
import com.yiqu.iyijiayi.model.NSDictionary;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.JsonUtils;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/12.
 */

public class RenewFragment extends AbsAllFragment implements LoadMoreView.OnMoreListener,RefreshList.IRefreshListViewListener {


    //分页
    private LoadMoreView mLoadMoreView;
    private int count = 0;
    private int rows = 10;
    private String tag = "RenewFragment";

    //刷新
    private RefreshList listView;
    private Tab4DiscoveryAdapter tab4DiscoveryAdapter;
    private String uid;
    private RelativeLayout loadErr;
    private String arr;
    private ArrayList<Discovery> discoveries;

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab4_renew_fragment;
    }

    @Override
    protected void initView(View v) {
        listView = (RefreshList) v.findViewById(R.id.listView);
        loadErr = (RelativeLayout) v.findViewById(R.id.loading_error);

        tab4DiscoveryAdapter = new Tab4DiscoveryAdapter(getActivity());
        listView.setAdapter(tab4DiscoveryAdapter);
        listView.setOnItemClickListener(tab4DiscoveryAdapter);
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
        MobclickAgent.onPageStart("发现"); //统计页面，"MainScreen"为页面名称，可自定义

        if (count>0){
            loadErr.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }else {
            loadErr.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

        count = 0;
        if (AppShare.getIsLogin(getActivity())) {
            uid = AppShare.getUserInfo(getActivity()).uid;
            NSDictionary nsDictionary = new NSDictionary();
            nsDictionary.isopen = "1";
            nsDictionary.ispay = "1";
            nsDictionary.isreply = "1";
            nsDictionary.status = "1";
            Gson gson = new Gson();
            arr = gson.toJson(nsDictionary);
            loadErr.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

            RestNetCallHelper.callNet(
                    getActivity(),
                    MyNetApiConfig.getFollowSoundList,
                    MyNetRequestConfig.getFollowSoundList(getActivity(), uid, arr, count, rows, "edited", "desc"),
                    "getSoundList", RenewFragment.this,false,true);
        } else {
            loadErr.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);

        }


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
        return FLAG_TXT|FLAG_BACK;
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
        setTitleText(getString(R.string.label_tab4));
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

        if (id.equals("getSoundList")) {
            if (type == NetCallBack.TYPE_SUCCESS) {

                try {
                    discoveries = JsonUtils.parseDiscoveryList(netResponse.data);
                    tab4DiscoveryAdapter.setData(discoveries);
                    if (discoveries.size() == rows) {
                        mLoadMoreView.setMoreAble(true);
                    }
                    count += rows;
                    resfreshOk();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                loadErr.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                resfreshFail();

            }
        } else if ("getSoundList_more".equals(id)) {
            if (TYPE_SUCCESS == type) {
                try {
                    discoveries = JsonUtils.parseDiscoveryList(netResponse.data);
                    tab4DiscoveryAdapter.addData(discoveries);
                    if (discoveries.size() < rows) {
                        mLoadMoreView.setMoreAble(false);
                    }
                    count += rows;
                    mLoadMoreView.end();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {
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
                        MyNetApiConfig.getFollowSoundList,
                        MyNetRequestConfig.getFollowSoundList(getActivity(), uid, arr, count, rows, "edited", "desc"),
                        "getSoundList_more", RenewFragment.this,false,true);

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
                MyNetApiConfig.getFollowSoundList,
                MyNetRequestConfig.getFollowSoundList(getActivity(), uid, arr, count, rows, "edited", "desc"),
                "getSoundList", RenewFragment.this,false,true);


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
