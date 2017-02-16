package com.yiqu.iyijiayi.fragment.tab3;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.yiqu.iyijiayi.R;
import com.yiqu.iyijiayi.adapter.MyFragmentPagerAdapter;
import com.yiqu.iyijiayi.fragment.TabContentFragment;
import com.yiqu.iyijiayi.fragment.tab5.LoginFragment;
import com.yiqu.iyijiayi.fragment.tab5.RegisterFragment;
import com.yiqu.iyijiayi.utils.NoScollViewPager;

import java.util.ArrayList;

public class TabRegisterOrLoginFragment extends Fragment {


	private NoScollViewPager mPager;
	private ArrayList<Fragment> fragmentList;

	TextView register;
	TextView login;

	RegisterFragment registerFragment = new RegisterFragment();
	LoginFragment loginFragment = new LoginFragment();


	/*
	 * 初始化标签名
	 */
	public void InitTextView(View v) {
		register = (TextView) v.findViewById(R.id.regist);
		login = (TextView) v.findViewById(R.id.login);


		register.setOnClickListener(new txListener(0));
		login.setOnClickListener(new txListener(1));
	}

	public class txListener implements View.OnClickListener {
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
	 * 初始化ViewPager
	 */
	public void InitViewPager(View v) {
		mPager = (NoScollViewPager) v.findViewById(R.id.viewpager);
		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(registerFragment);
		fragmentList.add(loginFragment);

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

			switch (arg0) {
			case 0:
				register.setTextColor(getResources().getColor(R.color.white));
				register.setBackgroundColor(getResources().getColor(R.color.redMain));

				login.setTextColor(getResources().getColor(R.color.dd_gray));
				login.setBackgroundColor(getResources().getColor(R.color.white));
				break;
			case 1:
				register.setTextColor(getResources().getColor(R.color.dd_gray));
				register.setBackgroundColor(getResources().getColor(R.color.white));
				login.setTextColor(getResources().getColor(R.color.white));
				login.setBackgroundColor(getResources().getColor(R.color.redMain));
//
				break;
			}
			int i = arg0 + 1;
		}
	}




	@Override
	public void onDestroy() {

		super.onDestroy();
	}


}
