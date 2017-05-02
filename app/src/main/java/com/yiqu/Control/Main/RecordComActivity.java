package com.yiqu.Control.Main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ui.views.CircleImageView;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.Tool.Function.AudioFunction;
import com.yiqu.Tool.Function.CommonFunction;
import com.yiqu.Tool.Function.FileFunction;
import com.yiqu.Tool.Function.LogFunction;
import com.yiqu.Tool.Function.UpdateFunction;
import com.yiqu.Tool.Function.VoiceFunction;
import com.yiqu.Tool.Global.RecordConstant;
import com.utils.Variable;
import com.yiqu.Tool.Interface.ComposeAudioInterface;
import com.yiqu.Tool.Interface.DecodeOperateInterface;
import com.yiqu.Tool.Interface.VoicePlayerInterface;
import com.yiqu.Tool.Interface.VoiceRecorderOperateInterface;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.adapter.DialogHelper;
import com.yiqu.iyijiayi.adapter.MenuDialogGiveupRecordHelper;
import com.yiqu.iyijiayi.adapter.MenuDialogListerner;
import com.yiqu.iyijiayi.adapter.MenuDialogSelectTeaHelper;
import com.db.ComposeVoiceInfoDBHelper;
import com.db.DownloadMusicInfoDBHelper;
import com.yiqu.iyijiayi.fragment.tab3.AddQuestionFragment;
import com.yiqu.iyijiayi.fragment.tab3.UploadXizuoFragment;
import com.model.ComposeVoice;
import com.yiqu.iyijiayi.model.LyricDownInfo;
import com.model.Music;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.utils.AppInfo;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.BitmapUtil;
import com.yiqu.iyijiayi.utils.DensityUtil;
import com.utils.LogUtils;
import com.yiqu.iyijiayi.utils.PermissionUtils;
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.iyijiayi.utils.String2TimeUtils;
import com.yiqu.iyijiayi.view.LyricLoader;
import com.yiqu.iyijiayi.view.LyricView;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class RecordComActivity extends Activity
        implements VoicePlayerInterface, DecodeOperateInterface, ComposeAudioInterface,
        VoiceRecorderOperateInterface, View.OnClickListener, NetCallBack {
    private boolean recordVoiceBegin;
    private boolean recordComFinish = false;
    private String tag = "RecordComActivity";

    private int recordTime;
    private int actualRecordTime;
    private TextView tv_record;
    private TextView musicName;
    private TextView musictime;

    private String className;

    private String tempVoicePcmUrl;
    private String musicFileUrl;
    private String decodeFileUrl;
    private String composeVoiceUrl;
    private String fileNameCom;

    private ProgressBar composeProgressBar;
    private static RecordComActivity instance;
    private Music music;
    private RelativeLayout rlHint;
    private ImageView title_back;
    private ImageView image_anim;
    private Animation rotate;

    private ComposeVoice composeVoice;
    private String2TimeUtils string2TimeUtils;
    private int totalTime;
    private AudioManager audoManager;


    @BindView(R.id.background)
    public ImageView background;
    @BindView(R.id.lyricview)
    public LyricView lyricView;

    @BindView(R.id.icon_record)
    public ImageView icon_record;
    @BindView(R.id.icon_finish)
    public ImageView icon_finish;
    @BindView(R.id.reset)
    public CircleImageView reset;
    @BindView(R.id.record)
    public CircleImageView recordVoiceButton;
    @BindView(R.id.finish)
    public CircleImageView finish;

    private File lrc;
    private String fileName;
    private TextView totaltime;
    private ProgressBar pb_record;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        instance = this;
        init(R.layout.record_and_com_fragment);

    }


    private void init(int layoutId) {
        setContentView(layoutId);
        ButterKnife.bind(this);
        PermissionGen.needPermission(this, 100, Manifest.permission.RECORD_AUDIO);
        className = getClass().getSimpleName();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void bindView() {

        rlHint = (RelativeLayout) findViewById(R.id.hint);

        musicName = (TextView) findViewById(R.id.musicname);
        musictime = (TextView) findViewById(R.id.musictime);
        totaltime = (TextView) findViewById(R.id.totaltime);
        tv_record = (TextView) findViewById(R.id.tv_record);
        // recordVoiceButton = (TextView) findViewById(R.id.recordVoiceButton);
        title_back = (ImageView) findViewById(R.id.title_back);
        image_anim = (ImageView) findViewById(R.id.image_anim);
        composeProgressBar = (ProgressBar) findViewById(R.id.composeProgressBar);
        pb_record = (ProgressBar) findViewById(R.id.pb_record);
        title_back.setOnClickListener(this);

    }

    public void initView() {

    }

    private Bitmap createColorBitmap(int color) {
        Bitmap bmp = Bitmap.createBitmap(DensityUtil.dip2px(this, 60), DensityUtil.dip2px(this, 60), Bitmap.Config.ARGB_8888);
        bmp.eraseColor(color);
        return bmp;
    }

    public void initData() {
        rotate = AnimationUtils.loadAnimation(this, R.anim.recording_animation);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);//setInterpolator表示设置旋转速率。LinearInterpolator为匀速效果，Accelerateinterpolator为加速效果、DecelerateInterpolator为减速效果
        audoManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        Intent intent = getIntent();
        music = (Music) intent.getSerializableExtra("music");
        string2TimeUtils = new String2TimeUtils();
        //  musictime.setText(string2TimeUtils.stringForTimeS(music.time));
        //    LogUtils.LOGE(tag,music.time+"");
        musicName.setText(music.musicname);
        totalTime = music.time;
        pb_record.setMax(music.time);


        Random random = new Random();
        int i = random.nextInt(4);
        String color[] = getResources().getStringArray(R.array.color);
        Bitmap bitmap = createColorBitmap(Color.parseColor(color[i]));
        reset.setImageBitmap(bitmap);
        recordVoiceButton.setImageBitmap(bitmap);
        finish.setImageBitmap(bitmap);
//        image_anim.setImageResource();
        UserInfo userInfo = AppShare.getUserInfo(this);
        String url = userInfo.userimage;
        if (url != null) {
            if (!url.contains("http://wx.qlogo.cn")) {
                url = MyNetApiConfig.ImageServerAddr + url;
            }
            String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
            File file = new File(Variable.StorageImagePath, fileName);
            //   background.setBackgroundResource(R.color.wechat_green);
            if (file.exists()) {
                initBackground(file);
            } else {
                DownLoaderTask downLoaderTask = new DownLoaderTask(url, fileName, Variable.StorageImagePath, image_anim, background);
                downLoaderTask.execute();
            }
        } else {
            //  Picasso.with(context).load(R.mipmap.home_bg).into(icon);
        }

        String Url = MyNetApiConfig.ImageServerAddr + music.musicpath;
        URI uri = URI.create(Url);
        fileName = FileFunction.getValidFileName(uri);
        File mFile = new File(Variable.StorageMusicCachPath, fileName);

        if (mFile.exists())

        {
            musicFileUrl = mFile.getAbsolutePath();
            getDuration(musicFileUrl);//设置音乐总时间
            String tempfileName = fileName.substring(0, fileName.lastIndexOf("."));
            decodeFileUrl = Variable.StorageMusicPath + tempfileName + ".pcm";
            recordTime = 0;
            long t = System.currentTimeMillis() / 1000;


            fileNameCom = music.musicname + t + "cv.mp3";
            composeVoiceUrl = Variable.StorageMusicPath + fileNameCom;
            recordVoiceButton.setOnClickListener(this);

        }

//        lrc = new File(Variable.StorageMusicCachPath, fileName + ".lrc");
//        if (!lrc.exists()) {
//
//            MyNetApiConfig myNetApiConfig = new MyNetApiConfig(music.musicname);
//
//            RestNetCallHelper.callNet(this,
//                    myNetApiConfig.getlyric, MyNetRequestConfig
//                            .getlyric(this),
//                    "getlyric", this);
//        } else {
//            lyricView.initLyricFile(LyricLoader.loadLyricFile(fileName));
//        }


    }

