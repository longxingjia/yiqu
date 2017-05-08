package com.yiqu.Tool.Recorder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.NoiseSuppressor;
import android.os.Build;
import android.util.Log;

import com.yiqu.Tool.Function.CommonFunction;
import com.yiqu.Tool.Function.FileFunction;
import com.yiqu.Tool.Function.LogFunction;
import com.yiqu.Tool.Function.PermissionFunction;
import com.yiqu.Tool.Global.RecordConstant;
import com.utils.Variable;
import com.yiqu.Tool.Common.CommonThreadPool;
import com.czt.mp3recorder.PCMFormat;

import java.io.BufferedOutputStream;
import java.io.IOException;

public class PCMRecorder {
    private short[] audioRecordBuffer;

    private int audioRecordBufferSize;
    private int realSampleDuration;
    private int realSampleNumberInOneDuration;

    private final static int toTransformLocationNumber = 3;
    private final static int receiveSuperEaCycleNumber = 10;
    private static final int recordSleepDuration = 500;

    private final static int sampleDuration = 100;

    //自定义 每160帧作为一个周期，通知一下需要进行编码
    private static final int FRAME_COUNT = 160;

    private static final PCMFormat pcmFormat = PCMFormat.PCM_16BIT;

    private double amplitude;

    private AudioRecord audioRecord = null;

    private RecordThread recordThread;
    private boolean mPause;

    public PCMRecorder() {
        init();
    }

    private void init() {
        initAudioRecord();
        Log.e("PCMRecorder","ff");
        recordThread = new RecordThread();

        CommonThreadPool.getThreadPool().addCachedTask(recordThread);
    }

