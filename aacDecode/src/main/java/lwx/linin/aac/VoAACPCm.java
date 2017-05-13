package lwx.linin.aac;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.sinaapp.bashell.VoAACEncoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class VoAACPCm implements Runnable {

	/**从默认的16000开始，若失败则会用其余的采样率*/
	private static int[] sampleRates = { 16000, 44100, 22050, 11025, 8000, 4000 };

	private boolean isStart = false;
	private FileOutputStream fos;

	//	int off1=0;
	FileInputStream fileInputStream;
	private int bufferRead;
	//	private AudioRecord recordInstance;

	public VoAACPCm(String fileName,String fileInputName){
		try {
			fos = new FileOutputStream(fileName);
			fileInputStream = new FileInputStream(fileInputName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**设置默认采样率，默认16000*/
	public VoAACPCm sampleRateInHz(int sampleRateInHz){
		sampleRates[0] = sampleRateInHz;
		return this;
	}

	public void start(){
		isStart = true;
		Thread t = new Thread(this);
		t.start();
	}

	public void stop(){
		isStart = false;
	}
	int off1=0;
	@Override
	public void run() {
		VoAACEncoder vo = new VoAACEncoder();
		byte[] tempBuffer = new byte[2048];

		vo.Init(44100, 32000, (short)2, (short)1);// 采样率:44100,bitRate:32k,声道数:1，编码:0.raw 1.ADTS

		while (isStart) {

//			try {
//				fileInputStream.skip((long)off1);
//				bufferRead = fileInputStream.read(tempBuffer,0,2048);
//				off1 +=2048;
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			try {
				while((bufferRead=fileInputStream.read(tempBuffer))!=-1){
					byte[] ret = vo.Enc(tempBuffer);
					if (bufferRead > 0) {
						System.out.println("ret:"+ret.length);
						try {
							fos.write(ret);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}


//			//计算出音量分贝值
//			if (bufferRead>0) {
//				long v = 0;
//				for (int i = 0; i < tempBuffer.length; i++) {
//					v += tempBuffer[i] * tempBuffer[i];//计算平方和
//				}
//				double mean = v/(double)bufferRead;//平方和/总长度，得到音量大小
//				volume = 10*Math.log10(mean);//转换公式
//				//L.e("volume","分贝值："+volume);
//			}
		}
//		recordInstance.stop();
//		recordInstance.release();
//		recordInstance = null;
		vo.Uninit();
		try {
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private double volume = 0;
	/**实时获取音量大小*/
	public double getVolume(){
		return volume;
	}

}
