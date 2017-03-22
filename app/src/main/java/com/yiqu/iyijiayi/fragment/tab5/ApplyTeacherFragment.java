package com.yiqu.iyijiayi.fragment.tab5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.TeacherApply;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;

/**
 * Created by Administrator on 2017/2/15.
 */

public class ApplyTeacherFragment extends AbsAllFragment {

    private String uid;
    private EditText name;
    private EditText school;
    private EditText title;
    private EditText desc;
    private Button sumbit;
    private String tag = "ApplyTeacherFragment";
    private EditText phonenum;
    private EditText from;
    private EditText city;

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
        setTitleText("成为导师");

    }

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab5_apply_teacher_fragment;
    }

    @Override
    protected void initView(View v) {
        name = (EditText) v.findViewById(R.id.name);
        school = (EditText) v.findViewById(R.id.school);
        title = (EditText) v.findViewById(R.id.et_title);
        desc = (EditText) v.findViewById(R.id.info);
        phonenum = (EditText) v.findViewById(R.id.phonenum);
        city = (EditText) v.findViewById(R.id.city);
        from = (EditText) v.findViewById(R.id.from);
        sumbit = (Button) v.findViewById(R.id.submit);
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        if (AppShare.getIsLogin(getActivity())) {
            uid = AppShare.getUserInfo(getActivity()).uid;
        } else {
            Model.startNextAct(getActivity(),
                    SelectLoginFragment.class.getName());
        }
        TeacherApply teacherApply = AppShare.getTeacherApplyInfo(getActivity());
        if (teacherApply != null) {
            name.setText(teacherApply.username);
            school.setText(teacherApply.school);
            title.setText(teacherApply.title);
            phonenum.setText(teacherApply.phone);
            city.setText(teacherApply.address);
            from.setText(teacherApply.source);
            desc.setText(teacherApply.desc);

            LogUtils.LOGE(tag,teacherApply.toString());

            RestNetCallHelper.callNet(getActivity(),
                    MyNetApiConfig.getTeacherApply, MyNetRequestConfig
                            .getTeacherApply(getActivity(), uid),
                    "getTeacherApply", ApplyTeacherFragment.this);


        }

        sumbit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String nameStr = name.getText().toString().trim();
                String schoolStr = school.getText().toString().trim();
                String titleStr = title.getText().toString().trim();
                String descStr = desc.getText().toString().trim();
                String phonenumStr = phonenum.getText().toString().trim();
                String cityStr = city.getText().toString().trim();
                String fromStr = desc.getText().toString().trim();
                if (nameStr.length() == 0 || schoolStr.length() == 0 || titleStr.length() == 0 || descStr.length() == 0
                        ||phonenumStr.length() == 0||cityStr.length() == 0||fromStr.length() == 0) {
                    ToastManager.getInstance(getActivity()).showText(
                            "请全部填写完毕后再提交!");
                    return;
                }

                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.addTeacherApply, MyNetRequestConfig
                                .addTeacherApply(getActivity(), uid, nameStr, schoolStr, titleStr, descStr,
                                        phonenumStr,cityStr,fromStr),
                        "addTeacherApply", ApplyTeacherFragment.this);
            }
        });


        super.init(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("申请导师"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("申请导师");
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        LogUtils.LOGE(tag,netResponse.toString());
        if (id.equals("addTeacherApply")) {
            if (type == NetCallBack.TYPE_SUCCESS) {

                TeacherApply teacherApply = new Gson().fromJson(netResponse.data, TeacherApply.class);
                LogUtils.LOGE(tag,teacherApply.toString());
                AppShare.setTeacherApplyInfo(getActivity(), teacherApply);
                if (teacherApply.status == 0) {
                    ToastManager.getInstance(getActivity()).showText("正在审核中，请耐心等待。。。");
                } else if (teacherApply.status == 1) {
                    ToastManager.getInstance(getActivity()).showText("恭喜您，审核已经通过。");
                } else if (teacherApply.status == -1) {
                    ToastManager.getInstance(getActivity()).showText("对不起，审核被拒绝了。");
                }
            }

        } else if (id.equals("getTeacherApply")) {
            if (type == NetCallBack.TYPE_SUCCESS) {
                TeacherApply teacherApply = new Gson().fromJson(netResponse.data, TeacherApply.class);
                if (teacherApply.status == 0) {
                    ToastManager.getInstance(getActivity()).showText("正在审核中，请耐心等待。。。");
                } else if (teacherApply.status == 1) {
                    ToastManager.getInstance(getActivity()).showText("恭喜您，审核已经通过。");
                } else if (teacherApply.status == -1) {
                    ToastManager.getInstance(getActivity()).showText("对不起，审核被拒绝了。");
                }
            }
        }
        super.onNetEnd(id, type, netResponse);
    }
}
