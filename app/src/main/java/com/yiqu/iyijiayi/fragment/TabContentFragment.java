package com.yiqu.iyijiayi.fragment;

import com.yiqu.iyijiayi.MainActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;


public abstract class TabContentFragment extends AbsAllFragment {
	
	
	public void onSelect(){
		
	}
	
	public void onNoSelect(){
		
	} 
	
	@Override
	protected boolean isTouchMaskForNetting() {
		return false;
	}

	@Override
	public void onResume() {
		super.onResume();
	}


}
