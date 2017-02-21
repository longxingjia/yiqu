package com.yiqu.iyijiayi.fragment.tab3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yiqu.Control.Main.RecordActivity;
import com.yiqu.Tool.Interface.ComposeAudioInterface;
import com.yiqu.Tool.Interface.DecodeOperateInterface;
import com.yiqu.Tool.Interface.VoicePlayerInterface;
import com.yiqu.Tool.Interface.VoiceRecorderOperateInterface;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.model.Music;
import com.yiqu.iyijiayi.net.MyNetApiConfig;

import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.Tools;

import java.io.File;

/**
 * Created by Administrator on 2017/2/15.
 */

public class RecordedXizuoFragment extends AbsAllFragment  implements VoicePlayerInterface, DecodeOperateInterface, ComposeAudioInterface, VoiceRecorderOperateInterface {


    private TextView musicName;
    private TextView sumbit;
    private String tag = "RecordedXizuoFragment";
    private Music music;
    private TextView submit;

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

    private Button recordVoiceButton;
    private Button composeVoiceButton;
    private Button deleteVoiceButton;
    private Button playComposeVoiceButton;

    private ProgressBar composeProgressBar;

    private static Activity instance;

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

        LogUtils.LOGE(tag,"hh");

        className = getClass().getSimpleName();
        musicName = (TextView) v.findViewById(R.id.musicname);
        TextView   musictime = (TextView) v.findViewById(R.id.musictime);

        submit = (TextView) v.findViewById(R.id.submit);


    }

    @Override
    protected void init(Bundle savedInstanceState) {
        submit.setClickable(false);
        Intent intent = getActivity().getIntent();
        music = (Music) intent.getSerializableExtra("music");

        musicName.setText(music.musicname + "");
        Tools.DB_PATH = Tools.getExternalCacheDirectory(getActivity(), Environment.DIRECTORY_DOWNLOADS).toString();

        String Url = MyNetApiConfig.ImageServerAddr + music.musicpath;
        String fileName = Url.substring(
                Url.lastIndexOf("/") + 1,
                Url.length());
        fileName = music.musicname + "_" + fileName;

        if (!TextUtils.isEmpty(Tools.DB_PATH)) {
            File mFile = new File(Tools.DB_PATH, fileName);
            if (mFile.exists()) {
                Log.d(tag, "file " + mFile.getName() + " already exits!!");
//                mFile.delete();
            } else {
                if (Tools.isNetworkAvailable(getActivity())){

                }
            }
        }
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
}
