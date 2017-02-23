package com.yiqu.iyijiayi.fragment.tab3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.ui.views.LoadMoreView;
import com.ui.views.RefreshList;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.SelectTeacherAdapter;
import com.yiqu.iyijiayi.adapter.Tab2ListFragmetAdapter;
import com.yiqu.iyijiayi.model.Teacher;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;
import com.yiqu.iyijiayi.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TeacherListFragment extends AbsAllFragment  {

    private ImageLoaderHm<ImageView> mImageLoaderHm;
    private String tag = "TeacherListFragment";
    private ArrayList<Teacher> datas;
    private SelectTeacherAdapter selectTeacherAdapter;

    //分页
    private int count = 0;
    private int rows = 1000;

    //刷新
    private ListView listView;
    private ListView listView_unfollow;
    private String uid;
    private String type;

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.teacher_list_fragment;
    }

    @Override
    protected void initView(View v) {
        listView = (ListView) v.findViewById(R.id.listView);
        listView_unfollow = (ListView) v.findViewById(R.id.listView_unfollow);
        mImageLoaderHm = new ImageLoaderHm<ImageView>(getActivity(), 300);


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

        selectTeacherAdapter = new SelectTeacherAdapter(getActivity(), mImageLoaderHm);
        listView.setAdapter(selectTeacherAdapter);
        listView.setOnItemClickListener(selectTeacherAdapter);

        RestNetCallHelper.callNet(getActivity(),
                MyNetApiConfig.getFollowTeacherList,
                MyNetRequestConfig.getFollowTeacherList(getActivity()
                        ,uid,rows+"",count+""),
                "getFollowTeacherList",
                this);

        RestNetCallHelper.callNet(getActivity(),
                MyNetApiConfig.getUnfollowTeacherList,
                MyNetRequestConfig.getUnfollowTeacherList(getActivity()
                        ,uid,rows+"",count+""),
                "getUnfollowTeacherList",
                this);
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
        return false;
    }

    @Override
    protected boolean onPageNext() {
        pageNextComplete();

        return true;
    }

    @Override
    protected void initTitle() {

            setTitleText("老师");

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {


        if (id.equals("getFollowTeacherList")) {
            if (type == NetCallBack.TYPE_SUCCESS) {
                LogUtils.LOGE(tag,netResponse.toString());

                try {
                    datas = parseList(netResponse.data.toString());
                    selectTeacherAdapter.setData(datas);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {

            }
        } else if ("getUnfollowTeacherList".equals(id)) {
            if (TYPE_SUCCESS == type) {
                LogUtils.LOGE(tag+"--",netResponse.toString());


//                ArrayList<FindAllPushMsg>  list = (ArrayList<FindAllPushMsg>) netResponse.body;
//                mBeautifulAdapter.addData(list);
//                try {
//                    datas = parseList(netResponse.data.toString());
//                    tab2ListFragmetAdapter.addData(datas);
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }
        }
        super.onNetEnd(id, type, netResponse);
    }

//    @Override
//    public void onRefresh() {
//        // TODO Auto-generated method stub
//        mLoadMoreView.end();
//        mLoadMoreView.setMoreAble(false);
//        count = 0;
//        RestNetCallHelper.callNet(getActivity(),
//                MyNetApiConfig.get_follow_recommend_list,
//                MyNetRequestConfig.get_follow_recommend_list(getActivity()
//                        ,uid,type,count,rows),
//                "get_follow_recommend_list",
//                this);
//
//    }

//    @Override
//    public boolean onMore(AbsListView view) {
//        // TODO Auto-generated method stub
//        if (mLoadMoreView.getMoreAble()) {
//            if (mLoadMoreView.isloading()) {
//                // 正在加载中
//            } else {
//                mLoadMoreView.loading();
//
//                RestNetCallHelper.callNet(getActivity(),
//                        MyNetApiConfig.get_follow_recommend_list,
//                        MyNetRequestConfig.get_follow_recommend_list(getActivity()
//                                ,uid,type,count,rows),
//                        "get_follow_recommend_list_more",
//                        this);
//
//            }
//        }
//
//
//        return false;
//    }
//
//    public void resfreshOk() {
//        listView.refreshOk();
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
//                listView.stopRefresh();
//            }
//
//
//        }.execute();
//
//    }
//
//    public void resfreshFail() {
//        listView.refreshFail();
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
//                listView.stopRefresh();
//            }
//
//
//        }.execute();
//    }

    public ArrayList<Teacher> parseList(String data) throws JSONException {
        ArrayList<Teacher> list = new ArrayList<Teacher>();
        JSONArray js = new JSONArray(data);
        for (int i = 0; i < js.length(); i++) {
            JSONObject j = (JSONObject) js.get(i);
            Gson gson = new Gson();
            Teacher f = gson.fromJson(j.toString(), Teacher.class);

            list.add(f);
        }
        return list;

    }







}