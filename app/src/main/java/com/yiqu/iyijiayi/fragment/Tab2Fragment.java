package com.yiqu.iyijiayi.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jauker.widget.BadgeView;
import com.yiqu.iyijiayi.R;

import java.util.ArrayList;

public class Tab2Fragment extends TabContentFragment {

	private static final int TAB_2 = 2;
//	private NoScollViewPager mPager;
	private ArrayList<Fragment> fragmentList;
	private RelativeLayout liaotian_tab, message_tab;
//	private PageCursorView cursor;
	private TextView liaotian;
	private TextView message;
//	NewConversationListFragment btFragment = new NewConversationListFragment();
//	Fragment secondFragment = new BeautifulFragment();

	@Override
	protected int getTitleView() {
		return R.layout.titlebar_tab1;
	}

	@Override
	public void onSelect() {
		super.onSelect();
	}

	@Override
	public void onNoSelect() {
		super.onNoSelect();
	}

	@Override
	protected int getBodyView() {
		return R.layout.tab2_fragment;
	}

	@Override
	protected void initView(View v) {

		InitTextView(v);
		InitImage(v);
		InitViewPager(v);
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
	protected int getTitleBarType() {
		return FLAG_TXT | FLAG_BACK;
	}

	@Override
	protected boolean onPageBack() {
		if (mOnFragmentListener != null) {
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
		setTitleText(getString(R.string.label_tab2));
	}

	/*
	 * 初始化标签名
	 */
	public void InitTextView(View v) {
		liaotian = (TextView) v.findViewById(R.id.regist);
		message = (TextView) v.findViewById(R.id.regist);
		liaotian_tab = (RelativeLayout) v.findViewById(R.id.liaotian_tab);
		message_tab = (RelativeLayout) v.findViewById(R.id.message_tab);

		liaotian_tab.setOnClickListener(new txListener(0));
		message_tab.setOnClickListener(new txListener(1));
	}

	public class txListener implements View.OnClickListener {
		private int index = 0;

		public txListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
//			mPager.setCurrentItem(index);
		}
	}

	/*
	 * 初始化图片的位移像素
	 */
	public void InitImage(View v) {
//		cursor = (PageCursorView) v.findViewById(R.id.cursor);
//		cursor.setCount(2);
	}

	/*
	 * 初始化ViewPager
	 */
	public void InitViewPager(View v) {
//		mPager = (NoScollViewPager) v.findViewById(R.id.viewpager);
//		fragmentList = new ArrayList<Fragment>();
//		fragmentList.add(btFragment);
//		fragmentList.add(secondFragment);
//
//		// 给ViewPager设置适配器
//		mPager.setAdapter(new MyFragmentPagerAdapter(getActivity()
//				.getSupportFragmentManager(), fragmentList));
//		mPager.setCurrentItem(0);// 设置当前显示标签页为第一页
//		mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
//		mPager.setOffscreenPageLimit(6);// 缓存数
	}

//	public class MyOnPageChangeListener implements OnPageChangeListener {
//
//		@Override
//		public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//		}
//
//		@Override
//		public void onPageScrollStateChanged(int arg0) {
//
//		}
//
//		@Override
//		public void onPageSelected(int arg0) {
//			cursor.setPosition(arg0);
//			switch (arg0) {
//			case 0:
//				// setSlidingMenuEnable(true);
//				Drawable drawable1_0 = getResources().getDrawable(
//						R.drawable.liaotian_tab2_pressed);
//				Drawable drawable2_0 = getResources().getDrawable(
//						R.drawable.message_tab2);
//				drawable1_0.setBounds(0, 0, drawable1_0.getMinimumWidth(),
//						drawable1_0.getMinimumHeight());
//				drawable2_0.setBounds(0, 0, drawable2_0.getMinimumWidth(),
//						drawable2_0.getMinimumHeight());
//				liaotian.setCompoundDrawables(drawable1_0, null, null, null);
//				message.setCompoundDrawables(drawable2_0, null, null, null);
//				liaotian.setTextColor(getResources().getColor(
//						R.color.main_color));
//				message.setTextColor(getResources().getColor(R.color.black));
//				break;
//			case 1:
//				Drawable drawable1_1 = getResources().getDrawable(
//						R.drawable.liaotian_tab2);
//				Drawable drawable2_1 = getResources().getDrawable(
//						R.drawable.message_tab2_pressed);
//				drawable1_1.setBounds(0, 0, drawable1_1.getMinimumWidth(),
//						drawable1_1.getMinimumHeight());
//				drawable2_1.setBounds(0, 0, drawable2_1.getMinimumWidth(),
//						drawable2_1.getMinimumHeight());
//				liaotian.setCompoundDrawables(drawable1_1, null, null, null);
//				message.setCompoundDrawables(drawable2_1, null, null, null);
//				liaotian.setTextColor(getResources().getColor(R.color.black));
//				message.setTextColor(getResources()
//						.getColor(R.color.main_color));
//				break;
//			}
//			int i = arg0 + 1;
//		}
//	}




	@Override
	public void onDestroy() {

		super.onDestroy();
	}


}
