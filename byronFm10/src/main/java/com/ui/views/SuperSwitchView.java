package com.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.byron.framework.R;

public class SuperSwitchView extends FrameLayout {

	private TextView mLabel;
	private View mRootView;
	private TextView mSubLabel;
	private CheckBox mCheckBox;

	public enum Location {
		SINGLE,
		TOP,
		CENTER,
		BOTTOM
	}
	
	private static final Location[] slocation = {
		Location.SINGLE,
		Location.TOP,
		Location.CENTER,
		Location.BOTTOM
    };
	
	public SuperSwitchView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public SuperSwitchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		mRootView = LayoutInflater.from(context).inflate(R.layout.view_super_switch, null);
		addView(mRootView);
		mLabel = (TextView)mRootView.findViewById(R.id.label);
		mSubLabel = (TextView)mRootView.findViewById(R.id.sub_label);
		mCheckBox = (CheckBox)mRootView.findViewById(R.id.checkBox);
		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.SuperSwitchView, defStyle, 0);
			
			CharSequence label = a.getText(R.styleable.SuperSwitchView_label_txt);
			mLabel.setText(label);
			
			CharSequence subLabel = a.getText(R.styleable.SuperSwitchView_sub_label_txt);
			if(subLabel != null && subLabel.toString().trim().length() > 0){
				mSubLabel.setVisibility(View.VISIBLE);
				mSubLabel.setText(subLabel);
			}
			
			Boolean b = a.getBoolean(R.styleable.SuperSwitchView_is_switch, false);
			mCheckBox.setChecked(b);
			
			int index = a.getInt(R.styleable.SuperSwitchView_location, -1);
			if (index >= 0) {
				setLocation(slocation[index]);
			}
			a.recycle();
		}
	}
	
	private void setLocation(Location l){
		View top = mRootView.findViewById(R.id.top_line);
		View bottom = mRootView.findViewById(R.id.bottom_line);
		int p = (int) getResources().getDimension(R.dimen.view_super_line_padding_left);
		switch(l){
		case SINGLE:
			break;
		case TOP:
			bottom.setVisibility(View.GONE);
			break;
		case CENTER:
			top.setPadding(p, 0, 0, 0);
			bottom.setVisibility(View.GONE);
			break;
		case BOTTOM:
			top.setPadding(p, 0, 0, 0);
			break;
		}
	}
	
	public void setLineClick(OnClickListener l){
		mRootView.setOnClickListener(l);
	}
	
	public void setOnCheckedChangeListener(OnCheckedChangeListener l){
		mCheckBox.setOnCheckedChangeListener(l);
	}
	
	public void setSwitch(boolean isOn){
		mCheckBox.setChecked(isOn);
	}

	public boolean isSwitch() {
		// TODO Auto-generated method stub
		return mCheckBox.isChecked();
	}
	
}
