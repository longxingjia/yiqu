package com.yiqu.iyijiayi.fragment.tab3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.Tool.Function.FileFunction;
import com.Tool.Function.LogFunction;
import com.Tool.Function.VoiceFunction;
import com.Tool.Global.Variable;
import com.yiqu.Control.Main.RecordActivity;
import com.yiqu.Tool.Interface.ComposeAudioInterface;
import com.yiqu.Tool.Interface.DecodeOperateInterface;
import com.yiqu.Tool.Interface.VoicePlayerInterface;
import com.yiqu.Tool.Interface.VoiceRecorderOperateInterface;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.model.Music;
import com.yiqu.iyijiayi.net.MyNetApiConfig;

import com.yiqu.iyijiayi.utils.FileSizeUtil;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.String2TimeUtils;
import com.yiqu.iyijiayi.utils.Tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/2/15.
 */

public class RecordXizuoFragment extends AbsAllFragment implements View.OnClickListener, VoicePlayerInterface, DecodeOperateInterface, ComposeAudioInterface, VoiceRecorderOperateInterface {


    private TextView musicName;
    private String tag = "RecordXizuoFragment";
    private Music music;

    private boolean recordVoiceBegin;
    private int width;
    private int height;
    private int recordTime;
    private int actualRecordTime;

    private String className;
    private String tempVoicePcmUrl;
    private String musicFileUrl;
    private String decodeFileUrl;
    private String composeVoiceUrl;

    private TextView recordHintTextView;
    private TextView recordDurationView;
    private TextView musictime;
    private TextView musicSize;
    private TextView recordVoiceButton;
    private Button composeVoiceButton;
    private Button deleteVoiceButton;
    private Button playComposeVoiceButton;

    private ProgressBar composeProgressBar;

    private static Activity instance;

    private TextView tv_record;

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
//
        setTitleText("录制习作");

    }

    @Override
    protected int getTitleView() {
        return R.layout.titlebar_tab5;
    }

    @Override
    protected int getBodyView() {
        return R.layout.record_xizuo_fragment;
    }

    @Override
    protected void initView(View v) {
        instance = getActivity();
//
        className = getClass().getSimpleName();
        musicName = (TextView) v.findViewById(R.id.musicname);
        musictime = (TextView) v.findViewById(R.id.musictime);
        musicSize = (TextView) v.findViewById(R.id.musicSize);
        tv_record = (TextView) v.findViewById(R.id.tv_record);
//
        recordVoiceButton = (TextView) v.findViewById(R.id.submit);

    }

    @Override
    protected void init(Bundle savedInstanceState) {
//        submit.setClickable(false);
        Intent intent = getActivity().getIntent();
        music = (Music) intent.getSerializableExtra("music");
        LogUtils.LOGE(tag, music.toString());
//
        musicName.setText(music.musicname + "");

        Tools.DB_PATH = Tools.getExternalCacheDirectory(getActivity(), Environment.DIRECTORY_DOWNLOADS).toString();
        String Url = MyNetApiConfig.ImageServerAddr + music.musicpath;
        String fileName = Url.substring(
                Url.lastIndexOf("/") + 1,
                Url.length());
        fileName = music.musicname + "_" + fileName;
        String2TimeUtils string2TimeUtils = new String2TimeUtils();
        musictime.setText(string2TimeUtils.stringForTimeS(music.time));

        if (!TextUtils.isEmpty(Tools.DB_PATH)) {
            File mFile = new File(Tools.DB_PATH, fileName);
            LogUtils.LOGE(tag, mFile.length() + "");
            musicSize.setText(FileSizeUtil.getAutoFileOrFilesSize(mFile.getAbsolutePath()));

            if (mFile.exists()) {
                Log.d(tag, "file " + mFile.getName() + " already exits!!");
                recordTime = 0;
                tempVoicePcmUrl = Variable.StorageDirectoryPath + music.musicname + "_tempVoice.pcm";
                musicFileUrl = mFile.getAbsolutePath();
                decodeFileUrl = Variable.StorageDirectoryPath + music.musicname + "_decodeFile.pcm";
                composeVoiceUrl = Variable.StorageDirectoryPath + music.musicname + "_composeVoice.mp3";
//                initMusicFile();
                recordVoiceButton.setOnClickListener(this);
            }
        }


//        recordVoiceButton.setEnabled(true);
//        deleteVoiceButton.setEnabled(false);
//        composeVoiceButton.setEnabled(false);
//        playComposeVoiceButton.setEnabled(false);
        super.init(savedInstanceState);

    }


    @Override
    public void playVoiceBegin() {

    }

    @Override
    public void playVoiceFail() {

    }

    @Override
    public void playVoiceFinish() {

    }

    @Override
    public void recordVoiceBegin() {

    }

    @Override
    public void recordVoiceStateChanged(int volume, long recordDuration) {

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

    }

    @Override
    public void recordVoiceFinish() {

    }

    @Override
    public void updateComposeProgress(int composeProgress) {

    }

    @Override
    public void composeSuccess() {

    }

    @Override
    public void composeFail() {

    }

    @Override
    public void updateDecodeProgress(int decodeProgress) {

    }

    @Override
    public void decodeSuccess() {

    }

    @Override
    public void decodeFail() {

    }

//    private void initMusicFile() {
//        byte buffer[] = new byte[1024];
//
//        InputStream inputStream = null;
//        FileOutputStream fileOutputStream = FileFunction.GetFileOutputStreamFromFile(musicFileUrl);
//
//        try {
//            inputStream = getResources().openRawResource(R.raw.test);
//
//            if (fileOutputStream != null) {
//                while (inputStream.read(buffer) > -1) {
//                    fileOutputStream.write(buffer);
//                }
//            }
//        } catch (Exception e) {
//            LogFunction.error("write file异常", e);
//        } finally {
//            if (inputStream != null) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    LogFunction.error("close file异常", e);
//                }
//            }
//
//            if (fileOutputStream != null) {
//                try {
//                    fileOutputStream.close();
//                } catch (IOException e) {
//                    LogFunction.error("close file异常", e);
//                }
//            }
//
//            inputStream = null;
//            fileOutputStream = null;
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:

                if (recordVoiceBegin) {
                    VoiceFunction.StopRecordVoice();

                    recordHintTextView.setText("已结束录音");
                    recordVoiceButton.setText(getResources().getString(R.string.start_recording));
                } else {
//                    VoiceFunction.StartRecordVoice(tempVoicePcmUrl,
//                            instance);
                    recordHintTextView.setText("松开结束录音");
                    recordVoiceButton.setText("结束录音");
                }



                break;
        }
    }




}
