package com.yiqu.iyijiayi.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ui.views.LoadMoreView;
import com.ui.views.RefreshList;
import com.umeng.analytics.MobclickAgent;
import com.utils.L;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.adapter.BannerAdapter;
import com.yiqu.iyijiayi.adapter.Tab1XizuoAdapter;
import com.yiqu.iyijiayi.adapter.Tab1XizuoAdapterTest;
import com.yiqu.iyijiayi.fragment.tab1.SearchAllFragment;
import com.yiqu.iyijiayi.fragment.tab1.Tab1SoundListFragment;
import com.yiqu.iyijiayi.fragment.tab1.Tab1XizuoListFragment;
import com.yiqu.iyijiayi.fragment.tab4.EventFragment;
import com.yiqu.iyijiayi.model.Banner;
import com.yiqu.iyijiayi.model.Events;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.NSDictionary;
import com.yiqu.iyijiayi.model.Remen;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.view.AutoPlayViewPager;

import java.util.ArrayList;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import static android.app.Activity.RESULT_OK;

public class Tab1Fragment extends TabContentFragment implements LoadMoreView.OnMoreListener, OnClickListener, RefreshList.IRefreshListViewListener {

    private static final int TAB_1 = 1;
    private static final String tag = Tab1Fragment.class.getSimpleName();
    //	List<>

    private AutoPlayViewPager vp_spinner;
    private LoadMoreView mLoadMoreView;
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
    private ArrayList<Sound> zuoping;

    private RefreshList lvSound;
    private Tab1XizuoAdapter tab1XizuoAdapter;
    private TextView more_xizuo;
    private int count = 0;
    private int rows = 10;
    //    private String arr;
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
    private ImageView[] imageViews;
    private LinearLayout mViewPoints;


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
        tab1XizuoAdapter = new Tab1XizuoAdapter(getActivity(), getClass().getSimpleName());
        lvSound.setAdapter(tab1XizuoAdapter);
        lvSound.setOnItemClickListener(tab1XizuoAdapter);
        lvSound.addHeaderView(headerView);
        lvSound.setOnScrollListener(mLoadMoreView);
        lvSound.setFooterDividersEnabled(false);
        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);
        lvSound.setRefreshListListener(this);
        top_play = (LinearLayout) v.findViewById(R.id.top_play);
        play = (ImageView) v.findViewById(R.id.play);
        stop = (ImageView) v.findViewById(R.id.stop);
        musicplaying = (TextView) v.findViewById(R.id.musicname);
        authorName = (TextView) v.findViewById(R.id.name);
        play.setOnClickListener(playListener);
        stop.setOnClickListener(stopListener);

    }

    private void showShare() {
        ShareSDK.initSDK(getActivity());
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("艺加艺");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("这个app 真好用，快来试用下吧");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://www.hunanyiqu.com");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        // oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        //   oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://www.hunanyiqu.com");
        oks.setImageUrl("http://www.hunanyiqu.com");
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
                if ("QZone".equals(platform.getName())) {
                    paramsToShare.setTitle(null);
                    paramsToShare.setTitleUrl(null);
                }
                if ("QQ".equals(platform.getName())) {
                    Bitmap imageData = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    paramsToShare.setImageData(imageData);
                }

                if ("SinaWeibo".equals(platform.getName())) {
                    paramsToShare.setUrl("http://www.hunanyiqu.com");
                    paramsToShare.setText("分享 http://www.hunanyiqu.com");
                }
                if ("Wechat".equals(platform.getName())) {
                    Bitmap imageData = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    paramsToShare.setImageData(imageData);
                }
                if ("WechatMoments".equals(platform.getName())) {
                    Bitmap imageData = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    paramsToShare.setImageData(imageData);
                }

            }
        });

