package com.yiqu.iyijiayi.fragment;


import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.adapter.Tab2ListAdapter;
import com.yiqu.iyijiayi.adapter.Tab2UserInfoAdapter;
import com.yiqu.iyijiayi.fragment.tab1.SearchFragment;
import com.yiqu.iyijiayi.model.Tab2Info;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Tab2Fragment extends TabContentFragment {

    private String tag = "Tab2Fragment";
    private int flag = 2;
    private String uid;

    @BindView(R.id.tab_teacher)
    public TextView tab_teacher;
    @BindView(R.id.tab_student)
    public TextView tab_student;

    @BindView(R.id.gridview)
    public GridView gridview;

    @BindView(R.id.listView)
    public ListView listView;
    private Context mContext;
    private Tab2UserInfoAdapter tab2UserInfoAdapter;
    private Tab2ListAdapter tab2ListAdapter;


    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab2;
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
        mContext = getActivity();
      //  View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.tab2_gv_header, null);

        tab2UserInfoAdapter = new Tab2UserInfoAdapter(mContext);
        gridview.setAdapter(tab2UserInfoAdapter);
        gridview.setOnItemClickListener(tab2UserInfoAdapter);
//        gridview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
//
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
//
//            }
//        });

        tab2ListAdapter = new Tab2ListAdapter(mContext);
        listView.setAdapter(tab2ListAdapter);
        listView.setOnItemClickListener(tab2ListAdapter);

    }

    @Override
    protected boolean isTouchMaskForNetting() {
        return false;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tab_teacher.setSelected(true);



    }

    private void initData() {

        if (AppShare.getIsLogin(getActivity())) {
            uid = AppShare.getUserInfo(getActivity()).uid;
        } else {
            uid = "0";
        }

        RestNetCallHelper.callNet(
                getActivity(),
                MyNetApiConfig.findPeople,
                MyNetRequestConfig.findPeople(getActivity(), uid, flag),
                "findPeople", Tab2Fragment.this, false, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("找人"); //统计页面，"MainScreen"为页面名称，可自定义
        initData();
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("找人");
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

        if (id.equals("findPeople")) {
            if (type == TYPE_SUCCESS) {
                LogUtils.LOGE(tag, netResponse.toString());

                Tab2Info tab2Info = new Gson().fromJson(netResponse.data, Tab2Info.class);

                tab2UserInfoAdapter.setData(tab2Info.topusers);

                tab2ListAdapter.setData(tab2Info.groups);
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

    @OnClick({R.id.tab_teacher, R.id.tab_student})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tab_teacher:
                tab_teacher.setSelected(true);
                tab_student.setSelected(false);
                tab_teacher.setTextColor(getResources().getColor(R.color.white));
                tab_student.setTextColor(getResources().getColor(R.color.tab_text_color));
                flag = 2;
                initData();

                break;
            case R.id.tab_student:
                tab_teacher.setSelected(false);
                tab_student.setSelected(true);
                tab_student.setTextColor(getResources().getColor(R.color.white));
                tab_teacher.setTextColor(getResources().getColor(R.color.tab_text_color));
                flag = 1;
                initData();


                break;
        }

    }
}
