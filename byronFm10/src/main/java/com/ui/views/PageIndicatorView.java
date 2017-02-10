package com.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.byron.framework.R;

/**
 * 
 * @author byron
 *
 */
public class PageIndicatorView extends View{

	private float horizontalSpace = 5;
	private int mPosition; 
	private int count;
	private Bitmap mBon;
	private Bitmap mBff;
	private Paint paint;
	

	public PageIndicatorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		horizontalSpace  *= getResources().getDisplayMetrics().scaledDensity;
		paint = new Paint();
	}
	
	public void setCount(int count){
		this.count = count;
		this.postInvalidate();
	}
	public void setPosition(int position){
		mPosition = position;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(mBon == null){
			return;
		}
		final float width = this.getWidth();
		final float height = this.getHeight();
		final float totalWidth = count * (mBon.getWidth() + horizontalSpace);
		float left = (width - totalWidth)/2;
		final float top = (height - mBon.getHeight())/2;
		for(int i =0; i< count ; i++){
			if(mPosition == i){
				canvas.drawBitmap(mBon, left, top, paint);
			}else{
				canvas.drawBitmap(mBff, left, top, paint);
			}
			left += mBon.getWidth() + horizontalSpace;
		}		
		super.onDraw(canvas);
	}

	@Override
	protected void onAttachedToWindow() {
		mBon = BitmapFactory.decodeResource(getResources(), R.drawable.ah_pageon);
		mBff = BitmapFactory.decodeResource(getResources(), R.drawable.ah_pageoff);
		super.onAttachedToWindow();
	}
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		if(mBon != null){
			mBon.recycle();
			mBon = null;
		}
		if(mBff != null){
			mBff.recycle();
			mBff = null;
		}
		super.onDetachedFromWindow();
	}
	
	

}
