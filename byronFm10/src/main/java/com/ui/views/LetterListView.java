package com.ui.views;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 *@comments 首字母快速索引组件
 */
public class LetterListView extends View {
	//回调句柄
	OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	//被选择的字母列表
	String[] b = {"#","A","B","C","D","E","F","G","H","I","J","K","L"
			,"M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	//被选中的字母
	int choose = -1;
	//画笔
	Paint paint = new Paint();
	//标记当前是否在索引中
	boolean showBkg = false;
	private RectF mRectF;
	//针对不同屏幕的缩放比率
	private float sacle;
	
	public LetterListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		sacle = this.getResources().getDisplayMetrics().scaledDensity;
	}

	public LetterListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		sacle = this.getResources().getDisplayMetrics().scaledDensity;
	}

	public LetterListView(Context context) {
		super(context);
		sacle = this.getResources().getDisplayMetrics().scaledDensity;
	}
	
	public void setABC(ArrayList<String> a){
		b = new String[a.size()];
		Log.i("byron", ""+b.length);
		a.toArray(b);
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(mRectF == null){
			mRectF = new RectF(5, 5, getWidth()-5, getHeight()-5);
		}
		mRectF.set(5, 0, getWidth()-5, getHeight());
		paint.setAntiAlias(true);
//		paint.setColor(Color.parseColor("#40000000"));
//		canvas.drawRoundRect(mRectF , (getWidth()-10)/2, (getWidth()-10)/2, paint);
////		canvas.drawRoundRect(mRectF , 0, 0, paint);
		if(showBkg){			
			paint.setColor(Color.parseColor("#bb000000"));
			canvas.drawRoundRect(mRectF , (getWidth()-10)/2, (getWidth()-10)/2, paint);
		}else{
			paint.setColor(Color.parseColor("#20000000"));
			canvas.drawRoundRect(mRectF , (getWidth()-10)/2, (getWidth()-10)/2, paint);
		}
		
	    int height = getHeight();
	    int width = getWidth();
	    float singleHeight = (float)height / b.length;
	    Rect bounds = new Rect();
	    for(int i=0;i<b.length;i++){
	       paint.setColor(Color.WHITE);
	       paint.setTypeface(Typeface.DEFAULT_BOLD);
	       paint.setAntiAlias(true);
	       if(i == choose){
	    	   paint.setColor(Color.parseColor("#3399ff"));
//	    	   paint.setFakeBoldText(true);
	       }
	   	   paint.setTextSize((int)(sacle * 9));
	   	   paint.getTextBounds(b[i], 0, b[i].length(), bounds);
	       float xPos = width/2  - bounds.width()/2;
	       float yPos = singleHeight * i + (singleHeight/2 + bounds.height()/2);
	       canvas.drawText(b[i], xPos, yPos, paint);
//	       canvas.drawLine(0, singleHeight * i + singleHeight/2, getWidth(), singleHeight * i + singleHeight/2, paint);
	       paint.reset();
	    }
	   
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
	    final float y = event.getY();
	    final int oldChoose = choose;
	    final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
	    final int c = (int) (y/getHeight()*b.length);
	    
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				//开始触摸的标记
				showBkg = true;
				if(oldChoose != c && listener != null){
					if(c >= 0 && c< b.length){
						listener.onTouchingLetterChanged(b[c]);
						//按下去被选择的字母
						choose = c;
						invalidate();
					}
				}
				
				break;
			case MotionEvent.ACTION_MOVE:
				if(oldChoose != c && listener != null){
					if(c >= 0 && c< b.length){
						listener.onTouchingLetterChanged(b[c]);
						//滑动过程中被选择的字母
						choose = c;
						invalidate();
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				if(listener != null){
					listener.onCancel();
				}
				//取消触摸的标记
				showBkg = false;
				choose = -1;
				invalidate();
				break;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}
	/**
	 * @comments 注册回调函数
	 * @param onTouchingLetterChangedListener
	 * @version 1.0
	 */
	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}
	/**
	 *@comments 字母索引监听接口
	 *@author byron(linbochuan@hopsun.cn)
	 *@date 2014-3-21
	 *@version 1.0
	 */
	public interface OnTouchingLetterChangedListener{
		/**
		 * @comments 字母被触摸回调函数
		 * @param s 被触摸的字母
		 * @version 1.0
		 */
		public void onTouchingLetterChanged(String s);
		/**
		 * @comments 取消本次索引回调函数
		 * @version 1.0
		 */
		public void onCancel();
	}
	
}
