package com.yiqu.iyijiayi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.ui.views.CircleImageView;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.adapter.MenuDialogPicHelper;
import com.yiqu.iyijiayi.fragment.tab5.HomePageFragment;
import com.yiqu.iyijiayi.fragment.tab5.InfoFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.PageCursorView;
import com.yiqu.iyijiayi.utils.PictureUtils;

public class Tab5Fragment extends TabContentFragment implements View.OnClickListener {


    private Button Btlogin;
    private UserInfo userInfo;
    private CircleImageView head;
    private MenuDialogPicHelper mMenuDialogPicHelper;
    private RelativeLayout llUserInfo;
    private Button logOutBt;
    private TextView menu_item_wodeyijiayizhuye;
    private TextView menu_item_wowen;
    private TextView menu_item_woting;

    private TextView menu_item_wodeyibi;
    private TextView menu_item_shezhi;
    private TextView menu_item_jiesuanshuoming;
    private TextView menu_item_bangzhu;
    private TextView menu_item_guanyu;
    private TextView username;
    private TextView user_school;
    private TextView user_desc;
    private LinearLayout teacherOnly;
    private TextView menu_item_woping;
    private TextView menu_item_wodexizuo;
    private LinearLayout ll_tabs;
    private ImageView sex;
    private TextView content;
    private ImageView background;

