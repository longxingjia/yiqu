package com.yiqu.iyijiayi.view;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.czt.mp3recorder.util.LameUtil;


/**
 * 自定义录音View
 * @author zhouyou
 */
public class AudioRecordView extends LinearLayout {
	
	// 录音缓存目录
	private static final String DIR = "LAME/mp3/";
	
	private static final int MSG_TIME_SHORT = 0x123;
	private Context mContext;
	private boolean mIsRecording = false;
	private boolean mIsLittleTime = false;
	private boolean mIsSendVoice = false;
	
	private RecorderAndPlayUtil mRecorderUtil = null;

	
	private TimerTask mTimerTask = null;
	private Timer mTimer = null;
	
	private float mSecond = 0;
	
	public AudioRecordView(Context context) {
		this(context, null);
	}

	public AudioRecordView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		

		mRecorderUtil = new RecorderAndPlayUtil();
		
		mRecorderUtil.getRecorder().setHandle(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case LameUtil.MSG_REC_STARTED:
					// 开始录音
					break;
				case LameUtil.MSG_REC_STOPPED:
					// 停止录音
					if (mIsSendVoice) {// 是否发送录音
						mIsSendVoice = false;
						audioRecordFinishListener.onFinish(mSecond, mRecorderUtil.getRecorderPath());
					}
					mSecond = 0;
					break;
				case LameUtil.MSG_ERROR_GET_MIN_BUFFERSIZE:
					initRecording();
					Toast.makeText(mContext, "采样率手机不支持", Toast.LENGTH_SHORT).show();
					break;
				case LameUtil.MSG_ERROR_CREATE_FILE:
					initRecording();
					Toast.makeText(mContext, "创建音频文件出错", Toast.LENGTH_SHORT).show();
					break;
				case LameUtil.MSG_ERROR_REC_START:
					initRecording();
					Toast.makeText(mContext, "初始化录音器出错", Toast.LENGTH_SHORT).show();
					break;
				case LameUtil.MSG_ERROR_AUDIO_RECORD:
					initRecording();
					Toast.makeText(mContext, "录音的时候出错", Toast.LENGTH_SHORT).show();
					break;
				case LameUtil.MSG_ERROR_AUDIO_ENCODE:
					initRecording();
					Toast.makeText(mContext, "编码出错", Toast.LENGTH_SHORT).show();
					break;
				case LameUtil.MSG_ERROR_WRITE_FILE:
					initRecording();
					Toast.makeText(mContext, "文件写入出错", Toast.LENGTH_SHORT).show();
					break;
				case LameUtil.MSG_ERROR_CLOSE_FILE:
					initRecording();
					Toast.makeText(mContext, "文件流关闭出错", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
	}

	// 播放完成回调
	public interface AudioRecordFinishListener {
		void onFinish(float second, String filePath);
	}

	private AudioRecordFinishListener audioRecordFinishListener;

	public void setAudioRecordFinishListener(AudioRecordFinishListener listener) {
		audioRecordFinishListener = listener;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if(action == MotionEvent.ACTION_DOWN){
			down();
		}else if(action == MotionEvent.ACTION_UP){
			if (mIsRecording) {
				audioRecordFinishListener.onFinish(mSecond, mRecorderUtil.getRecorderPath());
				initRecording();
			}
		}
		return true;
	}
	
	private void down() {
		if(mTimer != null) mTimer.cancel();
		if(mTimerTask != null) mTimerTask.cancel();
		
		mSecond = 0;
		mIsRecording = true;
		mIsLittleTime = true;
		mTimerTask = new TimerTask() {
			int i = 20;
			@Override
			public void run() {
				mIsLittleTime = false;
				mSecond += 1;
				i--;
				mHandler.post(new Runnable() {
					@Override
					public void run() {

					}
				});
				if (i == 0) {
					mIsSendVoice = true;
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							// 录音结束

							mTimer.cancel();
							mTimerTask.cancel();
							mIsRecording = false;
							mRecorderUtil.stopRecording();
						}
					});
				}
				if (i < 0) {
					mTimer.cancel();
					mTimerTask.cancel();
				}
			}
		};
		mRecorderUtil.startRecording();
		mTimer = new Timer(true);
		mTimer.schedule(mTimerTask, 1000, 1000);

	}

	Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_TIME_SHORT:

				break;
			}
		};
	};
	
	private void initRecording() {

		mTimer.cancel();
		mTimerTask.cancel();
		mRecorderUtil.stopRecording();
		mIsRecording = false;
		mSecond = 0;
		mRecorderUtil.getRecorderPath();
	}
	
	/**
	 * 删除当前录音文件
	 * @return true：删除成功 false： 删除失败
	 */
	public boolean deleteRecorderPath(){
		if (mRecorderUtil.getRecorderPath() != null && !TextUtils.isEmpty(mRecorderUtil.getRecorderPath())) {
			File file = new File(mRecorderUtil.getRecorderPath());
			file.delete();
			return true;
		}
		return false;
	}
}
