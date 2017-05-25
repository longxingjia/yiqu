package com.yiqu.iyijiayi.fragment.tab5;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.ui.views.DialogUtil;
import com.umeng.analytics.MobclickAgent;
import com.utils.L;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.TeacherApply;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.utils.LogUtils;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;

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
    private TextView tips;

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
        tips = (TextView) v.findViewById(R.id.tips);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tips.setText(Html.fromHtml("当您提交了以上真实信息后，" +
                "我们工作人员会第一时间联系您，还请耐心等候！一般处理周期为" +
                "<font color=\'#ff0000\'>3个工作日</font>" + "左右"));
        if (AppShare.getIsLogin(getActivity())) {
            UserInfo userInfo = AppShare.getUserInfo(getActivity());
            uid = userInfo.uid;
            String UserImage = userInfo.userimage;
            if (TextUtils.isEmpty(UserImage)){
                DialogUtil.showDialog(getActivity(),"请先上传您的头像");
                getActivity().finish();
            }

        } else {
            Model.startNextAct(getActivity(),
                    SelectLoginFragment.class.getName());
        }
//        TeacherApply teacherApply = AppShare.getTeacherApplyInfo(getActivity());
//        if (teacherApply != null) {
//            name.setText(teacherApply.username);
//            school.setText(teacherApply.school);
//            title.setText(teacherApply.title);
//            phonenum.setText(teacherApply.phone);
//            city.setText(teacherApply.address);
//            from.setText(teacherApply.source);
//            desc.setText(teacherApply.desc);

       //     LogUtils.LOGE(tag, teacherApply.toString());

//            RestNetCallHelper.callNet(getActivity(),
//                    MyNetApiConfig.getTeacherApply, MyNetRequestConfig
//                            .getTeacherApply(getActivity(), uid),
//                    "getTeacherApply", ApplyTeacherFragment.this);


//        }

        RestNetCallHelper.callNet(getActivity(),
                MyNetApiConfig.getTeacherApply, MyNetRequestConfig
                        .getTeacherApply(getActivity(), uid),
                "getTeacherApply", ApplyTeacherFragment.this);

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
                        || phonenumStr.length() == 0 || cityStr.length() == 0 || fromStr.length() == 0) {
                    ToastManager.getInstance(getActivity()).showText(
                            "请全部填写完毕后再提交!");
                    return;
                }

                if (phonenumStr.length() != 11) {
                    ToastManager.getInstance(getActivity()).showText(
                            "请输入正确的手机号码!");
                    return;
                }

                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.addTeacherApply, MyNetRequestConfig
                                .addTeacherApply(getActivity(), uid, nameStr, schoolStr, titleStr, descStr,
                                        phonenumStr, cityStr, fromStr),
                        "addTeacherApply", ApplyTeacherFragment.this);
            }
        });


        super.init(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("申请导师"); //统计页面，"MainScreen"为页面名称，可自定义
        JAnalyticsInterface.onPageStart(getActivity(), "申请导师");
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("申请导师");
        JAnalyticsInterface.onPageEnd(getActivity(), "申请导师");
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        //      LogUtils.LOGE(tag, netResponse.toString());
        if (id.equals("addTeacherApply")) {
            if (type == NetCallBack.TYPE_SUCCESS) {
                L.e(netResponse.toString());

                TeacherApply teacherApply = new Gson().fromJson(netResponse.data, TeacherApply.class);
           //     LogUtils.LOGE(tag, teacherApply.toString());
            //    AppShare.setTeacherApplyInfo(getActivity(), teacherApply);
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
                L.e(netResponse.toString());
                TeacherApply teacherApply = new Gson().fromJson(netResponse.data, TeacherApply.class);
                name.setText(teacherApply.username);
                school.setText(teacherApply.school);
                title.setText(teacherApply.title);
                phonenum.setText(teacherApply.phone);
                city.setText(teacherApply.address);
                from.setText(teacherApply.source);
                desc.setText(teacherApply.desc);

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
