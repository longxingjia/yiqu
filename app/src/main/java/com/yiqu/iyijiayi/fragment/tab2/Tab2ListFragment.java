package com.yiqu.iyijiayi.fragment.tab2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.ui.views.LoadMoreView;
import com.ui.views.RefreshList;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.Tab2ListFragmetAdapter;
import com.yiqu.iyijiayi.fragment.Tab4Fragment;
import com.yiqu.iyijiayi.model.Discovery;
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

public class Tab2ListFragment extends AbsAllFragment implements LoadMoreView.OnMoreListener, RefreshList.IRefreshListViewListener {

    private ImageLoaderHm<ImageView> mImageLoaderHm;
    Tab2ListFragmetAdapter tab2ListFragmetAdapter;
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
        listView = (RefreshList) v.findViewById(R.id.listView);
        mImageLoaderHm = new ImageLoaderHm<ImageView>(getActivity(), 300);

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
        if (AppShare.getIsLogin(getActivity())) {
            uid = AppShare.getUserInfo(getActivity()).uid;
        } else {
            uid = "0";
        }
        LogUtils.LOGE(tag,uid+"");
        RestNetCallHelper.callNet(getActivity(),
                MyNetApiConfig.get_follow_recommend_list,
                MyNetRequestConfig.get_follow_recommend_list(getActivity()
                        ,uid,type,count,rows),
                "get_follow_recommend_list",
                this);
        tab2ListFragmetAdapter = new Tab2ListFragmetAdapter(getActivity(), mImageLoaderHm,uid);
        listView.setAdapter(tab2ListFragmetAdapter);
        listView.setOnItemClickListener(tab2ListFragmetAdapter);
        listView.setRefreshListListener(this);

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




    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {


        if (id.equals("get_follow_recommend_list")) {
            if (type == NetCallBack.TYPE_SUCCESS) {

                try {
                    datas = parseList(netResponse.data.toString());
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
                // TODO Auto-generated method stub

//                ArrayList<FindAllPushMsg>  list = (ArrayList<FindAllPushMsg>) netResponse.body;
//                mBeautifulAdapter.addData(list);
                try {
                    datas = parseList(netResponse.data.toString());
                    tab2ListFragmetAdapter.addData(datas);
                    if (datas.size() < rows) {
                        mLoadMoreView.setMoreAble(false);
                    }
                    count += rows;
                    mLoadMoreView.end();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        super.onNetEnd(id, type, netResponse);
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        mLoadMoreView.end();
        mLoadMoreView.setMoreAble(false);
        count = 0;
        RestNetCallHelper.callNet(getActivity(),
                MyNetApiConfig.get_follow_recommend_list,
                MyNetRequestConfig.get_follow_recommend_list(getActivity()
                        ,uid,type,count,rows),
                "get_follow_recommend_list",
                this);

    }

    @Override
    public boolean onMore(AbsListView view) {
        // TODO Auto-generated method stub
        if (mLoadMoreView.getMoreAble()) {
            if (mLoadMoreView.isloading()) {
                // 正在加载中
            } else {
                mLoadMoreView.loading();

                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.get_follow_recommend_list,
                        MyNetRequestConfig.get_follow_recommend_list(getActivity()
                                ,uid,type,count,rows),
                        "get_follow_recommend_list_more",
                        this);

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