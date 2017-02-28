package com.yiqu.iyijiayi;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.ui.abs.AbsFragment;
import com.ui.abs.AbsFragmentAct;
import com.yiqu.iyijiayi.fragment.tab5.RegisterFragment;
import com.yiqu.iyijiayi.model.Model;
import com.yiqu.iyijiayi.utils.LogUtils;

public class StubActivity extends AbsFragmentAct {

	private AbsFragment f;
	private String tag = "StubActivity";

	@Override
	protected int getContentView() {
		return R.layout.act_sub;
	}

	@Override
	protected void initView() {
		String fragmentClass = this.getIntent().getStringExtra("fragment");
		if (fragmentClass == null) {
			fragmentClass = RegisterFragment.class.getName();
		}
		f = Model.creatFragment(fragmentClass);
		long time = this.getIntent().getLongExtra("time", 0);
		Bundle args = new Bundle();
		args.putLong("time", time);
		if (f != null) {
			FragmentManager fm = getSupportFragmentManager();
			fm.beginTransaction().add(R.id.content_frame,f, fragmentClass).commit();
			
		}
	}

	@Override
	public void finish() {
		super.finish();
	}

	@Override
	protected void init(Bundle savedInstanceState) {
	}

	@Override
	public void onBackPressed() {

		if (f != null) {
			LogUtils.LOGE(tag,"1");
			if (!f.onBackPressed()) {
				LogUtils.LOGE(tag,"2");
				super.onBackPressed();
			}
		} else {
			LogUtils.LOGE(tag,"f");
			super.onBackPressed();
		}
	}

}
