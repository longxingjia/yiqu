package com.yiqu.iyijiayi.fragment.tab1;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.Tool.Function.VoiceFunction;
import com.Tool.Global.Variable;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.yiqu.Tool.Interface.VoicePlayerInterface;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.model.Constant;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.model.Xizuo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.iyijiayi.utils.String2TimeUtils;
import com.yiqu.iyijiayi.utils.Tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/2/20.
 */

public class XizuoItemDetailFragment extends AbsAllFragment implements View.OnClickListener, VoicePlayerInterface {
    String tag = "XizuoItemDetailFragment";
    private TextView like;
    private TextView musicname;
    private TextView desc;
    private TextView soundtime;
    private TextView stu_listen;
    private TextView created;
    private TextView views;
    private ImageView stu_header;
    private Xizuo xizuo;

    private int totalTime;
    private int currentTime;
    private String fileName;
    private String url;
    private File mFile;
    private boolean ifplay = false;
    private boolean ifDownload = false;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private ImageView musictype;
    private DownloadManager downloadManager;
    private DownloadManager.Query query;
    private ScheduledExecutorService scheduledExecutorService;
    private Future<?> future;
    long fileTotalSize;
    private long downloadId = -1;
    private String sid;

    //处理进度条更新
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //更新进度
                    soundtime.setText(--totalTime + "\"");
                    if (totalTime == 0) {
                        totalTime = 1;
                    }
                    break;
                case Constant.QUERY:

