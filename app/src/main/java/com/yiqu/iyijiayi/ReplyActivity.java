package com.yiqu.iyijiayi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.czt.mp3recorder.RecordConstant;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ui.abs.AbsFragmentAct;
import com.umeng.analytics.MobclickAgent;
import com.utils.L;
import com.yiqu.Tool.Function.VoiceFunctionF2;
import com.yiqu.Tool.Interface.VoicePlayerInterface;
import com.yiqu.Tool.Interface.VoiceRecorderOperateInterface;
import com.yiqu.iyijiayi.adapter.DialogHelper;
import com.yiqu.iyijiayi.adapter.UploaderTask;
import com.yiqu.iyijiayi.fragment.tab5.Tab5WopingDetailFragment;
import com.yiqu.iyijiayi.model.Question;
import com.yiqu.iyijiayi.net.MyNetApiConfig;
import com.yiqu.iyijiayi.net.MyNetRequestConfig;
import com.yiqu.iyijiayi.net.RestNetCallHelper;
import com.yiqu.iyijiayi.utils.AppShare;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;

public class ReplyActivity extends AbsFragmentAct implements NetCallBack, VoicePlayerInterface, VoiceRecorderOperateInterface {

    @BindView(R.id.new_record)
    public ImageView new_record;
    @BindView(R.id.record)
    public ImageView record;
    @BindView(R.id.recording)
    public ImageView recording;
    @BindView(R.id.play)
    public ImageView play;
    @BindView(R.id.send)
    public ImageView send;
    @BindView(R.id.msecond)
    public TextView msecond;

    private boolean is2mp3 = true;
    private boolean recordComFinish = false;
    private String recordPath;
    private Context mContext;
    private int totalTime = 2 * 60;
    private int recordTime;
    private String sid;
    private String id;

//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_reply);
//      //  setFinishOnTouchOutside(true);//
//        mContext = this;
//        WindowManager m = getWindowManager();
//        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
//        WindowManager.LayoutParams p = getWindow().getAttributes();
//        p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.3
//        p.width = (int) (d.getWidth()); // 宽度设置为屏幕
//        p.alpha = 1.0f;      //设置本身透明度
//        p.dimAmount = 0.5f;      //设置黑暗度
//        getWindow().setAttributes(p);
//        getWindow().setGravity(Gravity.BOTTOM);       //设置靠右对齐
//        ButterKnife.bind(this);
//
//        Intent intent = getIntent();
//        sid = intent.getStringExtra("sid");
////        final String fromuid = intent.getStringExtra("fromuid");
////        final String touid = intent.getStringExtra("touid");
////        final String toname = intent.getStringExtra("toname");
//
//        String s = AppShare.getLastComment(this);
//
//
//    }

    @Override
    protected int getContentView() {
        setFinishOnTouchOutside(true);
        return R.layout.activity_reply;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mContext = this;
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.3
        p.width = (int) (d.getWidth()); // 宽度设置为屏幕
        p.alpha = 1.0f;      //设置本身透明度
        p.dimAmount = 0.5f;      //设置黑暗度
        getWindow().setAttributes(p);
        getWindow().setGravity(Gravity.BOTTOM);       //设置靠右对齐

        Intent intent = getIntent();
        sid = intent.getStringExtra("sid");
        id = intent.getStringExtra("id");
//
//        final String touid = intent.getStringExtra("touid");
//        final String toname = intent.getStringExtra("toname");

        String s = AppShare.getLastComment(this);
    }

    public void onResume() {
        super.onResume();
        JAnalyticsInterface.onPageStart(this, "老师回答追问");
        MobclickAgent.onPageStart("老师回答追问"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        JAnalyticsInterface.onPageEnd(this, "老师回答追问");
        MobclickAgent.onPageEnd("老师回答追问"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    public void onPublish(int progress) {

    }

    @Override
    public void onChange(int position) {

    }

    private ViewTreeObserver.OnGlobalLayoutListener getGlobalLayoutListener(final View decorView, final View contentView) {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);

                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
                int diff = (height - r.bottom) / 2;

                if (diff != 0) {
                    if (contentView.getPaddingBottom() != diff) {
                        contentView.setPadding(0, 0, 0, diff);
                    }
                } else {
                    if (contentView.getPaddingBottom() != 0) {
                        contentView.setPadding(0, 0, 0, 0);
                    }
                }
            }
        };
    }

    @Override
    public void onNetNoStart(String id) {

    }

    @Override
    public void onNetStart(String id) {

    }

