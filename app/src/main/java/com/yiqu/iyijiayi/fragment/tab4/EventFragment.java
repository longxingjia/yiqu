package com.yiqu.iyijiayi.fragment.tab4;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ui.views.LoadMoreView;
import com.ui.views.RefreshList;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.Tab4DiscoveryAdapter;
import com.yiqu.iyijiayi.adapter.Tab4HotAdapter;
import com.yiqu.iyijiayi.adapter.Tab4NewAdapter;
import com.yiqu.iyijiayi.fragment.Tab1Fragment;
import com.yiqu.iyijiayi.fragment.tab1.Tab1XizuoListFragment;
import com.yiqu.iyijiayi.model.Discovery;
import com.yiqu.iyijiayi.model.Events;
import com.yiqu.iyijiayi.model.NSDictionary;
import com.yiqu.iyijiayi.model.Remen;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.JsonUtils;
import com.yiqu.iyijiayi.utils.LogUtils;

import org.json.JSONException;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/12.
 */

public class EventFragment extends AbsAllFragment implements LoadMoreView.OnMoreListener, RefreshList.IRefreshListViewListener {


    //分页
    private LoadMoreView mLoadMoreView;
    private int count = 0;
    private int rows = 10;
    private String tag = "Tab4Fragment";

    //刷新
    private RefreshList listView;
    private Tab4HotAdapter tab4HotAdapter;
    private Tab4NewAdapter tab4NewAdapter;

    private String uid;

    private ArrayList<Discovery> discoveries;
    private Events events;
    private String arr;
    private ArrayList<Sound> sounds;

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab4_event_fragment;
    }

    @Override
    protected void initView(View v) {
        ButterKnife.bind(this, v);
        events = (Events) getActivity().getIntent().getSerializableExtra("data");
        listView = (RefreshList) v.findViewById(R.id.lv_sound);
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.tab4_event_fragment_header, null);
        initHeadView(headerView);


        tab4NewAdapter = new Tab4NewAdapter(getActivity());
        listView.setAdapter(tab4NewAdapter);
              listView.setOnItemClickListener(tab4NewAdapter);
        listView.setRefreshListListener(this);
//
        mLoadMoreView = (LoadMoreView) LayoutInflater.from(getActivity()).inflate(R.layout.list_footer, null);
        mLoadMoreView.setOnMoreListener(this);
        listView.addFooterView(mLoadMoreView);
        listView.setOnScrollListener(mLoadMoreView);
      //  listView.addHeaderView(headerView);
        listView.setFooterDividersEnabled(false);
        listView.setHeaderDividersEnabled(false);
        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);

    }

    @OnClick({R.id.join_in})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.join_in:
                Intent i = new Intent(getActivity(), StubActivity.class);
                i.putExtra("fragment", EventInfoFragment.class.getName());
                i.putExtra("eid",String.valueOf(events.id));
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("data",f);
//                i.putExtras(bundle);
                getActivity().startActivity(i);


                break;
        }
    }


    private void initHeadView(View v) {
        tab4HotAdapter = new Tab4HotAdapter(getActivity());
        ListView head_listview = (ListView) v.findViewById(R.id.head_listview);
        head_listview.setAdapter(tab4HotAdapter);

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

//        RestNetCallHelper.callNet(
//                getActivity(),
//                MyNetApiConfig.remen,
//                MyNetRequestConfig.remen(getActivity(), uid),
//                "Remen", EventFragment.this, false, true);

        NSDictionary nsDictionary = new NSDictionary();
        nsDictionary.isopen = "1";
        nsDictionary.ispay = "1";
        nsDictionary.isreply = "1";
        nsDictionary.status = "1";
        nsDictionary.stype = "2";
        nsDictionary.eid = events.id;
        Gson gson = new Gson();
        arr = gson.toJson(nsDictionary);

        RestNetCallHelper.callNet(
                getActivity(),
                MyNetApiConfig.getSoundList,
                MyNetRequestConfig.getSoundList(getActivity(), arr, count, rows, "created", "desc"),
                "getSoundList", EventFragment.this);


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
        setTitleText(events.title);
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

        if (id.equals("Remen")) {
            if (type == TYPE_SUCCESS) {
                Gson gson = new Gson();
                Remen remen = gson.fromJson(netResponse.data, Remen.class);
             //   LogUtils.LOGE(tag, remen.toString());
                tab4HotAdapter.setData(remen.xizuo);
                tab4NewAdapter.setData(remen.sound);
            }
        } else if (id.equals("getSoundList")) {
            if (type == TYPE_SUCCESS) {
                sounds = new Gson().fromJson(netResponse.data, new TypeToken<ArrayList<Sound>>() {
                }.getType());
                tab4NewAdapter.setData(sounds);
                if (sounds.size() == rows) {
                    mLoadMoreView.setMoreAble(true);
                }
                count += rows;
                resfreshOk();
            } else {
                ToastManager.getInstance(getActivity()).showText(netResponse.result);
                resfreshFail();
            }
//            LogUtils.LOGE(tag, netResponse.toString());
        } else if (id.equals("getSoundList_more")) {


            if (type == TYPE_SUCCESS) {
                sounds = new Gson().fromJson(netResponse.data, new TypeToken<ArrayList<Sound>>() {
                }.getType());

                //  remen.sound.addAll(sound);
                //    AppShare.setRemenList(getActivity(), remen);
//                tab1XizuoAdapter.setData(sound);
                tab4NewAdapter.addData(sounds);
                if (sounds.size() < rows) {
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
                        MyNetApiConfig.getSoundList,
                        MyNetRequestConfig.getSoundList(getActivity(), arr, count, rows, "created", "desc"),
                        "getSoundList_more", EventFragment.this, false, true);

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
                MyNetApiConfig.getSoundList,
                MyNetRequestConfig.getSoundList(getActivity(), arr, count, rows, "created", "desc"),
                "getSoundList", EventFragment.this);


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
