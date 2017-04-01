package com.yiqu.iyijiayi.fragment;


import android.content.Intent;
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
import com.yiqu.iyijiayi.adapter.Tab1SoundAdapter;
import com.yiqu.iyijiayi.adapter.Tab1XizuoAdapter;
import com.yiqu.iyijiayi.fragment.tab1.SearchAllFragment;
import com.yiqu.iyijiayi.fragment.tab1.Tab1XizuoListFragment;
import com.yiqu.iyijiayi.model.Banner;
import com.yiqu.iyijiayi.model.Like;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.NSDictionary;
import com.yiqu.iyijiayi.model.Remen;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.model.Xizuo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.NetWorkUtils;
import com.yiqu.iyijiayi.utils.PictureUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.ui.views.AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE;

public class Tab1Fragment extends TabContentFragment implements LoadMoreView.OnMoreListener, OnClickListener, RefreshList.IRefreshListViewListener {

    private static final int TAB_1 = 1;
    //	List<>

    private AutoScrollViewPager vp_spinner;
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
    private ArrayList<Xizuo> xizuo;
    private ListView lvXizuo;
    private Tab1XizuoAdapter tab1XizuoAdapter;
    private RefreshList lvSound;
    private Tab1SoundAdapter tab1SoundAdapter;
    private TextView more_xizuo;
    private ArrayList<Like> likes;
    private int count = 0;
    private int rows = 10;
    private String arr;
    private Remen remen;
    private ArrayList<Banner> banners;



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
        //   lvSound.setHeaderDividersEnabled(false);
        lvSound.setFooterDividersEnabled(false);
        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);
        lvSound.setRefreshListListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("热门"); //统计页面，"MainScreen"为页面名称，可自定义

    }

    private void initHeadView(View v) {
        vp_spinner = (AutoScrollViewPager) v.findViewById(R.id.vp_spinner);
        lvXizuo = (ListView) v.findViewById(R.id.lv_xizuo);
        more_xizuo = (TextView) v.findViewById(R.id.more_xizuo);
        more_xizuo.setOnClickListener(this);
    }

    @Override
    protected boolean isTouchMaskForNetting() {
        return false;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setSlidingMenuEnable(false);


//        imgIdArray = new int[]{R.mipmap.banner_1, R.mipmap.banner_2};
//        // 得到views集合
//        views = new ArrayList<View>();
//
//        for (int i = 1; i <= imgIdArray.length; i++) {
//            View view = getActivity().getLayoutInflater().inflate(R.layout.tab1_viewpage, null);
//            views.add(view);
//        }
        banners = AppShare.getBannerList(getActivity());
        if (banners !=null && banners.size()>0){
            List<Map<String, Object>> data = getData(banners);
            vp_spinner.setAdapter(new MyAdapter(data));
        }

        tab1XizuoAdapter = new Tab1XizuoAdapter(getActivity());
        lvXizuo.setAdapter(tab1XizuoAdapter);
        likes = AppShare.getLikeList(getActivity());
        tab1SoundAdapter = new Tab1SoundAdapter(this,likes);
        lvSound.setAdapter(tab1SoundAdapter);
        lvSound.setOnItemClickListener(tab1SoundAdapter);
        lvXizuo.setOnItemClickListener(tab1XizuoAdapter);

        NSDictionary nsDictionary = new NSDictionary();
        nsDictionary.isopen = "1";
        nsDictionary.ispay = "1";
        nsDictionary.isreply = "1";
        nsDictionary.status = "1";
        nsDictionary.stype = "1";
        Gson gson = new Gson();
        arr = gson.toJson(nsDictionary);

        if (AppShare.getIsLogin(getActivity())) {
            uid = AppShare.getUserInfo(getActivity()).uid;
            likes = AppShare.getLikeList(getActivity());
        } else {
            uid = "0";
        }

        if (!NetWorkUtils.isNetworkAvailable(getActivity())) {
            Remen remen = AppShare.getRemenList(getActivity());
            if (remen != null) {
                tab1XizuoAdapter.setData(remen.xizuo);
                tab1SoundAdapter.setData(remen.sound);
            }
        } else {
            count = 0;
            RestNetCallHelper.callNet(
                    getActivity(),
                    MyNetApiConfig.remen,
                    MyNetRequestConfig.remen(getActivity(), uid),
                    "Remen", Tab1Fragment.this, false, true);


            RestNetCallHelper.callNet(
                    getActivity(),
                    MyNetApiConfig.getBannerList,
                    MyNetRequestConfig.getBannerList(getActivity()),
                    "getBannerList", Tab1Fragment.this, false, true);
        }


    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected int getTitleBarType() {
        return FLAG_TXT | FLAG_BTN;
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
        // pageNextComplete();
        Model.startNextAct(getActivity(),
                SearchAllFragment.class.getName());
        return true;
    }

    @Override
    protected void initTitle() {
        setTitleText("热门");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_xizuo:
                Intent i = new Intent(getActivity(), StubActivity.class);
                i.putExtra("fragment", Tab1XizuoListFragment.class.getName());
                getActivity().startActivity(i);

                break;
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("热门");
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

//                tab1SoundAdapter.updateSingleRow(lvSound, 107);

                break;
            default:
                break;
        }
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

      //  LogUtils.LOGE("2",netResponse.toString());
        if (netResponse != null && "Remen".equals(id)) {
            if (netResponse.bool == 1) {
                Gson gson = new Gson();
                remen = gson.fromJson(netResponse.data, Remen.class);
                AppShare.setRemenList(getActivity(), remen);
                sound = remen.sound;
                xizuo = remen.xizuo;
                tab1XizuoAdapter.setData(xizuo);
                tab1SoundAdapter.setData(sound);

                if (sound.size() == rows) {
                    mLoadMoreView.setMoreAble(true);
                }
                count += rows;
                resfreshOk();
            } else {
                ToastManager.getInstance(getActivity()).showText(netResponse.result);
                resfreshFail();
            }

        } else if (id.equals("getSoundList")) {


            if (type == TYPE_SUCCESS) {
                sound = new Gson().fromJson(netResponse.data, new TypeToken<ArrayList<Sound>>() {
                }.getType());

              //  remen.sound.addAll(sound);
            //    AppShare.setRemenList(getActivity(), remen);
//                tab1SoundAdapter.setData(sound);
                tab1SoundAdapter.addData(sound);
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

                List<Map<String, Object>> data = getData(banners);
                vp_spinner.setAdapter(new MyAdapter(data));
            }
            //    LogUtils.LOGE("tab1", netResponse.toString());
        }

        super.onNetEnd(id, type, netResponse);
    }

    public List<Map<String, Object>> getData(ArrayList<Banner> banners) {
        List<Map<String, Object>> mdata = new ArrayList<Map<String, Object>>();
        for (Banner banner : banners) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("url", banner.image);
            map.put("view", new ImageView(getActivity()));
            mdata.add(map);
        }
        vp_spinner.startAutoScroll();
       // vp_spinner.setCycle(false);
        vp_spinner.setSlideBorderMode(SLIDE_BORDER_MODE_CYCLE);
        return mdata;
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
                MyNetApiConfig.remen,
                MyNetRequestConfig.remen(getActivity(), uid),
                "Remen", Tab1Fragment.this, false, true);

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
                        MyNetRequestConfig.getSoundList(getActivity(), arr, count, rows, "edited", "asc"),
                        "getSoundList", Tab1Fragment.this, false, true);

            }
        }


        return false;
    }

    // PagerAdapter是object的子类
    class MyAdapter extends PagerAdapter {

        List<Map<String, Object>> viewLists;

        public MyAdapter(List<Map<String, Object>> viewLists) {
            this.viewLists = viewLists;
        }

        /**
         * PagerAdapter管理数据大小
         */
        @Override
        public int getCount() {
            return viewLists.size();
        }

        /**
         * 关联key 与 obj是否相等，即是否为同一个对象
         */
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj; // key
        }

        /**
         * 销毁当前page的相隔2个及2个以上的item时调用
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object); // 将view 类型 的object熊容器中移除,根据key
        }

        /**
         * 当前的page的前一页和后一页也会被调用，如果还没有调用或者已经调用了destroyItem
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

//            View view = viewLists.get(position);
//            // 如果访问网络下载图片，此处可以进行异步加载
//            ImageView img = (ImageView) view.findViewById(R.id.spinner);
////            img.setImageBitmap(BitmapFactory.decodeFile(dir + getFile(position)));
//            img.setBackgroundResource(imgIdArray[position]);
//            container.addView(view);
//            return viewLists.get(position); // 返回该view对象，作为key
            ImageView x = (ImageView) viewLists.get(position).get("view");
            x.setScaleType(ImageView.ScaleType.FIT_CENTER);
            //   imageLoader.displayImage(viewLists.get(position).get("url").toString(), x,options);
            PictureUtils.showBannersPicture(getActivity(), viewLists.get(position).get("url").toString(), x);
            ((ViewPager) container).addView(x, 0);

            return viewLists.get(position).get("view");

        }

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
