package com.yiqu.iyijiayi.fragment.tab3;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.ui.abs.AbsFragment;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.utils.NoScollViewPager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/15.
 */

public class SoundsTab3fragment extends AbsFragment implements View.OnClickListener {


    @Override
    public void onClick(View v) {

    }

    @Override
    protected int getContentView() {
        return R.layout.record_tab1_fragment;
    }

    @Override
    protected void initView(View v) {
        ToastManager.getInstance(getActivity()).showText("3");
    }
}