                    Cursor cursor = downloadManager.query(query);
                    if (cursor != null && cursor.moveToFirst()) {
                        //此处直接查询文件大小
                        int bytesDLIdx =
                                cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                        int bytesDL = cursor.getInt(bytesDLIdx);

                        //获取文件下载总大小
                        fileTotalSize = cursor.getLong(cursor.getColumnIndex(
                                DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                        cursor.close();

//                        Log.w(tag, "downloaded size: " + bytesDL);
//                        Log.w(tag, "total size: " + fileTotalSize);

                        if (fileTotalSize != 0) {
                            int percentage = (int) (bytesDL * 100 / fileTotalSize);
                            stu_listen.setText(percentage + "%");
//                            statusBar.setProgress(percentage);
//                            statusText.setText(percentage + "%");
                        }

                        //终止轮询task
                        if (fileTotalSize == bytesDL)
                            future.cancel(true);
                    }
                    break;


                default:
                    break;
            }

        }
    };


    @Override
    protected int getTitleBarType() {
        return FLAG_BACK;
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
        setTitleText("音乐详情");
    }

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.remen_xizuo_detail;
    }

    @Override
    protected void initView(View v) {

        musicname = (TextView) v.findViewById(R.id.musicname);
        like = (TextView) v.findViewById(R.id.like);
        desc = (TextView) v.findViewById(R.id.desc);
        soundtime = (TextView) v.findViewById(R.id.soundtime);
        stu_listen = (TextView) v.findViewById(R.id.stu_listen);
        created = (TextView) v.findViewById(R.id.created);
        views = (TextView) v.findViewById(R.id.views);
        stu_header = (ImageView) v.findViewById(R.id.stu_header);
        musictype = (ImageView) v.findViewById(R.id.musictype);
        stu_listen.setOnClickListener(this);

        xizuo = (Xizuo) getActivity().getIntent().getSerializableExtra("xizuo");
        if (xizuo == null) {
            sid = getActivity().getIntent().getExtras().getString("data");

            if (AppShare.getIsLogin(getActivity())) {
                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.getSoundDetail, MyNetRequestConfig
                                .getSoundDetail(getActivity(), sid, AppShare.getUserInfo(getActivity()).uid),
                        "getSoundDetail", XizuoItemDetailFragment.this);
            }
        } else {
            sid = String.valueOf(xizuo.sid);
            initData();
        }

    }


    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        super.onNetEnd(id, type, netResponse);
        if (id.equals("getSoundDetail")) {
            if (type == NetCallBack.TYPE_SUCCESS) {

                Gson gson = new Gson();
                xizuo = gson.fromJson(netResponse.data, Xizuo.class);
                initData();


            } else {
                getActivity().finish();
            }
        }else  if (id.equals("like")) {

            if (type == NetCallBack.TYPE_SUCCESS) {
                LogUtils.LOGE(tag,netResponse.toString());
                Drawable leftDrawable = getResources().getDrawable(R.mipmap.dianzan_pressed);
                leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
                like.setCompoundDrawables(leftDrawable, null, null, null); //(Drawable left, Drawable top, Drawable right, Drawable bottom)

            } else {

            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stu_listen:

                if (mFile.exists()) {
                    palyVoice();
                } else {
                    dowoload(url, fileName);
                }
                break;
            case R.id.like:
                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.like, MyNetRequestConfig
                                .like(getActivity(), AppShare.getUserInfo(getActivity()).uid, sid),
                        "like", XizuoItemDetailFragment.this);


                break;
        }
    }

    private void dowoload(String downloadUrl, String fileName) {
        downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        //downloadUrl为下载地址
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));

        //设置文件下载目录和文件名
        //   request.setDestinationInExternalPublicDir("Android/data/com.yiqu.iyijiayi/cache/musiccach", fileName);
        request.setDestinationInExternalFilesDir(getActivity(), Environment.DIRECTORY_DOWNLOADS, fileName);

        //设置下载中通知栏提示的标题
        request.setTitle(fileName);

        //设置下载中通知栏提示的介绍
        request.setDescription(fileName);
        // 设置为可见和可管理
        request.setVisibleInDownloadsUi(false);

        /*
        表示下载进行中和下载完成的通知栏是否显示，默认只显示下载中通知，
        VISIBILITY_HIDDEN表示不显示任何通知栏提示，
        这个需要在AndroidMainfest中添加权限android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
        */
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);

        /*
        表示下载允许的网络类型，默认在任何网络下都允许下载，
        有NETWORK_MOBILE、NETWORK_WIFI、NETWORK_BLUETOOTH三种及其组合可供选择；
        如果只允许wifi下载，而当前网络为3g，则下载会等待
        */
        //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

        //移动网络情况下是否允许漫游
        //request.setAllowedOverRoaming(true);

        //表示允许MediaScanner扫描到这个文件，默认不允许
        // request.allowScanningByMediaScanner();

        /*
        设置下载文件的mineType,
        因为下载管理Ui中点击某个已下载完成文件及下载完成点击通知栏提示都会根据mimeType去打开文件，
        所以我们可以利用这个属性。比如上面设置了mimeType为application/package.name，
        我们可以同时设置某个Activity的intent-filter为application/package.name，用于响应点击的打开文件
        */
        //request.setMimeType("application/package.name");

        //添加请求下载的网络链接的http头，比如User-Agent，gzip压缩等
        //request.addRequestHeader(String header, String value);
        downloadId = downloadManager.enqueue(request);
        query = new DownloadManager.Query();

        query.setFilterById(downloadId);//筛选下载任务，传入任务ID，可变参数

        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        //每过1000ms通知handler去查询下载状态
        future = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Message msg = mHandler.obtainMessage();
                msg.what = Constant.QUERY;
                mHandler.sendMessage(msg);
            }
        }, 500, 300, TimeUnit.MILLISECONDS);

    }

    BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (completeDownloadId == downloadId) {
                stu_listen.setText("100%");
                if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
                    future.cancel(true);
                    scheduledExecutorService.shutdown();
                    scheduledExecutorService = null;
                }
                palyVoice();
                downloadId = -1;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        /** 注册下载完成接收广播 **/
        getActivity().registerReceiver(downloadCompleteReceiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(downloadCompleteReceiver);
        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
            future.cancel(true);
            scheduledExecutorService.shutdown();

            scheduledExecutorService = null;

        }
        super.onPause();
    }

    private void initData() {
        desc.setText(xizuo.desc);
        soundtime.setText(xizuo.soundtime + "\"");
        musicname.setText(xizuo.musicname);

        views.setText(xizuo.views + "");
        like.setText(xizuo.like + "");
        if (xizuo.type == 1) {
            musictype.setImageResource(R.mipmap.shengyue);
        } else {
            musictype.setImageResource(R.mipmap.boyin);
        }

        like.setOnClickListener(this);

        PictureUtils.showPicture(getActivity(), xizuo.stuimage, stu_header);

        String2TimeUtils string2TimeUtils = new String2TimeUtils();
        long currentTimeMillis = System.currentTimeMillis() / 1000;

        long time = currentTimeMillis - xizuo.edited;
        created.setText(string2TimeUtils.long2Time(time));

        url = MyNetApiConfig.ImageServerAddr + xizuo.soundpath;
        fileName = url.substring(
                url.lastIndexOf("/") + 1,
                url.length());
        fileName = xizuo.musicname + "_" + fileName;
        mFile = new File(Variable.StorageMusicCachPath, fileName);
    }

    @Override
    public void playVoiceBegin() {

    }

    @Override
    public void playVoiceFail() {

    }

    @Override
    public void playVoicePause() {

    }

    @Override
    public void playVoiceFinish() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }

        currentTime = 0;

        soundtime.setText(xizuo.soundtime + "\"");

    }


    private void palyVoice() {

        totalTime = xizuo.soundtime;

        if (VoiceFunction.IsPlayingVoice(mFile.getAbsolutePath())) {  //正在播放，点击暂停
            currentTime = VoiceFunction.pauseVoice(mFile.getAbsolutePath());
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }
            if (mTimerTask != null) {
                mTimerTask.cancel();
                mTimerTask = null;
            }

        } else {     //暂停，点击播放

            if (mTimer == null) {
                mTimer = new Timer();
            }
            if (mTimerTask == null) {
                mTimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(0);

                    }
                };
                mTimer.schedule(mTimerTask, 1000, 1000);

            }

            if (currentTime > 0) {


                totalTime = sub(xizuo.soundtime, currentTime);
                VoiceFunction.PlayToggleVoice(mFile.getAbsolutePath(), this, currentTime);
            } else {
                VoiceFunction.PlayToggleVoice(mFile.getAbsolutePath(), this, 0);
            }

        }
    }

    public static int sub(int totalTime, int currentTime) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(totalTime));
        BigDecimal b2 = new BigDecimal(Double.valueOf(currentTime) / 1000);
        return b1.subtract(b2).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }


    @Override
    public void onDestroy() {

        VoiceFunction.StopVoice();
        currentTime = 0;

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
            mTimerTask.cancel();
            mTimerTask = null;
        }

        if (downloadManager != null && downloadId != -1) {
            downloadManager.remove(downloadId);
        }

        super.onDestroy();
    }
}
