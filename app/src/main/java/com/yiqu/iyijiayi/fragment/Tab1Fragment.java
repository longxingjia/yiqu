package com.yiqu.iyijiayi.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ui.views.AutoScrollViewPager;
import com.ui.views.LoadMoreView;
import com.ui.views.RefreshList;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.adapter.BannerAdapter;
import com.yiqu.iyijiayi.adapter.Tab1XizuoAdapter;
import com.yiqu.iyijiayi.fragment.tab1.SearchAllFragment;
import com.yiqu.iyijiayi.fragment.tab1.Tab1SoundListFragment;
import com.yiqu.iyijiayi.fragment.tab1.Tab1XizuoListFragment;
import com.yiqu.iyijiayi.fragment.tab4.EventFragment;
import com.yiqu.iyijiayi.model.Banner;
import com.yiqu.iyijiayi.model.Events;
import com.yiqu.iyijiayi.model.Like;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.NSDictionary;
import com.yiqu.iyijiayi.model.Remen;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.service.MusicService;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.NetWorkUtils;
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.iyijiayi.view.AutoPlayViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.ui.views.AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE;

public class Tab1Fragment extends TabContentFragment implements LoadMoreView.OnMoreListener, OnClickListener, RefreshList.IRefreshListViewListener {

    private static final int TAB_1 = 1;
    //	List<>

    private AutoPlayViewPager vp_spinner;
    private LoadMoreView mLoadMoreView;
    /**
     * 图片资源id
     */
    private int[] imgIdArray;
    private ArrayList<View> views;
    private static final int REFRESH_COMPLETE = 0X110;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:

