package com.yiqu.Control.Main;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.base.utils.ToastManager;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.service.Download;
import com.service.DownloadService;
import com.service.PlayService;
import com.ui.views.CircleImageView;
import com.umeng.analytics.MobclickAgent;
import com.yiqu.Tool.Function.AudioFunction;
import com.yiqu.Tool.Function.CommonFunction;
import com.yiqu.Tool.Function.FileFunction;
import com.yiqu.Tool.Function.VoiceFunctionF2;
import com.czt.mp3recorder.RecordConstant;
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
import com.yiqu.iyijiayi.fragment.tab3.SelectArticalClassFragment;
import com.yiqu.iyijiayi.fragment.tab3.SelectArticalFragment;
import com.yiqu.iyijiayi.fragment.tab3.SelectBgMusicFragment;
import com.yiqu.iyijiayi.fragment.tab3.UploadXizuoFragment;
import com.model.ComposeVoice;
import com.model.Music;
import com.yiqu.iyijiayi.model.SelectArticle;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.utils.AppInfo;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.BitmapUtil;
import com.yiqu.iyijiayi.utils.DensityUtil;
import com.utils.LogUtils;
import com.yiqu.iyijiayi.utils.PermissionUtils;
import com.yiqu.iyijiayi.utils.PictureUtils;
import com.yiqu.iyijiayi.utils.RecorderAndPlayUtil;
import com.yiqu.iyijiayi.utils.String2TimeUtils;

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
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class RecordAllActivity extends Activity
        implements VoicePlayerInterface, DecodeOperateInterface, ComposeAudioInterface,
        VoiceRecorderOperateInterface, NetCallBack {
    private boolean recordVoiceBegin;
    private boolean recordComFinish = false;
    private String tag = "RecordAllActivity";
    //    private int width;
//    private int height;
    private int recordTime;
    private int actualRecordTime;
    private TextView tv_record;
    private TextView musicName;
    private TextView musictime;
    //  private TextView recordVoiceButton;
    private String className;
    private String tempVoicePcmUrl;
    private String musicFileUrl;
    private String decodeFileUrl;
    private String composeVoiceUrl;
    private ProgressBar composeProgressBar;
    private static RecordAllActivity instance;
    private final static int REQUESTMUSIC = 0x2001;
    private final static int REQUESTARTICLE = 0x2002;
    private final static int REQUESTUPLOAD = 0x2003;

    private ImageView title_back;
    private ImageView image_anim;
    //    private Animation rotate;
    private String fileNameCom;
    private ComposeVoice composeVoice;
    private String2TimeUtils string2TimeUtils;

    private static int TOTALTIME = 600;  //默认录音600s
    //    private static int TOTALTIME = 600;  //默认录音600s
    private int totalTime = TOTALTIME;  //默认录音600s
    private Music music;
    private AudioManager audoManager;
    private String eid;
    @BindView(R.id.background)
    public ImageView background;


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
    @BindView(R.id.select_music)
    public TextView select_music;
    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.author)
    public TextView author;
    @BindView(R.id.content)
    public TextView content;
    @BindView(R.id.select_article)
    public TextView select_article;
    @BindView(R.id.add_music)
    public ImageView add_music;
    @BindView(R.id.add_article)
    public ImageView add_article;
    @BindView(R.id.et_content)
    public EditText et_content;

    private String fileName;
    private TextView tv_totaltime;
    private ProgressBar pb_record;
    private int mid;
    protected DownloadService mDownloadService;
    private File backgroudMusciFile;
    private RecorderAndPlayUtil recorderUtil;
    private boolean is2mp3 = true;
    //    private String eventName;
    private String musicdesc;
    private SelectArticle selectArticle;
    private String event_title;
    protected PlayService mPlayService;
    private AlertDialog.Builder alertBuilder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        instance = this;
        init(R.layout.record_all_fragment);
        bindService(new Intent(this, com.service.DownloadService.class), mDownloadServiceConnection, Context.BIND_AUTO_CREATE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    private void init(int layoutId) {
        setContentView(layoutId);
        ButterKnife.bind(this);
        string2TimeUtils = new String2TimeUtils();
        PermissionGen.needPermission(this, 100, Manifest.permission.RECORD_AUDIO);
        className = getClass().getSimpleName();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = getIntent();
        eid = intent.getStringExtra("eid");
        event_title = intent.getStringExtra("title");
//        eventName = intent.getStringExtra("musicname");
//        musicdesc = intent.getStringExtra("musicdesc");
        if (TextUtils.isEmpty(eid)) {
            eid = "0";
        }

        if (!TextUtils.isEmpty(musicdesc)) {
            content.setText(musicdesc);
            et_content.setVisibility(View.GONE);
        }

        et_content.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                } else {

                }
            }
        });

    }

    public void bindView() {

        musicName = (TextView) findViewById(R.id.musicname);
        musictime = (TextView) findViewById(R.id.musictime);
        tv_totaltime = (TextView) findViewById(R.id.totaltime);
        tv_record = (TextView) findViewById(R.id.tv_record);
        title_back = (ImageView) findViewById(R.id.title_back);
        image_anim = (ImageView) findViewById(R.id.image_anim);
        composeProgressBar = (ProgressBar) findViewById(R.id.composeProgressBar);
        pb_record = (ProgressBar) findViewById(R.id.pb_record);
        tv_totaltime.setText(string2TimeUtils.stringForTimeS(totalTime));
        recorderUtil = new RecorderAndPlayUtil(this);
    }

    public void initView() {
//        composeProgressBar.getLayoutParams().width = (int) (width * 0.72);
        audoManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        Random random = new Random();
        int i = random.nextInt(4);
        String color[] = getResources().getStringArray(R.array.color);
        Bitmap bitmap = createColorBitmap(Color.parseColor(color[i]));

        reset.setImageBitmap(bitmap);
        recordVoiceButton.setImageBitmap(bitmap);
        finish.setImageBitmap(bitmap);


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
                try {
                    initBackground(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                DownLoaderTask downLoaderTask = new DownLoaderTask(url, fileName, Variable.StorageImagePath, image_anim, background);
                downLoaderTask.execute();
            }
        } else {
            //  Picasso.with(context).load(R.mipmap.home_bg).into(icon);
        }

        Intent intent = getIntent();
        music = (Music) intent.getSerializableExtra("music");
        if (music == null) {

        }

    }

    private Bitmap createColorBitmap(int color) {
        Bitmap bmp = Bitmap.createBitmap(DensityUtil.dip2px(this, 60), DensityUtil.dip2px(this, 60), Bitmap.Config.ARGB_8888);
        bmp.eraseColor(color);
        return bmp;
    }

    public void initMusicData() {
        String Url = MyNetApiConfig.ImageServerAddr + music.musicpath;
        URI uri = URI.create(Url);
        fileName = FileFunction.getValidFileName(uri);
        backgroudMusciFile = new File(Variable.StorageMusicCachPath(this), fileName);
        if (backgroudMusciFile.exists()) {
            initMusicUI();

        } else {
            if (mDownloadService != null) {
                mDownloadService.download(music.mid, Url, Variable.StorageMusicCachPath(this),
                        fileName);
            }
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

    private void initMusicUI() {
        is2mp3 = false;
        musicFileUrl = backgroudMusciFile.getAbsolutePath();
        String tempfileName = fileName.substring(0, fileName.lastIndexOf("."));
        decodeFileUrl = Variable.StorageMusicPath + tempfileName + ".pcm";
        recordTime = 0;
        long t = System.currentTimeMillis() / 1000;
        fileNameCom = music.musicname + t + "cv.mp3";
        composeVoiceUrl = Variable.StorageMusicPath + fileNameCom;
        mid = music.mid;
//        totalTime = music.time;
        tv_totaltime.setText(string2TimeUtils.stringForTimeS(totalTime));
        pb_record.setMax(totalTime);
        select_music.setText(music.musicname);
        add_music.setBackgroundResource(R.mipmap.cancle_music);
//        VoiceFunctionF2.PlayToggleVoice(musicFileUrl, this);
//        VoiceFunctionF2.StopVoice();
    }

    private void deleteMusicUI() {

        recordTime = 0;
        fileNameCom = "";
        composeVoiceUrl = "";
        mid = 0;
        totalTime = TOTALTIME;
        pb_record.setMax(totalTime);
        select_music.setText("请选择配乐");
        add_music.setBackgroundResource(R.mipmap.add_music);
        is2mp3 = true;
    }

    private void initArticleData(SelectArticle selectArticle) {
        select_article.setText(selectArticle.title);
        title.setText(selectArticle.title);
        author.setText(selectArticle.author);
        content.setText(selectArticle.content);
        image_anim.setVisibility(View.GONE);
        musicName.setText(selectArticle.title);
        add_article.setBackgroundResource(R.mipmap.cancle_music);
        et_content.setVisibility(View.GONE);
    }

    private void deleteArticleData() {
        select_article.setText("请选择范文");
        title.setText("");
        author.setText("");
        content.setText("");
        image_anim.setVisibility(View.GONE);
        musicName.setText("录制");
        et_content.setVisibility(View.VISIBLE);
        add_article.setBackgroundResource(R.mipmap.add_music);
        selectArticle = null;
        // et_content.setVisibility(View.VISIBLE);
    }


//
//    private Handler handler = new Handler();
//
//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            if (VoiceFunctionF2.IsRecordingVoice()) {
//                long time = mediaPlayer.getCurrentPosition();
//                lyricView.updateLyrics((int) time, mediaPlayer.getDuration());
//            }
//            handler.postDelayed(this, 100);
//        }
//    };

    private void initBackground(File file) throws Exception {
        PictureUtils.showPictureFile(instance, file, image_anim, 270);
        Bitmap bt = BitmapFactory.decodeFile(file.getAbsolutePath());

        //  background.setImageBitmap(bt);
        Bitmap b = BitmapUtil.blur(bt, 25f, this);
        Bitmap bb = BitmapUtil.blur(b, 25f, this);
        background.setImageBitmap(bb);
        //  background.setImageAlpha(120); //0完全透明，255
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("录制播音页面");
        MobclickAgent.onResume(this);
        JAnalyticsInterface.onPageStart(this, "录制播音页面");
    }

    @Override
    protected void onStart() {
        super.onStart();
        allowBindService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        allowUnbindService();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("录制播音页面");
        MobclickAgent.onPause(this);
        JAnalyticsInterface.onPageEnd(this, "录制播音页面");
    }


    private void goRecordSuccessState() {
        recordVoiceBegin = false;
        musictime.setText(string2TimeUtils.stringForTimeS(actualRecordTime));
        pb_record.setProgress(actualRecordTime);
        recordComFinish = true;
        icon_finish.setImageResource(R.mipmap.upload);
        if (backgroudMusciFile != null && backgroudMusciFile.exists()) {
            compose();
        } else {
            stopRecording();
        }
    }

    private void goRecordFailState() {
        recordVoiceBegin = false;
        // musictime.setVisibility(View.INVISIBLE);

    }


    @Override
    public void recordVoiceBegin() {
        pb_record.setMax(totalTime);
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
                VoiceFunctionF2.StopRecordVoice(is2mp3);

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
            if (actualRecordTime == totalTime) {
                if (selectArticle == null || TextUtils.isEmpty(selectArticle.title)) {
                    final EditText inputServer = new EditText(RecordAllActivity.this);
                    inputServer.setBackgroundResource(R.drawable.edit_bg);
                    inputServer.setFocusable(true);
                    inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
                    AlertDialog.Builder b = new AlertDialog.Builder(RecordAllActivity.this);
                    b.setTitle(getString(R.string.record_save_dialog_title));
                    b.setView(inputServer);
                    b.setNegativeButton("取消", null);
                    b.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    String inputName = inputServer.getText().toString();
                                    if (TextUtils.isEmpty(inputName)) {
                                        ToastManager.getInstance(instance).showText("请输入名字");
                                        return;
                                    }
                                    musicName.setText(inputName + "");
                                    goRecordSuccessState();
                                    icon_finish.setImageResource(R.mipmap.upload);
                                    //          ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(RecordAllActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                    hideSoftInput();

                                }
                            });
                    b.show();
                    icon_record.setImageResource(R.mipmap.icon_record);
                } else {
                    icon_finish.setImageResource(R.mipmap.upload);
                    goRecordSuccessState();
                }


            } else {
                goRecordSuccessState();
            }

        }
    }

    @Override
    public void playVoiceBegin(long duration) {
        if (!recordVoiceBegin){
            int pTotalTime = (int) duration / 1000;
            tv_totaltime.setText(string2TimeUtils.stringForTimeS(pTotalTime));
            pb_record.setMax(pTotalTime);
        }

        if (backgroudMusciFile != null && backgroudMusciFile.exists()) {
            DownloadMusicInfoDBHelper downloadMusicInfoDBHelper = new DownloadMusicInfoDBHelper(instance);
            Music m = downloadMusicInfoDBHelper.getDecode(mid);
            downloadMusicInfoDBHelper.close();
            if (m.isdecode == -1) {
                AudioFunction.DecodeMusicFile(musicFileUrl, decodeFileUrl, 0,
                        totalTime, instance);
                downloadMusicInfoDBHelper.updateDecode(mid, 0, System.currentTimeMillis());
            } else if (m.isdecode == 0 && System.currentTimeMillis() - m.decodetime > 2 * 60 * 1000) { //解码超过两分钟,重新解码。
                File file = new File(decodeFileUrl);
                file.delete();
                AudioFunction.DecodeMusicFile(musicFileUrl, decodeFileUrl, 0,
                        totalTime, instance);
                downloadMusicInfoDBHelper.updateDecode(mid, 0, System.currentTimeMillis());
            }
            downloadMusicInfoDBHelper.close();
        }
    }

    @Override
    public void playVoiceFail() {
//        playVoiceButton.setImageResource(R.drawable.selector_record_voice_play);
    }

    @Override
    public void playVoiceStateChanged(long currentDuration) {

        if (recordComFinish) {
            if (currentDuration > 0) {
                int playtime = (int) (currentDuration / RecordConstant.OneSecond);
                musictime.setText(string2TimeUtils.stringForTimeS(playtime));
                pb_record.setProgress(playtime);

            }
        }

    }

    @Override
    public void playVoicePause() {

    }

    @Override
    public void playVoiceFinish() {
        if (recordComFinish) {
            icon_record.setImageResource(R.mipmap.icon_play);
            upload();
        }

        if (recordVoiceBegin) {
            if (!is2mp3) {
                VoiceFunctionF2.PlayToggleVoice(backgroudMusciFile.getAbsolutePath(), this);
            }
        }
    }

    @Override
    public void updateDecodeProgress(int decodeProgress) {

    }

    private DialogHelper dialogHelper;

    private void compose() {
        composeProgressBar.setProgress(0);
        composeProgressBar.setVisibility(View.VISIBLE);
        recordVoiceButton.setEnabled(false);
        tempVoicePcmUrl = VoiceFunctionF2.getRecorderPath();
        if (audoManager.isWiredHeadsetOn()) { //true 带了耳机
            AudioFunction.BeginComposeAudio(tempVoicePcmUrl, decodeFileUrl, composeVoiceUrl, false,
                    RecordConstant.VoiceEarWeight, RecordConstant.VoiceEarBackgroundWeight,
                    0, this);
        } else {
            AudioFunction.BeginComposeAudio(tempVoicePcmUrl, decodeFileUrl, composeVoiceUrl, false,
                    RecordConstant.VoiceWeight, RecordConstant.VoiceBackgroundWeight,
                    0, this);
        }
        if (dialogHelper == null) {
            dialogHelper = new DialogHelper(instance, 100);
            dialogHelper.showProgressDialog();
        }
    }

    @Override
    public void decodeSuccess() {
        DownloadMusicInfoDBHelper downloadMusicInfoDBHelper = new DownloadMusicInfoDBHelper(this);
        downloadMusicInfoDBHelper.updateDecode(mid, 1, System.currentTimeMillis());
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
            dialogHelper.setProgress(composeProgress);
        }

    }

    private void stopRecording() {
        recordVoiceButton.setEnabled(true);
        composeProgressBar.setVisibility(View.GONE);
        recordComFinish = true;
        composeVoice = new ComposeVoice();
        composeVoice.fromuid = AppShare.getUserInfo(instance).uid;
        composeVoice.mid = 0;
        composeVoice.type = "2";
        composeVoice.musicname = musicName.getText().toString();
        composeVoice.musictype = "";
        composeVoice.chapter = "";
        if (!TextUtils.isEmpty(content.getText().toString())) {
            composeVoice.article_content = content.getText().toString();
        }
        composeVoice.voicename = VoiceFunctionF2.getRecorderPath();
        composeVoice.accompaniment = "";
        composeVoice.soundtime = actualRecordTime;
        composeVoice.isformulation = "0";
        composeVoice.isopen = "1";
        composeVoice.status = "1";
        composeVoice.listenprice = "1";
        composeVoice.questionprice = "0";
        composeVoice.commenttime = "0";
        composeVoice.commentpath = "";
        composeVoice.touid = 0;
        composeVoice.soundpath = "";
        composeVoice.isreply = "0";
        composeVoice.ispay = "0";
        composeVoice.createtime = System.currentTimeMillis();
        ComposeVoiceInfoDBHelper composeVoiceInfoDBHelper = new ComposeVoiceInfoDBHelper(instance);
        composeVoiceInfoDBHelper.insert(composeVoice, ComposeVoiceInfoDBHelper.UNCOMPOSE);
        composeVoiceInfoDBHelper.close();
        VoiceFunctionF2.StopVoice();
        VoiceFunctionF2.PlayToggleVoice(VoiceFunctionF2.getRecorderPath(), instance);

    }

    @Override
    public void composeSuccess() {
        recordVoiceButton.setEnabled(true);
        composeProgressBar.setVisibility(View.GONE);
        recordComFinish = true;
        composeVoice = new ComposeVoice();
        composeVoice.fromuid = AppShare.getUserInfo(instance).uid;
        composeVoice.mid = mid;
        if (!TextUtils.isEmpty(content.getText().toString())) {
            composeVoice.article_content = content.getText().toString();
        }
        composeVoice.musicname = musicName.getText().toString();
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
        composeVoice.voicename = composeVoiceUrl;
        composeVoice.type = music.type + "";
        composeVoice.isreply = "0";
        composeVoice.ispay = "0";
        composeVoice.createtime = System.currentTimeMillis();

        if (AppInfo.isForeground(instance, getClass().getSimpleName())) {
            VoiceFunctionF2.PlayToggleVoice(composeVoiceUrl, this);
            icon_record.setImageResource(R.mipmap.icon_pause);
            CommonFunction.showToast("合成成功", className);
        }

        ComposeVoiceInfoDBHelper composeVoiceInfoDBHelper = new ComposeVoiceInfoDBHelper(instance);
        composeVoiceInfoDBHelper.insert(composeVoice, ComposeVoiceInfoDBHelper.UNCOMPOSE);
        composeVoiceInfoDBHelper.close();

        if (dialogHelper != null) {
            dialogHelper.dismissProgressDialog();
            dialogHelper = null;
        }
    }

    @Override
    public void composeFail() {
        recordVoiceButton.setEnabled(true);
        composeProgressBar.setVisibility(View.GONE);
        CommonFunction.showToast("合成失败", className);
    }


    @OnClick({R.id.record, R.id.reset, R.id.finish, R.id.select_music,
            R.id.select_article, R.id.add_article, R.id.title_back, R.id.add_music})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.record:
                if (recordComFinish) {
                    if (recordVoiceBegin) {

                    } else {
                        if (VoiceFunctionF2.IsPlaying()) {
                            VoiceFunctionF2.pauseVoice();
                            icon_record.setImageResource(R.mipmap.icon_play);
                        } else {
                            VoiceFunctionF2.startPlayVoice();
                            icon_record.setImageResource(R.mipmap.icon_pause);
                        }
                    }
                } else if (recordVoiceBegin) {

                    if (VoiceFunctionF2.isPauseRecordVoice(is2mp3)) {
                        VoiceFunctionF2.restartRecording(is2mp3);
                        icon_record.setImageResource(R.mipmap.icon_pause);
                        if (backgroudMusciFile != null && backgroudMusciFile.exists()) {
                            VoiceFunctionF2.startPlayVoice();
                        }
                    } else {
                        VoiceFunctionF2.pauseRecordVoice(is2mp3);
                        icon_record.setImageResource(R.mipmap.icon_record);
                        if (backgroudMusciFile != null && backgroudMusciFile.exists()) {
                            VoiceFunctionF2.pauseVoice();
                        }
                    }
                } else {
                    content.setVisibility(View.VISIBLE);
                    icon_record.setImageResource(R.mipmap.icon_pause);
                    if (!TextUtils.isEmpty(et_content.getText().toString())) {
                        content.setText(et_content.getText().toString());
                        et_content.setVisibility(View.GONE);
                    }
                    if (selectArticle != null && !TextUtils.isEmpty(selectArticle.content)) {
                        content.setText(selectArticle.content);
                    }

                    if (!is2mp3) {
                        VoiceFunctionF2.PlayToggleVoice(backgroudMusciFile.getAbsolutePath(), this);

                    } else {
                        select_music.setText("无配乐");
                    }
                    VoiceFunctionF2.StartRecordVoice(is2mp3, instance);
                    select_music.setEnabled(false);
                    add_article.setEnabled(false);
                    add_music.setEnabled(false);
                    add_article.setVisibility(View.INVISIBLE);
                    add_music.setVisibility(View.INVISIBLE);
                    select_article.setEnabled(false);
                    et_content.setVisibility(View.INVISIBLE);
                    if (select_article.getText().equals("请选择范文")) {
                        select_article.setText("无范文");
                    }
                }
                break;
            case R.id.finish:
                if (recordComFinish) {
                    upload();
                } else if (recordVoiceBegin) {

                    if (recordTime > 9 && recordTime < TOTALTIME) {
                        if (selectArticle == null || TextUtils.isEmpty(selectArticle.title)) {

                            AlertDialog.Builder b = new AlertDialog.Builder(RecordAllActivity.this);
                            final EditText inputServer = new EditText(RecordAllActivity.this);
                            inputServer.setBackgroundResource(R.drawable.edit_bg);
                            inputServer.setFocusable(true);
                            inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
                            b.setTitle(getString(R.string.record_save_dialog_title));
                            b.setView(inputServer);
                            b.setNegativeButton("取消", null);
                            b.setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            String inputName = inputServer.getText().toString();
                                            if (TextUtils.isEmpty(inputName)) {
                                                ToastManager.getInstance(instance).showText("请输入名字");
                                                return;
                                            }
                                            musicName.setText(inputName + "");
                                            //
                                            if (!is2mp3) {
                                                VoiceFunctionF2.StopVoice();
                                            }
                                            VoiceFunctionF2.StopRecordVoice(is2mp3);
                                            icon_finish.setImageResource(R.mipmap.upload);
                                            hideSoftInput();

                                        }
                                    });
                            b.show();

                            VoiceFunctionF2.pauseRecordVoice(is2mp3);
                            if (!is2mp3) {
                                VoiceFunctionF2.pauseVoice();
                            }
                            icon_record.setImageResource(R.mipmap.icon_record);
                        } else {
                            if (!is2mp3){
                                VoiceFunctionF2.StopVoice();
                            }
                         //
                            VoiceFunctionF2.StopRecordVoice(is2mp3);

                            icon_finish.setImageResource(R.mipmap.upload);
                        }

                    } else if (recordTime <= 9) {
                        ToastManager.getInstance(instance).showText("录音时间要大于10秒钟");
                    } else {
                        final EditText inputServer = new EditText(RecordAllActivity.this);
                        inputServer.setBackgroundResource(R.drawable.edit_bg);
                        inputServer.setFocusable(true);
                        inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(RecordAllActivity.this);
                        alertBuilder.setTitle(getString(R.string.record_save_dialog_title));

                        alertBuilder.setView(inputServer);
                        alertBuilder.setNegativeButton("取消", null);
                        alertBuilder.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {
                                        String inputName = inputServer.getText().toString();
                                        if (TextUtils.isEmpty(inputName)) {
                                            ToastManager.getInstance(instance).showText("请输入名字");
                                            return;
                                        }
                                        musicName.setText(inputName + "");
                                        goRecordSuccessState();
                                        hideSoftInput();
                                    }
                                });
                        alertBuilder.show();
                    }
                }
                break;
            case R.id.reset:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("重新录制");
                builder.setMessage("确定删除已录制作品，重新录制？");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reset();
                    }
                });
                builder.show();

                break;
            case R.id.title_back:
                exit();

                break;
            case R.id.add_article:
                deleteArticleData();

                break;
            case R.id.add_music:
                deleteMusicUI();

                break;
            case R.id.select_music:
                Intent i = new Intent(this, StubActivity.class);
                i.putExtra("fragment", SelectBgMusicFragment.class.getName());
                startActivityForResult(i, REQUESTMUSIC);

                break;
            case R.id.select_article:

                Intent intent = new Intent(this, StubActivity.class);
                if (TextUtils.isEmpty(eid) || eid.equals("0")) {
                    intent.putExtra("fragment", SelectArticalFragment.class.getName());
                } else {
                    intent.putExtra("fragment", SelectArticalClassFragment.class.getName());
                    intent.putExtra("title", event_title);
                    intent.putExtra("eid", eid);
                }
                startActivityForResult(intent, REQUESTARTICLE);

                break;
        }
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void hideSoftInput() {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void reset() {
        actualRecordTime = 0;
        recordVoiceBegin = false;
        recordComFinish = false;
        VoiceFunctionF2.StopRecordVoice(is2mp3);
        VoiceFunctionF2.StopVoice();
        icon_finish.setImageResource(R.mipmap.finish);
        icon_record.setImageResource(R.mipmap.icon_record);
        pb_record.setProgress(0);
        musictime.setText("00:00");
        select_music.setEnabled(true);
        select_article.setEnabled(true);
        add_article.setEnabled(true);
        add_music.setEnabled(true);
        add_article.setVisibility(View.VISIBLE);
        add_music.setVisibility(View.VISIBLE);
        if (selectArticle == null || TextUtils.isEmpty(selectArticle.content)) {
            et_content.setVisibility(View.VISIBLE);
            content.setVisibility(View.GONE);
            content.setText("");
            select_article.setText("请选择范文");

        }
        if (music == null || is2mp3 ){
            select_music.setText("请选择配乐");
        }

    }

    private void upload() {
        final Bundle bundle = new Bundle();
        bundle.putSerializable("composeVoice", composeVoice);
        String title = "提示";
        String[] items = new String[]{"免费上传作品"};
        VoiceFunctionF2.pauseVoice();
        icon_record.setImageResource(R.mipmap.icon_play);
        if (TextUtils.isEmpty(eid) || eid.equals("0")) {
            title = "找个导师点评一下吗？";
            items = new String[]{"免费上传作品", "找导师请教"};
        }
        MenuDialogSelectTeaHelper menuDialogSelectTeaHelper = new MenuDialogSelectTeaHelper(instance, title, items, new MenuDialogSelectTeaHelper.TeaListener() {
            @Override
            public void onTea(int tea) {
                switch (tea) {
                    case 1:
                        Intent intent = new Intent(instance, StubActivity.class);
                        intent.putExtra("fragment", AddQuestionFragment.class.getName());
                        intent.putExtras(bundle);
                        instance.startActivityForResult(intent, REQUESTUPLOAD);
                        break;
                    case 0:
                        Intent i = new Intent(instance, StubActivity.class);
                        i.putExtra("fragment", UploadXizuoFragment.class.getName());
                        i.putExtras(bundle);
                        i.putExtra("eid", eid);
                        instance.startActivityForResult(i, REQUESTUPLOAD);
                        break;
                }
            }
        });
        menuDialogSelectTeaHelper.show(recordVoiceButton);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUESTMUSIC:
                    Bundle b = data.getExtras(); //data为B中回传的Intent
                    //str即为回传的值
                    music = (Music) b.getSerializable("data");
                    initMusicData();
                    break;

                case REQUESTARTICLE:
                    Bundle bundle = data.getExtras(); //data为B中回传的Intent
                    //str即为回传的值
                    selectArticle = (SelectArticle) bundle.getSerializable("data");
                    initArticleData(selectArticle);
                    break;
                case REQUESTUPLOAD:
                    finish();

                    break;
            }
        }
    }

    private ServiceConnection mPlayServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mPlayService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayService = ((PlayService.PlayBinder) service).getService();
            //  mPlayService.setOnMusicEventListener(mMusicEventListener);
            //  onChange(mPlayService.getPlayingPosition());
            if (mPlayService != null) {
                mPlayService.pause();
            }

        }
    };

    /**
     * Fragment的view加载完成后回调
     */
    public void allowBindService() {
        bindService(new Intent(this, PlayService.class), mPlayServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    /**
     * fragment的view消失后回调
     */
    public void allowUnbindService() {
        unbindService(mPlayServiceConnection);
    }

    @Override
    protected void onDestroy() {
        //handler.removeCallbacks(runnable);
        VoiceFunctionF2.StopRecordVoice(is2mp3);
        VoiceFunctionF2.StopVoice();
        unbindService(mDownloadServiceConnection);
        super.onDestroy();
    }

    private Download.OnDownloadListener downloadListener =
            new Download.OnDownloadListener() {
                private DialogHelper dialogHelper;

                @Override
                public void onStart(int downloadId, long fileSize) {
                    if (dialogHelper == null) {
                        dialogHelper = new DialogHelper(instance, (int) fileSize, "配乐下载中...");
                        dialogHelper.showProgressDialog();
                    }
                }

                @Override
                public void onPublish(int downloadId, long size) {
                    //   Log.w("download", "publish" + size);
                    dialogHelper.setProgress((int) size);
                }

                @Override
                public void onSuccess(int downloadId) {
                    if (dialogHelper != null) {
                        dialogHelper.dismissProgressDialog();
                        dialogHelper = null;
                    }
                    initMusicUI();
                }

                @Override
                public void onPause(int downloadId) {
                    LogUtils.LOGE(tag, "onPause");
                }

                @Override
                public void onError(int downloadId) {
                    LogUtils.LOGE(tag, "onError");
                }

                @Override
                public void onCancel(int downloadId) {
                    LogUtils.LOGE(tag, "onCancel");
                }

                @Override
                public void onGoon(int downloadId, long localSize) {
                    LogUtils.LOGE(tag, "onGoon");
                }
            };


    private ServiceConnection mDownloadServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mDownloadService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mDownloadService = ((DownloadService.DownloadBinder) service).getService();
            mDownloadService.setOnDownloadEventListener(downloadListener);

        }
    };


    private void exit() {
        if (recordVoiceBegin) {
            MenuDialogGiveupRecordHelper menuDialogGiveupRecordHelper = new MenuDialogGiveupRecordHelper(instance, new MenuDialogListerner() {
                @Override
                public void onSelected(int selected) {
                    switch (selected) {
                        case 0:
                            reset();

                            break;
                        case 1:
                            finish();
                            VoiceFunctionF2.GiveUpRecordVoice(true);

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

//    private void startAnimation() {
//        image_anim.startAnimation(rotate);
//
//    }

//    private void clearAnimation() {
//        image_anim.clearAnimation();
//
//    }

    @PermissionSuccess(requestCode = 100)
    public void openContact() {
        bindView();
        initView();
        //  initData();
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
            try {
                initBackground(mFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
