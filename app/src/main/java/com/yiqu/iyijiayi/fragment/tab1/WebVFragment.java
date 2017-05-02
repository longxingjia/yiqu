package com.yiqu.iyijiayi.fragment.tab1;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.base.utils.ToastManager;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.model.Banner;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Administrator on 2017/2/20.
 */

public class WebVFragment extends AbsAllFragment {
    private RelativeLayout webParent;
    private WebView webview;
    private ProgressBar progressBar;
    private Banner banner;

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.web_fragment;
    }

    @Override
    protected int getTitleBarType() {
        return FLAG_BACK|FLAG_TXT;
    }

    @Override
    protected boolean onPageBack() {
        back();
        return true;
    }

    @Override
    protected boolean onPageNext() {
        return false;
    }

    @Override
    protected void initTitle() {
//        setTitleBtnImg(R.drawable.share);
    }

    @Override
    protected void initView(View v) {
        // TODO Auto-generated method stub
        initWebView(v);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        banner = (Banner) getActivity().getIntent().getSerializableExtra("data");
        setTitleText(banner.title);
        webview.loadUrl(banner.url);
        super.init(savedInstanceState);
    }


    private void back(){
        if(webview.canGoBack()){
            webview.goBack();
        }else{
            getActivity().finish();
        }
    }
    @Override
    public void onDestroy() {
        try{
            webview.stopLoading();
            webParent.removeView(webview);
            webview.removeAllViews();
            webview.destroy();
        }catch(Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }

    protected void initWebView(View v){
        webParent = (RelativeLayout) v.findViewById(R.id.webParent);
        webview = (WebView) v.findViewById(R.id.webview);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webview.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        webview.setWebChromeClient(new WebChromeClient());
        webview. setWebViewClient(client);

        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webview.getSettings().setDatabaseEnabled(true);
//		String dir = getActivity().getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();

//		//启用地理定位
//		webview.getSettings().setGeolocationEnabled(true);
//		//设置定位的数据库路径
//		webview.getSettings().setGeolocationDatabasePath(dir);
//
//		//最重要的方法，一定要设置，这就是出不来的主要原因
//
//		webview.getSettings().setDomStorageEnabled(true);
    }



    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            } else {
                if (progressBar.getVisibility() == View.GONE)
                    progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        //配置权限（同样在WebChromeClient中实现）
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }



    }

    private WebViewClient client = new WebViewClient(){
        private Timer timer;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            try{
                timer = new Timer();
                TimerTask tt = new TimerTask() {
                    @Override
                    public void run() {
                        mTimeHandler.sendEmptyMessage(0);
                        timer.cancel();
                        timer.purge();
                    }
                };
                timer.schedule(tt, 1000 * 30);
            }catch(Exception e){
                e.printStackTrace();
            }

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            mTimeHandler.removeMessages(0);
            try{
                timer.cancel();
                timer.purge();
            }catch(Exception e){
                e.printStackTrace();
            }
            super.onPageFinished(view, url);
        }

        public void onReceivedSslError(WebView view,
                                       SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    };

    private Handler mTimeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            try{
                switch(msg.what){
                    case 0:
                        if(webview.getProgress() < 100){
                            ToastManager.getInstance(getActivity()).showText("系统繁忙，请重试");
                        }
                        break;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            super.handleMessage(msg);
        }

    };

}