                    break;

            }
        }

        ;
    };
    private String uid;
    private ArrayList<Sound> sound;
    private ArrayList<Sound> xizuo;

    //  private Tab1XizuoAdapter tab1XizuoAdapter;
    private RefreshList lvSound;
    private Tab1XizuoAdapter tab1XizuoAdapter;
    private TextView more_xizuo;
    private ArrayList<Like> likes;
    private int count = 0;
    private int rows = 10;
    private String arr;
    private Remen remen;
    private ArrayList<Banner> banners;
    private LinearLayout top_play;
    private ImageView play;
    private TextView musicplaying;
    private TextView authorName;
    private ImageView stop;
    private BannerAdapter bannerAdapter;
    private TextView question;
    private TextView raokouling;
    private TextView reader;


    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab1;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab1_fragment;
    }

    @Override
    protected void initView(View v) {
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.tab1_fragment_header, null);
        initHeadView(headerView);
        lvSound = (RefreshList) v.findViewById(R.id.lv_sound);
        mLoadMoreView = (LoadMoreView) LayoutInflater.from(getActivity()).inflate(R.layout.list_footer, null);
        mLoadMoreView.setOnMoreListener(this);
        lvSound.addFooterView(mLoadMoreView);
        lvSound.addHeaderView(headerView);
        lvSound.setOnScrollListener(mLoadMoreView);
        lvSound.setFooterDividersEnabled(false);
        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);
        lvSound.setRefreshListListener(this);

        //注册广播接收器
        MyReceiver receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.yiqu.iyijiayi.service.MusicService");
        getActivity().registerReceiver(receiver, filter);
    }

    /**
     * 获取广播数据
     *
     * @author jiqinlin
     */
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Bundle bundle=intent.getExtras();
//            int count=bundle.getInt("count");
//            editText.setText(count+"");
            //  LogUtils.LOGE("tag","fs");

            String data = intent.getStringExtra("data");
            switch (data) {
                case "stop":
                    top_play.setVisibility(View.GONE);
                    break;

                case "rePlay":
                    play.setImageResource(R.mipmap.play_banner);

                    break;
                case "pause":
                    play.setImageResource(R.mipmap.pause_banner);
                    break;
            }

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.home_page)); //统计页面，"MainScreen"为页面名称，可自定义

    }

    private Intent intent = new Intent();

    private void initHeadView(View v) {
        vp_spinner = (AutoPlayViewPager) v.findViewById(R.id.vp_spinner);
//        lvXizuo = (ListView) v.findViewById(R.id.lv_xizuo);
        more_xizuo = (TextView) v.findViewById(R.id.more_xizuo);
        musicplaying = (TextView) v.findViewById(R.id.musicname);
        authorName = (TextView) v.findViewById(R.id.name);
        question = (TextView) v.findViewById(R.id.question);
        raokouling = (TextView) v.findViewById(R.id.raokouling);
        reader = (TextView) v.findViewById(R.id.reader);
        top_play = (LinearLayout) v.findViewById(R.id.top_play);
        play = (ImageView) v.findViewById(R.id.play);
        stop = (ImageView) v.findViewById(R.id.stop);

        play.setOnClickListener(playListener);
        stop.setOnClickListener(stopListener);
        question.setOnClickListener(this);
        raokouling.setOnClickListener(this);
        reader.setOnClickListener(this);

        more_xizuo.setOnClickListener(this);
    }

    private OnClickListener playListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            intent.putExtra("choice", "pause");
            //  play.setImageResource(R.mipmap.play_icon);
            intent.setClass(getActivity(), MusicService.class);
            getActivity().startService(intent);
        }
    };

    private OnClickListener stopListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            intent.putExtra("choice", "stop");
            top_play.setVisibility(View.GONE);
            intent.setClass(getActivity(), MusicService.class);
            getActivity().startService(intent);
        }
    };

    @Override
    protected boolean isTouchMaskForNetting() {
        return false;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        bannerAdapter = new BannerAdapter(getActivity());

        banners = AppShare.getBannerList(getActivity());
        if (banners != null && banners.size() > 0) {
            bannerAdapter.update(banners);
            vp_spinner.setAdapter(bannerAdapter);
            vp_spinner.start();
        }

        //   tab1XizuoAdapter = new Tab1XizuoAdapter(getActivity(), top_play, play, musicplaying, authorName);
        //lvXizuo.setAdapter(tab1XizuoAdapter);
        likes = AppShare.getLikeList(getActivity());
        tab1XizuoAdapter = new Tab1XizuoAdapter(getActivity(),getClass().getSimpleName(), top_play, play, musicplaying, authorName);
        lvSound.setAdapter(tab1XizuoAdapter);
        lvSound.setOnItemClickListener(tab1XizuoAdapter);
        //  lvXizuo.setOnItemClickListener(tab1XizuoAdapter);

        NSDictionary nsDictionary = new NSDictionary();
        nsDictionary.isopen = "1";
        nsDictionary.ispay = "1";
        nsDictionary.isreply = "1";
        nsDictionary.status = "1";
        nsDictionary.stype = "2";
        Gson gson = new Gson();
        arr = gson.toJson(nsDictionary);

        if (AppShare.getIsLogin(getActivity())) {
            uid = AppShare.getUserInfo(getActivity()).uid;
            likes = AppShare.getLikeList(getActivity());
        } else {
            uid = "0";
        }

//        if (!NetWorkUtils.isNetworkAvailable(getActivity())) {
//            Remen remen = AppShare.getRemenList(getActivity());
//            if (remen != null) {
//                //tab1XizuoAdapter.setData(remen.xizuo);
//                tab1XizuoAdapter.setData(remen.sound);
//            }
//        } else {
//            count = 0;
////            RestNetCallHelper.callNet(
////                    getActivity(),
////                    MyNetApiConfig.remen,
////                    MyNetRequestConfig.remen(getActivity(), uid),
////                    "Remen", Tab1Fragment.this, false, true);
//
//
//
//        }
        count = 0;
        RestNetCallHelper.callNet(
                getActivity(),
                MyNetApiConfig.getSoundList,
                MyNetRequestConfig.getSoundList(getActivity(), arr, count, rows, "edited", "desc"),
                "getSoundList", Tab1Fragment.this);

        RestNetCallHelper.callNet(
                getActivity(),
                MyNetApiConfig.getBannerList,
                MyNetRequestConfig.getBannerList(getActivity()),
                "getBannerList", Tab1Fragment.this, false, true);

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
        Model.startNextAct(getActivity(),
                SearchAllFragment.class.getName());
        return true;
    }

    @Override
    protected boolean onPageNext() {
        // pageNextComplete();

        return true;
    }

    @Override
    protected void initTitle() {
        setTitleText(getString(R.string.home_page));
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(getActivity(), StubActivity.class);
        switch (v.getId()) {
            case R.id.more_xizuo:
                //    Intent i = new Intent(getActivity(), StubActivity.class);
                i.putExtra("fragment", Tab1XizuoListFragment.class.getName());
                getActivity().startActivity(i);

                break;
            case R.id.question:

                i.putExtra("fragment", Tab1SoundListFragment.class.getName());
                getActivity().startActivity(i);

                break;
            case R.id.raokouling:
                Events events = new Events();
                events.id =1;
                events.title ="挑战绕口令";

                i.putExtra("fragment", EventFragment.class.getName());
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", events);
                i.putExtras(bundle);
                getActivity().startActivity(i);

                break;
            case R.id.reader:
                Events e = new Events();
                e.id =2;
                e.title ="为你读诗";

                i.putExtra("fragment", EventFragment.class.getName());
                Bundle b = new Bundle();
                b.putSerializable("data", e);
                i.putExtras(b);
                getActivity().startActivity(i);

                break;
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.home_page));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
//                Bundle b=data.getExtras(); //data为B中回传的Intent
//                String str=b.getString("str1");//str即为回传的值

