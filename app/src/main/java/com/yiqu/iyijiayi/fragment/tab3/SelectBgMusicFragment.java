package com.yiqu.iyijiayi.fragment.tab3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ui.views.LoadMoreView;
import com.ui.views.RefreshList;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.Tab1XizuoAdapterTest;
import com.yiqu.iyijiayi.adapter.Tab3MusicAdapter;
import com.yiqu.iyijiayi.fileutils.utils.Player;
import com.model.Music;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.utils.LogUtils;
import com.yiqu.iyijiayi.utils.PageCursorView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/15.
 */

public class SelectBgMusicFragment extends AbsAllFragment implements LoadMoreView.OnMoreListener, RefreshList.IRefreshListViewListener {
    private static final String TAG = "SelectBgMusicFragment";


    @BindView(R.id.cursor)
    public PageCursorView cursor;
    @BindView(R.id.tab1_tv)
    public TextView tab1_tv;
    @BindView(R.id.tab2_tv)
    public TextView tab2_tv;
    @BindView(R.id.tab3_tv)
    public TextView tab3_tv;
    @BindView(R.id.tab4_tv)
    public TextView tab4_tv;
    @BindView(R.id.tab1)
    public LinearLayout tab1;
    @BindView(R.id.tab2)
    public LinearLayout tab2;
    @BindView(R.id.tab3)
    public LinearLayout tab3;
    @BindView(R.id.tab4)
    public LinearLayout tab4;
    @BindView(R.id.listView)
    public RefreshList listView;
    private int count = 0;
    private int rows = 20;
    private String typename;
    private LoadMoreView mLoadMoreView;

    private Tab3MusicAdapter tab3MusicAdapter;
    private ArrayList<Music> backgroudMusics;
    private Player player;

    //    @OnClick(R.id.submit)
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.submit:


                break;
            default:
                break;
        }
    }


    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.select_music_fragmet;
    }

    @Override
    protected void initView(View v) {
        ButterKnife.bind(this, v);
        player = new Player(getActivity(), null, null, null, null, new Player.onPlayCompletion() {
            @Override
            public void completion() {

            }
        });
        tab3MusicAdapter = new Tab3MusicAdapter(getActivity(),player);
        listView.setAdapter(tab3MusicAdapter);
        listView.setOnItemClickListener(tab3MusicAdapter);
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
    public void onDestroy() {

        if (mPlayService!=null){
            mPlayService.stop();
        }

        super.onDestroy();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        tab1.setOnClickListener(new txListener(1));
        tab2.setOnClickListener(new txListener(2));
        tab3.setOnClickListener(new txListener(3));
        tab4.setOnClickListener(new txListener(4));

        InitCursor(4);
        typename = tab1_tv.getText().toString().trim();

        RestNetCallHelper.callNet(getActivity(),
                MyNetApiConfig.getBackgroundMusicList,
                MyNetRequestConfig.getBackgroundMusicList(getActivity()
                        , typename, String.valueOf(rows), String.valueOf(count)),
                "getBackgroundMusicList",
                SelectBgMusicFragment.this);
    }

    /*
 * 初始化图片的位移像素
 */
    private void InitCursor(int count) {
        cursor.setCount(count);
        cursor.setPosition(0);
    }

    @Override
    public boolean onMore(AbsListView view) {
        if (mLoadMoreView.getMoreAble()) {
            if (mLoadMoreView.isloading()) {
                // 正在加载中
            } else {
                mLoadMoreView.loading();

                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.getBackgroundMusicList,
                        MyNetRequestConfig.getBackgroundMusicList(getActivity()
                                , typename, String.valueOf(rows), String.valueOf(count)),
                        "getBackgroundMusicList_more",
                        SelectBgMusicFragment.this);

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
                MyNetApiConfig.getBackgroundMusicList,
                MyNetRequestConfig.getBackgroundMusicList(getActivity()
                        , typename, String.valueOf(rows), String.valueOf(count)),
                "getBackgroundMusicList",
                SelectBgMusicFragment.this);

    }


    private class txListener implements View.OnClickListener {
        private int index = 0;


        public txListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {

            switch (index) {
                case 1:
                    typename = tab1_tv.getText().toString().trim();
                    break;
                case 2:
                    typename = tab2_tv.getText().toString().trim();
                    break;

                case 3:
                    typename = tab3_tv.getText().toString().trim();

                    break;
                case 4:

                    typename = tab4_tv.getText().toString().trim();
                    break;

            }
            selectedTab(index);
            cursor.setPosition(index - 1);
            count = 0;
            RestNetCallHelper.callNet(getActivity(),
                    MyNetApiConfig.getBackgroundMusicList,
                    MyNetRequestConfig.getBackgroundMusicList(getActivity()
                            , typename, String.valueOf(rows), String.valueOf(count)),
                    "getBackgroundMusicList",
                    SelectBgMusicFragment.this);

        }
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        super.onNetEnd(id, type, netResponse);

        if (id.equals("getBackgroundMusicList")) {
            if (type == TYPE_SUCCESS) {
                backgroudMusics = new Gson().fromJson(netResponse.data, new TypeToken<ArrayList<Music>>() {
                }.getType());
                tab3MusicAdapter.setData(backgroudMusics);
                if (backgroudMusics.size() == rows) {
                    mLoadMoreView.setMoreAble(true);
                }
                tab3MusicAdapter.setCurrent(-1);
                if (mPlayService!=null){
                    mPlayService.pause();
                }

                count += rows;
                resfreshOk();
            } else {
              //  tab3MusicAdapter.setData(null);
                resfreshFail();
            }
        } else if (id.equals("getBackgroundMusicList_more")) {
            if (TYPE_SUCCESS == type) {

                backgroudMusics = new Gson().fromJson(netResponse.data, new TypeToken<ArrayList<Music>>() {
                }.getType());
                tab3MusicAdapter.addData(backgroudMusics);
                if (backgroudMusics.size() < rows) {
                    mLoadMoreView.setMoreAble(false);
                }
                count += rows;
                mLoadMoreView.end();

            } else {
                mLoadMoreView.setMoreAble(false);
                mLoadMoreView.end();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        allowBindService(getActivity());
        tab3MusicAdapter.setOnMoreClickListener(new Tab3MusicAdapter.OnMoreClickListener() {
            @Override
            public void onMoreClick(Music music) {
                String url = MyNetApiConfig.ImageServerAddr + music.musicpath;
                mPlayService.play(url, music.mid);

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

    private void selectedTab(int i) {
        tab1_tv.setTextColor(getResources().getColor(R.color.black));
        tab2_tv.setTextColor(getResources().getColor(R.color.black));
        tab3_tv.setTextColor(getResources().getColor(R.color.black));
        tab4_tv.setTextColor(getResources().getColor(R.color.black));
        switch (i) {
            case 1:
                tab1_tv.setTextColor(getResources().getColor(R.color.redMain));
                break;
            case 2:
                tab2_tv.setTextColor(getResources().getColor(R.color.redMain));
                break;
            case 3:
                tab3_tv.setTextColor(getResources().getColor(R.color.redMain));
                break;
            case 4:
                tab4_tv.setTextColor(getResources().getColor(R.color.redMain));
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("配乐");

    }


    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("配乐");


    }

    @Override
    protected int getTitleBarType() {
        return FLAG_BACK | FLAG_TXT;
    }

    @Override
    protected boolean onPageBack() {
        return false;
    }

    @Override
    protected boolean onPageNext() {
        return false;
    }

    @Override
    protected void initTitle() {
        setTitleText("选择配乐");

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
