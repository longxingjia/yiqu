package com.yiqu.iyijiayi.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.jauker.widget.BadgeView;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.adapter.Tab1SoundAdapter;
import com.yiqu.iyijiayi.adapter.Tab1ViewPagerAdapter;
import com.yiqu.iyijiayi.adapter.Tab1XizuoAdapter;
import com.yiqu.iyijiayi.model.Remen;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.model.Xizuo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.view.VpSwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.Inflater;

public class Tab1Fragment extends TabContentFragment implements OnClickListener ,SwipeRefreshLayout.OnRefreshListener {

    private static final int TAB_1 = 1;
    //	List<>
//	private ArrayList<BookStyle> style = new ArrayList<BookStyle>();
    private BadgeView imageBadgeView;

    private ImageLoaderHm<ImageView> mImageLoaderHm;
    private ViewPager vp_spinner;
    private ImageView[] tips;
    /**
     * 装ImageView数组
     */
    private ArrayList<ImageView> mImageViews;
    /**
     * 图片资源id
     */
    private int[] imgIdArray;
    private ArrayList<View> views;
    private VpSwipeRefreshLayout vpSwipeRefreshLayout;
    private static final int REFRESH_COMPLETE = 0X110;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case REFRESH_COMPLETE:
//                    mDatas.addAll(Arrays.asList("Lucene", "Canvas", "Bitmap"));
//                    mAdapter.notifyDataSetChanged();
                    vpSwipeRefreshLayout.setRefreshing(false);
                    break;

            }
        };
    };
    private String uid;
    private ArrayList<Sound> sound;
    private ArrayList<Xizuo> xizuo;
    private ListView lvXizuo;
    private Tab1XizuoAdapter tab1XizuoAdapter;
    private ListView lvSound;
    private Tab1SoundAdapter tab1SoundAdapter;

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

        lvXizuo = (ListView) v.findViewById(R.id.lv_xizuo);
        lvSound = (ListView) v.findViewById(R.id.lv_sound);
        vp_spinner = (ViewPager) v.findViewById(R.id.vp_spinner);
        vpSwipeRefreshLayout = (VpSwipeRefreshLayout) v.findViewById(R.id.id_swipe_ly);

        mImageLoaderHm = new ImageLoaderHm<ImageView>(getActivity(),300);
        vpSwipeRefreshLayout.setOnRefreshListener(this);

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
        //此处可以根据需要自由设定，这里只是简单的测试
        for (int i = 1; i <= imgIdArray.length; i++) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.tab1_viewpage, null);
            views.add(view);
        }

        vp_spinner.setAdapter(new MyAdapter());
        vp_spinner.setCurrentItem(0);

        if (AppShare.getIsLogin(getActivity())){
            uid = AppShare.getUserInfo(getActivity()).uid;
        }else{
            uid = "0";
        }

        tab1XizuoAdapter = new Tab1XizuoAdapter(getActivity(),mImageLoaderHm);

        lvXizuo.setAdapter(tab1XizuoAdapter);

        tab1SoundAdapter = new Tab1SoundAdapter(getActivity(),mImageLoaderHm);
        lvSound.setAdapter(tab1SoundAdapter);

//        lvXizuo.setOnItemClickListener(tab1XizuoAdapter);
        RestNetCallHelper.callNet(
                getActivity(),
                MyNetApiConfig.remen,
                MyNetRequestConfig.remen(getActivity(),uid),
                "Remen", Tab1Fragment.this);
    }

    @Override
    public void onDestroy() {
        mImageLoaderHm.stop();
        super.onDestroy();
    }

    @Override
    protected int getTitleBarType() {
        return FLAG_ALL;
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
    public void onClick(View arg0) {

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

        if ("Remen".equals(id)) {

            if(netResponse.bool==1){
                Gson gson = new Gson();
                Remen remen = gson.fromJson(netResponse.data,Remen.class);
                sound = remen.sound;
                xizuo = remen.xizuo;

//                ArrayAdapter<Xizuo> adapter = new ArrayAdapter<Xizuo>(this,
//                        R.layout.remen_xizuo, new String[xizuo.]{},new int[]{R.id.musicname});

                tab1XizuoAdapter.setData(xizuo);
                tab1SoundAdapter.setData(sound);

            }else {
                ToastManager.getInstance(getActivity()).showText(netResponse.result);
            }


        }

        super.onNetEnd(id, type, netResponse);
    }



    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 2000);

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


}
