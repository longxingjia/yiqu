package com.yiqu.iyijiayi.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.fwrestnet.NetResponse;
import com.jauker.widget.BadgeView;
import com.ui.views.LoadMoreView;
import com.ui.views.LoadMoreView.OnMoreListener;
import com.ui.views.RefreshList;
import com.ui.views.RefreshList.IRefreshListViewListener;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.utils.ImageLoaderHm;

import java.util.ArrayList;

public class Tab3Fragment extends TabContentFragment  {
	

	@Override
	protected int getTitleView() {
		return R.layout.titlebar_tab1;
	}

	@Override
	protected int getBodyView() {
		return R.layout.tab3_fragment;
	}
	
	@Override
	protected void initView(View v) {

	
	}
	
	@Override
	protected boolean isTouchMaskForNetting() {
		return false;
	}

	@Override
	protected void init(Bundle savedInstanceState) {
		setSlidingMenuEnable(false);

	}
	

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	@Override
	protected int getTitleBarType() {
		return FLAG_TXT ;
	}
	@Override
	protected boolean onPageBack() {
		if(mOnFragmentListener != null){
			mOnFragmentListener.onFragmentBack(this);
		}
		return true;
	}
	@Override
	protected boolean onPageNext() {
		pageNextComplete();
		return true;
	}
	@Override
	protected void initTitle() {
		setTitleText("录制和提问");
	}

	
}
