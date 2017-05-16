package com.yiqu.iyijiayi.fragment.tab1;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.ui.views.LoadMoreView;
import com.ui.views.RefreshList;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.Tab1XizuoAdapter;
import com.yiqu.iyijiayi.adapter.Tab1XizuoAdapterTest;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.NSDictionary;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.JsonUtils;

import java.util.ArrayList;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;

public class Tab1XizuoListFragment extends AbsAllFragment implements LoadMoreView.OnMoreListener, RefreshList.IRefreshListViewListener {


    private Tab1XizuoAdapterTest tab1XizuoAdapter;
    private String tag = "Tab2ListFragment";
    private ArrayList<Sound> sounds;
    private Context mContext;

    //分页
    private LoadMoreView mLoadMoreView;
    private int count = 0;
    private int rows = 10;
    private LinearLayout top_play;
    private ImageView play;
    private TextView musicplaying;
    private TextView authorName;
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

        musicplaying = (TextView) v.findViewById(R.id.musicname);
        authorName = (TextView) v.findViewById(R.id.name);
        top_play = (LinearLayout) v.findViewById(R.id.top_play);
        play = (ImageView) v.findViewById(R.id.play);
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
                MyNetRequestConfig.getSoundList(getActivity(), arr, count, rows, "views", "desc"),
                "getSoundList", Tab1XizuoListFragment.this);

        tab1XizuoAdapter = new Tab1XizuoAdapterTest(getActivity(),getClass().getSimpleName());
        listView.setAdapter(tab1XizuoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Sound f = sounds.get(position - 1);
                if (AppShare.getIsLogin(mContext)) {
                    Intent i = new Intent(mContext, StubActivity.class);
                    i.putExtra("fragment", ItemDetailFragment.class.getName());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Sound",f);
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
    public void onStart() {
        super.onStart();
        allowBindService(getActivity());
        tab1XizuoAdapter.setOnMoreClickListener(new Tab1XizuoAdapterTest.OnMoreClickListener() {
            @Override
            public void onMoreClick(int position) {

                String url = MyNetApiConfig.ImageServerAddr + sounds.get(position).soundpath;
                mPlayService.play(url, sounds.get(position).sid);

               // initTopPlayUI(sounds.get(position));
            }

        });
    }

    /**
     * stop时， 回调通知activity解除绑定服务
     */
    @Override
    public void onStop() {
        super.onStop();
        Log.e("fragment", "onDestroyView");
        allowUnbindService(getActivity());
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

        setTitleText(getString(R.string.remenzuopin));

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

        if (id.equals("getSoundList")) {
            if (type == NetCallBack.TYPE_SUCCESS) {

                try {

                    sounds = JsonUtils.parseXizuoList(netResponse.data);
                    tab1XizuoAdapter.setData(sounds);
                    if (sounds.size() == rows) {
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
                    sounds.addAll(JsonUtils.parseXizuoList(netResponse.data));
                    if (sounds.size() < rows) {
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
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("作品列表");
        JAnalyticsInterface.onPageStart(getActivity(),"作品列表");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("作品列表");
        JAnalyticsInterface.onPageEnd(getActivity(),"作品列表");
    }

    @Override
    public void onRefresh() {

        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);
        count = 0;
        RestNetCallHelper.callNet(
                getActivity(),
                MyNetApiConfig.getSoundList,
                MyNetRequestConfig.getSoundList(getActivity(), arr, count, rows, "views", "desc"),
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
                        MyNetRequestConfig.getSoundList(getActivity(), arr, count, rows, "views", "desc"),
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