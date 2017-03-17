package com.yiqu.iyijiayi.fragment.tab1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.ui.views.LoadMoreView;
import com.ui.views.RefreshList;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.Tab1XizuoAdapter;
import com.yiqu.iyijiayi.adapter.Tab2ListFragmetAdapter;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.NSDictionary;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.model.Teacher;
import com.yiqu.iyijiayi.model.Xizuo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Tab1XizuoListFragment extends AbsAllFragment implements LoadMoreView.OnMoreListener, RefreshList.IRefreshListViewListener {


    private Tab1XizuoAdapter tab1XizuoAdapter;
    private String tag = "Tab2ListFragment";
    private ArrayList<Xizuo> datas;
    private Context mContext;

    //分页
    private LoadMoreView mLoadMoreView;
    private int count = 0;
    private int rows = 10;

    //刷新
    private RefreshList listView;
    private String arr;

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab2_list_fragment;
    }

    @Override
    protected void initView(View v) {
        mContext = getActivity();
        listView = (RefreshList) v.findViewById(R.id.listView);

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

        NSDictionary nsDictionary = new NSDictionary();
        nsDictionary.isopen = "1";
        nsDictionary.ispay = "1";
        nsDictionary.isreply = "1";
        nsDictionary.status = "1";
        nsDictionary.stype = "2";
        Gson gson = new Gson();
        arr = gson.toJson(nsDictionary);

        RestNetCallHelper.callNet(
                getActivity(),
                MyNetApiConfig.getSoundList,
                MyNetRequestConfig.getSoundList(getActivity(), arr, count, rows, "edited", "desc"),
                "getSoundList", Tab1XizuoListFragment.this);

        tab1XizuoAdapter = new Tab1XizuoAdapter(getActivity());
        listView.setAdapter(tab1XizuoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Xizuo f = datas.get(position - 1);
                if (AppShare.getIsLogin(mContext)) {
                    Intent i = new Intent(mContext, StubActivity.class);
                    i.putExtra("fragment", XizuoItemDetailFragment.class.getName());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("xizuo",f);
                    i.putExtras(bundle);
                    mContext.startActivity(i);
                } else {
                    Intent i = new Intent(mContext, StubActivity.class);
                    i.putExtra("fragment", SelectLoginFragment.class.getName());
                    ToastManager.getInstance(mContext).showText("请登录后再试");
                    mContext.startActivity(i);

                }
            }
        });
        listView.setRefreshListListener(this);

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

        setTitleText(getString(R.string.zuopin));

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

        if (id.equals("getSoundList")) {
            if (type == NetCallBack.TYPE_SUCCESS) {

                try {

                    datas = JsonUtils.parseXizuoList(netResponse.data);
                    tab1XizuoAdapter.setData(datas);
                    if (datas.size() == rows) {
                        mLoadMoreView.setMoreAble(true);
                    }
                    count += rows;
                    resfreshOk();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                resfreshFail();
            }
        } else if ("getSoundList_more".equals(id)) {
            if (TYPE_SUCCESS == type) {

                try {
                    datas.addAll(JsonUtils.parseXizuoList(netResponse.data));
                    if (datas.size() < rows) {
                        mLoadMoreView.setMoreAble(false);
                    }
                    count += rows;
                    mLoadMoreView.end();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else {
                mLoadMoreView.end();
                mLoadMoreView.setMoreAble(false);
            }
        }
        super.onNetEnd(id, type, netResponse);
    }

    @Override
    public void onRefresh() {

        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);
        count = 0;
        RestNetCallHelper.callNet(
                getActivity(),
                MyNetApiConfig.getSoundList,
                MyNetRequestConfig.getSoundList(getActivity(), arr, count, rows, "edited", "desc"),
                "getSoundList", Tab1XizuoListFragment.this,false,true);

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
                        MyNetRequestConfig.getSoundList(getActivity(), arr, count, rows, "edited", "desc"),
                        "getSoundList_more", Tab1XizuoListFragment.this,false,true);

            }
        }


        return false;
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