    private void initAudioRecord() {
        int audioRecordMinBufferSize = AudioRecord
                .getMinBufferSize(RecordConstant.RecordSampleRate, AudioFormat.CHANNEL_IN_MONO,
                        pcmFormat.getAudioFormat());

        audioRecordBufferSize =
                RecordConstant.RecordSampleRate * pcmFormat.getBytesPerFrame() / (1000 / sampleDuration);

        if (audioRecordMinBufferSize > audioRecordBufferSize) {
            audioRecordBufferSize = audioRecordMinBufferSize;
        }

//        LogUtils.LOGE("audioRecordBufferSize",audioRecordBufferSize+"");
//        LogUtils.LOGE("audioRecordMinBufferSize",audioRecordMinBufferSize+"");

        /* Get number of samples. Calculate the buffer size
         * (round up to the factor of given frame size)
		 * 使能被整除，方便下面的周期性通知
		 * */
        int bytesPerFrame = pcmFormat.getBytesPerFrame();
        int frameSize = audioRecordBufferSize / bytesPerFrame;

        if (frameSize % FRAME_COUNT != 0) {
            frameSize += (FRAME_COUNT - frameSize % FRAME_COUNT);
            audioRecordBufferSize = frameSize * bytesPerFrame;
        }




        audioRecordBuffer = new short[audioRecordBufferSize];

        double sampleNumberInOneMicrosecond = (double) RecordConstant.RecordSampleRate / 1000;

        realSampleDuration = audioRecordBufferSize * 1000 /
                (RecordConstant.RecordSampleRate * pcmFormat.getBytesPerFrame());

        realSampleNumberInOneDuration = (int) (sampleNumberInOneMicrosecond * realSampleDuration);

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, RecordConstant.RecordSampleRate,
                AudioFormat.CHANNEL_IN_MONO, pcmFormat.getAudioFormat(), audioRecordBufferSize);
    }

    public void release() {
        if (recordThread != null) {
            recordThread.release();
        }
    }

    /**
     * Start recording. Create an encoding thread. Start record from this
     * thread.
     */
    public boolean startRecordVoice(String recordFileUrl) {
        try {
            recordThread.startRecordVoice(recordFileUrl);
            return true;
        } catch (Exception e) {
            LogFunction.error("开始录音异常", e);

            CommonFunction.showToast("初始化录音失败", "MP3Recorder");
        }

        return false;
    }

    public boolean stopRecordVoice() {
        if (recordThread != null) {
            recordThread.stopRecordVoice();
            mPause = false;
        }

        return true;
    }


    public boolean isPause() {
        return mPause;
    }

    /**
     * 是否暂停
     */
    public void setPause(boolean pause) {
        this.mPause = pause;
    }

    /**
     * 此计算方法来自samsung开发范例
     *
     * @param buffer   buffer
     * @param readSize readSize
     */
    private void calculateRealVolume(short[] buffer, int readSize) {
        int sum = 0;

        for (int index = 0; index < readSize; index++) {
            // 这里没有做运算的优化，为了更加清晰的展示代码
            sum += Math.abs(buffer[index]);
        }

        if (readSize > 0) {
            amplitude = sum / readSize;
        }
    }

    public int getVolume() {
        int volume = (int) (Math.sqrt(amplitude)) * RecordConstant.RecordVolumeMaxRank / 60;
        return volume;
    }

    private class RecordThread implements Runnable {
        private boolean running;
        private boolean recordVoice;

        private String recordFileUrl;

        public RecordThread() {
            running = true;
        }

        public void startRecordVoice(String recordFileUrl) throws IOException {
            if (!running) {
                return;
            }

            this.recordFileUrl = recordFileUrl;

            recordVoice = true;
        }

        public void stopRecordVoice() {
            recordVoice = false;
        }

        public void release() {
            running = false;
            recordVoice = false;
        }

        private void NoRecordPermission() {
            PermissionFunction.ShowCheckPermissionNotice("录音");
            stopRecordVoice();
        }

        @Override
        public void run() {
            while (running) {
                if (recordVoice) {
                    audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                            RecordConstant.RecordSampleRate, AudioFormat.CHANNEL_IN_MONO,
                            pcmFormat.getAudioFormat(), audioRecordBufferSize);

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        if (NoiseSuppressor.isAvailable()) {
                            NoiseSuppressor.create(audioRecord.getAudioSessionId());
                            //    Log.e("he",":he");
                        }
                        if (AcousticEchoCanceler.isAvailable()) {
                            AcousticEchoCanceler.create(audioRecord.getAudioSessionId());

                        }
                        if (AutomaticGainControl.isAvailable()) {
                            AutomaticGainControl.create(audioRecord.getAudioSessionId());
                        }
                    }


                    try {
                        audioRecord.startRecording();

                    } catch (Exception e) {
                        NoRecordPermission();
                        continue;
                    }

                    BufferedOutputStream bufferedOutputStream = FileFunction
                            .GetBufferedOutputStreamFromFile(recordFileUrl);

                    while (recordVoice) {
                        int audioRecordReadDataSize =
                                audioRecord.read(audioRecordBuffer, 0, audioRecordBufferSize);

                        if (audioRecordReadDataSize > 0) {
                            if (mPause) {
                                continue;
                            }

                            calculateRealVolume(audioRecordBuffer, audioRecordReadDataSize);
                            if (bufferedOutputStream != null) {
                                try {
                                    byte[] outputByteArray = CommonFunction
                                            .GetByteBuffer(audioRecordBuffer,
                                                    audioRecordReadDataSize, Variable.isBigEnding);
                                    bufferedOutputStream.write(outputByteArray);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            NoRecordPermission();
                            continue;
                        }
                    }

                    if (bufferedOutputStream != null) {
                        try {
                            bufferedOutputStream.close();
                        } catch (Exception e) {
                            LogFunction.error("关闭录音输出数据流异常", e);
                        }
                    }

                    audioRecord.stop();
                    audioRecord.release();
                    audioRecord = null;
                }

                try {
                    Thread.sleep(recordSleepDuration);
                } catch (Exception e) {
                    LogFunction.error("录制语音线程异常", e);
                }
            }
        }
    }
}