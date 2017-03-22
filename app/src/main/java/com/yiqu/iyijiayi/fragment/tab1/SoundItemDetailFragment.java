package com.yiqu.iyijiayi.fragment.tab1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
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
import android.widget.ListView;
import android.widget.TextView;

import com.Tool.Function.VoiceFunction;
import com.Tool.Global.Variable;
import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ui.views.DialogUtil;
import com.ui.views.DialogView;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.Tool.Interface.VoicePlayerInterface;
import com.yiqu.iyijiayi.CommentActivity;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.Tab1CommentsAdapter;
import com.yiqu.iyijiayi.fragment.Tab5Fragment;
import com.yiqu.iyijiayi.fragment.tab5.HomePageFragment;
import com.yiqu.iyijiayi.fragment.tab5.PayforYBFragment;
import com.yiqu.iyijiayi.model.CommentsInfo;
import com.yiqu.iyijiayi.model.Constant;
import com.yiqu.iyijiayi.model.HomePage;
import com.yiqu.iyijiayi.model.Like;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.model.Sound;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.model.Xizuo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.MathUtils;
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.iyijiayi.utils.String2TimeUtils;

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
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/2/20.
 */

public class SoundItemDetailFragment extends AbsAllFragment implements View.OnClickListener, VoicePlayerInterface {
    String tag = "SoundItemDetailFragment";
    private TextView like;
    private TextView musicname;
    private TextView desc;
    private TextView soundtime;
    private TextView stu_listen;
    private TextView tea_name;
    private TextView tectitle;
    private TextView commenttime;
    private TextView created;
    private TextView views;
    private ImageView stu_header;
    private ImageView tea_header;
    private int teatotalTime;
    private int stutotalTime;
    private int stucurrentTime;
    private int teacurrentTime;
    private String stufileName;
    private String teafileName;
    private String stuUrl;
    private String teaUrl;
    private File stuFile;
    private File teaFile;
    private Timer mTimer;
    private TimerTask mTimerTask;
    private TextView tea_listen;
    private Sound sound;
    private ImageView musictype;
    private AlertDialog dialog;
    private UserInfo userInfo;
    private String sid;
    private DownloadManager downloadManager;

