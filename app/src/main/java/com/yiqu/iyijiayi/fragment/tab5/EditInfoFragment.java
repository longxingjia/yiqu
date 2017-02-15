package com.yiqu.iyijiayi.fragment.tab5;

import android.os.Bundle;
import android.view.View;

import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;

/**
 * Created by Administrator on 2017/2/15.
 */

public class EditInfoFragment extends AbsAllFragment {
    @Override
    protected int getTitleBarType() {
        return FLAG_BACK|FLAG_TXT;
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
        setTitleText("修改资料");

    }

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab1;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab5_edit_info_fragment;
    }

    @Override
    protected void initView(View v) {

    }

    @Override
    protected void init(Bundle savedInstanceState) {



        super.init(savedInstanceState);
    }
}