// 启动分享GUI
        oks.show(getActivity());
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.home_page)); //统计页面，"MainScreen"为页面名称，可自定义
        JAnalyticsInterface.onPageStart(getActivity(), getString(R.string.home_page));
        if (AppShare.getIsLogin(getActivity())) {
            uid = AppShare.getUserInfo(getActivity()).uid;
        } else {
            uid = "0";
        }

   //     onRefresh();

        if (mPlayService != null && mPlayService.isPlaying()) {

            int sid = mPlayService.getPlayingPosition();
            if (sid > 0) {
                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.getSoundDetail, MyNetRequestConfig
                                .getSoundDetail(getActivity(), String.valueOf(sid), "0"),
                        "getSoundDetail", Tab1Fragment.this);

            }
        }

    }

    private Intent intent = new Intent();

    private void initHeadView(View v) {
        vp_spinner = (AutoPlayViewPager) v.findViewById(R.id.vp_spinner);
        more_xizuo = (TextView) v.findViewById(R.id.more_xizuo);
        question = (TextView) v.findViewById(R.id.question);
        raokouling = (TextView) v.findViewById(R.id.raokouling);
        reader = (TextView) v.findViewById(R.id.reader);
        mViewPoints = (LinearLayout) v.findViewById(R.id.viewGroup);
        question.setOnClickListener(this);
        raokouling.setOnClickListener(this);
        reader.setOnClickListener(this);

        more_xizuo.setOnClickListener(this);

    }

    private OnClickListener playListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            if (mPlayService != null) {
                if (mPlayService.isPlaying()) {
                    mPlayService.pause();
                    play.setImageResource(R.mipmap.pause_banner);
                } else {
                    mPlayService.resume();
//                    play.setImageResource(R.mipmap.pause_banner);
                    play.setImageResource(R.mipmap.play_banner);
                }
            }


        }
    };

    private OnClickListener stopListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            top_play.setVisibility(View.GONE);
            if (mPlayService != null) {
                mPlayService.pause();
            }
            tab1XizuoAdapter.setCurrent(-1);

        }
    };

    @Override
    public void onStart() {
        super.onStart();

        allowBindService(getActivity());

        tab1XizuoAdapter.setOnMoreClickListener(new Tab1XizuoAdapter.OnMoreClickListener() {
            @Override
            public void onMoreClick(int position) {
                String url = MyNetApiConfig.ImageServerAddr + zuoping.get(position).soundpath;
                mPlayService.play(url, zuoping.get(position).sid);
                initTopPlayUI(zuoping.get(position));
            }

        });


    }

    private void initTopPlayUI(Sound sound) {
        top_play.setVisibility(View.VISIBLE);
        play.setImageResource(R.mipmap.play_banner);
        musicplaying.setText(sound.musicname);
        authorName.setText(sound.stuname);
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

//        NSDictionary nsDictionary = new NSDictionary();
//        nsDictionary.isopen = "1";
////        nsDictionary.ispay = "1";
////        nsDictionary.isreply = "1";
//        nsDictionary.status = "1";
//        nsDictionary.stype = "2";
//        Gson gson = new Gson();
//        arr = gson.toJson(nsDictionary);

        if (AppShare.getIsLogin(getActivity())) {
            uid = AppShare.getUserInfo(getActivity()).uid;
        } else {
            uid = "0";
        }

        onRefresh();

        RestNetCallHelper.callNet(
                getActivity(),
                MyNetApiConfig.getBannerList,
                MyNetRequestConfig.getBannerList(getActivity()),
                "getBannerList", Tab1Fragment.this, false, true);


    }

    //设置导航点的状态
    private void setPointStatus(int position) {
        position = position % banners.size();
        for (int i = 0; i < imageViews.length; i++) {
//            imageViews[position] .setBackgroundResource(R.mipmap.play_icon);
            imageViews[position].setBackgroundResource(R.drawable.oval_red_stroke);
            // 不是当前选中的page，其小圆点设置为未选中的状态
            if (position != i) {
//                imageViews[i] .setBackgroundResource(R.mipmap.play_icon);
                imageViews[i].setBackgroundResource(R.drawable.oval_gray_stroke);
            }
        }
    }

    //创建viewpager的那几个滑动的点
    private void initPoint() {
        // 创建imageviews数组，大小是要显示的图片的数量
        imageViews = new ImageView[banners.size()];
        // 添加小圆点的图片
        for (int i = 0; i < banners.size(); i++) {
            ImageView imageView = new ImageView(getActivity());
            // 设置小圆点imageview的参数
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    18, 18);
            layoutParams.setMargins(10, 0, 10, 0);
            imageView.setLayoutParams(layoutParams);// 创建一个宽高均为20 的布局
            // 将小圆点layout添加到数组中
            imageViews[i] = imageView;
            // 默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
            if (i == 0) {
                imageViews[i].setBackgroundResource(R.drawable.oval_red_stroke);
//                imageViews[i].setBackgroundResource(R.drawable.oval_red_stroke);
            } else {
                imageViews[i].setBackgroundResource(R.drawable.oval_gray_stroke);
//                imageViews[i].setBackgroundResource(R.mipmap.play_icon);
            }

            // 将imageviews添加到小圆点视图组
            mViewPoints.addView(imageViews[i]);
        }
        // 设置viewpager的适配器和监听事件

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected int getTitleBarType() {
        return FLAG_ALL;
    }

    @Override
    protected boolean onPageBack() {
        Model.startNextAct(getActivity(),
                SearchAllFragment.class.getName());
        return true;
    }

    @Override
    protected boolean onPageNext() {
//        setTitleBtnImg(R.mipmap.edit_tab);
        showShare();
        return true;
    }

    @Override
    protected void initTitle() {
        setTitleBtnImg(R.mipmap.share_icon);
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
                events.id = 1;
                events.title = "挑战绕口令";

                i.putExtra("fragment", EventFragment.class.getName());
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", events);
                i.putExtras(bundle);
                getActivity().startActivity(i);

                break;
            case R.id.reader:
                Events e = new Events();
                e.id = 2;
                e.title = "为你朗诵";
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
        JAnalyticsInterface.onPageEnd(getActivity(), getString(R.string.home_page));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:

                break;
            default:
                break;
        }
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
             L.e(netResponse.toString());
        if (netResponse != null && "getHomeData".equals(id)) {
            if (netResponse.bool == 1) {
                ArrayList<Sound> s = new Gson().fromJson(netResponse.data, new TypeToken<ArrayList<Sound>>() {
                }.getType());
                tab1XizuoAdapter.setData(s);
                zuoping = s;

                if (s.size() == rows) {
                    mLoadMoreView.setMoreAble(true);
                }
                count += rows;
                resfreshOk();
            } else {
                ToastManager.getInstance(getActivity()).showText(netResponse.result);
                resfreshFail();
            }

        } else if (id.equals("getHomeData_more")) {

            if (type == TYPE_SUCCESS) {
                ArrayList<Sound> s = new Gson().fromJson(netResponse.data, new TypeToken<ArrayList<Sound>>() {
                }.getType());
                zuoping.addAll(s);
                //  tab1XizuoAdapter.addData(s);
                if (s.size() < rows) {
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
                vp_spinner.setAdapter(bannerAdapter);

                vp_spinner.setDirection(AutoPlayViewPager.Direction.LEFT);// 设置播放方向
                vp_spinner.setCurrentItem(200); // 设置每个Item展示的时间
                vp_spinner.start(); // 开始轮播
                vp_spinner.setAdapter(bannerAdapter);
                initPoint();
                vp_spinner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        setPointStatus(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }
            //    LogUtils.LOGE("tab1", netResponse.toString());
        } else if (id.equals("getSoundDetail")) {
            if (type == NetCallBack.TYPE_SUCCESS) {
                Gson gson = new Gson();
                Sound sound = gson.fromJson(netResponse.data, Sound.class);
                initTopPlayUI(sound);

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
                MyNetApiConfig.getHomeData,
                MyNetRequestConfig.getHomeData(getActivity(), uid, count, rows, "created"),
                "getHomeData", Tab1Fragment.this);

    }

    @Override
    public boolean onMore(AbsListView view) {

        if (mLoadMoreView.getMoreAble()) {
            if (mLoadMoreView.isloading()) {
                // 正在加载中
            } else {
                mLoadMoreView.loading();
//                RestNetCallHelper.callNet(
//                        getActivity(),
//                        MyNetApiConfig.getSoundList,
//                        MyNetRequestConfig.getSoundList(getActivity(), arr, count, rows, "created", "asc", uid),
//                        "getSoundList_more", Tab1Fragment.this, false, true);
                RestNetCallHelper.callNet(
                        getActivity(),
                        MyNetApiConfig.getHomeData,
                        MyNetRequestConfig.getHomeData(getActivity(), uid, count, rows, "created"),
                        "getHomeData_more", Tab1Fragment.this);

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
