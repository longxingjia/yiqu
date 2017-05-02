package com.yiqu.iyijiayi.fragment.tab5;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.base.utils.ToastManager;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.DensityUtil;

/**
 * Created by Administrator on 2017/2/15.
 */

public class EditInfoFragment extends AbsAllFragment {

    private String data;
    private EditText content;
    private Button sumbit;
    private String contentStr;
    private String datastr = "";
    private String uid;
    private String tag = "EditInfoFragment";

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
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("修改个人信息"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("修改个人信息");
    }

    @Override
    protected void initTitle() {
        data = getActivity().getIntent().getStringExtra("data");

        switch (data) {
            case "username":
                datastr = "名字";

                break;
            case "school":
                datastr = "学校";
                break;
            case "specialities":
                datastr = "专业";
                break;
            case "sex":
                datastr = "性别";
                break;
            case "desc":
                datastr = "个人介绍";
                break;
            case "price":
                datastr = "收费";
                break;
            case "advices":
                datastr = "建议反馈";
                break;
        }
        if (data.equals("advices")){
            setTitleText(datastr);
        }else {
            setTitleText("修改"+datastr);
        }

    }

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab5_edit_info_fragment;
    }

    @Override
    protected void initView(View v) {
        content = (EditText) v.findViewById(R.id.content);
        sumbit = (Button) v.findViewById(R.id.submit);

        if (AppShare.getIsLogin(getActivity())) {
            uid = AppShare.getUserInfo(getActivity()).uid;
        } else {
            Model.startNextAct(getActivity(),
                    SelectLoginFragment.class.getName());
        }

        sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentStr = content.getText().toString().trim();
                if (contentStr.length() == 0) {
                    ToastManager.getInstance(getActivity()).showText(
                            "请输入您要修改的内容!");
                    return;
                }
                if (data.equals("advices")) {
                    RestNetCallHelper.callNet(getActivity(),
                            MyNetApiConfig.addFeedback, MyNetRequestConfig
                                    .addFeedback(getActivity(), uid, contentStr),
                            "addFeedback", EditInfoFragment.this);
                } else {
                    RestNetCallHelper.callNet(getActivity(),
                            MyNetApiConfig.editUser, MyNetRequestConfig
                                    .editUser(getActivity(), uid, data, contentStr),
                            "editUser", EditInfoFragment.this);
                }

            }
        });
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        if (id.equals("editUser")) {
            if (netResponse != null) {
                if (netResponse.bool == 1) {
                    Gson gson = new Gson();
                    UserInfo userInfo = gson.fromJson(netResponse.data, UserInfo.class);

                    AppShare.setUserInfo(getActivity(), userInfo);
//                Intent i = new Intent(getActivity(), StubActivity.class);
//                i.putExtra("fragment", InfoFragment.class.getName());
////                    i.putExtra("fromLogin", "10");
                    getActivity().finish();
//                getActivity().startActivity(i);


                } else {
                    ToastManager.getInstance(getActivity()).showText(netResponse.result);
                }
            }
        } else if (id.equals("addFeedback")) {
            if (type==TYPE_SUCCESS){
                getActivity().finish();
                ToastManager.getInstance(getActivity()).showText("提交成功，请耐心等下我们的工作人员与您联系");
            }


        }

        super.onNetEnd(id, type, netResponse);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        content.setHint("请填写您的" + datastr);
        if (data.equals("desc") || data.equals("advices")) {

            ViewGroup.LayoutParams lp = content.getLayoutParams();
            lp.height = DensityUtil.dip2px(getActivity(), 200);
            content.setLayoutParams(lp);
            content.setGravity(Gravity.TOP);
            content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
            //  content.setHeight(DensityUtil.dip2px(getActivity(),200));
        }else if (data.equals("username")){
            content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        }

        super.init(savedInstanceState);
    }
}
