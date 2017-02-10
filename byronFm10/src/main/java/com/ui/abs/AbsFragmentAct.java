package com.ui.abs;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import com.base.utils.ToastManager;
import com.base.utils.Utils;
import com.byron.framework.R;
import com.ui.App;

public abstract class AbsFragmentAct extends FragmentActivity {
//public abstract class AbsFragmentAct extends SlidingFragmentActivity {

	protected boolean isActVisibile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(getContentView());
			
			initView();
			init(savedInstanceState);
		} catch (Exception e) {
			ToastManager.getInstance(this).showText(
					R.string.fm_indeterminism_error);
			e.printStackTrace();
		}
	}


	/**
	 * @comments activity是否resume
	 * @return
	 * @version 1.0
	 */
	protected boolean isActVisibile() {
		return isActVisibile;
	}

	/**
	 * @comments 界面的布局文件
	 * @return
	 * @version 1.0
	 */
	protected abstract int getContentView();
	

	/**
	 * @comments 界面初始化组件的地方
	 * @version 1.0
	 */
	protected abstract void initView();

	/**
	 * @comments 界面初始化数据的地方
	 * @param savedInstanceState
	 * @version 1.0
	 */
	protected abstract void init(Bundle savedInstanceState);

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if (!Utils.isAppOnForeground(this)) {
			App.isActive = false;
		}
		super.onStop();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		isActVisibile = true;
		if (!App.isActive) {
			App.isActive = true;
			onForeground();
		}
		super.onResume();
	}

	/**
	 * @comments 应用程序从后台切换到前台的回调函数
	 * @version 1.0
	 */
	protected void onForeground() {

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		isActVisibile = false;
		super.onPause();
	}

}
