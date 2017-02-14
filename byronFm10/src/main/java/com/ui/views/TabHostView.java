package com.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.byron.framework.R;

public class TabHostView extends LinearLayout {

	private int mNums;
	private String[] mNames;
	private int[] mIcons;
	private int[] mIconsDisable;
	private View[] tabView;
	private int position = -1;
	
	private onTabSelectListener mOnTabSelectListener;
	
	public interface onTabSelectListener {
		public boolean onTabSelect(int positon);
	}

	public TabHostView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.TabHostView, 0, 0);
			mNums = a.getInteger(R.styleable.TabHostView_num, 0);
			int names = a.getResourceId(R.styleable.TabHostView_name_array, 0);
			if(names != 0){
				mNames = getResources().getStringArray(names);
			}
			int icons = a.getResourceId(R.styleable.TabHostView_icon_array, 0);
			if(icons != 0){
				String[] iconsArray = getResources().getStringArray(icons);
				if(iconsArray != null && iconsArray.length > 0){
					mIcons = new int[iconsArray.length];
					for(int i = 0; i < iconsArray.length; i++){
						mIcons[i] = getResources().getIdentifier(iconsArray[i], "mipmap", context.getPackageName());
					}
				}
			}
			int iconsDisable = a.getResourceId(R.styleable.TabHostView_icon_disable_array, 0);
			if(iconsDisable != 0){
				String[] iconsArray = getResources().getStringArray(iconsDisable);
				if(iconsArray != null && iconsArray.length > 0){
					mIconsDisable = new int[iconsArray.length];
					for(int i = 0; i < iconsArray.length; i++){
						mIconsDisable[i] = getResources().getIdentifier(iconsArray[i], "mipmap", context.getPackageName());
					}
				}
			}
			a.recycle();
		}
		if(mNums > 0){
			init();
		}
	}
	
	private void init(){
		tabView = new View[mNums];
		for(int i = 0; i<mNums; i++){
			View v = LayoutInflater.from(getContext()).inflate(R.layout.tab_host, null);
			TextView txt = (TextView) v.findViewById(R.id.tab_host_txt);
			if(mNames != null && i < mNames.length){
				txt.setText(mNames[i]);
			}
			if(mIconsDisable != null && i >= 0 && i < mIconsDisable.length){
				txt.setCompoundDrawablesWithIntrinsicBounds(0, mIconsDisable[i], 0, 0);
			}
			v.setOnClickListener(onclick(i));
			tabView[i] = v;
			addView(v);
			LayoutParams l = (LayoutParams) v.getLayoutParams();
			l.width = LayoutParams.MATCH_PARENT;
			l.height = LayoutParams.MATCH_PARENT;
			l.weight = 1;
		}
		
	}

	private OnClickListener onclick(final int i) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					setCurrentTab(i, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}
	
	public void setOnTabSelectListener(onTabSelectListener l){
		mOnTabSelectListener = l;
	}
	
	public void setCurrentTab(int p, boolean isCallBack) throws Exception{
		if( p < 0 || p >= mNums){
			throw new Exception("mast be 0 ~ " + (mNums - 1));
		}
		if(position != p){
			try{
				if(mOnTabSelectListener != null && isCallBack){
					if(!mOnTabSelectListener.onTabSelect(p)){
						return;
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			//old
			if(mIconsDisable != null && position >= 0 && position < mIconsDisable.length){
				TextView txt = (TextView) tabView[position].findViewById(R.id.tab_host_txt);
				txt.setCompoundDrawablesWithIntrinsicBounds(0, mIconsDisable[position], 0, 0);
				txt.setTextColor(getResources().getColor(R.color.tab_txt_disable_color));
			}
			//current
			position = p;
			if(mIcons != null && position >= 0 && position < mIcons.length){
				TextView txt = (TextView) tabView[position].findViewById(R.id.tab_host_txt);
				txt.setCompoundDrawablesWithIntrinsicBounds(0, mIcons[position], 0, 0);
				txt.setTextColor(getResources().getColor(R.color.tab_txt_eable_color));
			}
		}
	}

	
}
