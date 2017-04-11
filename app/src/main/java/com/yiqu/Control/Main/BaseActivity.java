package com.yiqu.Control.Main;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yiqu.iyijiayi.R;

public class BaseActivity extends Activity {
    private TextView mTitleTx;
    private View mBack;
    private LinearLayout contentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView();
        initTitleBar();
    }

    public void initTitleBar() {
//        mTitleTx = (TextView) findViewById(R.id.titlebar_title);
//        mBack = findViewById(R.id.titlebar_left);
//        mBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
    }

    private void initContentView() {
        ViewGroup content = (ViewGroup) findViewById(android.R.id.content);
        content.removeAllViews();
        contentLayout = new LinearLayout(this);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        content.addView(contentLayout);
        LayoutInflater.from(this).inflate(R.layout.activity_base, contentLayout, true);
    }

    @Override
    public void setContentView(int layoutResID) {
        //View customContentView = LayoutInflater.from(this).inflate(layoutResID,null);
        /*this is the same result with
         View customContentView = LayoutInflater.from(this).inflate(layoutResID,contentLayout, false);
         */

        //contentLayout.addView(customContentView,LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        LayoutInflater.from(this).inflate(layoutResID, contentLayout, true);

    }

    @Override
    public void setContentView(View customContentView) {
        contentLayout.addView(customContentView);

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitleTx.setText(title);
    }
}
