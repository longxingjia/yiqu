package com.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.byron.framework.R;


public class ClearableEditText extends EditText implements OnFocusChangeListener {
	
	private Drawable xD;
	private Drawable xNull;
	private OnFocusChangeListener f;
	private TextWatcher mTextWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			setClearIconVisible(s.length() > 0); 
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {}
		@Override
		public void afterTextChanged(Editable s) {}
	};
	
	public ClearableEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}
	private void init() { 
		xD = getCompoundDrawables()[2]; 
		if (xD == null) { 
		xD = getResources() 
		.getDrawable(R.drawable.edit_input_clear); 
		} 
		xD.setBounds(0, 0, xD.getIntrinsicWidth(), xD.getIntrinsicHeight()); 
		int i = getGravity();
		if(i == Gravity.CENTER){
			xNull = new ColorDrawable(Color.TRANSPARENT);
			xNull.setBounds(0, 0, xD.getIntrinsicWidth(), xD.getIntrinsicHeight()); 
		}
		setClearIconVisible(false); 
		setOnFocusChangeListener(this); 
		addTextChangedListener(mTextWatcher ); 
	}
	protected void setClearIconVisible(boolean visible) { 
		Drawable x = visible ? xD : xNull; 
		setCompoundDrawables(xNull, 
		getCompoundDrawables()[1], x, getCompoundDrawables()[3]); 
	}
	@Override 
	public void setOnFocusChangeListener(OnFocusChangeListener f) { 
		this.f = f; 
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (getCompoundDrawables()[2] != null) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				boolean tappedX = event.getX() > (getWidth()
						- getPaddingRight() - xD.getIntrinsicWidth());
				if (tappedX) {
					requestFocus();
					setText("");
					event.setAction(MotionEvent.ACTION_CANCEL);
				}
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if (hasFocus) {
			setClearIconVisible(getText().length() > 0);
		} else {
			setClearIconVisible(false);
		}
		if (f != null) {
			f.onFocusChange(v, hasFocus);
		}
	}
}