    ScheduledExecutorService scheduledExecutorService;
    Future<?> future;
    private long downloadTeaId = -1;
    private long downloadStuId = -1;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //更新进度
                    if (VoiceFunction.IsPlayingVoice(stuFile.getAbsolutePath())) {
                        soundtime.setText(--stutotalTime + "\"");
                        if (stutotalTime == 0) {
                            stutotalTime = 1;
                        }
                    } else if (VoiceFunction.IsPlayingVoice(teaFile.getAbsolutePath())) {
                        commenttime.setText(--teatotalTime + "\"");
                        if (teatotalTime == 0) {
                            teatotalTime = 1;
                        }
                    }
                    break;
                case Constant.QUERY_STUDENT:
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadStuId);//筛选下载任务，传入任务ID，可变参数
                    Cursor cursor = downloadManager.query(query);

                    if (cursor != null && cursor.moveToFirst()) {
                        //此处直接查询文件大小
                        int bytesDLIdx =
                                cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                        int bytesDL = cursor.getInt(bytesDLIdx);

                        //获取文件下载总大小
                        long fileTotalSize = cursor.getLong(cursor.getColumnIndex(
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
//                        if (fileTotalSize == bytesDL)
//                            future.cancel(true);
                    }
                    break;
                case Constant.QUERY_TEACHER:

                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(downloadTeaId);//筛选下载任务，传入任务ID，可变参数
                    Cursor c = downloadManager.query(q);

                    if (c != null && c.moveToFirst()) {
                        //此处直接查询文件大小
                        int bytesDLIdx =
                                c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                        int bytesDL = c.getInt(bytesDLIdx);

                        //获取文件下载总大小
                        long fileTotalSize = c.getLong(c.getColumnIndex(
                                DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                        c.close();

//                        Log.w(tag, "downloaded size: " + bytesDL);
//                        Log.w(tag, "total size: " + fileTotalSize);

                        if (fileTotalSize != 0) {
                            int percentage = (int) (bytesDL * 100 / fileTotalSize);
                            tea_listen.setText(percentage + "%");
//                            statusBar.setProgress(percentage);
//                            statusText.setText(percentage + "%");
                        }

                        //终止轮询task
//                        if (fileTotalSize == bytesDL)
//                            future.cancel(true);
                    }
                    break;

                default:

                    break;
            }

        }
    };
    private ArrayList<Like> likes;
    private ListView listview;
    private Tab1CommentsAdapter tab1CommentsAdapter;
    private TextView no_comments;
    private TextView comment;
    private int likesIndex = -1;
    private TextView isformulation;
    private TextView accompaniment;
    private TextView chapter;
    private int payforTag = 0;
    private int position;


    @Override
    protected int getTitleBarType() {
        return FLAG_BACK;
    }

    @Override
    protected boolean onPageBack() {
//        if (payforTag == 1) {
//            Intent intent = new Intent();
//            if (position!=-1){
//                intent.putExtra("position", position);
//                getActivity().setResult(RESULT_OK, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
//                getActivity().finish();//此处一定要调用finish()方法
//            }
//
//        }
        Intent intent = new Intent();
        if (position!=-1){
            intent.putExtra("position", position);
            getActivity().setResult(RESULT_OK, intent); //intent为A传来的带有Bundle的intent，当然也可以自己定义新的Bundle
            getActivity().finish();//此处一定要调用finish()方法
        }
        return false;
    }


    @Override
    protected boolean onPageNext() {
        return false;
    }

    @Override
    protected void initTitle() {
        setTitleText("问题详情");
    }

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.remen_sound_detail;
    }

    @Override
    protected void initView(View v) {


        musicname = (TextView) v.findViewById(R.id.musicname);
        like = (TextView) v.findViewById(R.id.like);
        desc = (TextView) v.findViewById(R.id.desc);
        isformulation = (TextView) v.findViewById(R.id.isformulation);
        accompaniment = (TextView) v.findViewById(R.id.accompaniment);
        chapter = (TextView) v.findViewById(R.id.chapter);
        soundtime = (TextView) v.findViewById(R.id.soundtime);
        stu_listen = (TextView) v.findViewById(R.id.stu_listen);
        tea_listen = (TextView) v.findViewById(R.id.tea_listen);
        tea_name = (TextView) v.findViewById(R.id.tea_name);
        tectitle = (TextView) v.findViewById(R.id.tectitle);
        commenttime = (TextView) v.findViewById(R.id.commenttime);
        created = (TextView) v.findViewById(R.id.created);
        views = (TextView) v.findViewById(R.id.views);
        stu_header = (ImageView) v.findViewById(R.id.stu_header);
        tea_header = (ImageView) v.findViewById(R.id.tea_header);
        musictype = (ImageView) v.findViewById(R.id.musictype);
        listview = (ListView) v.findViewById(R.id.listview);
        no_comments = (TextView) v.findViewById(R.id.no_comments);

        likes = AppShare.getLikeList(getActivity());

        comment = (TextView) v.findViewById(R.id.comment);
        like.setOnClickListener(this);
        comment.setOnClickListener(this);
        likes = AppShare.getLikeList(getActivity());

        stu_header.setOnClickListener(this);
        tea_header.setOnClickListener(this);

    }

    private void initDianZan() {
        Drawable leftDrawable = getResources().getDrawable(R.mipmap.dianzan_pressed);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        like.setCompoundDrawables(leftDrawable, null, null, null); //(Drawable left, Drawable top, Drawable right, Drawable bottom)
    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        super.onNetEnd(id, type, netResponse);
        if (id.equals("eavesdrop")) {
            if (type == NetCallBack.TYPE_SUCCESS) {
                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.getSoundDetail, MyNetRequestConfig
                                .getSoundDetail(getActivity(), sid, userInfo.uid),
                        "getSoundDetail", SoundItemDetailFragment.this);
                userInfo.coin_apple--;
                payforTag = 1;
                AppShare.setUserInfo(getActivity(), userInfo);
            }

            ToastManager.getInstance(getActivity()).showText(netResponse.result);

        } else if (id.equals("getSoundDetail")) {
            if (type == NetCallBack.TYPE_SUCCESS) {

                Gson gson = new Gson();
                sound = gson.fromJson(netResponse.data, Sound.class);

                initData();

            } else {
                getActivity().finish();

            }
        } else if (id.equals("like")) {

            if (type == NetCallBack.TYPE_SUCCESS) {

                if (likesIndex == -1) {
                    Like l = new Like();
                    l.sid = sid;
                    l.islike = 1;
                    if (likes == null) {
                        likes = new ArrayList<Like>();
                    }
                    likes.add(l);
                    like.setText(String.valueOf(sound.like + 1));
                    AppShare.setLikeList(getActivity(), likes);
                    initDianZan();
                }


            } else {

            }
        } else if (id.equals("getComments")) {
            LogUtils.LOGE(tag, netResponse.toString());
            if (type == NetCallBack.TYPE_SUCCESS) {
                ArrayList<CommentsInfo> commentsInfos
                        = new Gson().fromJson(netResponse.data, new TypeToken<ArrayList<CommentsInfo>>() {
                }.getType());

                if (commentsInfos == null || commentsInfos.size() == 0) {

                } else {
                    tab1CommentsAdapter.setData(commentsInfos);
                    no_comments.setVisibility(View.GONE);
                }

            }
        }


    }

    private void initData() {
        desc.setText(sound.desc);
        soundtime.setText(sound.soundtime + "\"");

        tea_name.setText(sound.tecname);
        musicname.setText(sound.musicname);
        tectitle.setText(sound.tectitle);
        commenttime.setText(sound.commenttime + "\"");
        soundtime.setText(sound.soundtime + "\"");
        views.setText(sound.views + "");
        like.setText(sound.like + "");
        if (sound.type == 1) {
            musictype.setImageResource(R.mipmap.shengyue);
            String text = "";
            if (!TextUtils.isEmpty(sound.chapter)) {
                text = sound.chapter + " | ";
            }
            if (!TextUtils.isEmpty(sound.musictype)) {
                text = text + sound.musictype + " | ";
            }
            if (!TextUtils.isEmpty(sound.accompaniment)) {
                text = text + sound.accompaniment + " | ";
                accompaniment.setText(sound.accompaniment);
            }
            chapter.setVisibility(View.VISIBLE);
            chapter.setText(text);


        } else {
            musictype.setImageResource(R.mipmap.boyin);
        }

        long t = System.currentTimeMillis() / 1000 - sound.edited;
        if (t < 2 * 24 * 60 * 60 && t > 0) {
            tea_listen.setText("限时免费听");
        } else {
            if (sound.listen == 1) {
                tea_listen.setText("已付费");
            } else {
                tea_listen.setText("1元偷偷听");
            }
        }

        tab1CommentsAdapter = new Tab1CommentsAdapter(getActivity(), sid, sound.fromuid + "");
        listview.setAdapter(tab1CommentsAdapter);
        listview.setOnItemClickListener(tab1CommentsAdapter);

        RestNetCallHelper.callNet(getActivity(),
                MyNetApiConfig.getComments, MyNetRequestConfig
                        .getComments(getActivity(), sid),
                "getComments", SoundItemDetailFragment.this, false, true);

        PictureUtils.showPicture(getActivity(), sound.tecimage, tea_header);
        PictureUtils.showPicture(getActivity(), sound.stuimage, stu_header);

        String2TimeUtils string2TimeUtils = new String2TimeUtils();
        long currentTimeMillis = System.currentTimeMillis() / 1000;

        long time = currentTimeMillis - sound.edited;
        created.setText(string2TimeUtils.long2Time(time));

        stuUrl = MyNetApiConfig.ImageServerAddr + sound.soundpath;

        stufileName = stuUrl.substring(
                stuUrl.lastIndexOf("/") + 1,
                stuUrl.length());
        stufileName = sound.musicname + "_" + stufileName;
        stuFile = new File(Variable.StorageMusicCachPath, stufileName);

        teaUrl = MyNetApiConfig.ImageServerAddr + sound.commentpath;
        teafileName = teaUrl.substring(
                teaUrl.lastIndexOf("/") + 1,
                teaUrl.length());
        teafileName = sound.musicname + "_" + teafileName;
        teaFile = new File(Variable.StorageMusicCachPath, teafileName);

        if (likes != null) {
            for (int i = 0; i < likes.size(); i++) {
                Like dz = likes.get(i);
                if (dz.sid.equals(sid)) {

                    likesIndex = i;
                    initDianZan();
                }

            }
        }
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        stu_listen.setOnClickListener(this);
        tea_listen.setOnClickListener(this);

        sound = (Sound) getActivity().getIntent().getSerializableExtra("Sound");
        position = getActivity().getIntent().getIntExtra("position",-1);
        userInfo = AppShare.getUserInfo(getActivity());

        if (sound == null) {
            sid = getActivity().getIntent().getExtras().getString("data");

            if (AppShare.getIsLogin(getActivity())) {

                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.getSoundDetail, MyNetRequestConfig
                                .getSoundDetail(getActivity(), sid, userInfo.uid),
                        "getSoundDetail", SoundItemDetailFragment.this);

            }
        } else {
            sid = String.valueOf(sound.sid);
            initData();
        }
        super.init(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stu_listen:

                if (stuFile.exists()) {
                    palyStudentVoice();

                } else {

                    downloadStuId = dowoload(stuUrl, stufileName, 0);
                }


                break;
            case R.id.tea_listen:
                long t = System.currentTimeMillis() / 1000 - sound.edited;

                if (t < 2 * 24 * 60 * 60 && t > 0) {
                    if (teaFile.exists()) {
                        palyTeacherVoice();

                    } else {
                        downloadTeaId = dowoload(teaUrl, teafileName, 1);
                    }
                } else {
                    if (sound.listen == 1) {
                        if (teaFile.exists()) {
                            palyTeacherVoice();

                        } else {
                            downloadTeaId = dowoload(teaUrl, teafileName, 1);
                        }
                    } else {
                        String desc = "";
                        if (userInfo.coin_apple > 0) {
                            desc = "支付";
                        } else {
                            desc = "去充值";
                        }
                        String coin = "0";
                        if (userInfo.coin_apple > 0) {
                            coin = userInfo.coin_apple + "00";
                        }

                        dialog = DialogUtil.showMyDialog(getActivity(), "使用艺币支付", "偷听需支付100艺币，当前余额"
                                + coin + "艺币", "取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();
                            }
                        }, desc, new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (userInfo.coin_apple >= 1) {

                                    RestNetCallHelper.callNet(getActivity(),
                                            MyNetApiConfig.eavesdrop, MyNetRequestConfig
                                                    .eavesdrop(getActivity(), userInfo.uid, sound.sid),
                                            "eavesdrop", SoundItemDetailFragment.this);
                                } else {
                                    Model.startNextAct(getActivity(),
                                            PayforYBFragment.class.getName());
                                }
                                dialog.dismiss();

                            }
                        });
                    }
                }

                break;

            case R.id.like:
                RestNetCallHelper.callNet(getActivity(),
                        MyNetApiConfig.like, MyNetRequestConfig
                                .like(getActivity(), AppShare.getUserInfo(getActivity()).uid, sid),
                        "like", SoundItemDetailFragment.this, false, true);
                break;
            case R.id.comment:
                Intent intent = new Intent(getActivity(), CommentActivity.class);
