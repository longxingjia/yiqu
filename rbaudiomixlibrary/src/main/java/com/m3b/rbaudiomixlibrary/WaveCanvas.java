package com.m3b.rbaudiomixlibrary;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.SurfaceView;

import com.m3b.rbaudiomixlibrary.view.WaveSurfaceView;

import java.util.ArrayList;
import java.util.Date;


public class WaveCanvas{
	
    private ArrayList<Short> inBuf = new ArrayList<Short>();//缓冲区数据


	private int line_off ;//上下边距的距离
	public int rateX = 50;//控制多少帧取一帧
	public int rateY = 1; //  Y轴缩小的比例 默认为1
	public int baseLine = 0;// Y轴基线
	private int marginRight=30;//波形图绘制距离右边的距离
	private int draw_time = 1000 / 200;//两次绘图间隔的时间
	private float divider = 0.2f;//为了节约绘画时间，每0.2个像素画一个数据
	long c_time;
	private Paint circlePaint;
	private Paint center;
	private Paint paintLine;
	private Paint mPaint;

	private SurfaceView sfv;

	private boolean formRight = true;



	public  void init(SurfaceView sfv){
		circlePaint = new Paint();//画圆
		circlePaint.setColor(Color.rgb(246, 131, 126));//设置上圆的颜色
		center = new Paint();
		center.setColor(Color.WHITE);// 画笔为color
		center.setStrokeWidth(1);// 设置画笔粗细
		center.setAntiAlias(true);
		center.setFilterBitmap(true);
		center.setStyle(Style.FILL);
		paintLine =new Paint();
		paintLine.setColor(Color.rgb(169, 169, 169));
		mPaint = new Paint();
		mPaint.setColor(Color.parseColor("#FF4081"));// 画笔为color
		mPaint.setStrokeWidth(2);// 设置画笔粗
		mPaint.setAntiAlias(true);
		mPaint.setFilterBitmap(true);
		mPaint.setStyle(Style.FILL);
		this.sfv = sfv;
		this.line_off = ((WaveSurfaceView)sfv).getLine_off();
	}



    

    public  void updateAudioData(short[] buffer,int readsize, boolean mformRight){
		synchronized (inBuf) {
			for (int i = 0; i < readsize; i += rateX) {
					inBuf.add(buffer[i]);
				}
		}
            long time = new Date().getTime();
            if(time - c_time >= draw_time){
    			synchronized (inBuf) {
    				if (inBuf.size() == 0)
    	                return;
    	            while(inBuf.size() > (sfv.getWidth()-marginRight) / divider){
    	            	inBuf.remove(0);
    	            }
    			}
				this.formRight = mformRight;
            	SimpleDraw(inBuf, sfv.getHeight()/2);// 把缓冲区数据画出来
            	c_time = new Date().getTime();
            }

		}


	public byte[] getBytes(short s)
	{
		byte[] buf = new byte[2];
		for (int i = 0; i < buf.length; i++)
		{
			buf[i] = (byte) (s & 0x00ff);
			s >>= 8;
		}
		return buf;
	}


	/**
         * 绘制指定区域 
         *  
         * @param buf 
         *            缓冲区 
         * @param baseLine 
         *            Y轴基线 
         */  
        void SimpleDraw(ArrayList<Short> buf, int baseLine) {
			rateY = (65535 /2/ (sfv.getHeight()-line_off));

        	for (int i = 0; i < buf.size(); i++) {
        		byte bus[] = getBytes(buf.get(i));
        		buf.set(i, (short)((0x0000 | bus[1]) << 8 | bus[0]));//高低位交换
			}
            Canvas canvas = sfv.getHolder().lockCanvas(
                    new Rect(0, 0, sfv.getWidth(), sfv.getHeight()));// 关键:获取画布
            if(canvas==null)
            	return;
           // canvas.drawColor(Color.rgb(241, 241, 241));// 清除背景  
            canvas.drawARGB(255, 239, 239, 239);

            int start =(int) ((buf.size())* divider);
            float y;

            if(sfv.getWidth() - start <= marginRight){//如果超过预留的右边距距离
            	start = sfv.getWidth() -marginRight;//画的位置x坐标
            }

			canvas.drawCircle(start, line_off/4, line_off/4, circlePaint);// 上圆
	        canvas.drawCircle(start, sfv.getHeight()-line_off/4, line_off/4, circlePaint);// 下圆
	        //canvas.drawLine(start, 0, start, sfv.getHeight(), circlePaint);//垂直的线
	        int height = sfv.getHeight()-line_off;

	        canvas.drawLine(0, line_off/2, sfv.getWidth(), line_off/2, paintLine);//最上面的那根线
			canvas.drawLine(0, height*0.5f+line_off/2, sfv.getWidth() ,height*0.5f+line_off/2, center);//中心线
	        canvas.drawLine(0, sfv.getHeight()-line_off/2-1, sfv.getWidth(), sfv.getHeight()-line_off/2-1, paintLine);//最下面的那根线
            for (int i = 0; i < buf.size(); i++) {
				y =buf.get(i)/rateY + baseLine;// 调节缩小比例，调节基准线
				float x;
				if(formRight)
					x = sfv.getWidth() - (i)*divider;
				else
					x = (i)*divider;
                if(sfv.getWidth() - (i-1) * divider <= marginRight){
                	x = sfv.getWidth() -marginRight;
                }
				canvas.drawLine(x, y,  x,sfv.getHeight()-y, mPaint);//中间出波形
            }
            sfv.getHolder().unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
        }
}
