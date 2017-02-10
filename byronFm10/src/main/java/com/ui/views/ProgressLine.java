package com.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class ProgressLine extends View {
	public int progress = 0;
	private Drawable drawableBg, drawablePg;
	public String packageName;
	public boolean isFinish = false;
	

	public ProgressLine(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void setProgress(int progress) {
		if(progress < 0){
			progress = 0;
		}
		if(progress > 100){
			progress = 100;
		}
		this.progress = progress;
		invalidate();
	}
	
	private void init() {
		drawableBg = new ColorDrawable(0XFF888888);
		drawablePg = new ColorDrawable(0xFFb9a05d);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		final int width = this.getWidth();
		final int height = this.getHeight();
		float per = width / 100f;
		drawableBg.setBounds(0, 0, width, height);
		drawableBg.draw(canvas);
		int w = (int) (progress * per);
		drawablePg.setBounds(0, 0, w, height);
		drawablePg.draw(canvas);
		super.onDraw(canvas);
	}
}