//                Bundle bundle = new Bundle();

                intent.putExtra("sid", sid + "");
                intent.putExtra("fromuid", AppShare.getUserInfo(getActivity()).uid + "");
                LogUtils.LOGE(tag, sound.toString());
                intent.putExtra("touid", sound.fromuid + "");
                getActivity().startActivity(intent);

                break;

            case R.id.stu_header:
                initHomepage(getActivity(), String.valueOf(sound.fromuid));
                break;
            case R.id.tea_header:
                initHomepage(getActivity(), String.valueOf(sound.touid));
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /** 注册下载完成接收广播 **/
        getActivity().registerReceiver(downloadCompleteReceiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        MobclickAgent.onPageStart("声乐详情");

    }


    private void initHomepage(final Context mContext, String uid) {
        String mUid = "0";
        if (AppShare.getIsLogin(mContext)) {
            mUid = AppShare.getUserInfo(mContext).uid;
        }
        RestNetCallHelper.callNet(mContext,
                MyNetApiConfig.getUserPage,
                MyNetRequestConfig.getUserPage(mContext
                        , uid, mUid),
                "getUserPage",
                new NetCallBack() {
                    @Override
                    public void onNetNoStart(String id) {

                    }

                    @Override
                    public void onNetStart(String id) {

                    }

                    @Override
                    public void onNetEnd(String id, int type, NetResponse netResponse) {
                        if (TYPE_SUCCESS == type) {
                            Gson gson = new Gson();
                            HomePage homePage = gson.fromJson(netResponse.data, HomePage.class);
                            Intent i = new Intent(mContext, StubActivity.class);
                            i.putExtra("fragment", HomePageFragment.class.getName());
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("data", homePage);
                            i.putExtras(bundle);
                            mContext.startActivity(i);
                        }

                    }
                });
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(downloadCompleteReceiver);
        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
            future.cancel(true);
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;

        }
        MobclickAgent.onPageEnd("声乐详情");
        super.onPause();

    }



    private long dowoload(String downloadUrl, String fileName, final int tag) {
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
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);

        scheduledExecutorService = Executors.newScheduledThreadPool(1);

        future = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Message msg = mHandler.obtainMessage();
                if (tag == 0) {
                    msg.what = Constant.QUERY_STUDENT;
                } else {
                    msg.what = Constant.QUERY_TEACHER;
                }

                mHandler.sendMessage(msg);
            }
        }, 500, 300, TimeUnit.MILLISECONDS);

        return downloadManager.enqueue(request);

    }

    BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (completeDownloadId == downloadTeaId) {
                tea_listen.setText("100%");
                downloadTeaId = -1;
                palyTeacherVoice();
            }
            if (completeDownloadId == downloadStuId) {
                stu_listen.setText("100%");
                downloadStuId = -1;
                palyStudentVoice();

            }
            if (downloadTeaId == -1 && downloadTeaId == -1) {
                if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
                    future.cancel(true);
                    scheduledExecutorService.shutdown();
                    scheduledExecutorService = null;
                }
            }
        }
    };


    private void palyStudentVoice() {

        teacurrentTime = 0;

        stutotalTime = sound.soundtime;
        teatotalTime = sound.commenttime;
        commenttime.setText(teatotalTime + "\"");

        if (VoiceFunction.IsPlayingVoice(stuFile.getAbsolutePath())) {  //正在播放，点击暂停
            stucurrentTime = VoiceFunction.pauseVoice(stuFile.getAbsolutePath());
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

            if (stucurrentTime > 0) {

                stutotalTime = MathUtils.sub(sound.soundtime, stucurrentTime);
                VoiceFunction.PlayToggleVoice(stuFile.getAbsolutePath(), this, stucurrentTime);
            } else {
                VoiceFunction.PlayToggleVoice(stuFile.getAbsolutePath(), this, 0);
            }

        }
    }

    private void palyTeacherVoice() {
        stucurrentTime = 0;
        teatotalTime = sound.commenttime;
        stutotalTime = sound.soundtime;
        soundtime.setText(stutotalTime + "\"");


        if (VoiceFunction.IsPlayingVoice(teaFile.getAbsolutePath())) {  //正在播放，点击暂停
            teacurrentTime = VoiceFunction.pauseVoice(teaFile.getAbsolutePath());
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
            if (teacurrentTime > 0) {
                teatotalTime = MathUtils.sub(sound.commenttime, teacurrentTime);
                VoiceFunction.PlayToggleVoice(teaFile.getAbsolutePath(), this, teacurrentTime);
            } else {
                VoiceFunction.PlayToggleVoice(teaFile.getAbsolutePath(), this, 0);
            }
        }
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

        stucurrentTime = 0;
        teacurrentTime = 0;

        commenttime.setText(sound.commenttime + "\"");
        soundtime.setText(sound.soundtime + "\"");

    }

    @Override
    public void onDestroy() {

        VoiceFunction.StopVoice();
        stucurrentTime = 0;
        teacurrentTime = 0;

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
            mTimerTask.cancel();
            mTimerTask = null;
        }

        if (downloadManager != null) {
            if (downloadTeaId != -1) {
                downloadManager.remove(downloadTeaId);
            }
            if (downloadStuId != -1) {
                downloadManager.remove(downloadStuId);
            }

        }

        super.onDestroy();
    }
}
