package com.yiqu.iyijiayi.fragment.tab3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ui.abs.AbsFragment;
import com.ui.views.RefreshList;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.Control.Main.PlayActivity;
import com.yiqu.Control.Main.RecordActivityForRecordFrag;
import com.yiqu.Control.Main.RecordOnlyActivity;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.adapter.SoundsTab3Adapter;
import com.yiqu.iyijiayi.db.ComposeVoiceInfoDBHelper;
import com.yiqu.iyijiayi.model.ComposeVoice;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.NoScollViewPager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2017/2/15.
 */

public class RecordFragment extends AbsFragment implements View.OnClickListener, RefreshList.IRefreshListViewListener {


    private TextView buttonOne;
    private TextView buttonTwo;
    private RelativeLayout rl_no_record;
    private RelativeLayout rl_have_record;
    private ArrayList<ComposeVoice> voice;
    private RefreshList listView;
    private SoundsTab3Adapter soundsTab3Adapter;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.buttonTwo:
                record();
                break;
            default:
                break;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.record_fragment;
    }

    @Override
    protected void initView(View v) {
        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        View headView = inflater.inflate(R.layout.header_view_record, null);

        buttonOne = (TextView) headView.findViewById(R.id.buttonOne);
        buttonTwo = (TextView) v.findViewById(R.id.buttonTwo);
        rl_no_record = (RelativeLayout) v.findViewById(R.id.rl_no_record);
        rl_have_record = (RelativeLayout) v.findViewById(R.id.rl_have_record);
        listView = (RefreshList) v.findViewById(R.id.listView);
        listView.addHeaderView(headView);

        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record();
            }
        });
        buttonTwo.setOnClickListener(this);

        UserInfo userInfo = (UserInfo) getActivity().getIntent().getSerializableExtra("data");

        soundsTab3Adapter = new SoundsTab3Adapter(getActivity(),userInfo);
        listView.setAdapter(soundsTab3Adapter);
        listView.setRefreshListListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), PlayActivity.class);

                intent.putExtra("data", (Serializable) voice);
                intent.putExtra("position", position - 2);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        ComposeVoiceInfoDBHelper com = new ComposeVoiceInfoDBHelper(getActivity());
        voice = com.getAll(ComposeVoiceInfoDBHelper.UNCOMPOSE);
        com.close();
        if (voice.size() == 0) {
            rl_no_record.setVisibility(View.VISIBLE);
            rl_have_record.setVisibility(View.INVISIBLE);
        } else {
            rl_no_record.setVisibility(View.INVISIBLE);
            rl_have_record.setVisibility(View.VISIBLE);
            soundsTab3Adapter.setData(voice);
        }

        MobclickAgent.onPageStart("录音列表");

    }

    private void record() {

        Model.startNextAct(getActivity(),
                RecordInfoFragment.class.getName());


    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("录音列表");

    }

    @Override
    public void onRefresh() {
        ComposeVoiceInfoDBHelper com = new ComposeVoiceInfoDBHelper(getActivity());
        voice = com.getAll(ComposeVoiceInfoDBHelper.UNCOMPOSE);
        com.close();
        if (voice.size() == 0) {
            rl_no_record.setVisibility(View.VISIBLE);
            rl_have_record.setVisibility(View.INVISIBLE);
        } else {
            rl_no_record.setVisibility(View.INVISIBLE);
            rl_have_record.setVisibility(View.VISIBLE);
            soundsTab3Adapter.setData(voice);
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
}
