package com.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.byron.framework.R;

/**
 * @ClassName: RatioVerticalVariableImageView
 * @Description: 按照比例，垂直可变，宽度定死
 *
 */

public class RVVImageView extends ImageView {
	
	private float mRatio;

	public RVVImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		  final TypedArray a = context.obtainStyledAttributes(attrs,  
	                R.styleable.RatioImageView, 0, 0);  
		  mRatio = a.getFloat(R.styleable.RatioImageView_ratio, -1);
		  a.recycle();
	}
	
	

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		if(mRatio == -1){
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}else{
			int w = 0;
	        int h = 0;
	                
	        w = Math.max(w, getSuggestedMinimumWidth());
	        h = Math.max(h, getSuggestedMinimumHeight());

	        int widthSize = resolveSize(w, widthMeasureSpec);
	        int heightSize = (int) ((float)widthSize * mRatio);
			setMeasuredDimension(widthSize, heightSize);
		}
	}

	
	
}
