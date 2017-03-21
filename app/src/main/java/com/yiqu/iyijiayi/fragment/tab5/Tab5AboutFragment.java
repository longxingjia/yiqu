package com.yiqu.iyijiayi.fragment.tab5;

import android.content.Intent;
import android.view.View;

import com.base.utils.ToastManager;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.utils.AppAvilibleUtils;
import com.yiqu.iyijiayi.wxapi.WXEntryActivity;

/**
 * Created by Administrator on 2017/2/9.
 */

public class Tab5AboutFragment extends AbsAllFragment {

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getTitleBarType() {
        return FLAG_TXT|FLAG_BACK;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab5_about_fragment;
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
        setTitleText("关于我们");

    }


    @Override
    protected void initView(View v) {


    }


}