//                String sid = data.getExtras().getString("data");
//                LogUtils.LOGE("tab1f",Integer.parseInt(sid)+"");
//                int position = data.getIntExtra("position", -1);

//                LogUtils.LOGE("s",position+"");
//                Sound s = sound.get(position);
//                LogUtils.LOGE("1",s.toString());
//                s.listen = 1;
//                sound.remove(position);
//                sound.add(position,s);
//
//                LogUtils.LOGE("tab1f", sound.toString());

//                tab1XizuoAdapter.updateSingleRow(lvSound, 107);

                break;
            default:
                break;
        }
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

        //  LogUtils.LOGE("2",netResponse.toString());
        if (netResponse != null && "getSoundList".equals(id)) {
            if (netResponse.bool == 1) {

                sound = new Gson().fromJson(netResponse.data, new TypeToken<ArrayList<Sound>>() {
                }.getType());
                //tab1XizuoAdapter.setData(xizuo);
                tab1XizuoAdapter.setData(sound);

                if (sound.size() == rows) {
                    mLoadMoreView.setMoreAble(true);
                }
                count += rows;
                resfreshOk();
            } else {
                ToastManager.getInstance(getActivity()).showText(netResponse.result);
                resfreshFail();
            }

        } else if (id.equals("getSoundList_more")) {


            if (type == TYPE_SUCCESS) {
                sound = new Gson().fromJson(netResponse.data, new TypeToken<ArrayList<Sound>>() {
                }.getType());

                //  remen.sound.addAll(sound);
                //    AppShare.setRemenList(getActivity(), remen);
//                tab1XizuoAdapter.setData(sound);
                tab1XizuoAdapter.addData(sound);
                if (sound.size() < rows) {
                    mLoadMoreView.setMoreAble(false);
                }
                count += rows;
                mLoadMoreView.end();

            } else {
                mLoadMoreView.setMoreAble(false);
                mLoadMoreView.end();
            }
        } else if (id.equals("getBannerList")) {
            if (type == TYPE_SUCCESS) {
                banners = new Gson()
                        .fromJson(netResponse.data, new TypeToken<ArrayList<Banner>>() {
                        }.getType());
                AppShare.setBannerList(getActivity(), banners);

                bannerAdapter.update(banners);
                //    LogUtils.LOGE("tag",banners.toString());
                vp_spinner.setAdapter(bannerAdapter);

                vp_spinner.setDirection(AutoPlayViewPager.Direction.LEFT);// 设置播放方向
                vp_spinner.setCurrentItem(200); // 设置每个Item展示的时间
                vp_spinner.start(); // 开始轮播
                vp_spinner.setAdapter(bannerAdapter);

            }
            //    LogUtils.LOGE("tab1", netResponse.toString());
        }

        super.onNetEnd(id, type, netResponse);
    }


    @Override
    public void onRefresh() {
//        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
//        mImageLoaderHm.stop();
        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);
        count = 0;
        RestNetCallHelper.callNet(
                getActivity(),
                MyNetApiConfig.getSoundList,
                MyNetRequestConfig.getSoundList(getActivity(), arr, count, rows, "edited", "desc"),
                "getSoundList", Tab1Fragment.this);


//        RestNetCallHelper.callNet(
//                getActivity(),
//                MyNetApiConfig.remen,
//                MyNetRequestConfig.remen(getActivity(), uid),
//                "Remen", Tab1Fragment.this, false, true);

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
                        "getSoundList_more", Tab1Fragment.this, false, true);

            }
        }


        return false;
    }


    public void resfreshOk() {
        lvSound.refreshOk();
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
                lvSound.stopRefresh();
            }


        }.execute();

    }

    public void resfreshFail() {
        lvSound.refreshFail();
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
                lvSound.stopRefresh();
            }


        }.execute();
    }


}
