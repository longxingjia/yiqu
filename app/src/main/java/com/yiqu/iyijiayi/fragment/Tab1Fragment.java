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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.jauker.widget.BadgeView;
import com.ui.views.LoadMoreView;
import com.ui.views.RefreshList;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.adapter.Tab1SoundAdapter;
import com.yiqu.iyijiayi.adapter.Tab1XizuoAdapter;
import com.yiqu.iyijiayi.fragment.tab1.Tab1XizuoListFragment;
import com.yiqu.iyijiayi.model.Remen;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.model.Xizuo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;
import com.yiqu.iyijiayi.view.VpSwipeRefreshLayout;
import java.util.ArrayList;

public class Tab1Fragment extends TabContentFragment implements LoadMoreView.OnMoreListener,OnClickListener ,RefreshList.IRefreshListViewListener {

    private static final int TAB_1 = 1;
    //	List<>

    private ViewPager vp_spinner;
    private LoadMoreView mLoadMoreView;
    /**
     * 图片资源id
     */
    private int[] imgIdArray;
    private ArrayList<View> views;
    private static final int REFRESH_COMPLETE = 0X110;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case REFRESH_COMPLETE:

                    break;

            }
        };
    };
    private String uid;
    private ArrayList<Sound> sound;
    private ArrayList<Xizuo> xizuo;
    private ListView lvXizuo;
    private Tab1XizuoAdapter tab1XizuoAdapter;
    private RefreshList lvSound;
    private Tab1SoundAdapter tab1SoundAdapter;
    private TextView more_xizuo;

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab1_fragment;
    }

    @Override
    protected void initView(View v) {
        View headerView =  LayoutInflater.from(getActivity()).inflate(R.layout.tab1_fragment_header, null);

        initHeadView(headerView);
        lvSound = (RefreshList) v.findViewById(R.id.lv_sound);
        mLoadMoreView = (LoadMoreView) LayoutInflater.from(getActivity()).inflate(R.layout.list_footer, null);
        mLoadMoreView.setOnMoreListener(this);
        lvSound.addFooterView(mLoadMoreView);
        lvSound.addHeaderView(headerView);
        lvSound.setOnScrollListener(mLoadMoreView);
        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);
        lvSound.setRefreshListListener(this);

    }

    private void initHeadView(View v){
        vp_spinner = (ViewPager) v.findViewById(R.id.vp_spinner);
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

        imgIdArray = new int[]{R.mipmap.banner_1, R.mipmap.banner_2};
        // 得到views集合
        views = new ArrayList<View>();

        for (int i = 1; i <= imgIdArray.length; i++) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.tab1_viewpage, null);
            views.add(view);
        }

        vp_spinner.setAdapter(new MyAdapter());
        vp_spinner.setCurrentItem(0);
        tab1XizuoAdapter = new Tab1XizuoAdapter(getActivity());
        lvXizuo.setAdapter(tab1XizuoAdapter);

        tab1SoundAdapter = new Tab1SoundAdapter(getActivity());
        lvSound.setAdapter(tab1SoundAdapter);
        lvSound.setOnItemClickListener(tab1SoundAdapter);
        lvXizuo.setOnItemClickListener(tab1XizuoAdapter);
        if (AppShare.getIsLogin(getActivity())){
            uid = AppShare.getUserInfo(getActivity()).uid;
        }else{
            uid = "0";
        }
        Remen remen = AppShare.getRemenList(getActivity());
        if (remen==null){
            RestNetCallHelper.callNet(
                    getActivity(),
                    MyNetApiConfig.remen,
                    MyNetRequestConfig.remen(getActivity(),uid),
                    "Remen", Tab1Fragment.this);
        }else {
            tab1XizuoAdapter.setData(remen.xizuo);
            tab1SoundAdapter.setData(remen.sound);

        }

    }

    @Override
    public void onDestroy() {

//        mImageLoaderHm.
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
        	setTitleText("热门");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.more_xizuo:
                Intent i = new Intent(getActivity(), StubActivity.class);
                i.putExtra("fragment", Tab1XizuoListFragment.class.getName());

                getActivity().startActivity(i);

                break;
        }

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

        if (netResponse!=null&&"Remen".equals(id)) {

            if(netResponse.bool==1){
                Gson gson = new Gson();
                Remen remen = gson.fromJson(netResponse.data,Remen.class);
                AppShare.setRemenList(getActivity(),remen);
                sound = remen.sound;
                xizuo = remen.xizuo;
                tab1XizuoAdapter.setData(xizuo);
                tab1SoundAdapter.setData(sound);
                mLoadMoreView.setMoreAble(false);
                resfreshOk();
            }else {
                ToastManager.getInstance(getActivity()).showText(netResponse.result);
                resfreshFail();
            }

        }

        super.onNetEnd(id, type, netResponse);
    }

    @Override
    public void onRefresh() {
//        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);
//        mImageLoaderHm.stop();
        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);

        RestNetCallHelper.callNet(
                getActivity(),
                MyNetApiConfig.remen,
                MyNetRequestConfig.remen(getActivity(),uid),
                "Remen", Tab1Fragment.this);

    }

    @Override
    public boolean onMore(AbsListView view) {

        if (mLoadMoreView.getMoreAble()) {
            if (mLoadMoreView.isloading()) {
                // 正在加载中
            } else {
                mLoadMoreView.loading();
//                page++;
//                RestNetCallHelper.callNet(getActivity(),
//                        MyNetApiConfig.findAllPushMsg,
//                        MyNetRequestConfig.findAllPushMsg(getActivity()
//                                , AppShare.getToken(getActivity())
//                                ,  AppShare.getPhone(getActivity())
//                                ,""+page
//                                ,""+PAGE_SIZE),
//                        "findAllPushMsg_more",
//                        this,false,true);

            }
        }


        return false;
    }

    // PagerAdapter是object的子类
    class MyAdapter extends PagerAdapter {

        /**
         * PagerAdapter管理数据大小
         */
        @Override
        public int getCount() {
            return views.size();
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

            View view = views.get(position);
            // 如果访问网络下载图片，此处可以进行异步加载
            ImageView img = (ImageView) view.findViewById(R.id.spinner);
//            img.setImageBitmap(BitmapFactory.decodeFile(dir + getFile(position)));
            img.setBackgroundResource(imgIdArray[position]);
            container.addView(view);
            return views.get(position); // 返回该view对象，作为key
        }
    }


    public void resfreshOk(){
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

    public void resfreshFail(){
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
