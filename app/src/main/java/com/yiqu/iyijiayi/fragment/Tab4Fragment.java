package com.yiqu.iyijiayi.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

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
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.adapter.Tab4Adapter;
import com.yiqu.iyijiayi.model.Discovery;
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

public class Tab4Fragment extends TabContentFragment implements OnMoreListener, IRefreshListViewListener {


    private ImageLoaderHm<ImageView> mImageLoaderHm;

    //分页
    private LoadMoreView mLoadMoreView;
    private int count = 0;
    private int rows = 10;
    private String tag = "Tab4Fragment";

    //刷新
    private RefreshList listView;
    private Tab4Adapter tab4Adapter;
    private String uid;
    private ImageView loadErr;
    private String arr;
    private ArrayList<Discovery> discoveries;

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
        listView = (RefreshList) v.findViewById(R.id.listView);
        loadErr = (ImageView) v.findViewById(R.id.loading_error);
        mImageLoaderHm = new ImageLoaderHm<ImageView>(getActivity(), 300);
        tab4Adapter = new Tab4Adapter(getActivity());
        listView.setAdapter(tab4Adapter);
        listView.setOnItemClickListener(tab4Adapter);
        listView.setRefreshListListener(this);
//
        mLoadMoreView = (LoadMoreView) LayoutInflater.from(getActivity()).inflate(R.layout.list_footer, null);
        mLoadMoreView.setOnMoreListener(this);
        listView.addFooterView(mLoadMoreView);
        listView.setOnScrollListener(mLoadMoreView);
        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);


    }

    @Override
    protected boolean isTouchMaskForNetting() {

        return false;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setSlidingMenuEnable(false);

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
                    "getSoundList", Tab4Fragment.this);
        } else {
            loadErr.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);

        }

    }

    @Override
    public void onDestroy() {
        mImageLoaderHm.stop();

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

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

        if (id.equals("getSoundList")) {
            if (type == NetCallBack.TYPE_SUCCESS) {
                LogUtils.LOGE(tag,netResponse.toString());
                try {
                    discoveries = JsonUtils.parseDiscoveryList(netResponse.data);
                    LogUtils.LOGE(tag,discoveries.toString());
                    tab4Adapter.setData(discoveries);
                    if (discoveries.size() == rows) {
                        mLoadMoreView.setMoreAble(true);
                    }
                    count += rows;
                    resfreshOk();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                resfreshFail();
            }
        } else if ("getSoundList_more".equals(id)) {
            if (TYPE_SUCCESS == type) {

                try {
                    discoveries = JsonUtils.parseDiscoveryList(netResponse.data);
                    tab4Adapter.addData(discoveries);
                    if (discoveries.size() < rows) {
                        mLoadMoreView.setMoreAble(false);
                    }
                    count += rows;
                    mLoadMoreView.end();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {
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
                        "getSoundList_more", Tab4Fragment.this);

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
                "getSoundList", Tab4Fragment.this);


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
