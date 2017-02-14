package com.yiqu.iyijiayi.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.jauker.widget.BadgeView;
import com.ui.views.RefreshList;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.adapter.Tab2StudentAdapter;
import com.yiqu.iyijiayi.adapter.Tab2TeacherAdapter;
import com.yiqu.iyijiayi.model.Remen;
import com.yiqu.iyijiayi.model.Student;
import com.yiqu.iyijiayi.model.Teacher;
import com.yiqu.iyijiayi.model.ZhaoRen;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.view.ReScrollViewWithListView;
import com.yiqu.iyijiayi.view.ScrollViewWithListView;

import java.util.ArrayList;

public class Tab2Fragment extends TabContentFragment implements RefreshList.IRefreshListViewListener {

    private static final int TAB_2 = 2;
    private String uid;
    private ScrollViewWithListView lvTeacher;
    private ArrayList<Teacher> teacher;
    private ArrayList<Student> student;
    private ImageLoaderHm<ImageView> mImageLoaderHm;
    private Tab2TeacherAdapter tab2TeacherAdapter;
    private Tab2StudentAdapter tab2StudentAdapter;
    private ScrollViewWithListView lvStudent;

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab1;
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
        View footView = LayoutInflater.from(getActivity()).inflate(R.layout.tab2_fragment_footer, null);
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.tab2_fragment_header, null);

        lvTeacher = (ScrollViewWithListView) v.findViewById(R.id.lv_teacher);
        lvTeacher.addHeaderView(headView);

//        lvTeacher.addFooterView(footView);
        lvStudent = (ScrollViewWithListView) v.findViewById(R.id.lv_student);

//        lvTeacher.setRefreshListListener(this);
        mImageLoaderHm = new ImageLoaderHm<ImageView>(getActivity(),300);
        ImageLoaderHm mImageLoader = new ImageLoaderHm<ImageView>(getActivity(),300);
        tab2TeacherAdapter = new Tab2TeacherAdapter(getActivity(),mImageLoaderHm);
        lvTeacher.setAdapter(tab2TeacherAdapter);

        tab2StudentAdapter = new Tab2StudentAdapter(getActivity(),mImageLoader);
        lvStudent.setAdapter(tab2StudentAdapter);


    }

    @Override
    protected boolean isTouchMaskForNetting() {
        return false;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setSlidingMenuEnable(false);

        if (AppShare.getIsLogin(getActivity())){
            uid = AppShare.getUserInfo(getActivity()).uid;
        }else{
            uid = "0";
        }

        RestNetCallHelper.callNet(
                getActivity(),
                MyNetApiConfig.follow_recommend,
                MyNetRequestConfig.follow_recommend(getActivity(),uid),
                "teacher", Tab2Fragment.this);

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

        LogUtils.LOGE(netResponse.toString());

        if(netResponse.bool==1){
            Gson gson = new Gson();
            ZhaoRen zhaoRen = gson.fromJson(netResponse.data,ZhaoRen.class);
            teacher = zhaoRen.teacher;
            student = zhaoRen.student;

            LogUtils.LOGE(student.toString());

            tab2TeacherAdapter.setData(teacher);
            setListViewHeightBasedOnChildren(lvTeacher);
            tab2StudentAdapter.setData(student);
//            setListViewHeightBasedOnChildren(lvStudent);

//            resfreshOk();
        }else {
            ToastManager.getInstance(getActivity()).showText(netResponse.result);
//            resfreshFail();
        }

        super.onNetEnd(id, type, netResponse);
    }

    @Override
    protected int getTitleBarType() {
        return FLAG_TXT | FLAG_BACK;
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

    }
//
//
//    public void resfreshOk(){
//        lvTeacher.refreshOk();
//        new AsyncTask<Void, Void, Void>() {
//            protected Void doInBackground(Void... params) {
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void result) {
//                lvTeacher.stopRefresh();
//            }
//
//
//        }.execute();
//
//    }
//
//    public void resfreshFail(){
//        lvTeacher.refreshFail();
//        new AsyncTask<Void, Void, Void>() {
//            protected Void doInBackground(Void... params) {
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void result) {
//                lvTeacher.stopRefresh();
//            }
//
//
//        }.execute();
//    }

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
}