    @Override
    protected int getTitleView() {

        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {

        return R.layout.tab5_fragment;
    }

    @Override
    protected void initView(View v) {


        Btlogin = (Button) v.findViewById(R.id.tab5_login);
        logOutBt = (Button) v.findViewById(R.id.logout);

        llUserInfo = (RelativeLayout) v.findViewById(R.id.userinfo);
        background = (ImageView) v.findViewById(R.id.background);
        ll_tabs = (LinearLayout) v.findViewById(R.id.ll_tabs);

        InitHeadView(v);

        menu_item_wodeyijiayizhuye = (TextView) v.findViewById(R.id.menu_item_wodeyijiayizhuye);
        menu_item_wowen = (TextView) v.findViewById(R.id.menu_item_wowen);  //学生独有
        teacherOnly = (LinearLayout) v.findViewById(R.id.mine_teacher);
        menu_item_woping = (TextView) v.findViewById(R.id.menu_item_woping);
        menu_item_wodexizuo = (TextView) v.findViewById(R.id.menu_item_wodexizuo);

        menu_item_woting = (TextView) v.findViewById(R.id.menu_item_woting);

        menu_item_wodeyibi = (TextView) v.findViewById(R.id.menu_item_wodeyibi);
        menu_item_shezhi = (TextView) v.findViewById(R.id.menu_item_shezhi);
        menu_item_jiesuanshuoming = (TextView) v.findViewById(R.id.menu_item_jiesuanshuoming);
//        menu_item_bangzhu = (TextView) v.findViewById(R.id.menu_item_bangzhu);
        menu_item_guanyu = (TextView) v.findViewById(R.id.menu_item_guanyu);


        menu_item_wodeyijiayizhuye.setOnClickListener(this);
        menu_item_wowen.setOnClickListener(this);
        menu_item_woting.setOnClickListener(this);

        menu_item_wodeyibi.setOnClickListener(this);
        menu_item_shezhi.setOnClickListener(this);
        menu_item_jiesuanshuoming.setOnClickListener(this);
//        menu_item_bangzhu.setOnClickListener(this);
        menu_item_guanyu.setOnClickListener(this);
        logOutBt.setOnClickListener(this);
        menu_item_woping.setOnClickListener(this);
        menu_item_wodexizuo.setOnClickListener(this);
    }

    public void InitHeadView(View v) {

        username = (TextView) v.findViewById(R.id.name);
        head = (CircleImageView) v.findViewById(R.id.head);
        user_desc = (TextView) v.findViewById(R.id.desc);
        content = (TextView) v.findViewById(R.id.content);
        sex = (ImageView) v.findViewById(R.id.sex);

//        name = (TextView) v.findViewById(R.id.name);
//
//        price = (TextView) v.findViewById(R.id.price);
//        questioncount = (TextView) v.findViewById(R.id.questioncount);
//        totalincome = (TextView) v.findViewById(R.id.totalincome);
//        tincome = (TextView) v.findViewById(R.id.tincome);
//        questionincome = (TextView) v.findViewById(R.id.questionincome);
//
//
//        dianping_tab = (LinearLayout) v.findViewById(R.id.dianping_tab);
//        tiwen_tab = (LinearLayout) v.findViewById(R.id.tiwen_tab);
//        tea = (LinearLayout) v.findViewById(R.id.tea);
//        stu = (LinearLayout) v.findViewById(R.id.stu);
//        xizuo_tab = (RelativeLayout) v.findViewById(R.id.xizuo_tab);


    }

    @Override
    protected boolean isTouchMaskForNetting() {

        return false;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        Btlogin.setOnClickListener(new View.OnClickListener() {
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

        if (isLogin) {
            userInfo=AppShare.getUserInfo(getActivity());
            Btlogin.setVisibility(View.GONE);
            RestNetCallHelper.callNet(getActivity(),
                    MyNetApiConfig.getUserByPhoneUid, MyNetRequestConfig.getUserByPhoneUid(
                            getActivity(), userInfo.uid), "getUserByPhoneUid", Tab5Fragment.this);


            llUserInfo.setVisibility(View.VISIBLE);

            logOutBt.setVisibility(View.VISIBLE);
        } else {
            logOutBt.setVisibility(View.GONE);
            ll_tabs.setVisibility(View.GONE);
            llUserInfo.setVisibility(View.GONE);
            Btlogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        //   LogUtils.LOGE("hhhh");
        initUI();
        super.onResume();
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected int getTitleBarType() {

        return FLAG_TXT;
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
        setTitleText(getString(R.string.label_tab5));
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        super.onNetEnd(id, type, netResponse);

        if (type == NetCallBack.TYPE_SUCCESS) {
            Gson gson = new Gson();
            userInfo = gson.fromJson(netResponse.data.toString(), UserInfo.class);
            AppShare.setIsLogin(getActivity(), true);
            AppShare.setUserInfo(getActivity(), userInfo);
        }
        String descStr = String.format("%s | 粉丝:%s | 收听:%s", userInfo.title, userInfo.followcount, userInfo.myfollowcount);
        content.setText(descStr);
        username.setText(userInfo.username);
        content.setText(descStr);
        if (!TextUtils.isEmpty(userInfo.desc)) {
            user_desc.setText(userInfo.desc);
        }
        head.setOnClickListener(this);
        PictureUtils.showPicture(getActivity(), userInfo.userimage, head);
        PictureUtils.showPicture(getActivity(), userInfo.backgroundimage, background);

        if (userInfo.sex.equals("0")) {
            sex.setBackgroundResource(R.mipmap.sex_female);
        } else {
            sex.setBackgroundResource(R.mipmap.sex_male);
        }

        ll_tabs.setVisibility(View.VISIBLE);
        if (userInfo.type.equals("1")) {  //1是学生
            menu_item_wowen.setVisibility(View.VISIBLE);
            teacherOnly.setVisibility(View.GONE);
        } else {
            menu_item_wowen.setVisibility(View.GONE);
            teacherOnly.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head:
//                mMenuDialogPicHelper.show(v, head);

                if (AppShare.getIsLogin(getActivity())) {
                    Model.startNextAct(getActivity(),
                            InfoFragment.class.getName());
                } else {
                    Model.startNextAct(getActivity(),
                            SelectLoginFragment.class.getName());
                }

                break;
            case R.id.logout:
                AppShare.setIsLogin(getActivity(), false);
                AppShare.clearShare(getActivity());
                initUI();

                break;
            case R.id.menu_item_wodeyijiayizhuye:
                Intent i = new Intent(getActivity(), StubActivity.class);
                i.putExtra("fragment", HomePageFragment.class.getName());
                getActivity().startActivity(i);
                break;
        }

    }
}
