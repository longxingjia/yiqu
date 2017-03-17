package com.yiqu.iyijiayi.fragment.tab5;

import android.content.ContentValues;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.yiqu.iyijiayi.MainActivity;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.fragment.Tab5Fragment;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.model.WechatUserInfo;
import com.yiqu.iyijiayi.model.YzmKey;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.wxapi.WXEntryActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetPhoneFragment extends AbsAllFragment {

    EditText txt01;
    EditText txt02;

    TextView btn01;
    Button btn02;

    private Count mMyCount;

    private String phonenum;
    private String key;

    /* 定义一个倒计时的内部类 */
    class Count extends CountDownTimer {
        public Count(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            btn01.setEnabled(true);
            btn01.setText(R.string.resend_verification_code_normal);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn01.setEnabled(false);

//			String tr = getResources().getString(
//					R.string.resend_verification_code);
            btn01.setText("已发送" +
                    millisUntilFinished / 1000 + "s");
        }
    }


    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.set_phone_fragment;
    }

    @Override
    protected void initView(View v) {
        txt01 = (EditText) v.findViewById(R.id.txt01);
        txt02 = (EditText) v.findViewById(R.id.txt02);

        btn01 = (TextView) v.findViewById(R.id.btn01);
        btn02 = (Button) v.findViewById(R.id.btn02);


        btn01.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                phonenum = txt01.getText().toString().trim();

                if (phonenum.length() == 0) {
                    ToastManager.getInstance(getActivity()).showText(
                            "请输入您的手机号码");
                    return;
                }
                if (phonenum.length() != 11) {
                    ToastManager.getInstance(getActivity()).showText(
                            "手机号格式有误，请您核对后重新输入");
                    return;
                }


                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.getLoginMessageCode, MyNetRequestConfig
                                .getLoginMessageCode(getActivity(), phonenum),
                        "getLoginMessageCode", SetPhoneFragment.this);

            }
        });

        btn02.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String code = txt02.getText().toString().trim();
                phonenum = txt01.getText().toString().trim();

                if (phonenum.length() == 0) {
                    ToastManager.getInstance(getActivity()).showText(
                            "请输入您的手机号码");
                    return;
                }
                if (phonenum.length() != 11) {
                    ToastManager.getInstance(getActivity()).showText(
                            "手机号格式有误，请您核对后重新输入");
                    return;
                }


                if (code.length() == 0) {
                    ToastManager.getInstance(getActivity()).showText(
                            "请输入收到的短信验证码");
                    return;
                }
              //  LogUtils.LOGE(phonenum + "--" + key + "--" + code);//key --

                WechatUserInfo wechatUserInfo = AppShare.getWechatUserInfo(getActivity());

                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.loginPhoneCheck, MyNetRequestConfig.loginPhoneCheck(
                                getActivity(), wechatUserInfo.openid,
                                wechatUserInfo.nickname, wechatUserInfo.headimgurl, wechatUserInfo.sex
                                , phonenum, code, key), "loginPhoneCheck",
                        SetPhoneFragment.this);

            }
        });
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {


        if ("getLoginMessageCode".equals(id)) {
            mMyCount = new Count(60000 * 1, 1000);
            mMyCount.start();
            ToastManager.getInstance(getActivity()).showText("发送成功");

            //key = netResponse.data;
            Gson gson = new Gson();
            YzmKey yzmKey = gson.fromJson(netResponse.data, YzmKey.class);
            key = yzmKey.key;


        } else if ("loginPhoneCheck".equals(id)) {

//            LogUtils.LOGE(netResponse.toString());
            if (netResponse.bool == 0) {
                ToastManager.getInstance(getActivity()).showText(netResponse.result);
            } else {
                AppShare.setIsLogin(getActivity(), true);
                Gson gson = new Gson();
                UserInfo userInfo = gson.fromJson(netResponse.data.toString(), UserInfo.class);
                AppShare.setUserInfo(getActivity(), userInfo);

                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("fragmentName", Tab5Fragment.class.getName());
                startActivity(intent);
                getActivity().finish();


            }

        }

        super.onNetEnd(id, type, netResponse);
    }


    @Override
    protected int getTitleBarType() {
        // TODO Auto-generated method stub
        return FLAG_TXT | FLAG_BACK;
    }

    @Override
    protected boolean onPageBack() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected boolean onPageNext() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void initTitle() {
        // TODO Auto-generated method stub
        setTitleText("绑定手机号");
    }

    @Override
    public void onDestroy() {

        if (mMyCount != null) {
            mMyCount.cancel();
        }
//        if (content != null) {
//            getActivity().getContentResolver().unregisterContentObserver(content);
//        }

        super.onDestroy();
    }

    /**
     * 监听短信数据库
     */
    class SmsContent extends ContentObserver {

        private Cursor cursor = null;

        public SmsContent(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {

            super.onChange(selfChange);
            // 读取收件箱中指定号码的短信
            cursor = getActivity().getContentResolver().query(Uri.parse("content://sms/inbox"),
                    new String[]{"_id", "address", "read", "body"},
                    " read=?", new String[]{"0"}, "_id desc");// 按id排序，如果按date排序的话，修改手机时间后，读取的短信就不准了
            // MyLog.l("cursor.isBeforeFirst() " + cursor.isBeforeFirst() +
            // " cursor.getCount()  " + cursor.getCount());
            if (cursor != null && cursor.getCount() > 0) {
                ContentValues values = new ContentValues();
                values.put("read", "1"); // 修改短信为已读模式
                cursor.moveToNext();
                int smsbodyColumn = cursor.getColumnIndex("body");
                String smsBody = cursor.getString(smsbodyColumn);
                // MyLog.v("smsBody = " + smsBody);

                // edtPassword.setText();
//				Log.e("=", getDynamicPassword(smsBody) + " =");
//				Log.e("==", smsBody + " =");
                txt02.setText(getDynamicPassword(smsBody));

            }

            // 在用managedQuery的时候，不能主动调用close()方法， 否则在Android 4.0+的系统上， 会发生崩溃
            if (Build.VERSION.SDK_INT < 14) {
                cursor.close();
            }
        }
    }

    /**
     * 从字符串中截取连续6位数字 用于从短信中获取动态密码
     *
     * @param str 短信内容
     * @return 截取得到的6位动态密码
     */
    public static String getDynamicPassword(String str) {
        Pattern continuousNumberPattern = Pattern.compile("[0-9\\.]+");
        Matcher m = continuousNumberPattern.matcher(str);
        String dynamicPassword = "";
        while (m.find()) {
            if (m.group().length() == 6) {
                System.out.print(m.group());
                dynamicPassword = m.group();
            }
        }

        return dynamicPassword;
    }

}
