package com.yiqu.iyijiayi.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fwrestnet.NetResponse;
import com.ui.views.CircleImageView;
import com.ui.views.LoadMoreView;
import com.ui.views.LoadMoreView.OnMoreListener;
import com.ui.views.RefreshList;
import com.ui.views.RefreshList.IRefreshListViewListener;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.adapter.MenuDialogPicHelper;
import com.yiqu.iyijiayi.fragment.menu.LoginFragment;
import com.yiqu.iyijiayi.fragment.menu.RegisterFragment;
import com.yiqu.iyijiayi.fragment.menu.SelectLoginFragment;
import com.yiqu.iyijiayi.fragment.menu.TabRegisterOrLoginFragment;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;
import com.yiqu.iyijiayi.utils.LogUtils;

import static android.R.attr.background;
import static android.R.attr.onClick;

public class Tab5Fragment extends TabContentFragment implements OnMoreListener, IRefreshListViewListener,View.OnClickListener {


    private Button loginBt;
    private UserInfo userInfo;
    private CircleImageView head;
    private MenuDialogPicHelper mMenuDialogPicHelper;
    private LinearLayout llUserInfo;
    private Button loginOutBt;
    private TextView menu_item_wodeyijiayizhuye;
    private TextView menu_item_wowen;
    private TextView menu_item_woting;
    private TextView menu_item_wodeyue;
    private TextView menu_item_wodeyibi;
    private TextView menu_item_shezhi;
    private TextView menu_item_jiesuanshuoming;
    private TextView menu_item_bangzhu;
    private TextView menu_item_guanyu;
    private TextView username;
    private TextView user_school;
    private TextView user_desc;

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
        loginOutBt = (Button) v.findViewById(R.id.logout);
        head = (CircleImageView) v.findViewById(R.id.head);
        llUserInfo = (LinearLayout) v.findViewById(R.id.userinfo);
        menu_item_wodeyijiayizhuye = (TextView) v.findViewById(R.id.menu_item_wodeyijiayizhuye);
        menu_item_wowen = (TextView) v.findViewById(R.id.menu_item_wowen);
        menu_item_woting = (TextView) v.findViewById(R.id.menu_item_woting);
        menu_item_wodeyue = (TextView) v.findViewById(R.id.menu_item_wodeyue);
        menu_item_wodeyibi = (TextView) v.findViewById(R.id.menu_item_wodeyibi);
        menu_item_shezhi = (TextView) v.findViewById(R.id.menu_item_shezhi);
        menu_item_jiesuanshuoming = (TextView) v.findViewById(R.id.menu_item_jiesuanshuoming);
        menu_item_bangzhu = (TextView) v.findViewById(R.id.menu_item_bangzhu);
        menu_item_guanyu = (TextView) v.findViewById(R.id.menu_item_guanyu);
        username = (TextView) v.findViewById(R.id.username);
        user_school = (TextView) v.findViewById(R.id.user_school);
        user_desc = (TextView) v.findViewById(R.id.user_desc);


    }

    @Override
    protected boolean isTouchMaskForNetting() {

        return false;
    }

    @Override
    protected void init(Bundle savedInstanceState) {



        mMenuDialogPicHelper = new MenuDialogPicHelper(this, new MenuDialogPicHelper.BitmapListener() {
            @Override
            public void onBitmapUrl(String url) {
                // TODO Auto-generated method stub
                //headBase64 = url;
            }
        });
        initUI();

        menu_item_wodeyijiayizhuye .setOnClickListener(this);
        menu_item_wowen .setOnClickListener(this);
        menu_item_woting .setOnClickListener(this);
        menu_item_wodeyue .setOnClickListener(this);
        menu_item_wodeyibi .setOnClickListener(this);
        menu_item_shezhi .setOnClickListener(this);
        menu_item_jiesuanshuoming .setOnClickListener(this);
        menu_item_bangzhu.setOnClickListener(this);
        menu_item_guanyu .setOnClickListener(this);
        loginOutBt.setOnClickListener(this);

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.startNextAct(getActivity(),
                        SelectLoginFragment.class.getName());
            }
        });
//
    }

    private void initUI() {
        boolean isLogin = AppShare.getIsLogin(getActivity());
        if(isLogin){
            loginBt.setVisibility(View.GONE);
            userInfo = AppShare.getUserInfo(getActivity());
            llUserInfo.setVisibility(View.VISIBLE);
            head.setOnClickListener(this);
            LogUtils.LOGE(userInfo.toString());

            username.setText(userInfo.username);


        }else {
            llUserInfo.setVisibility(View.GONE);
            loginBt.setVisibility(View.VISIBLE);
        }
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
        switch (v.getId()){
            case R.id.head:
                mMenuDialogPicHelper.show(v, head);
                break;
            case R.id.logout:
                AppShare.setIsLogin(getActivity(),false);
                initUI();

                break;
        }

    }
}
