package com.yiqu.iyijiayi.fragment.tab3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.ui.abs.AbsFragment;
import com.ui.views.RefreshList;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.adapter.SoundsTab1Adapter;
import com.yiqu.iyijiayi.adapter.SoundsTab3Adapter;
import com.yiqu.iyijiayi.db.DownloadMusicInfoDBHelper;
import com.yiqu.iyijiayi.db.UploadVoiceInfoDBHelper;
import com.yiqu.iyijiayi.model.Music;
import com.yiqu.iyijiayi.model.UploadVoice;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.NoScollViewPager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/15.
 */

public class SoundsTab3fragment extends AbsFragment implements  View.OnClickListener,RefreshList.IRefreshListViewListener{


    private String tag = "SoundsTab3fragment";
    private ImageLoaderHm<ImageView> mImageLoaderHm;
    private RefreshList listView;
    private SoundsTab3Adapter soundsTab3Adapter;
    private ArrayList<UploadVoice> uploadVoices;
    private UploadVoiceInfoDBHelper uploadVoiceInfoDBHelper;


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
        mImageLoaderHm = new ImageLoaderHm<ImageView>(getActivity(), 300);
        soundsTab3Adapter = new SoundsTab3Adapter(getActivity(), mImageLoaderHm);
        listView.setAdapter(soundsTab3Adapter);
        listView.setOnItemClickListener(soundsTab3Adapter);
        listView.setRefreshListListener(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        uploadVoiceInfoDBHelper = new UploadVoiceInfoDBHelper(getActivity());
        uploadVoices = uploadVoiceInfoDBHelper.getAll();
        if (uploadVoices!=null&&uploadVoices.size()>0){
            soundsTab3Adapter.setData(uploadVoices);
        }

//        LogUtils.LOGE(tag, uploadVoices.toString());
    }

    @Override
    public void onRefresh() {
        uploadVoices = uploadVoiceInfoDBHelper.getAll();
        if (uploadVoices!=null&&uploadVoices.size()>0){
            soundsTab3Adapter.setData(uploadVoices);
            resfreshOk();
        }

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
