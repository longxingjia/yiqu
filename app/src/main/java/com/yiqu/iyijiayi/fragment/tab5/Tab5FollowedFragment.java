package com.yiqu.iyijiayi.fragment.tab5;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ui.views.LoadMoreView;
import com.ui.views.RefreshList;
import com.umeng.analytics.MobclickAgent;
import com.utils.L;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.Tab5GuanzhuAdapter;
import com.yiqu.iyijiayi.fragment.tab4.RenewFragment;
import com.yiqu.iyijiayi.model.Student;
import com.yiqu.iyijiayi.model.Teacher;
import com.yiqu.iyijiayi.model.ZhaoRen;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.view.ScrollViewWithListView;

import java.util.ArrayList;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;

public class Tab5FollowedFragment extends AbsAllFragment implements
        RefreshList.IRefreshListViewListener, LoadMoreView.OnMoreListener, View.OnClickListener {

    private String tag = "Tab5FollowedFragment";
    private String uid;
    private ScrollViewWithListView lvTeacher;

    private Tab5GuanzhuAdapter tab2StudentAdapter;
    private RefreshList lvStudent;
    private LoadMoreView mLoadMoreView;
    private int count = 0;
    private int rows = 10;
    private TextView loadmore_student;
    private TextView loadmore_teacher;
    private int teacher = 1;
    private ArrayList<Teacher> teachers;

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab5_followed_fragment;
    }

    @Override
    protected void initView(View v) {

        lvTeacher = (ScrollViewWithListView) v.findViewById(R.id.lv_teacher);
        lvStudent = (RefreshList) v.findViewById(R.id.lv_student);
        loadmore_student = (TextView) v.findViewById(R.id.loadmore_student);
        loadmore_teacher = (TextView) v.findViewById(R.id.loadmore_teacher);
        uid = AppShare.getUserInfo(getActivity()).uid;
        tab2StudentAdapter = new Tab5GuanzhuAdapter(getActivity(), uid);
        lvStudent.setAdapter(tab2StudentAdapter);
        lvStudent.setOnItemClickListener(tab2StudentAdapter);

        mLoadMoreView = (LoadMoreView) LayoutInflater.from(getActivity()).inflate(R.layout.list_footer, null);
        mLoadMoreView.setOnMoreListener(this);
        lvStudent.addFooterView(mLoadMoreView);
        lvStudent.setOnScrollListener(mLoadMoreView);
        lvStudent.setFooterDividersEnabled(false);
        lvStudent.setHeaderDividersEnabled(false);
        lvStudent.setRefreshListListener(this);
        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);
        count = 0;
        onRefresh();
    }

    @Override
    protected boolean isTouchMaskForNetting() {
        return false;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        loadmore_teacher.setOnClickListener(this);
        loadmore_student.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("关注"); //统计页面，"MainScreen"为页面名称，可自定义
        JAnalyticsInterface.onPageStart(getActivity(),"关注");
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("关注");
        JAnalyticsInterface.onPageEnd(getActivity(),"关注");
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        L.e(netResponse.toString());
        if (id.equals("teacher")) {
            if (type == TYPE_SUCCESS) {
                ArrayList<Teacher> teachers = new Gson().fromJson(netResponse.data, new TypeToken<ArrayList<Teacher>>() {
                }.getType());
                tab2StudentAdapter.setData(teachers);
                if (teachers.size() == rows) {
                    mLoadMoreView.setMoreAble(true);
                }

                count += rows;
                resfreshOk();
            } else {
                resfreshFail();
            }

        } else if (id.equals("teacher_more")) {
            if (TYPE_SUCCESS == type) {

                ArrayList<Teacher> teachers = new Gson().fromJson(netResponse.data, new TypeToken<ArrayList<Teacher>>() {
                }.getType());
                tab2StudentAdapter.addData(teachers);
                if (teachers.size() < rows) {
                    mLoadMoreView.setMoreAble(false);
                }
                count += rows;
                mLoadMoreView.end();

            } else {
                mLoadMoreView.setMoreAble(false);
                mLoadMoreView.end();
            }
        } else if (id.equals("student")) {
            if (type == TYPE_SUCCESS) {
                Gson gson = new Gson();
                ArrayList<Teacher> student = gson.fromJson(netResponse.data, new TypeToken<ArrayList<Teacher>>() {
                }.getType());
                tab2StudentAdapter.setData(student);
                if (student.size() == rows) {
                    mLoadMoreView.setMoreAble(true);
                }
                count += rows;
                resfreshOk();
            } else {
                resfreshFail();
            }

        } else if (id.equals("student_more")) {
            if (type == TYPE_SUCCESS) {
                Gson gson = new Gson();
                ArrayList<Teacher> student = gson.fromJson(netResponse.data, new TypeToken<ArrayList<Teacher>>() {
                }.getType());
                tab2StudentAdapter.addData(student);
                if (student.size() < rows) {
                    mLoadMoreView.setMoreAble(false);
                }
                count += rows;
                mLoadMoreView.end();

            } else {
                mLoadMoreView.setMoreAble(false);
                mLoadMoreView.end();
            }
        }
//        else if (id.equals("getFollowList")){
//            LogUtils.LOGE(tag,netResponse.toString());
//            Gson gson = new Gson();
//            ArrayList<Teacher> student = gson.fromJson(netResponse.data, new TypeToken<ArrayList<Teacher>>() {
//            }.getType());
//            tab2StudentAdapter.setData(student);
//            student_size.setText(String.valueOf(student.size()));
//
//        }
        super.onNetEnd(id, type, netResponse);
    }


    public void resfreshOk() {
        lvStudent.refreshOk();
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
                lvStudent.stopRefresh();
            }


        }.execute();

    }

    public void resfreshFail() {
        lvStudent.refreshFail();
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
                lvStudent.stopRefresh();
            }


        }.execute();
    }

    @Override
    protected int getTitleBarType() {
        return FLAG_TXT | FLAG_BACK;
    }

    @Override
    protected boolean onPageBack() {
        return false;
    }

    @Override
    protected boolean onPageNext() {

        return true;
    }

    @Override
    protected void initTitle() {
        setTitleText("关注");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * @param listView
     */
    private void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onRefresh() {
        count = 0;
        if (teacher == 1) {
            RestNetCallHelper.callNet(
                    getActivity(),
                    MyNetApiConfig.getFollowTeacherList,
                    MyNetRequestConfig.getFollowTeacherList(getActivity(), uid,String.valueOf(rows), String.valueOf(count)),
                    "teacher", Tab5FollowedFragment.this);
        } else {
            RestNetCallHelper.callNet(
                    getActivity(),
                    MyNetApiConfig.getFollowStudentList,
                    MyNetRequestConfig.getFollowTeacherList(getActivity(), uid, String.valueOf(rows), String.valueOf(count)),
                    "student", Tab5FollowedFragment.this);
        }
    }

    @Override
    public boolean onMore(AbsListView view) {
        if (mLoadMoreView.getMoreAble()) {
            if (mLoadMoreView.isloading()) {
                // 正在加载中
            } else {
                mLoadMoreView.loading();

                if (teacher == 1) {
                    RestNetCallHelper.callNet(
                            getActivity(),
                            MyNetApiConfig.getFollowTeacherList,
                            MyNetRequestConfig.getFollowTeacherList(getActivity(), uid,String.valueOf(rows), String.valueOf(count)),
                            "teacher_more", Tab5FollowedFragment.this);
                } else {
                    RestNetCallHelper.callNet(
                            getActivity(),
                            MyNetApiConfig.getFollowStudentList,
                            MyNetRequestConfig.getFollowTeacherList(getActivity(), uid, String.valueOf(rows), String.valueOf(count)),
                            "student_more", Tab5FollowedFragment.this);
                }

            }
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loadmore_student:
                teacher = 0;
                count = 0;
                onRefresh();
                loadmore_student.setTextColor(getResources().getColor(R.color.redMain));
                loadmore_teacher.setTextColor(getResources().getColor(R.color.dd_gray));

                break;
            case R.id.loadmore_teacher:
                teacher = 1;
                count = 0;
                onRefresh();
                loadmore_teacher.setTextColor(getResources().getColor(R.color.redMain));
                loadmore_student.setTextColor(getResources().getColor(R.color.dd_gray));
                break;
        }
    }
}
