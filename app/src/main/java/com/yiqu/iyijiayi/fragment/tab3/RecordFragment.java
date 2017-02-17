package com.yiqu.iyijiayi.fragment.tab3;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.widget.TextView;

import com.ui.abs.AbsFragment;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.utils.NoScollViewPager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/15.
 */

public class RecordFragment extends AbsFragment implements View.OnClickListener {


    private TextView buttonOne;
    private TextView buttonTwo;
    private TextView buttonThree;
    private NoScollViewPager mViewPager;
    private ArrayList<Fragment> fragmentList;
    @Override
    public void onClick(View v)
    {
//        switch (v.getId())
//        {
//            case R.id.btn_one:
//                changeView(0);
//                break;
//            case R.id.btn_two:
//                changeView(1);
//                break;
//            case R.id.btn_three:
//                changeView(2);
//                break;
//
//            default:
//                break;
//        }
    }


    @Override
    protected int getContentView() {
        return R.layout.record_fragment;
    }

    @Override
    protected void initView(View v) {


    }




}
