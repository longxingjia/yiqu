package com.yiqu.iyijiayi.fragment.tab3;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.model.Constant;
import com.yiqu.iyijiayi.model.Order;
import com.yiqu.iyijiayi.model.PayInfo;
import com.yiqu.iyijiayi.model.Teacher;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.model.Wx_arr;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/2/15.
 */

public class TextQuestionFragment extends AbsAllFragment implements View.OnClickListener, TextWatcher {
    private static final String TAG = "TextQuestionFragment";

    @BindView(R.id.ll_select)
    public LinearLayout ll_select;
    @BindView(R.id.txt02)
    public EditText questionDesc;
    @BindView(R.id.tea_name)
    public TextView tea_name;
    @BindView(R.id.tea_price)
    public TextView tea_price;
    @BindView(R.id.times)
    public TextView times;
    @BindView(R.id.submit)
    public ImageView submit;
    private static int requestCode = 2;
    private String username;
    private String price;
    private String uid;
    private UserInfo userInfo;

    @OnClick(R.id.submit)
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.submit:
                // LogUtils.LOGE("t",userInfo.uid+"-"+uid);
                String isfree = "1";
                if (userInfo.free_question<=0){
                    isfree = "0";
                }
                RestNetCallHelper.callNet(
                        getActivity(),
                        MyNetApiConfig.addSound,
                        MyNetRequestConfig.addSound(getActivity(), userInfo.uid, uid, isfree, questionDesc.getText().toString(), price),
                        "addSound", TextQuestionFragment.this);
                break;
            default:
                break;
        }
    }

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.text_qustion_fragment;
    }

    @Override
    protected void initView(View v) {
        ButterKnife.bind(this, v);
        //  UserInfo userInfo = (UserInfo) getActivity().getIntent().getSerializableExtra("data");


    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        tea_name.addTextChangedListener(this);
        questionDesc.addTextChangedListener(this);
        userInfo = AppShare.getUserInfo(getActivity());
        times.setText(String.valueOf(userInfo.free_question));


        ll_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), StubActivity.class);
                i.putExtra("fragment", TeacherListFragment.class.getName());
                startActivityForResult(i, requestCode);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("问题描述");

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        super.onNetEnd(id, type, netResponse);
        if (id.equals("addSound")) {
            if (type == TYPE_SUCCESS) {

                try {
                    JSONObject jsonObject = new JSONObject(netResponse.data);
                    String sid = jsonObject.getString("sid");

                    RestNetCallHelper.callNet(
                            getActivity(),
                            MyNetApiConfig.getNewOrder,
                            MyNetRequestConfig.getNewOrder(getActivity(), userInfo.uid, sid, price),
                            "getNewOrder", TextQuestionFragment.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                ToastManager.getInstance(getActivity()).showText(netResponse.result);
            }

        } else if (id.equals("getNewOrder"))

        {
            if (type == NetCallBack.TYPE_SUCCESS) {

                try {

                    if (netResponse.data.contains("wx_arr")) {
                        PayInfo payInfo = new Gson().fromJson(netResponse.data, PayInfo.class);
                        Order order = payInfo.order;
                        Wx_arr wx_arr = payInfo.wx_arr;

                        Intent i = new Intent(getActivity(), StubActivity.class);
                        i.putExtra("fragment", PayforFragment.class.getName());
                        Bundle b = new Bundle();
                        b.putSerializable("data", payInfo);
                        i.putExtras(b);
                        startActivityForResult(i, requestCode);
                    } else {
                        Order order = new Gson().fromJson(netResponse.data, Order.class);
                        RestNetCallHelper.callNet(
                                getActivity(),
                                MyNetApiConfig.orderQuery,
                                MyNetRequestConfig.orderQuery(getActivity(), order.order_number),
                                "orderQuery", TextQuestionFragment.this);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (id.equals("orderQuery"))

        {

            if (type == NetCallBack.TYPE_SUCCESS) {
                ToastManager.getInstance(getActivity()).showText("提问成功,请等待老师回答");
                userInfo.free_question--;
                AppShare.setUserInfo(getActivity(), userInfo);
                getActivity().finish();
            } else {
                ToastManager.getInstance(getActivity()).showText("提问失败，请重试");
            }
        }
    }



    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("问题描述");

    }

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
        setTitleText("问题描述");

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(tea_name.getText().toString()) || TextUtils.isEmpty(questionDesc.getText().toString())) {

            submit.setBackgroundResource(R.mipmap.submit_unclick);
            submit.setEnabled(false);
        } else {
            submit.setBackgroundResource(R.mipmap.submit_clickable);
            submit.setEnabled(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                Bundle b = data.getExtras(); //data为B中回传的Intent
                //str即为回传的值
                Teacher teacher = (Teacher) b.getSerializable("teacher");
                username = teacher.username;
                price = teacher.price;
                uid = teacher.uid;
                tea_name.setText(username);
                tea_price.setText(price);
                break;
            case Constant.FINISH:
                getActivity().finish();
            default:
                break;
        }
    }
}
