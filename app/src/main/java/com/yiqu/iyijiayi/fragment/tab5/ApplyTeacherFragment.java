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
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.model.Model;
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
        sumbit = (Button) v.findViewById(R.id.submit);
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        if (AppShare.getIsLogin(getActivity())){
            uid = AppShare.getUserInfo(getActivity()).uid;
        }else{
            Model.startNextAct(getActivity(),
                    SelectLoginFragment.class.getName());
        }

        sumbit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String   nameStr = name.getText().toString().trim();
             String  schoolStr = school.getText().toString().trim();
             String  titleStr = title.getText().toString().trim();
             String  descStr = desc.getText().toString().trim();
                if (nameStr.length() == 0||schoolStr.length() == 0||titleStr.length() == 0||descStr.length() == 0) {
                    ToastManager.getInstance(getActivity()).showText(
                            "请全部填写完毕后再提交!");
                    return;
                }

                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.addTeacherApply, MyNetRequestConfig
                                .addTeacherApply(getActivity(), uid,nameStr , schoolStr,titleStr,descStr),
                        "addTeacherApply", ApplyTeacherFragment.this);
            }
        });


        super.init(savedInstanceState);
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        if (type== NetCallBack.TYPE_SUCCESS){
            Gson gson = new Gson();
            UserInfo userInfo = gson.fromJson(netResponse.data,UserInfo.class);
            AppShare.setUserInfo(getActivity(),userInfo);

            Intent i = new Intent(getActivity(), StubActivity.class);
            i.putExtra("fragment",  InfoFragment.class.getName());

            getActivity().startActivity(i);
        }
        super.onNetEnd(id, type, netResponse);
    }
}
