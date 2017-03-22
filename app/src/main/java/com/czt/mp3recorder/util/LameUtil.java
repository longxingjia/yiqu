package com.czt.mp3recorder.util;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;

import com.Tool.Global.RecordConstant;
import com.Tool.Global.Variable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class LameUtil {


    private String mFilePath = null;
    private int sampleRate = 8000;
    private boolean isRecording = false;
    private boolean isPause = false;
    private Handler handler = null;
    private int mVolume = 1;

    /**
     * 开始录音
     */
    public static final int MSG_REC_STARTED = 1;

    /**
     * 结束录音
     */
    public static final int MSG_REC_STOPPED = 2;

    /**
     * 暂停录音
     */
    public static final int MSG_REC_PAUSE = 3;

    /**
     * 继续录音
     */
    public static final int MSG_REC_RESTORE = 4;

    /**
     * 缓冲区挂了,采样率手机不支持
     */
    public static final int MSG_ERROR_GET_MIN_BUFFERSIZE = -1;

    /**
     * 创建文件时扑街了
     */
    public static final int MSG_ERROR_CREATE_FILE = -2;

    /**
     * 初始化录音器时扑街了
     */
    public static final int MSG_ERROR_REC_START = -3;

    /**
     * 录音的时候出错
     */
    public static final int MSG_ERROR_AUDIO_RECORD = -4;

    /**
     * 编码时挂了
     */
    public static final int MSG_ERROR_AUDIO_ENCODE = -5;

    /**
     * 写文件时挂了
     */
    public static final int MSG_ERROR_WRITE_FILE = -6;

    /**
     * 没法关闭文件流
     */
    public static final int MSG_ERROR_CLOSE_FILE = -7;

//	public MP3Recorder(String dir) {
//		this.RecordSampleRate = 8000;
//		this.mDir = dir;
//	}

    /**
     * 开片
     */
    public void start() {
        if (isRecording) {
            return;
        }

        new Thread() {
            @Override
            public void run() {

                mFilePath = Variable.StorageMusicPath
                        + System.currentTimeMillis() + ".mp3";

                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                // 根据定义好的几个配置，来获取合适的缓冲大小
                final int minBufferSize = AudioRecord.getMinBufferSize(
                        sampleRate, AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT);
                if (minBufferSize < 0) {
                    if (handler != null) {
                        handler.sendEmptyMessage(MSG_ERROR_GET_MIN_BUFFERSIZE);
                    }
                    return;
                }
                AudioRecord audioRecord = new AudioRecord(
                        MediaRecorder.AudioSource.MIC, sampleRate,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT, minBufferSize * 2);

                // 5秒的缓冲
                short[] buffer = new short[sampleRate * (16 / 8) * 1 * 5];
                byte[] mp3buffer = new byte[(int) (7200 + buffer.length * 2 * 1.25)];

                FileOutputStream output = null;
                try {
                    output = new FileOutputStream(new File(mFilePath));
                } catch (FileNotFoundException e) {
                    if (handler != null) {
                        handler.sendEmptyMessage(MSG_ERROR_CREATE_FILE);
                    }
                    return;
                }

                LameUtil.init(sampleRate, 1, sampleRate, RecordConstant.LameBehaviorBitRate, RecordConstant.LameMp3Quality);
                isRecording = true; // 录音状态
                isPause = false; // 录音状态
                try {
                    try {
                        audioRecord.startRecording(); // 开启录音获取音频数据
                    } catch (IllegalStateException e) {
                        // 不给录音...
                        if (handler != null) {
                            handler.sendEmptyMessage(MSG_ERROR_REC_START);
                        }
                        return;
                    }

                    try {
                        // 开始录音
                        if (handler != null) {
                            handler.sendEmptyMessage(MSG_REC_STARTED);
                        }

                        int readSize = 0;
                        boolean pause = false;
                        while (isRecording) {
                            // 暂停
                            if (isPause) {
                                if (!pause) {
                                    handler.sendEmptyMessage(MSG_REC_PAUSE);
                                    pause = true;
                                }
                                continue;
                            }
                            if (pause) {
                                handler.sendEmptyMessage(MSG_REC_RESTORE);
                                pause = false;
                            }

                            // 实时录音写数据
                            readSize = audioRecord.read(buffer, 0,
                                    minBufferSize);

                            // 计算分贝值
                            long v = 0;
                            // 将 buffer 内容取出，进行平方和运算
                            for (int i = 0; i < buffer.length; i++) {
                                v += buffer[i] * buffer[i];
                            }
                            // 平方和除以数据总长度，得到音量大小。
                            double mean = v / (double) readSize;
                            double volume = 10 * Math.log10(mean);
                            volume = volume * readSize / 32768 - 1;
                            if (volume > 0) {
                                mVolume = (int) Math.abs(volume);
                            } else {
                                mVolume = 1;
                            }
                            if (readSize < 0) {
                                if (handler != null) {
                                    handler.sendEmptyMessage(MSG_ERROR_AUDIO_RECORD);
                                }
                                break;
                            } else if (readSize == 0) {
                                ;
                            } else {
                                int encResult = LameUtil.encode(buffer,
                                        buffer, readSize, mp3buffer);
                                if (encResult < 0) {
                                    if (handler != null) {
                                        handler.sendEmptyMessage(MSG_ERROR_AUDIO_ENCODE);
                                    }
                                    break;
                                }
                                if (encResult != 0) {
                                    try {
                                        output.write(mp3buffer, 0, encResult);
                                    } catch (IOException e) {
                                        if (handler != null) {
                                            handler.sendEmptyMessage(MSG_ERROR_WRITE_FILE);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        // 录音完
                        int flushResult = LameUtil.flush(mp3buffer);
                        if (flushResult < 0) {
                            if (handler != null) {
                                handler.sendEmptyMessage(MSG_ERROR_AUDIO_ENCODE);
                            }
                        }
                        if (flushResult != 0) {
                            try {
                                output.write(mp3buffer, 0, flushResult);
                            } catch (IOException e) {
                                if (handler != null) {
                                    handler.sendEmptyMessage(MSG_ERROR_WRITE_FILE);
                                }
                            }
                        }
                        try {
                            output.close();
                        } catch (IOException e) {
                            if (handler != null) {
                                handler.sendEmptyMessage(MSG_ERROR_CLOSE_FILE);
                            }
                        }
                    } finally {
                        audioRecord.stop();
                        audioRecord.release();
                    }
                } finally {
                    LameUtil.close();
                    isRecording = false;
                }
                if (handler != null) {
                    handler.sendEmptyMessage(MSG_REC_STOPPED);
                }
            }
        }.start();
    }

    public void stop() {
        isRecording = false;
    }

    public void pause() {
        isPause = true;
    }

    public void restore() {
        isPause = false;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public boolean isPaus() {
        if (!isRecording) {
            return false;
        }
        return isPause;
    }

    public String getFilePath() {
        return mFilePath;
    }

    /**
     * 获取分贝
     *
     * @return
     */
    public int getVolume() {
        return mVolume;
    }

    /**
     * 录音状态管理
     * //	 *
     * //	 * @see RecMicToMp3#MSG_REC_STARTED
     * //	 * @see RecMicToMp3#MSG_REC_STOPPED
     * //	 * @see RecMicToMp3#MSG_REC_PAUSE
     * //	 * @see RecMicToMp3#MSG_REC_RESTORE
     * //	 * @see RecMicToMp3#MSG_ERROR_GET_MIN_BUFFERSIZE
     * //	 * @see RecMicToMp3#MSG_ERROR_CREATE_FILE
     * //	 * @see RecMicToMp3#MSG_ERROR_REC_START
     * //	 * @see RecMicToMp3#MSG_ERROR_AUDIO_RECORD
     * //	 * @see RecMicToMp3#MSG_ERROR_AUDIO_ENCODE
     * //	 * @see RecMicToMp3#MSG_ERROR_WRITE_FILE
     * //	 * @see RecMicToMp3#MSG_ERROR_CLOSE_FILE
     */
    public void setHandle(Handler handler) {
        this.handler = handler;
    }

    static {
        System.loadLibrary("mp3lame");
    }

    /**
     * Initialize LAME.
     *
     * @param inRecordSampleRate  input sample rate in Hz.
     * @param inChannel           number of channels in input stream.
     * @param outRecordSampleRate output sample rate in Hz.
     * @param outBitrate          brate compression ratio in KHz.
     * @param quality             quality=0..9. 0=best (very slow). 9=worst.<br />
     *                            recommended:<br />
     *                            2 near-best quality, not too slow<br />
     *                            5 good quality, fast<br />
     *                            7 ok quality, really fast
     *                            /**
     *                            初始化录制参数  :0=很好很慢 9=很差很快
     */

    public native static void init(int inRecordSampleRate, int inChannel,
                                   int outRecordSampleRate, int outBitrate, int quality);

    /**
     * Encode buffer to mp3.
     *
     * @param bufferLeft  PCM data for left channel.
     * @param bufferRight PCM data for right channel.
     * @param samples     number of samples per channel.
     * @param mp3buf      result encoded MP3 stream. You must specified
     *                    "7200 + (1.25 * buffer_l.length)" length array.
     * @return number of bytes output in mp3buf. Can be 0.<br />
     * -1: mp3buf was too small<br />
     * -2: malloc() problem<br />
     * -3: lame_init_params() not called<br />
     * -4: psycho acoustic problems
     * 音频数据编码(PCM左进,PCM右进,MP3输出)
     */
    public native static int encode(short[] bufferLeft, short[] bufferRight,
                                    int samples, byte[] mp3buf);

    /**
     * Flush LAME buffer.
     * <p>
     * REQUIRED:
     * lame_encode_flush will flush the intenal PCM buffers, padding with
     * 0's to make sure the final frame is complete, and then flush
     * the internal MP3 buffers, and thus may return a
     * final few mp3 frames.  'mp3buf' should be at least 7200 bytes long
     * to hold all possible emitted data.
     * <p>
     * will also write id3v1 tags (if any) into the bitstream
     * <p>
     * return code = number of bytes output to mp3buf. Can be 0
     *
     * @param mp3buf result encoded MP3 stream. You must specified at least 7200
     *               bytes.
     * @return number of bytes output to mp3buf. Can be 0.
     * 刷干净缓冲区
     */
    public native static int flush(byte[] mp3buf);

    /**
     * Close LAME.
     */
    public native static void close();
}
