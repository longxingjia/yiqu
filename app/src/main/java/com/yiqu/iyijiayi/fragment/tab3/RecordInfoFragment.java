package com.yiqu.iyijiayi.fragment.tab3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ui.abs.AbsFragment;
import com.ui.views.RefreshList;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.Control.Main.PlayActivity;
import com.yiqu.Control.Main.RecordActivityForRecordFrag;
<<<<<<< HEAD
import com.yiqu.Control.Main.RecordOnlyActivity;
=======
>>>>>>> 429a4c1dac7c9590b80443b9eb9e50e4abf32725
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.SoundsTab3Adapter;
import com.yiqu.iyijiayi.db.ComposeVoiceInfoDBHelper;
import com.yiqu.iyijiayi.model.ComposeVoice;
import com.yiqu.iyijiayi.model.UserInfo;
<<<<<<< HEAD
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
=======
>>>>>>> 429a4c1dac7c9590b80443b9eb9e50e4abf32725
import com.yiqu.iyijiayi.utils.LogUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/15.
 */

public class RecordInfoFragment extends AbsAllFragment implements View.OnClickListener, TextWatcher {
    private static final String TAG = "RecordInfoFragment";
    @BindView(R.id.txt01)
    public EditText musicname;
    @BindView(R.id.txt02)
    public EditText musicdesc;
    @BindView(R.id.submit)
    public ImageView submit;

    @OnClick(R.id.submit)
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.submit:
<<<<<<< HEAD
         //       LogUtils.LOGE("22", "hhhh");
                Intent intent = new Intent(getActivity(), RecordOnlyActivity.class);
                intent.putExtra("musicname",musicname.getText().toString());
                intent.putExtra("musicdesc",musicdesc.getText().toString());
                getActivity().startActivity(intent);

=======
                LogUtils.LOGE("22", "hhhh");
>>>>>>> 429a4c1dac7c9590b80443b9eb9e50e4abf32725
                break;
            default:
                break;
        }
    }


    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.record_info_fragment;
    }

    @Override
    protected void initView(View v) {
        ButterKnife.bind(this, v);
        //  UserInfo userInfo = (UserInfo) getActivity().getIntent().getSerializableExtra("data");

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        musicname.addTextChangedListener(this);
        musicdesc.addTextChangedListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
<<<<<<< HEAD
        MobclickAgent.onPageStart("录制作品");

    }

=======

        MobclickAgent.onPageStart("录音列表");

    }

    private void record() {
        Intent intent = new Intent(getActivity(), RecordActivityForRecordFrag.class);
        getActivity().startActivity(intent);

    }
>>>>>>> 429a4c1dac7c9590b80443b9eb9e50e4abf32725


    @Override
    public void onPause() {
        super.onPause();
<<<<<<< HEAD
        MobclickAgent.onPageEnd("录制作品");
=======
        MobclickAgent.onPageEnd("录音列表");
>>>>>>> 429a4c1dac7c9590b80443b9eb9e50e4abf32725

    }

    @Override
    protected int getTitleBarType() {
        return FLAG_BACK | FLAG_TXT;
    }

    @Override
    protected boolean onPageBack() {
        return false;
    }

    @Override
    protected boolean onPageNext() {
        return false;
    }

    @Override
    protected void initTitle() {
        setTitleText("录制作品");

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(musicname.getText().toString()) || TextUtils.isEmpty(musicdesc.getText().toString())) {

            submit.setBackgroundResource(R.mipmap.submit_unclick);
            submit.setEnabled(false);
        } else {
            submit.setBackgroundResource(R.mipmap.submit_clickable);
            submit.setEnabled(true);
        }
    }
}
