package com.yiqu.iyijiayi.fragment.tab5;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017/2/9.
 */

public class ShowSettingFragment extends AbsAllFragment {

    private TextView teView;
    private String title = "";
    private InputStream inputStream;
    private String data;

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getTitleBarType() {
        return FLAG_TXT | FLAG_BACK;
    }

    @Override
    protected int getBodyView() {
        return R.layout.tab5_show_setting_fragment;
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


        setTitleText(title);

    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(title); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(title);
    }


    @Override
    protected void initView(View v) {
        teView = (TextView) v.findViewById(R.id.button1);
        Intent intent = getActivity().getIntent();
        data = intent.getStringExtra("data");
        switch (data) {
            case "help":
                title = "使用帮助";
                inputStream = getResources().openRawResource(R.raw.help);
                break;
            case "agreement":
                title = "用户协议";
                inputStream = getResources().openRawResource(R.raw.agreement);
                break;
            case "publish_rules":
                title = "发布规则";
                inputStream = getResources().openRawResource(R.raw.publish_rules);
                break;
            default:
                title = "使用帮助";
                inputStream = getResources().openRawResource(R.raw.help);
                break;
        }

        teView.setText(getString(inputStream));
    }

    private  String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
