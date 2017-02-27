package com.yiqu.iyijiayi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fwrestnet.NetResponse;
import com.ui.views.CircleImageView;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.adapter.MenuDialogPicHelper;
import com.yiqu.iyijiayi.fragment.tab5.HomePageFragment;
import com.yiqu.iyijiayi.fragment.tab5.InfoFragment;
import com.yiqu.iyijiayi.fragment.tab5.SelectLoginFragment;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;

public class Tab5Fragment extends TabContentFragment implements View.OnClickListener {


    private Button Btlogin;
    private UserInfo userInfo;
    private CircleImageView head;
    private MenuDialogPicHelper mMenuDialogPicHelper;
    private LinearLayout llUserInfo;
    private Button loginOutBt;
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

        Btlogin = (Button) v.findViewById(R.id.tab5_login);
        loginOutBt = (Button) v.findViewById(R.id.logout);
        head = (CircleImageView) v.findViewById(R.id.head);
        llUserInfo = (LinearLayout) v.findViewById(R.id.userinfo);

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
        username = (TextView) v.findViewById(R.id.username);
        user_school = (TextView) v.findViewById(R.id.user_school);
        user_desc = (TextView) v.findViewById(R.id.user_desc);

        menu_item_wodeyijiayizhuye .setOnClickListener(this);
        menu_item_wowen .setOnClickListener(this);
        menu_item_woting .setOnClickListener(this);

        menu_item_wodeyibi .setOnClickListener(this);
        menu_item_shezhi .setOnClickListener(this);
        menu_item_jiesuanshuoming .setOnClickListener(this);
//        menu_item_bangzhu.setOnClickListener(this);
        menu_item_guanyu .setOnClickListener(this);
        loginOutBt.setOnClickListener(this);
        menu_item_woping.setOnClickListener(this);
        menu_item_wodexizuo.setOnClickListener(this);
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
                //headBase64 = url;
            }
        });
      //  initUI();

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
        if(isLogin){
            Btlogin.setVisibility(View.GONE);
            userInfo = AppShare.getUserInfo(getActivity());
            user_school.setText(userInfo.school);
            user_desc.setText(userInfo.desc);
           // LogUtils.LOGE(Tab5Fragment.class.getName(),userInfo.toString());


            if(userInfo.type.equals("1")){  //1是学生
                menu_item_wowen.setVisibility(View.VISIBLE);
                teacherOnly.setVisibility(View.GONE);
            }else {
                menu_item_wowen.setVisibility(View.GONE);
                teacherOnly.setVisibility(View.VISIBLE);
            }

            llUserInfo.setVisibility(View.VISIBLE);
            head.setOnClickListener(this);
//            LogUtils.LOGE(userInfo.toString());
            username.setText(userInfo.username);

        }else {
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head:
//                mMenuDialogPicHelper.show(v, head);

                if (AppShare.getIsLogin(getActivity())){
                    Model.startNextAct(getActivity(),
                            InfoFragment.class.getName());
                }else{
                    Model.startNextAct(getActivity(),
                            SelectLoginFragment.class.getName());
                }

                break;
            case R.id.logout:
                AppShare.setIsLogin(getActivity(),false);
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
