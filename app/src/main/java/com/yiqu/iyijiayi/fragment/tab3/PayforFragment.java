package com.yiqu.iyijiayi.fragment.tab3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Tool.Global.Variable;
import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.DialogHelper;
import com.yiqu.iyijiayi.model.ComposeVoice;
import com.yiqu.iyijiayi.model.Constant;
import com.yiqu.iyijiayi.model.Music;
import com.yiqu.iyijiayi.model.Order;
import com.yiqu.iyijiayi.model.PayInfo;
import com.yiqu.iyijiayi.model.Teacher;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.model.Wx_arr;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.net.UploadImage;
import com.yiqu.iyijiayi.utils.AppAvilibleUtils;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.wxapi.WXPayEntryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/2/15.
 */

public class PayforFragment extends AbsAllFragment implements View.OnClickListener {

    private String tag = "PayforFragment";
    private TextView order_number;
    private TextView price;
    private int requestCode = 0x1;
    private Wx_arr wx_arr;

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
        setTitleText("订单确认");
    }

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.payfor_fragment;
    }

    @Override
    protected void initView(View v) {
        order_number = (TextView) v.findViewById(R.id.order_number);
        price = (TextView) v.findViewById(R.id.price);
        v.findViewById(R.id.submit).setOnClickListener(this);
        // v.findViewById(R.id.title_back).setOnClickListener(this);


    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Intent intent = getActivity().getIntent();
        PayInfo payInfo = (PayInfo) intent.getSerializableExtra("data");
        wx_arr = payInfo.wx_arr;
        order_number.setText(payInfo.order.order_number);
        price.setText(payInfo.order.price + "");


    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("支付");

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("支付");

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:

                Intent intent = new Intent();
                getActivity().setResult(Constant.FINISH, intent);

                getActivity().finish();
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
                if (AppAvilibleUtils.isWeixinAvilible(getActivity())) {
                    Intent i = new Intent(getActivity(), WXPayEntryActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("data", wx_arr);
                    i.putExtras(b);
                    startActivityForResult(i, requestCode);
                } else {
                    ToastManager.getInstance(getActivity()).showText("您还没有安装微信，请您先安装微信。");
                }

                break;
        }


    }


    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        super.onNetEnd(id, type, netResponse);

    }
}
