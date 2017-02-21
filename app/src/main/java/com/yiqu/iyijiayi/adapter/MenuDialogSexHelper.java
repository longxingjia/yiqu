package com.yiqu.iyijiayi.adapter;

import android.support.v4.app.Fragment;
import android.view.View;

import com.ui.views.MenuDialog;
import com.ui.views.MenuDialog.OnMenuListener;

public class MenuDialogSexHelper {


	private Fragment mFragment;
	private MenuDialog mMenuDialog;
	private SexListener sexListener;

	public MenuDialogSexHelper(Fragment f, final SexListener sexListener){
		mFragment = f;
		String[] items = new String[]{"男","女"};
		mMenuDialog = new MenuDialog(f.getActivity(), items , new OnMenuListener() {
			@Override
			public void onMenuClick(MenuDialog dialog, int which, String item) {
				switch(which){
				case 0:
//					catchPicture();
					sexListener.onSex("1");
					break;
				case 1:
					sexListener.onSex("0");
//					selectPicture();
					break;
				}
			}
			@Override
			public void onMenuCanle(MenuDialog dialog) {
				
			}
		});
		this.sexListener = sexListener;

	}
	
	public void show(View v) {
//		this.photo = photo;
		mMenuDialog.show(v);
	}

	public interface SexListener {
		public void onSex(String sex);
	}

}
