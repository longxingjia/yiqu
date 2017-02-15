package com.yiqu.iyijiayi.fragment.tab2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.ui.views.LoadMoreView;
import com.ui.views.RefreshList;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.Tab2ListFragmetAdapter;
import com.yiqu.iyijiayi.adapter.Tab2StudentAdapter;
import com.yiqu.iyijiayi.adapter.Tab2TeacherAdapter;
import com.yiqu.iyijiayi.fragment.TabContentFragment;
import com.yiqu.iyijiayi.model.Student;
import com.yiqu.iyijiayi.model.Teacher;
import com.yiqu.iyijiayi.model.ZhaoRen;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.view.ScrollViewWithListView;

import java.util.ArrayList;

public class Tab2ListFragment extends AbsAllFragment implements LoadMoreView.OnMoreListener, RefreshList.IRefreshListViewListener {

    private ImageLoaderHm<ImageView> mImageLoaderHm;
    Tab2ListFragmetAdapter tab2ListFragmetAdapter;
    private String tag = "Tab2ListFragment";

    //分页
    private LoadMoreView mLoadMoreView;
    private int count = 0;
    private int rows = 10;

    //刷新
    private RefreshList listView;
    private String uid;
    private String type;

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab2;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab2_list_fragment;
    }

    @Override
    protected void initView(View v) {
        listView = (RefreshList) v.findViewById(R.id.listView);
        mImageLoaderHm = new ImageLoaderHm<ImageView>(getActivity(), 300);
        tab2ListFragmetAdapter = new Tab2ListFragmetAdapter(getActivity(), mImageLoaderHm,AppShare.getUserInfo(getActivity()).uid);
        listView.setAdapter(tab2ListFragmetAdapter);
        listView.setOnItemClickListener(tab2ListFragmetAdapter);
        listView.setRefreshListListener(this);

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



    }

    @Override
    public void onDestroy() {
        mImageLoaderHm.stop();
        super.onDestroy();
    }

    @Override
    protected int getTitleBarType() {
        return FLAG_TXT;
    }

    @Override
    protected boolean onPageBack() {
//        if (mOnFragmentListener != null) {
//            mOnFragmentListener.onFragmentBack(this);
//        }
        return true;
    }

    @Override
    protected boolean onPageNext() {
        pageNextComplete();

        LogUtils.LOGE(tag,"1");
        return true;
    }

    @Override
    protected void initTitle() {
        type = getActivity().getIntent().getStringExtra("data");
        if (type.equals("2")){
            setTitleText("老师");
        }else {
            setTitleText("学生");
        }

        if (AppShare.getIsLogin(getActivity())) {
            uid = AppShare.getUserInfo(getActivity()).uid;
        } else {
            uid = "0";
        }

        RestNetCallHelper.callNet(getActivity(),
                MyNetApiConfig.get_follow_recommend_list,
                MyNetRequestConfig.get_follow_recommend_list(getActivity()
                        ,uid,type,count,rows),
                "list",
                this);
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        LogUtils.LOGE(tag,netResponse.toString());
//        if ("list".equals(id)) {
//            if (TYPE_SUCCESS == type) {
//                ArrayList<FindAllPushMsg> list = (ArrayList<FindAllPushMsg>) netResponse.body;
//
//                if (list.size() == PAGE_SIZE) {
//                    mLoadMoreView.setMoreAble(true);
//                }
//                resfreshOk();
//            } else {
//                resfreshFail();
//            }
//        } else if ("findAllPushMsg_more".equals(id)) {
//            if (TYPE_SUCCESS == type) {
//                // TODO Auto-generated method stub
//
//                ArrayList<FindAllPushMsg> list = (ArrayList<FindAllPushMsg>) netResponse.body;
//                mBeautifulAdapter.addData(list);
//                mLoadMoreView.end();
//
//                if (list.size() < PAGE_SIZE) {
//                    mLoadMoreView.setMoreAble(false);
//                }
//            }
//        }
        super.onNetEnd(id, type, netResponse);
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);
//        page = 1;
//
//        RestNetCallHelper.callNet(getActivity(),
//                MyNetApiConfig.findAllPushMsg,
//                MyNetRequestConfig.findAllPushMsg(getActivity()
//                        , AppShare.getToken(getActivity())
//                        , AppShare.getPhone(getActivity())
//                        , "" + page
//                        , "" + PAGE_SIZE),
//                "findAllPushMsg",
//                this, false, true);

    }

    @Override
    public boolean onMore(AbsListView view) {
        // TODO Auto-generated method stub
//        if (mLoadMoreView.getMoreAble()) {
//            if (mLoadMoreView.isloading()) {
//                // 正在加载中
//            } else {
//                mLoadMoreView.loading();
//                page++;
//                RestNetCallHelper.callNet(getActivity(),
//                        MyNetApiConfig.findAllPushMsg,
//                        MyNetRequestConfig.findAllPushMsg(getActivity()
//                                , AppShare.getToken(getActivity())
//                                , AppShare.getPhone(getActivity())
//                                , "" + page
//                                , "" + PAGE_SIZE),
//                        "findAllPushMsg_more",
//                        this, false, true);
//
//            }
//        }

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