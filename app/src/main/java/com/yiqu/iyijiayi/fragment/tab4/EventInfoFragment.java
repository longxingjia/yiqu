package com.yiqu.iyijiayi.fragment.tab4;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.umeng.analytics.MobclickAgent;
import com.yiqu.Control.Main.RecordOnlyActivity;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/15.
 */

public class EventInfoFragment extends AbsAllFragment implements View.OnClickListener, TextWatcher {
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

         //       LogUtils.LOGE("22", "hhhh");
                Intent intent = new Intent(getActivity(), RecordOnlyActivity.class);
                intent.putExtra("musicname",musicname.getText().toString());
                intent.putExtra("musicdesc",musicdesc.getText().toString());
                getActivity().startActivity(intent);


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
        return R.layout.event_info_fragment;
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

        MobclickAgent.onPageStart("录制作品");

    }




    @Override
    public void onPause() {
        super.onPause();

        MobclickAgent.onPageEnd("录制作品");


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