//    private void initMediaPlayer() {
//
//        try {
//            mediaPlayer.reset();
//            mediaPlayer.setDataSource(musicFileUrl);
//            mediaPlayer.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private Handler handler = new Handler();



    private void initBackground(File file) {
        PictureUtils.showPictureFile(instance, file, image_anim, 270);
        Bitmap bt = BitmapFactory.decodeFile(file.getAbsolutePath());

        //  background.setImageBitmap(bt);
        Bitmap b = BitmapUtil.blur(bt, 25f, this);
        Bitmap bb = BitmapUtil.blur(b, 25f, this);
        background.setImageBitmap(bb);
        //  background.setImageAlpha(120); //0完全透明，255
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("录制声乐页面");
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("录制声乐页面");
        MobclickAgent.onPause(this);
    }


    private void goRecordSuccessState() {
        recordVoiceBegin = false;
        musictime.setText(string2TimeUtils.stringForTimeS(actualRecordTime));
//        recordHintTextView.setVisibility(View.VISIBLE);
//        recordHintTextView.setText("录音完成，正在合成");
        pb_record.setProgress(actualRecordTime);
        compose();

    }

    private void goRecordFailState() {
        recordVoiceBegin = false;
        // musictime.setVisibility(View.INVISIBLE);

    }

    private void getDuration(String url) {
        final MediaPlayer vp = new MediaPlayer();
        vp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                totalTime = vp.getDuration() / 1000;
                totaltime.setText(string2TimeUtils.stringForTimeS(totalTime));
                pb_record.setMax(totalTime);
                vp.release();

                DownloadMusicInfoDBHelper downloadMusicInfoDBHelper = new DownloadMusicInfoDBHelper(instance);
                Music m = downloadMusicInfoDBHelper.getDecode(music.mid);
                downloadMusicInfoDBHelper.close();
                if (m.isdecode == -1) {
                    AudioFunction.DecodeMusicFile(musicFileUrl, decodeFileUrl, 0,
                            totalTime, instance);
                    downloadMusicInfoDBHelper.updateDecode(music.mid, 0, System.currentTimeMillis());
                } else if (m.isdecode == 0 && System.currentTimeMillis() - m.decodetime > 2 * 60 * 1000) { //解码超过两分钟,重新解码。
                    File file = new File(decodeFileUrl);
                    file.delete();
                    AudioFunction.DecodeMusicFile(musicFileUrl, decodeFileUrl, 0,
                            totalTime, instance);
                    downloadMusicInfoDBHelper.updateDecode(music.mid, 0, System.currentTimeMillis());
                }
                downloadMusicInfoDBHelper.close();
            }
        });

        try {
            vp.reset();
            vp.setDataSource(url);
            vp.prepareAsync();
        } catch (Exception e) {

            UpdateFunction.ShowToastFromThread("播放语音文件失败");
            LogFunction.error("播放语音异常", e);
        }

    }

    @Override
    public void recordVoiceBegin() {
        if (!recordVoiceBegin) {
            recordVoiceBegin = true;

            recordTime = 0;

            musictime.setText(string2TimeUtils.stringForTimeS(recordTime));
            pb_record.setProgress(actualRecordTime);
            musictime.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void recordVoiceStateChanged(int volume, long recordDuration) {
        if (recordDuration > 0) {
            recordTime = (int) (recordDuration / RecordConstant.OneSecond);
            int leftTime = totalTime - recordTime;
            musictime.setText(string2TimeUtils.stringForTimeS(recordTime));

            pb_record.setProgress(recordTime);

            if (leftTime <= 0) {
                VoiceFunction.StopRecordVoice();
                compose();
            }

        }
    }

    @Override
    public void prepareGiveUpRecordVoice() {

    }

    @Override
    public void recoverRecordVoice() {

    }

    @Override
    public void giveUpRecordVoice() {

    }

    @Override
    public void recordVoiceFail() {
        if (recordVoiceBegin) {
            if (actualRecordTime != 0) {
                goRecordSuccessState();
            } else {
                goRecordFailState();
            }
        }
    }

    @Override
    public void recordVoiceFinish() {
        if (recordVoiceBegin) {
            actualRecordTime = recordTime;
            goRecordSuccessState();
        }
    }


    @Override
    public void playVoiceBegin(long duration) {

    }

    @Override
    public void playVoiceFail() {
//        playVoiceButton.setImageResource(R.drawable.selector_record_voice_play);
    }

    @Override
    public void playVoiceStateChanged(long currentDuration) {

    }

    @Override
    public void playVoicePause() {

    }

    @Override
    public void playVoiceFinish() {
//        playVoiceButton.setImageResource(R.drawable.selector_record_voice_play);
    }

    @Override
    public void updateDecodeProgress(int decodeProgress) {
//        composeProgressBar.setProgress(
//                decodeProgress * RecordConstant.MaxDecodeProgress / RecordConstant.NormalMaxProgress);
    }

    private void compose() {
        composeProgressBar.setProgress(0);
        composeProgressBar.setVisibility(View.VISIBLE);
        recordVoiceButton.setEnabled(false);

        LogUtils.LOGE(tag,tempVoicePcmUrl);
        LogUtils.LOGE(tag,decodeFileUrl);
        LogUtils.LOGE(tag,composeVoiceUrl);

//        if (audoManager.isWiredHeadsetOn()) { //true 带了耳机
//            AudioFunction.BeginComposeAudio(tempVoicePcmUrl, decodeFileUrl, composeVoiceUrl, false,
//                    RecordConstant.VoiceEarWeight, RecordConstant.VoiceEarBackgroundWeight,
//                    0, this);
//        } else {
//            AudioFunction.BeginComposeAudio(tempVoicePcmUrl, decodeFileUrl, composeVoiceUrl, false,
//                    RecordConstant.VoiceWeight, RecordConstant.VoiceBackgroundWeight,
//                    0, this);
//        }

        AudioFunction.BeginComposeAudio(tempVoicePcmUrl, decodeFileUrl, composeVoiceUrl, false,
                RecordConstant.VoiceWeight, RecordConstant.VoiceBackgroundWeight,
                0, this);
    }

    @Override
    public void decodeSuccess() {
        DownloadMusicInfoDBHelper downloadMusicInfoDBHelper = new DownloadMusicInfoDBHelper(this);
        downloadMusicInfoDBHelper.updateDecode(music.mid, 1, System.currentTimeMillis());
        downloadMusicInfoDBHelper.close();
    }

    @Override
    public void decodeFail() {
        ToastManager.getInstance(instance).showText("对不起，音频解码失败，请在设置意见反馈中提交您的机型。");
        composeProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void updateComposeProgress(int composeProgress) {
        if (composeProgress == 10 || composeProgress == 20 ||
                composeProgress == 30 || composeProgress == 40 ||
                composeProgress == 50 || composeProgress == 60 ||
                composeProgress == 70 || composeProgress == 80 ||
                composeProgress == 90 || composeProgress == 100) {
            composeProgressBar.setProgress(composeProgress);
        }
    }

    @Override
    public void composeSuccess() {
        recordVoiceButton.setEnabled(true);
        composeProgressBar.setVisibility(View.GONE);

        recordComFinish = true;
        composeVoice = new ComposeVoice();
        composeVoice.fromuid = AppShare.getUserInfo(instance).uid;
        composeVoice.mid = music.mid;
        composeVoice.musicname = music.musicname;
        composeVoice.musictype = music.musictype;
        composeVoice.chapter = music.chapter;
        composeVoice.accompaniment = music.accompaniment;
        composeVoice.soundtime = actualRecordTime;
        composeVoice.isformulation = music.isformulation;
        composeVoice.isopen = "1";
        composeVoice.status = "1";
        composeVoice.listenprice = "1";
        composeVoice.questionprice = "0";
        composeVoice.commenttime = "0";
        composeVoice.commentpath = "";
        composeVoice.touid = 0;
        composeVoice.soundpath = "";
        composeVoice.voicename = fileNameCom;
        composeVoice.type = music.type + "";
        composeVoice.isreply = "0";
        composeVoice.ispay = "0";
        composeVoice.createtime = System.currentTimeMillis();

        if (AppInfo.isForeground(instance, "RecordComActivity")) {

            icon_record.setImageResource(R.mipmap.icon_pause);
            CommonFunction.showToast("合成成功", className);
        }

        ComposeVoiceInfoDBHelper composeVoiceInfoDBHelper = new ComposeVoiceInfoDBHelper(instance);
        composeVoiceInfoDBHelper.insert(composeVoice, ComposeVoiceInfoDBHelper.COMPOSE);
        composeVoiceInfoDBHelper.close();
    }

    @Override
    public void composeFail() {
        recordVoiceButton.setEnabled(true);
        composeProgressBar.setVisibility(View.GONE);
        CommonFunction.showToast("合成失败", className);
    }


    @OnClick({R.id.record, R.id.reset, R.id.finish})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record:
                rlHint.setVisibility(View.INVISIBLE);
                if (recordComFinish) {
                    if (VoiceFunction.IsRecordingVoice()) {
                    } else {
//                        if (playUtils.isPlaying()) {
//                            playUtils.pause();
//                            icon_record.setImageResource(R.mipmap.icon_play);
//                        } else {
//                            playUtils.playUrl(composeVoiceUrl);
//                            icon_record.setImageResource(R.mipmap.icon_pause);
//                        }
                    }
                } else if (recordVoiceBegin) {

                } else {
                    VoiceFunction.StartRecordVoice(instance);
                    tempVoicePcmUrl = VoiceFunction.getRecorderPcmPath();
                }
                break;
            case R.id.finish:
                if (recordComFinish) {

                    final Bundle bundle = new Bundle();
                    bundle.putSerializable("composeVoice", composeVoice);
                    String title = "找个导师点评一下吗？";
                    String[] items = new String[]{"免费上传作品", "找导师请教"};
                    MenuDialogSelectTeaHelper menuDialogSelectTeaHelper = new MenuDialogSelectTeaHelper(instance, title, items, new MenuDialogSelectTeaHelper.TeaListener() {
                        @Override
                        public void onTea(int tea) {
                            switch (tea) {
                                case 1:
                                    Intent intent = new Intent(instance, StubActivity.class);
                                    intent.putExtra("fragment", AddQuestionFragment.class.getName());
                                    intent.putExtras(bundle);
                                    instance.startActivity(intent);

                                    break;
                                case 0:

                                    Intent i = new Intent(instance, StubActivity.class);
                                    i.putExtra("fragment", UploadXizuoFragment.class.getName());
                                    i.putExtras(bundle);
                                    instance.startActivity(i);
                                    //  mediaPlayer.stop();
                                    //   playUtils.stop();
                                    break;
                            }

                        }
                    });
                    menuDialogSelectTeaHelper.show(recordVoiceButton);
                } else if (recordVoiceBegin) {
                    VoiceFunction.StopRecordVoice();

                    icon_finish.setImageResource(R.mipmap.upload);

//                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setTitle("完成录制");
//                    builder.setMessage("当前伴奏还没有结束，确定要提前完成录制吗？");
//                    builder.setNegativeButton("取消", null);
//                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            //  recordVoiceButton.setText(getResources().getString(R.string.start_recording));
//                            if (recordTime > 9) {
//
//                                VoiceFunction.StopRecordVoice();
//                                compose();
//                                icon_finish.setImageResource(R.mipmap.upload);
//
//                            } else {
//                                ToastManager.getInstance(instance).showText("录音时间要大于10秒钟");
//                            }
//                            dialog.dismiss();
//
//                        }
//                    });
//                    builder.show();

                }


                break;
            case R.id.reset:
                actualRecordTime = 0;
                recordVoiceBegin = false;
                recordComFinish = false;


                VoiceFunction.StopRecordVoice();
                icon_finish.setImageResource(R.mipmap.finish);
                //   playUtils.stop();
                icon_record.setImageResource(R.mipmap.icon_record);

                break;
            case R.id.title_back:
                finish();

                break;


        }

    }

    @Override
    protected void onDestroy() {
        VoiceFunction.StopRecordVoice();

        super.onDestroy();
    }


    private void exit() {
        if (recordVoiceBegin) {
            MenuDialogGiveupRecordHelper menuDialogGiveupRecordHelper = new MenuDialogGiveupRecordHelper(instance, new MenuDialogListerner() {
                @Override
                public void onSelected(int selected) {
                    switch (selected) {
                        case 0:
                            actualRecordTime = 0;
                            recordVoiceBegin = false;
                            VoiceFunction.StopRecordVoice();
                            break;
                        case 1:
                            finish();
                            VoiceFunction.GiveUpRecordVoice(true);
                            break;
                    }
                }
            });
            menuDialogGiveupRecordHelper.show(recordVoiceButton);
        } else {
            finish();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
//            dialog();
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }



    @PermissionSuccess(requestCode = 100)
    public void openContact() {
        bindView();
        initView();
        initData();
    }

    @PermissionFail(requestCode = 100)
    public void failContact() {

        Toast.makeText(this, getString(R.string.permission_record_hint), Toast.LENGTH_SHORT).show();
        finish();
        PermissionUtils.openSettingActivity(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onNetNoStart(String id) {

    }

    @Override
    public void onNetStart(String id) {

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {
        if (id.equals("getlyric")) {
            if (!TextUtils.isEmpty(netResponse.result)) {
                ArrayList<LyricDownInfo> lyricDownInfos = new Gson().fromJson(netResponse.result, new TypeToken<ArrayList<LyricDownInfo>>() {
                }.getType());
                String url = lyricDownInfos.get(0).lrc;
                LogUtils.LOGE(tag, url);
                if (!TextUtils.isEmpty(url)) {
                    DownLoaderlrcTask downLoaderlrcTask = new DownLoaderlrcTask(RecordComActivity.this, url, Variable.StorageMusicCachPath + fileName + ".lrc");
                    downLoaderlrcTask.execute();
                }

            }


        }

    }

    public class DownLoaderlrcTask extends AsyncTask<Void, Integer, Long> {
        private URL mUrl;
        private File mFile;
        private final String TAG = "DownLoaderTask";
        private ProgressReportingOutputStream mOutputStream;
        private int mProgress = 0;
        private Context mContext;
        private DialogHelper dialogHelper;

        public DownLoaderlrcTask(Context context, String downloadPath, String fileName) {
            try {
                mUrl = new URL(downloadPath);
                mFile = new File(fileName);
                mContext = context;
            } catch (MalformedURLException e) {

                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            if (dialogHelper == null) {
                dialogHelper = new DialogHelper(mContext, this);
            }
            dialogHelper.showProgressDialog();
        }

        @Override
        protected Long doInBackground(Void... params) {

            return download();
        }

        @Override
        protected void onPostExecute(Long result) {
            // TODO Auto-generated method stub
            if (dialogHelper != null) {
                dialogHelper.dismissProgressDialog();
            }

            lyricView.initLyricFile(LyricLoader.loadLyricFile(fileName));

            if (isCancelled())
                return;
        }

        private long download() {
            URLConnection connection = null;
            int bytesCopied = 0;
            try {
                connection = mUrl.openConnection();
                int length = connection.getContentLength();
                if (mFile.exists()/* && length == mFile.length()*/) {
                    Log.d(TAG, "file " + mFile.getName() + " already exits!!");
                    mFile.delete();
                }

                mOutputStream = new ProgressReportingOutputStream(mFile);
                publishProgress(0, length);
                bytesCopied = copy(connection.getInputStream(), mOutputStream);
                if (bytesCopied != length && length != -1) {
                    Log.e(TAG, "Download incomplete bytesCopied=" + bytesCopied
                            + ", length" + length);
                }
                mOutputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return bytesCopied;
        }

        private int copy(InputStream input, OutputStream output) {
            byte[] buffer = new byte[1024 * 8];
            BufferedInputStream in = new BufferedInputStream(input, 1024 * 8);
            BufferedOutputStream out = new BufferedOutputStream(output, 1024 * 8);
            int count = 0, n = 0;
            try {
                while ((n = in.read(buffer, 0, 1024 * 8)) != -1) {
                    out.write(buffer, 0, n);
                    count += n;
                }
                out.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return count;
        }

        private final class ProgressReportingOutputStream extends FileOutputStream {

            public ProgressReportingOutputStream(File file)
                    throws FileNotFoundException {
                super(file);
                // TODO Auto-generated constructor stub
            }

            @Override
            public void write(byte[] buffer, int byteOffset, int byteCount)
                    throws IOException {
                // TODO Auto-generated method stub
                super.write(buffer, byteOffset, byteCount);
                mProgress += byteCount;
                publishProgress(mProgress);
            }

        }

    }


    public class DownLoaderTask extends AsyncTask<Void, Integer, Long> {

        private final String TAG = "DownLoaderTask";
        private URL mUrl;
        private File mFile;
        private ImageView cicle;
        private ImageView background;
        private ProgressReportingOutputStream mOutputStream;

        private int mProgress = 0;

        public DownLoaderTask(String downloadPath, String fileName, String out, ImageView cicle, ImageView background) {
            super();
            this.cicle = cicle;
            this.background = background;

            try {
                mUrl = new URL(downloadPath);

                mFile = new File(out, fileName);
            } catch (MalformedURLException e) {

                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Long doInBackground(Void... params) {

            return download();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            // super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Long result) {
            // TODO Auto-generated method stub
            // super.onPostExecute(result);

            Log.e(TAG, "下载完");
            initBackground(mFile);
            if (isCancelled())
                return;
        }

        private long download() {
            URLConnection connection = null;
            int bytesCopied = 0;
            try {
                connection = mUrl.openConnection();
                int length = connection.getContentLength();
                if (mFile.exists()/* && length == mFile.length()*/) {
                    Log.d(TAG, "file " + mFile.getName() + " already exits!!");
                    mFile.delete();
                }

                mOutputStream = new ProgressReportingOutputStream(mFile);
                publishProgress(0, length);
                bytesCopied = copy(connection.getInputStream(), mOutputStream);
                if (bytesCopied != length && length != -1) {
                    Log.e(TAG, "Download incomplete bytesCopied=" + bytesCopied
                            + ", length" + length);
                }
                mOutputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return bytesCopied;
        }

        private int copy(InputStream input, OutputStream output) {
            byte[] buffer = new byte[1024 * 8];
            BufferedInputStream in = new BufferedInputStream(input, 1024 * 8);
            BufferedOutputStream out = new BufferedOutputStream(output, 1024 * 8);
            int count = 0, n = 0;
            try {
                while ((n = in.read(buffer, 0, 1024 * 8)) != -1) {
                    out.write(buffer, 0, n);
                    count += n;
                }
                out.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return count;
        }

        private final class ProgressReportingOutputStream extends FileOutputStream {

            public ProgressReportingOutputStream(File file)
                    throws FileNotFoundException {
                super(file);
                // TODO Auto-generated constructor stub
            }

            @Override
            public void write(byte[] buffer, int byteOffset, int byteCount)
                    throws IOException {
                // TODO Auto-generated method stub
                super.write(buffer, byteOffset, byteCount);
                mProgress += byteCount;
                publishProgress(mProgress);
            }

        }

    }

}