    @Override
    public void onNetEnd(String id, int type, NetResponse netResponse) {

        if (type == TYPE_SUCCESS) {
            ToastManager.getInstance(ReplyActivity.this).showText("回复成功");
            ArrayList<Question> questions = new Gson().fromJson(netResponse.data, new TypeToken<ArrayList<Question>>() {
            }.getType());
            Question q = questions.get(0);

            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("data",q);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    @OnClick({R.id.send, R.id.record, R.id.recording, R.id.new_record, R.id.play})
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.send:
                if (recordComFinish) {
                    final Map<String, String> params = new HashMap<String, String>();
                    params.put("type", String.valueOf(1));
                    if (!TextUtils.isEmpty(recordPath)) {
                        File file = new File(recordPath);
                        if (file.exists()) {
                            upLoaderTask u = new upLoaderTask(mContext, MyNetApiConfig.uploadSounds.getPath(), params, file);
                            u.execute();
                        }
                    }
                }

                break;
            case R.id.record:
                if (mPlayService != null) {
                    if (mPlayService.isPlaying()) {
                        mPlayService.pause();
                    }
                }

                VoiceFunctionF2.StopVoice();
                VoiceFunctionF2.StartRecordVoice(is2mp3, mContext, this);

                recordPath = VoiceFunctionF2.getRecorderPath();
                record.setVisibility(View.GONE);
                recording.setVisibility(View.VISIBLE);

                break;
            case R.id.recording:
                VoiceFunctionF2.StopVoice();
                if (!VoiceFunctionF2.isPauseRecordVoice(is2mp3)) {
                    VoiceFunctionF2.pauseRecordVoice(is2mp3);
                    recording.setImageResource(R.mipmap.tab5_icon_pause);
                    recordComFinish = true;
                } else {
                    VoiceFunctionF2.restartRecording(is2mp3);
                    recording.setImageResource(R.mipmap.icon_recording);
                    recordComFinish = false;
                }
                break;
            case R.id.new_record:

                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("重新录制");
                builder.setMessage("确定删除已录制作品，重新录制？");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  recordVoiceButton.setText(getResources().getString(R.string.start_recording));
                        recordComFinish = false;
                        VoiceFunctionF2.StopRecordVoice(is2mp3);
                        VoiceFunctionF2.StopVoice();
                        msecond.setText("0");
                        record.setVisibility(View.VISIBLE);
                        recording.setVisibility(View.GONE);
                    }
                });
                builder.show();

                break;
            case R.id.play:

                if (recordComFinish && !TextUtils.isEmpty(recordPath)) {
                    VoiceFunctionF2.StopVoice();
                    VoiceFunctionF2.PlayToggleVoice(recordPath, this);
                }
                break;

        }
    }

    @Override
    public void playVoiceBegin(long duration) {

    }

    @Override
    public void playVoiceFail() {

    }

    @Override
    public void playVoiceStateChanged(long currentDuration) {

    }

    @Override
    public void playVoicePause() {

    }

    @Override
    public void playVoiceFinish() {

    }

    @Override
    public void recordVoiceBegin() {

    }

    @Override
    public void recordVoiceStateChanged(int volume, long recordDuration) {
        if (recordDuration > 0) {
            recordTime = (int) (recordDuration / RecordConstant.OneSecond);
            int leftTime = totalTime - recordTime;
            msecond.setText(String.valueOf(recordTime));

            if (leftTime <= 0) {
                VoiceFunctionF2.StopRecordVoice(is2mp3);
                recording.setImageResource(R.mipmap.tab5_icon_pause);
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

    }

    @Override
    public void recordVoiceFinish() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VoiceFunctionF2.StopVoice();
        VoiceFunctionF2.StopRecordVoice(is2mp3);
        if (mPlayService != null) {
            if (mPlayService.isPlaying()) {
                mPlayService.stop();

            }
        }
    }

    private class upLoaderTask extends UploaderTask {
        private DialogHelper dialogHelper;

        public upLoaderTask(Context context, String RequestURL, Map<String, String> params, File file) {
            super(context, RequestURL, params, file);
        }

        @Override
        protected void onPreExecute() {
            if (dialogHelper == null) {
                dialogHelper = new DialogHelper(mContext, this, 100);
                dialogHelper.showProgressDialog();
            }
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            return super.doInBackground(params);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {


            dialogHelper.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            if (dialogHelper != null) {
                dialogHelper.dismissProgressDialog();

            }

            if (!TextUtils.isEmpty(s)) {
                RestNetCallHelper.callNet(
                        mContext,
                        MyNetApiConfig.questionReply,
                        MyNetRequestConfig.questionReply(mContext,id, String.valueOf(sid),
                                s, String.valueOf(recordTime), "1"),
                        "soundReply", ReplyActivity.this);

            } else {
                ToastManager.getInstance(mContext).showText(getString(R.string.net_error));

            }

            super.onPostExecute(s);
        }
    }
}
