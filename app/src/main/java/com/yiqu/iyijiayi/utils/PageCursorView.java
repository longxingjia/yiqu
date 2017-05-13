package com.yiqu.iyijiayi.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.utils.LogUtils;
import com.yiqu.iyijiayi.R;


public class PageCursorView extends View {

	private int color = getResources().getColor(R.color.redMain);
	private int count;
	private Paint paint;
	private RectF r;
	private int currIndex;
	private float preW;
	
	public PageCursorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setColor(color);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		float w = getWidth();
		float h = getHeight();
		preW = w / count;
		LogUtils.LOGE("tag",preW+"");
		if(r == null){
			r = new RectF(0, 0, preW, h);
		}
		canvas.drawRect(r, paint);
		super.onDraw(canvas);
	}

	public void setCount(int c){
		r = null;
		count = c;
	}

	public void setPosition(int p){
		 Animation animation = new TranslateAnimation(currIndex*preW, p*preW,0,0);//平移动画  
         currIndex = p;  
         animation.setFillAfter(true);//动画终止时停留在最后一帧，不然会回到没有执行前的状态  
         animation.setDuration(200);//动画持续时间0.2秒  
         startAnimation(animation);//是用ImageView来显示动画的  
	}
}
