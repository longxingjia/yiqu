package com.yiqu.iyijiayi.fragment.tab5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.model.PayInfo;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppAvilibleUtils;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.wxapi.WXPayEntryActivity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/2/15.
 */

public class PayforYBFragment extends AbsAllFragment implements View.OnClickListener {

    private String tag = "PayforYBFragment";
    private TextView mine_yibi;
    private TextView price;
    private int requestCode = 0x1;
    private UserInfo userInfo;
    private RadioGroup rg;
    private RadioButton b1;
    private RadioButton b2;
    private RadioButton b3;

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
        setTitleText("艺币充值");
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("艺币充值"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("艺币充值");
    }

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.payfor_yibi_fragment;
    }

    @Override
    protected void initView(View v) {
        mine_yibi = (TextView) v.findViewById(R.id.mine_yibi);
        price = (TextView) v.findViewById(R.id.price);
        v.findViewById(R.id.submit).setOnClickListener(this);

        rg = (RadioGroup) v.findViewById(R.id.radioGroup1);
        b1 = (RadioButton) v.findViewById(R.id.radio1);
        b2 = (RadioButton) v.findViewById(R.id.radio2);
        b3 = (RadioButton) v.findViewById(R.id.radio3);

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if (AppShare.getIsLogin(getActivity())) {
            userInfo = AppShare.getUserInfo(getActivity());
        } else {
            return;
        }
        String coin = "0";
        if (userInfo.coin_apple > 0) {
            coin = userInfo.coin_apple + "00";
        }

        mine_yibi.setText(coin);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == b1.getId()) {
                    price.setText("6");
                }
                if (checkedId == b2.getId()) {
                    price.setText("12");
                }
                if (checkedId == b3.getId()) {
                    price.setText("50");
                }


            }

        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:


                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.getUserByPhoneUid, MyNetRequestConfig.getUserByPhoneUid(
                                getActivity(), userInfo.uid), "getUserByPhoneUid", PayforYBFragment.this, true, true);

                break;
            case RESULT_CANCELED:
                break;
            default:
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:

                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.getNewCoinOrder, MyNetRequestConfig.getNewCoinOrder(
                                getActivity(), userInfo.uid, "1", "1", price.getText().toString().trim()), "getNewCoinOrder", PayforYBFragment.this, true, true);

                break;
        }

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        super.onNetEnd(id, type, netResponse);
        if (id.equals("getUserByPhoneUid")) {
            Gson gson = new Gson();
            userInfo = gson.fromJson(netResponse.data.toString(), UserInfo.class);
            AppShare.setUserInfo(getActivity(), userInfo);
            mine_yibi.setText(userInfo.coin_apple + "00");

        } else if (id.equals("getNewCoinOrder")) {
            if (type == TYPE_SUCCESS) {

                if (AppAvilibleUtils.isWeixinAvilible(getActivity())) {
                    PayInfo payInfo = new Gson().fromJson(netResponse.data, PayInfo.class);
                    Intent i = new Intent(getActivity(), WXPayEntryActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("data", payInfo.wx_arr);
                    i.putExtras(b);
                    startActivityForResult(i, requestCode);
                } else {
                    ToastManager.getInstance(getActivity()).showText("您还没有安装微信，请您先安装微信。");
                }

            }
        }

    }
}
