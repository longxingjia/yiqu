package com.yiqu.iyijiayi.fragment;


import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.adapter.Tab2StudentAdapter;
import com.yiqu.iyijiayi.adapter.Tab2TeacherAdapter;
import com.yiqu.iyijiayi.fragment.tab2.Tab2ListFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
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

public class Tab2Fragment extends TabContentFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private static final int TAB_2 = 2;
    private String tag = "Tab2Fragment";
    private String uid;
    private ScrollViewWithListView lvTeacher;
    private ArrayList<Teacher> teacher;
    private ArrayList<Student> student;
    private ImageLoaderHm<ImageView> mImageLoaderHm;
    private Tab2TeacherAdapter tab2TeacherAdapter;
    private Tab2StudentAdapter tab2StudentAdapter;
    private ScrollViewWithListView lvStudent;
    private SwipeRefreshLayout swipeRe;
    private TextView loadMoreTeacher;
    private TextView loadMoreStudent;

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
        return R.layout.tab2_fragment;
    }

    @Override
    protected void initView(View v) {
        swipeRe = (SwipeRefreshLayout) v.findViewById(R.id.swipeRe);
        swipeRe.setOnRefreshListener(this);
        View footView = LayoutInflater.from(getActivity()).inflate(R.layout.tab2_fragment_footer, null);
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.tab2_fragment_header, null);

        loadMoreTeacher = (TextView) v.findViewById(R.id.loadmore_teacher);
        loadMoreStudent = (TextView) v.findViewById(R.id.loadmore_student);

        loadMoreStudent.setOnClickListener(this);
        loadMoreTeacher.setOnClickListener(this);

        lvTeacher = (ScrollViewWithListView) v.findViewById(R.id.lv_teacher);
        lvTeacher.addHeaderView(headView);

        lvStudent = (ScrollViewWithListView) v.findViewById(R.id.lv_student);

        mImageLoaderHm = new ImageLoaderHm<ImageView>(getActivity(), 300);

        lvStudent.addHeaderView(footView);


    }

    @Override
    protected boolean isTouchMaskForNetting() {
        return false;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setSlidingMenuEnable(false);
        ZhaoRen zhaoRen = AppShare.getZhaoRenList(getActivity());
        if (AppShare.getIsLogin(getActivity())) {
            uid = AppShare.getUserInfo(getActivity()).uid;
        } else {
            uid = "0";
        }



        tab2TeacherAdapter = new Tab2TeacherAdapter(getActivity(), mImageLoaderHm, uid);
        lvTeacher.setAdapter(tab2TeacherAdapter);

        tab2StudentAdapter = new Tab2StudentAdapter(getActivity(), mImageLoaderHm,uid);
        lvStudent.setAdapter(tab2StudentAdapter);
        if (zhaoRen == null) {

            RestNetCallHelper.callNet(
                    getActivity(),
                    MyNetApiConfig.follow_recommend,
                    MyNetRequestConfig.follow_recommend(getActivity(), uid),
                    "teacher", Tab2Fragment.this);
        } else {
            teacher = zhaoRen.teacher;
            student = zhaoRen.student;

            LogUtils.LOGE(tag, zhaoRen.toString());
            tab2TeacherAdapter.setData(zhaoRen);
            setListViewHeightBasedOnChildren(lvTeacher);
            tab2StudentAdapter.setData(zhaoRen);
            loadMoreTeacher.setVisibility(View.VISIBLE);
            loadMoreStudent.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

        if (netResponse != null) {
            if (netResponse.bool == 1) {
                loadMoreTeacher.setVisibility(View.VISIBLE);
                loadMoreStudent.setVisibility(View.VISIBLE);
                Gson gson = new Gson();
                ZhaoRen zhaoRen = gson.fromJson(netResponse.data, ZhaoRen.class);
                AppShare.setZhaoRenList(getActivity(), zhaoRen);
                teacher = zhaoRen.teacher;
                student = zhaoRen.student;
//                LogUtils.LOGE(student.toString());
                tab2TeacherAdapter.setData(zhaoRen);
                setListViewHeightBasedOnChildren(lvTeacher);
                tab2StudentAdapter.setData(zhaoRen);
//            resfreshOk();
            } else {
                ToastManager.getInstance(getActivity()).showText(netResponse.result);
//            resfreshFail();
            }
        }
        super.onNetEnd(id, type, netResponse);
    }

    @Override
    protected int getTitleBarType() {
        return FLAG_TXT ;
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
        setTitleText(getString(R.string.label_tab2));
    }


    @Override
    public void onDestroy() {
        mImageLoaderHm.stop();
        super.onDestroy();
    }


    @Override
    public void onRefresh() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RestNetCallHelper.callNet(
                        getActivity(),
                        MyNetApiConfig.follow_recommend,
                        MyNetRequestConfig.follow_recommend(getActivity(), uid),
                        "teacher", Tab2Fragment.this);

                swipeRe.setRefreshing(false);

            }
        }, 300);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loadmore_student:
                if (uid.equals("0")){
                    Intent i = new Intent(getActivity(), StubActivity.class);
                    i.putExtra("fragment", SelectLoginFragment.class.getName());
                    getActivity().startActivity(i);
                    ToastManager.getInstance(getActivity()).showText("请您登录后在操作");

                    return;
                }
                Intent i = new Intent(getActivity(), StubActivity.class);
                i.putExtra("fragment", Tab2ListFragment.class.getName());
                i.putExtra("data", "1");  //0 代表学生
                getActivity().startActivity(i);
                break;
            case R.id.loadmore_teacher:
                if (uid.equals("0")){
                    Intent it = new Intent(getActivity(), StubActivity.class);
                    it.putExtra("fragment", SelectLoginFragment.class.getName());
                    getActivity().startActivity(it);
                    ToastManager.getInstance(getActivity()).showText("请您登录后在操作");

                    return;
                }


                Intent in = new Intent(getActivity(), StubActivity.class);
                in.putExtra("fragment", Tab2ListFragment.class.getName());
                in.putExtra("data", "2");  //1 代表老师
                getActivity().startActivity(in);
                break;
        }

    }
}
