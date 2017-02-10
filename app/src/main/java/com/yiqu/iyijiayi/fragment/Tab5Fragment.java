package com.yiqu.iyijiayi.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;

import com.fwrestnet.NetResponse;
import com.ui.views.LoadMoreView;
import com.ui.views.LoadMoreView.OnMoreListener;
import com.ui.views.RefreshList;
import com.ui.views.RefreshList.IRefreshListViewListener;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.fragment.menu.LoginFragment;
import com.yiqu.iyijiayi.fragment.menu.RegisterFragment;
import com.yiqu.iyijiayi.fragment.menu.SelectLoginFragment;
import com.yiqu.iyijiayi.fragment.menu.TabRegisterOrLoginFragment;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;

import static android.R.attr.onClick;

public class Tab5Fragment extends TabContentFragment implements OnMoreListener, IRefreshListViewListener,View.OnClickListener {


    private Button loginBt;
    private UserInfo userInfo;

    @Override
    protected int getTitleView() {

        return R.layout.titlebar_tab1;
    }

    @Override
    protected int getBodyView() {

        return R.layout.tab5_fragment;
    }

    @Override
    protected void initView(View v) {

        loginBt = (Button) v.findViewById(R.id.login);



    }

    @Override
    protected boolean isTouchMaskForNetting() {

        return false;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        boolean isLogin = AppShare.getIsLogin(getActivity());
        if(isLogin){
            loginBt.setVisibility(View.GONE);
            userInfo = AppShare.getUserInfo(getActivity());
        }else {
            loginBt.setVisibility(View.VISIBLE);
        }


        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.startNextAct(getActivity(),
                        SelectLoginFragment.class.getName());
            }
        });
//
    }


    @Override
    public void onDestroy() {


        super.onDestroy();
    }

    @Override
    protected int getTitleBarType() {

        return FLAG_BACK|FLAG_TXT;
    }

    @Override
    protected boolean onPageBack() {

        if (mOnFragmentListener != null) {
            mOnFragmentListener.onFragmentBack(this);
        }
        return true;
    }

    @Override
    protected boolean onPageNext() {
        return false;
    }

    @Override
    protected void initTitle() {
        // TODO Auto-generated method stub
        setTitleText(getString(R.string.label_tab5));
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        // TODO Auto-generated method stub
//
        super.onNetEnd(id, type, netResponse);
    }

    @Override
    public boolean onMore(AbsListView view) {
        // TODO Auto-generated method stub
//
        return false;
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
//
    }


    @Override
    public void onClick(View v) {

    }
}
