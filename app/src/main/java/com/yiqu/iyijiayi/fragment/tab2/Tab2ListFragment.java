package com.yiqu.iyijiayi.fragment.tab2;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;

import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ui.views.LoadMoreView;
import com.ui.views.RefreshList;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.Tab2ListFragmetAdapter;
import com.yiqu.iyijiayi.model.Tab2_groups;
import com.yiqu.iyijiayi.model.Teacher;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.JsonUtils;
import com.utils.LogUtils;

import org.json.JSONException;

import java.util.ArrayList;

public class Tab2ListFragment extends AbsAllFragment implements LoadMoreView.OnMoreListener, RefreshList.IRefreshListViewListener {


    private Tab2ListFragmetAdapter tab2ListFragmetAdapter;
    private String tag = "Tab2ListFragment";
    private ArrayList<Teacher> datas;

    //分页
    private LoadMoreView mLoadMoreView;
    private int count = 0;
    private int rows = 10;

    //刷新
    private RefreshList listView;
    private String uid;
    private String type;
    private Context mContext;
    private Tab2_groups tab2_groups;

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
        tab2_groups = (Tab2_groups) getActivity().getIntent().getSerializableExtra("data");
        listView = (RefreshList) v.findViewById(R.id.listView);
        mContext = getActivity();

        mLoadMoreView = (LoadMoreView) LayoutInflater.from(getActivity()).inflate(R.layout.list_footer, null);
        mLoadMoreView.setOnMoreListener(this);
//        listView.addFooterView(mLoadMoreView);
//        listView.setOnScrollListener(mLoadMoreView);
        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("老师学生列表");

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("老师学生列表");

    }

    @Override
    protected boolean isTouchMaskForNetting() {
        return false;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (AppShare.getIsLogin(getActivity())) {
            uid = AppShare.getUserInfo(getActivity()).uid;
        } else {
            uid = "0";
        }
        // LogUtils.LOGE(tag,uid+"");
//        RestNetCallHelper.callNet(getActivity(),
//                MyNetApiConfig.get_follow_recommend_list,
//                MyNetRequestConfig.get_follow_recommend_list(getActivity()
//                        , uid, type, count, rows),
//                "get_follow_recommend_list",
//                this, false, true);
        tab2ListFragmetAdapter = new Tab2ListFragmetAdapter(getActivity(), uid);
        listView.setAdapter(tab2ListFragmetAdapter);
        listView.setOnItemClickListener(tab2ListFragmetAdapter);
        //  listView.setRefreshListListener(this);




        RestNetCallHelper.callNet(
                mContext,
                MyNetApiConfig.getGroups,
                MyNetRequestConfig.getGroups(mContext, tab2_groups.id + "", uid, 0, 10),
                "getGroups", this, false, true);

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

        setTitleText(tab2_groups.group_name);


    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {


        if (id.equals("get_follow_recommend_list")) {
            if (type == NetCallBack.TYPE_SUCCESS) {

                try {
                    datas = JsonUtils.parseTeacherList(netResponse.data);
                    tab2ListFragmetAdapter.setData(datas);
                    if (datas.size() == rows) {
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
        } else if ("get_follow_recommend_list_more".equals(id)) {
            if (TYPE_SUCCESS == type) {

                try {
                    datas = JsonUtils.parseTeacherList(netResponse.data);
                    tab2ListFragmetAdapter.addData(datas);
                    if (datas.size() < rows) {
                        mLoadMoreView.setMoreAble(false);
                    }
                    count += rows;
                    mLoadMoreView.end();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                mLoadMoreView.setMoreAble(false);
                mLoadMoreView.end();
            }
        } else if (id.equals("getGroups")) {
            if (type == TYPE_SUCCESS) {

                ArrayList<Teacher> userInfos = new Gson().fromJson(netResponse.data, new TypeToken<ArrayList<Teacher>>() {
                }.getType());
                LogUtils.LOGE(tag,userInfos.toString());
                tab2ListFragmetAdapter.setData(userInfos);
            }

        }
        super.onNetEnd(id, type, netResponse);
    }

    @Override
    public void onRefresh() {

        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);
        count = 0;
//        RestNetCallHelper.callNet(getActivity(),
//                MyNetApiConfig.get_follow_recommend_list,
//                MyNetRequestConfig.get_follow_recommend_list(getActivity()
//                        , uid, type, count, rows),
//                "get_follow_recommend_list",
//                this, false, true);

    }

    @Override
    public boolean onMore(AbsListView view) {
        if (mLoadMoreView.getMoreAble()) {
            if (mLoadMoreView.isloading()) {
                // 正在加载中
            } else {
                mLoadMoreView.loading();

                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.get_follow_recommend_list,
                        MyNetRequestConfig.get_follow_recommend_list(getActivity()
                                , uid, type, count, rows),
                        "get_follow_recommend_list_more",
                        this, false, true);

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