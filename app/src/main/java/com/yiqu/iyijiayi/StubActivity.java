package com.yiqu.iyijiayi;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

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

//	@Override
//	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//		switch (requestCode) {
//			//就像onActivityResult一样这个地方就是判断你是从哪来的。
//			case 222:
//				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//					// Permission Granted
//
//				} else {
//					// Permission Denied
//					Toast.makeText(StubActivity.this, "很遗憾你把相机权限禁用了。请务必开启相机权限享受我们提供的服务吧。", Toast.LENGTH_SHORT)
//							.show();
//				}
//				break;
//			default:
//				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//		}
//	}
}
