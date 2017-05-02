package com.yiqu.iyijiayi.fragment.tab3;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.ui.abs.AbsFragment;
import com.ui.views.LoadMoreView;
import com.ui.views.RefreshList;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.adapter.SoundsTab1Adapter;
import com.yiqu.iyijiayi.fragment.tab1.SearchFragment;
import com.model.Music;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.JsonUtils;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/15.
 */

public class SoundsTab1Fragment extends AbsFragment implements LoadMoreView.OnMoreListener, RefreshList.IRefreshListViewListener, NetCallBack {

    private LoadMoreView mLoadMoreView;
    private int count = 0;
    private int rows = 10;
    private String tag = "SoundsTab1Fragment";
    //刷新
    private RefreshList listView;
    private SoundsTab1Adapter soundsTab1Adapter;
    private String uid;
    private ArrayList<Music> musics;


    @Override
    protected int getContentView() {
        return R.layout.record_tab1_fragment;
    }

    @Override
    protected void initView(View v) {

        listView = (RefreshList) v.findViewById(R.id.tab1_list);
        soundsTab1Adapter = new SoundsTab1Adapter(getActivity());
        listView.setAdapter(soundsTab1Adapter);
        LinearLayout search = (LinearLayout) v.findViewById(R.id.search);

        listView.setOnItemClickListener(soundsTab1Adapter);
        listView.setRefreshListListener(this);

        mLoadMoreView = (LoadMoreView) LayoutInflater.from(getActivity()).inflate(R.layout.list_footer, null);
        mLoadMoreView.setOnMoreListener(this);
        listView.addFooterView(mLoadMoreView);
        listView.setOnScrollListener(mLoadMoreView);
        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);

        count = 0;
        RestNetCallHelper.callNet(getActivity(),
                MyNetApiConfig.getMusicList, MyNetRequestConfig
                        .getMusicList(getActivity(), count, rows),
                "getMusicList", SoundsTab1Fragment.this,false,true);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StubActivity.class);
                intent.putExtra("fragment", SearchFragment.class.getName());
                intent.putExtra("data","search_music");
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("声乐伴奏页面");


    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("声乐伴奏页面");

    }


    @Override
    public boolean onMore(AbsListView view) {
        if (mLoadMoreView.getMoreAble()) {
            if (mLoadMoreView.isloading()) {
                // 正在加载中
            } else {
                mLoadMoreView.loading();
                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.getMusicList, MyNetRequestConfig
                                .getMusicList(getActivity(), count, rows),
                        "getMusicList_more", SoundsTab1Fragment.this,false,true);
            }
        }
        return false;
    }

    @Override
    public void onRefresh() {
        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);

        count = 0;
        RestNetCallHelper.callNet(getActivity(),
                MyNetApiConfig.getMusicList, MyNetRequestConfig
                        .getMusicList(getActivity(), count, rows),
                "getMusicList", SoundsTab1Fragment.this,false,true);
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

        if (id.equals("getMusicList")) {
            if (type == NetCallBack.TYPE_SUCCESS) {
                try {
                    musics = JsonUtils.parseMusicList(netResponse.data);
                    soundsTab1Adapter.setData(musics);
                    if (musics.size() == rows) {
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
        }else if ("getMusicList_more".equals(id)) {
            if (TYPE_SUCCESS == type) {

                try {
                    musics = JsonUtils.parseMusicList(netResponse.data);
                    soundsTab1Adapter.addData(musics);
                    if (musics.size() < rows) {
                        mLoadMoreView.setMoreAble(false);
                    }
                    count += rows;
                    mLoadMoreView.end();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

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



    @Override
    public void onNetNoStart(String id) {

    }

    @Override
    public void onNetStart(String id) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
