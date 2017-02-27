package com.yiqu.iyijiayi.fragment.tab5;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.fwrestnet.NetResponse;
import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.StubActivity;
import com.yiqu.iyijiayi.abs.AbsAllFragment;
import com.yiqu.iyijiayi.adapter.MyFragmentPagerAdapter;
import com.yiqu.iyijiayi.model.UserInfo;
import com.yiqu.iyijiayi.utils.AppShare;
import com.yiqu.iyijiayi.utils.LogUtils;
import com.yiqu.iyijiayi.utils.NoScollViewPager;
import com.yiqu.iyijiayi.utils.PageCursorView;

import java.util.ArrayList;

public class HomePageFragment extends AbsAllFragment {

	private EditText username;
	private EditText pass;
	private NoScollViewPager mPager;
	private ArrayList<Fragment> fragmentList;
	private PageCursorView cursor;
	private UserInfo userInfo;
	private String tag = "HomePageFragment";
	private RelativeLayout dianping_tab;
	private RelativeLayout tiwen_tab;
	private RelativeLayout xizuo_tab;
	private TextView name;
	private ImageView sex;
	private TextView desc;

	@Override
	protected int getTitleView() {

		return R.layout.titlebar_tab5;
	}

	@Override
	protected int getBodyView() {

		return R.layout.home_page_fragment;
	}

	@Override
	protected int getTitleBarType() {

		return FLAG_BACK|FLAG_TXT;
	}

	@Override
	protected boolean onPageBack() {

		return false;
	}

	@Override
	protected boolean onPageNext() {

		return false;
	}

	@Override
	protected void initTitle() {

		setTitleText("我");
	}

	@Override
	protected void initView(View v) {
		InitTextView(v);

//		String uid = getActivity().getIntent().getStringExtra("uid");
//		if (TextUtils.isEmpty(uid)){
//			if (AppShare.getIsLogin(getActivity())){
//				userInfo = AppShare.getUserInfo(getContext());
//			}
//		}else {
//
//
//		}
		if (AppShare.getIsLogin(getActivity())){
			userInfo = AppShare.getUserInfo(getContext());
			LogUtils.LOGE(tag,userInfo.toString());
			if (userInfo.type.equals("2") ){
				InitImage(v,3);
				dianping_tab.setVisibility(View.VISIBLE);
			}else {
				InitImage(v,2);
				dianping_tab.setVisibility(View.INVISIBLE);
			}

		}else {
			getActivity().finish();
		}

		name.setText(userInfo.username);
		if (userInfo.sex.equals("0")){
			sex.setBackgroundResource(R.mipmap.sex_female);
		}else {
			sex.setBackgroundResource(R.mipmap.sex_male);
		}

		InitViewPager(v);

//		String str = String.format("%s|粉丝:", userInfo.school,);

	}

	/*
 * 初始化标签名
 */
	public void InitTextView(View v) {
//		liaotian = (TextView) v.findViewById(R.id.liaotian);
//		message = (TextView) v.findViewById(R.id.message);
		name = (TextView) v.findViewById(R.id.name);
		desc = (TextView) v.findViewById(R.id.desc);
		sex = (ImageView) v.findViewById(R.id.sex);
		dianping_tab = (RelativeLayout) v.findViewById(R.id.dianping_tab);
		tiwen_tab = (RelativeLayout) v.findViewById(R.id.tiwen_tab);
		xizuo_tab = (RelativeLayout) v.findViewById(R.id.xizuo_tab);
//
		dianping_tab.setOnClickListener(new txListener(0));
		tiwen_tab.setOnClickListener(new txListener(1));
		xizuo_tab.setOnClickListener(new txListener(2));
	}

	private class txListener implements View.OnClickListener {
		private int index = 0;

		public txListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {

			mPager.setCurrentItem(index);
		}
	}

	/*
	 * 初始化图片的位移像素
	 */
	private void InitImage(View v,int count) {
		cursor = (PageCursorView) v.findViewById(R.id.cursor);
		cursor.setCount(count);
	}

	/*
	 * 初始化ViewPager
	 */
	public void InitViewPager(View v) {
		mPager = (NoScollViewPager) v.findViewById(R.id.viewpager);
		fragmentList = new ArrayList<Fragment>();
//		fragmentList.add(btFragment);
//		fragmentList.add(secondFragment);

		// 给ViewPager设置适配器
		mPager.setAdapter(new MyFragmentPagerAdapter(getActivity()
				.getSupportFragmentManager(), fragmentList));
		mPager.setCurrentItem(0);// 设置当前显示标签页为第一页
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
		mPager.setOffscreenPageLimit(6);// 缓存数
	}

	public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageSelected(int arg0) {
			LogUtils.LOGE(tag,arg0+"");
			cursor.setPosition(arg0);
			switch (arg0) {
				case 0:
//					setSlidingMenuEnable(true);
//					Drawable drawable1_0 = getResources().getDrawable(
//							R.drawable.liaotian_tab2_pressed);
//					Drawable drawable2_0 = getResources().getDrawable(
//							R.drawable.message_tab2);
//					drawable1_0.setBounds(0, 0, drawable1_0.getMinimumWidth(),
//							drawable1_0.getMinimumHeight());
//					drawable2_0.setBounds(0, 0, drawable2_0.getMinimumWidth(),
//							drawable2_0.getMinimumHeight());
//					liaotian.setCompoundDrawables(drawable1_0, null, null, null);
//					message.setCompoundDrawables(drawable2_0, null, null, null);
//					liaotian.setTextColor(getResources().getColor(
//							R.color.main_color));
//					message.setTextColor(getResources().getColor(R.color.black));
					break;
				case 1:
//					Drawable drawable1_1 = getResources().getDrawable(
//							R.drawable.liaotian_tab2);
//					Drawable drawable2_1 = getResources().getDrawable(
//							R.drawable.message_tab2_pressed);
//					drawable1_1.setBounds(0, 0, drawable1_1.getMinimumWidth(),
//							drawable1_1.getMinimumHeight());
//					drawable2_1.setBounds(0, 0, drawable2_1.getMinimumWidth(),
//							drawable2_1.getMinimumHeight());
//					liaotian.setCompoundDrawables(drawable1_1, null, null, null);
//					message.setCompoundDrawables(drawable2_1, null, null, null);
//					liaotian.setTextColor(getResources().getColor(R.color.black));
//					message.setTextColor(getResources()
//							.getColor(R.color.main_color));
					break;
			}
			int i = arg0 + 1;
		}
	}


	@Override
	public void onNetEnd(String id, int type, NetResponse netResponse) {
		if(TYPE_SUCCESS == type){


		}
		
		super.onNetEnd(id, type, netResponse);
	}

	
}
