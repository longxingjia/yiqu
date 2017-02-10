package com.yiqu.iyijiayi;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.ui.abs.AbsFragment;
import com.ui.abs.AbsFragmentAct;
import com.yiqu.iyijiayi.fragment.menu.LoginFragment;
import com.yiqu.iyijiayi.model.Model;

public class StubActivity extends AbsFragmentAct {

	private AbsFragment f;

	@Override
	protected int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.act_sub;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		String fragmentClass = this.getIntent().getStringExtra("fragment");
		if (fragmentClass == null) {
			fragmentClass = LoginFragment.class.getName();
		}
		f = Model.creatFragment(fragmentClass);
		long time = this.getIntent().getLongExtra("time", 0);
		Bundle args = new Bundle();
		args.putLong("time", time);
		if (f != null) {
			FragmentManager fm = getSupportFragmentManager();
		//	Log.e("", fragmentClass);
			fm.beginTransaction().add(R.id.content_frame,f, fragmentClass).commit();
			
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

	@Override
	protected void init(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (f != null) {
			if (!f.onBackPressed()) {
				super.onBackPressed();
			}
		} else {
			super.onBackPressed();
		}
	}

}
