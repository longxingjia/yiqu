package com.yiqu.iyijiayi.fragment;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
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


	/**
	 * 设置侧边栏开启或关闭
	 *
	 * @param enable
	 */
	public void setSlidingMenuEnable(boolean enable) {
		MainActivity mainUi = (MainActivity) getActivity();

		SlidingMenu slidingMenu = mainUi.getSlidingMenu();

		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
}
