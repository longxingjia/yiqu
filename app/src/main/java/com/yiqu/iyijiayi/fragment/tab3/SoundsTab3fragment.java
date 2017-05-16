package com.yiqu.iyijiayi.fragment.tab3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.base.utils.ToastManager;
import com.ui.abs.AbsFragment;
import com.ui.views.RefreshList;
import com.umeng.analytics.MobclickAgent;
import com.utils.L;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.adapter.SoundsTab3Adapter;
import com.db.ComposeVoiceInfoDBHelper;
import com.model.ComposeVoice;
import com.yiqu.iyijiayi.model.UserInfo;

import java.util.ArrayList;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;

/**
 * Created by Administrator on 2017/2/15.
 */

public class SoundsTab3fragment extends AbsFragment implements  View.OnClickListener,RefreshList.IRefreshListViewListener{


    private String tag = "SoundsTab3fragment";

    private RefreshList listView;
    private SoundsTab3Adapter soundsTab3Adapter;
    private ArrayList<ComposeVoice> composeVoices;
    private ComposeVoiceInfoDBHelper composeVoiceInfoDBHelper;


    @Override
    public void onClick(View v) {

    }

    @Override
    protected int getContentView() {
        return R.layout.record_tab2_fragment;
    }

    @Override
    protected void initView(View v) {
        listView = (RefreshList) v.findViewById(R.id.tab1_list);


    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

         Fragment fm =  getParentFragment();
        Intent intent = fm.getActivity().getIntent();
        UserInfo userInfo = (UserInfo)intent.getSerializableExtra("data");

        soundsTab3Adapter = new SoundsTab3Adapter(getActivity(),userInfo);
        listView.setAdapter(soundsTab3Adapter);
        listView.setOnItemClickListener(soundsTab3Adapter);
        listView.setRefreshListListener(this);

        composeVoiceInfoDBHelper = new ComposeVoiceInfoDBHelper(getActivity());
        composeVoices = composeVoiceInfoDBHelper.getAll(ComposeVoiceInfoDBHelper.COMPOSE);
        if (composeVoices !=null&& composeVoices.size()>0){
            soundsTab3Adapter.setData(composeVoices);
        }

    }

    @Override
    public void onRefresh() {
        composeVoices = composeVoiceInfoDBHelper.getAll(ComposeVoiceInfoDBHelper.COMPOSE);
        if (composeVoices !=null&& composeVoices.size()>0){
            soundsTab3Adapter.setData(composeVoices);
            resfreshOk();
        }else {
            resfreshFail();
            ToastManager.getInstance(getActivity()).showText("没有数据");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("声乐本地");
        JAnalyticsInterface.onPageStart(getActivity(),"声乐本地");

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("声乐本地");
        JAnalyticsInterface.onPageEnd(getActivity(),"声乐本地");

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
