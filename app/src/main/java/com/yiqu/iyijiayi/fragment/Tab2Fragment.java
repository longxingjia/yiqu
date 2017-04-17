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
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.adapter.Tab2StudentAdapter;
import com.yiqu.iyijiayi.adapter.Tab2TeacherAdapter;
import com.yiqu.iyijiayi.fragment.tab1.SearchFragment;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Tab2Fragment extends TabContentFragment {

    private String tag = "Tab2Fragment";
    private int flag = 0;
    private String uid;

    private ArrayList<Teacher> teacher;
    private ArrayList<Student> student;

    private Tab2TeacherAdapter tab2TeacherAdapter;
    private Tab2StudentAdapter tab2StudentAdapter;

    @BindView(R.id.tab_teacher)
    public TextView tab_teacher;
    @BindView(R.id.tab_student)
    public TextView tab_student;
    @BindView(R.id.shengyue)
    public TextView shengyue;
    @BindView(R.id.boyin)
    public TextView boyin;
    @BindView(R.id.yueqi)
    public TextView yueqi;


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
        ButterKnife.bind(this, v);

    }

    @Override
    protected boolean isTouchMaskForNetting() {
        return false;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tab_teacher.setSelected(true);

        initData();

    }

    private void initData() {
        if (flag==0){
            shengyue.setText("资深声乐导师 TOP10");
            boyin.setText("资深播音导师 TOP10");
            yueqi.setText("资深乐器导师 TOP10");
        }else {
            shengyue.setText("优秀声乐学生");
            boyin.setText("优秀播音学生");
            yueqi.setText("优秀乐器学生");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("找人"); //统计页面，"MainScreen"为页面名称，可自定义


        if (AppShare.getIsLogin(getActivity())) {
            uid = AppShare.getUserInfo(getActivity()).uid;
        } else {
            uid = "0";
        }

        tab2TeacherAdapter = new Tab2TeacherAdapter(getActivity(), uid);
        tab2StudentAdapter = new Tab2StudentAdapter(getActivity(), uid);


    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("找人");
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

        if (netResponse != null) {
            if (netResponse.bool == 1) {

                Gson gson = new Gson();
                ZhaoRen zhaoRen = gson.fromJson(netResponse.data, ZhaoRen.class);
                AppShare.setZhaoRenList(getActivity(), zhaoRen);
                teacher = zhaoRen.teacher;
                student = zhaoRen.student;

                tab2TeacherAdapter.setData(zhaoRen);

                tab2StudentAdapter.setData(zhaoRen);

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
        if (mOnFragmentListener != null) {
            mOnFragmentListener.onFragmentBack(this);
        }
        return true;
    }

    @Override
    protected boolean onPageNext() {
        // pageNextComplete();
        Intent intent = new Intent(getActivity(), StubActivity.class);
        intent.putExtra("fragment", SearchFragment.class.getName());
        intent.putExtra("data", "search_user");
        getActivity().startActivity(intent);

        return true;
    }

    @Override
    protected void initTitle() {
        setTitleText(getString(R.string.label_tab2));
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

    @OnClick({R.id.tab_teacher, R.id.tab_student, R.id.boyin, R.id.shengyue, R.id.yueqi})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tab_teacher:
                tab_teacher.setSelected(true);
                tab_student.setSelected(false);
                tab_teacher.setTextColor(getResources().getColor(R.color.white));
                tab_student.setTextColor(getResources().getColor(R.color.tab_text_color));
                flag=0;
                initData();

                break;
            case R.id.tab_student:
                tab_teacher.setSelected(false);
                tab_student.setSelected(true);
                tab_student.setTextColor(getResources().getColor(R.color.white));
                tab_teacher.setTextColor(getResources().getColor(R.color.tab_text_color));
                flag=1;
                initData();

                break;
            case R.id.boyin:


                break;
            case R.id.shengyue:


                break;
            case R.id.yueqi:


                break;
        }

    }
}
