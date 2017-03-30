package com.yiqu.iyijiayi.fragment.tab5;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.adapter.Tab2StudentAdapter;
import com.yiqu.iyijiayi.adapter.Tab2TeacherAdapter;
import com.yiqu.iyijiayi.adapter.Tab5GuanzhuAdapter;
import com.yiqu.iyijiayi.fragment.Tab2Fragment;
import com.yiqu.iyijiayi.fragment.TabContentFragment;
import com.yiqu.iyijiayi.fragment.tab1.SearchFragment;
import com.yiqu.iyijiayi.fragment.tab2.Tab2ListFragment;
import com.yiqu.iyijiayi.model.Followed;
import com.yiqu.iyijiayi.model.Student;
import com.yiqu.iyijiayi.model.Teacher;
import com.yiqu.iyijiayi.model.ZhaoRen;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.view.ScrollViewWithListView;

import java.util.ArrayList;

public class Tab5FollowedFragment extends TabContentFragment implements SwipeRefreshLayout.OnRefreshListener{

    private String tag = "Tab5FollowedFragment";
    private String uid;
    private ScrollViewWithListView lvTeacher;
    private ArrayList<Teacher> teacher;
    private ArrayList<Student> student;

    private Tab5GuanzhuAdapter tab2TeacherAdapter;
    private Tab5GuanzhuAdapter tab2StudentAdapter;
    private ScrollViewWithListView lvStudent;
    private ZhaoRen zhaoRen;
    private static int REQUESTNO = 1;
    private SwipeRefreshLayout swipeRe;
    private TextView teacher_size;
    private TextView student_size;

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    public void onSelect() {
        super.onSelect();
    }

    @Override
    public void onNoSelect() {
        super.onNoSelect();
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab5_followed_fragment;
    }

    @Override
    protected void initView(View v) {

        swipeRe = (SwipeRefreshLayout) v.findViewById(R.id.swipeRe);
        swipeRe.setOnRefreshListener(this);
        lvTeacher = (ScrollViewWithListView) v.findViewById(R.id.lv_teacher);
        lvStudent = (ScrollViewWithListView) v.findViewById(R.id.lv_student);
        teacher_size = (TextView) v.findViewById(R.id.teacher_size);
        student_size = (TextView) v.findViewById(R.id.student_size);
        uid = AppShare.getUserInfo(getActivity()).uid;
        tab2TeacherAdapter = new Tab5GuanzhuAdapter(getActivity(), uid);
        lvTeacher.setAdapter(tab2TeacherAdapter);
        lvTeacher.setOnItemClickListener(tab2TeacherAdapter);
        tab2StudentAdapter = new Tab5GuanzhuAdapter(getActivity(), uid);
        lvStudent.setAdapter(tab2StudentAdapter);
        lvStudent.setOnItemClickListener(tab2StudentAdapter);

        RestNetCallHelper.callNet(
                getActivity(),
                MyNetApiConfig.followed,
                MyNetRequestConfig.follow_recommend(getActivity(), uid),
                "teacher", Tab5FollowedFragment.this);
    }

    @Override
    protected boolean isTouchMaskForNetting() {
        return false;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("关注"); //统计页面，"MainScreen"为页面名称，可自定义



    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("关注");
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

        if (netResponse != null) {
            if (netResponse.bool == 1) {

                Gson gson = new Gson();
                Followed followed = gson.fromJson(netResponse.data, Followed.class);
                tab2TeacherAdapter.setData(followed.teacher);
                setListViewHeightBasedOnChildren(lvTeacher);
                tab2StudentAdapter.setData(followed.student);
                teacher_size.setText(String.valueOf(followed.teacher.size()));
                student_size.setText(String.valueOf(followed.student.size()));

            } else {
                ToastManager.getInstance(getActivity()).showText(netResponse.result);
            }
        }
        super.onNetEnd(id, type, netResponse);
    }

    @Override
    protected int getTitleBarType() {
        return FLAG_TXT | FLAG_BTN;
    }

    @Override
    protected boolean onPageBack() {
        return false;
    }

    @Override
    protected boolean onPageNext() {
        // pageNextComplete();

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RestNetCallHelper.callNet(
                        getActivity(),
                        MyNetApiConfig.followed,
                        MyNetRequestConfig.follow_recommend(getActivity(), uid),
                        "teacher", Tab5FollowedFragment.this);

                swipeRe.setRefreshing(false);

            }
        }, 300);

    }
}
