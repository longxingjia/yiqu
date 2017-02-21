package com.yiqu.iyijiayi.fragment.tab3;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.ui.abs.AbsFragment;
import com.ui.views.LoadMoreView;
import com.ui.views.RefreshList;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.adapter.SoundsTab1Adapter;
import com.yiqu.iyijiayi.model.Music;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/15.
 */

public class SoundsTab1Fragment extends AbsFragment implements LoadMoreView.OnMoreListener, RefreshList.IRefreshListViewListener, NetCallBack {

    private ImageLoaderHm<ImageView> mImageLoaderHm;
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
//        ToastManager.getInstance(getActivity()).showText("1");
        listView = (RefreshList) v.findViewById(R.id.tab1_list);

        //  listView = (RefreshList) v.findViewById(R.id.listView);
        mImageLoaderHm = new ImageLoaderHm<ImageView>(getActivity(), 300);
        soundsTab1Adapter = new SoundsTab1Adapter(getActivity(), mImageLoaderHm);
        listView.setAdapter(soundsTab1Adapter);
        listView.setOnItemClickListener(soundsTab1Adapter);
        listView.setRefreshListListener(this);
//
        mLoadMoreView = (LoadMoreView) LayoutInflater.from(getActivity()).inflate(R.layout.list_footer, null);
        mLoadMoreView.setOnMoreListener(this);
        listView.addFooterView(mLoadMoreView);
        listView.setOnScrollListener(mLoadMoreView);
        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);

        RestNetCallHelper.callNet(getActivity(),
                MyNetApiConfig.getMusicList, MyNetRequestConfig
                        .getMusicList(getActivity(), count, rows),
                "getMusicList", SoundsTab1Fragment.this);

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
                        "getMusicList_more", SoundsTab1Fragment.this);
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
                "getMusicList", SoundsTab1Fragment.this);
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

        if (id.equals("getMusicList")) {
            if (type == NetCallBack.TYPE_SUCCESS) {
                try {
                    musics = parseList(netResponse.data.toString());
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
                    musics = parseList(netResponse.data.toString());
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

    public ArrayList<Music> parseList(String data) throws JSONException {
        ArrayList<Music> list = new ArrayList<Music>();
        JSONArray js = new JSONArray(data);
        for (int i = 0; i < js.length(); i++) {
            JSONObject j = (JSONObject) js.get(i);
            Gson gson = new Gson();
            Music f = gson.fromJson(j.toString(), Music.class);
            list.add(f);
        }
        return list;

    }

    @Override
    public void onNetNoStart(String id) {

    }

    @Override
    public void onNetStart(String id) {

    }
}
