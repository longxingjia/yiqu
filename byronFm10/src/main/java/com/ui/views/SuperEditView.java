package com.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.byron.framework.R;

public class SuperEditView extends FrameLayout {

	private TextView mLabel;
	private TextView mEdit;
	private View moreLay;
	private View mRootView;

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
	
	public SuperEditView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public SuperEditView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		mRootView = LayoutInflater.from(context).inflate(R.layout.view_super_edit, null);
		addView(mRootView);
		mLabel = (TextView)mRootView.findViewById(R.id.label);
		moreLay = mRootView.findViewById(R.id.moreLay);
		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.SuperEditView, defStyle, 0);
			CharSequence label = a.getText(R.styleable.SuperEditView_label_txt);
			mLabel.setText(label);
			Drawable label_d = a.getDrawable(R.styleable.SuperEditView_label_icon);
			if(label_d != null){
				mLabel.setCompoundDrawables(label_d, null, null, null);
			}
			
			ViewGroup edit_lay = (ViewGroup)mRootView.findViewById(R.id.edit_lay);
			int layRes = a.getResourceId(R.styleable.SuperEditView_edit_layout, 0);
			if( layRes != 0){
				View e = LayoutInflater.from(context).inflate(layRes, null);
				edit_lay.addView(e);
			}
			mEdit = (TextView)mRootView.findViewById(R.id.edit);
			
			CharSequence hint = a.getText(R.styleable.SuperEditView_edit_hint);
			mEdit.setHint(hint);
			CharSequence editTxt = a.getText(R.styleable.SuperEditView_edit_txt);
			mEdit.setText(editTxt);
			
			Drawable more_d = a.getDrawable(R.styleable.SuperEditView_more_icon);
			if(more_d != null){
				moreLay.setVisibility(View.VISIBLE);
				ImageView more = (ImageView) moreLay.findViewById(R.id.more);
				more.setImageDrawable(more_d);
			}else{
				View view_more_padding = mRootView.findViewById(R.id.view_more_padding);
				view_more_padding.setVisibility(View.VISIBLE);
			}
			
			int index = a.getInt(R.styleable.SuperEditView_location, -1);
			if (index >= 0) {
				setLocation(slocation[index]);
			}
			a.recycle();
		}
	}
	
	public void setLocation(Location l){
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
	
	public String getEditText(){
		return mEdit.getText().toString();
	}
	
	public void setEditText(String s){
		mEdit.setText(s);
	}
	
	public void setLabelText(String s){
		mLabel.setText(s);
	}
	
	public void setMoreClick(OnClickListener l){
		moreLay.setOnClickListener(l);
	}
	
	public void setLineClick(OnClickListener l){
		mRootView.setOnClickListener(l);
	}
	
	public void setLineLongClick(OnLongClickListener l){
		mRootView.setOnLongClickListener(l);
	}
}
