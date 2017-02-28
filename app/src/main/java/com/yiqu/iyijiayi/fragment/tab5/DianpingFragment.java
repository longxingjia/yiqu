package com.yiqu.iyijiayi.fragment.tab5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.ui.views.CircleImageView;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.MenuDialogSexHelper;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;

/**
 * Created by Administrator on 2017/2/15.
 */

public class DianpingFragment extends AbsAllFragment implements View.OnClickListener {



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
        setTitleText("修改资料");

    }

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab5_info_fragment;
    }

    @Override
    protected void initView(View v) {


    }

    @Override
    protected void init(Bundle savedInstanceState) {

//        if (AppShare.getIsLogin(getActivity())) {
//            userInfo = AppShare.getUserInfo(getActivity());
//        } else {
//            Model.startNextAct(getActivity(),
//                    SelectLoginFragment.class.getName());
//            return;
//        }



        super.init(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rl_edit_sex:
//                MenuDialogSexHelper menuDialogSexHelper = new MenuDialogSexHelper(this, new MenuDialogSexHelper.SexListener() {
//                    @Override
//                    public void onSex(String sex) {
//                        RestNetCallHelper.callNet(getActivity(),
//                                MyNetApiConfig.editUser, MyNetRequestConfig
//                                        .editUser(getActivity(), userInfo.uid, "sex", sex),
//                                "sex", DianpingFragment.this);
//
//                    }
//                });
//                menuDialogSexHelper.show(rl_edit_sex);
        }

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
//        if (type == NetCallBack.TYPE_SUCCESS) {
//         //   LogUtils.LOGE(tag,netResponse.result);
//            Gson gson = new Gson();
//            UserInfo userInfo = gson.fromJson(netResponse.data,UserInfo.class);
//            AppShare.setUserInfo(getActivity(),userInfo);
//            if (userInfo.sex.equals("1")) {
//                edit_sex.setText("男");
//            } else {
//                edit_sex.setText("女");
//            }
//        }else if (type ==NetCallBack.TYPE_ERROR){
//            ToastManager.getInstance(getActivity()).showText(netResponse.result);
//        }

        super.onNetEnd(id, type, netResponse);
    }
}
