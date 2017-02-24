package com.yiqu.iyijiayi.fragment.tab3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ui.abs.AbsFragment;
import com.yiqu.Control.Main.RecordActivityForRecordFrag;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.db.ComposeVoiceInfoDBHelper;
import com.yiqu.iyijiayi.model.ComposeVoice;
import com.yiqu.iyijiayi.utils.NoScollViewPager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/15.
 */

public class RecordFragment extends AbsFragment implements View.OnClickListener {


    private TextView buttonOne;
    private TextView buttonTwo;
    private RelativeLayout rl_no_record;
    private RelativeLayout rl_have_record;
    private ArrayList<ComposeVoice> voice;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonOne:
                record();
                break;
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
        buttonOne = (TextView) v.findViewById(R.id.buttonOne);
        buttonTwo = (TextView) v.findViewById(R.id.buttonTwo);
        rl_no_record = (RelativeLayout) v.findViewById(R.id.rl_no_record);
        rl_have_record = (RelativeLayout) v.findViewById(R.id.rl_have_record);

        buttonOne.setOnClickListener(this);
        buttonTwo.setOnClickListener(this);

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        ComposeVoiceInfoDBHelper com = new ComposeVoiceInfoDBHelper(getActivity());
        voice = com.getAll(ComposeVoiceInfoDBHelper.UPCOMPOSE);
        if (voice.size()==0){
            rl_no_record.setVisibility(View.VISIBLE);
            rl_have_record.setVisibility(View.INVISIBLE);
        }else {
            rl_no_record.setVisibility(View.INVISIBLE);
            rl_have_record.setVisibility(View.VISIBLE);
        }


    }

    private void record() {
        Intent intent = new Intent(getActivity(), RecordActivityForRecordFrag.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

}
