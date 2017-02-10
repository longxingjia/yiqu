package com.ui.abs;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.base.utils.ToastManager;
import com.byron.framework.R;

public abstract class AbsFragment extends Fragment {

	protected static String tag = "AbsFragment";

	public View menuOrBack;

	protected String getLogTag() {
		return "AbsFragment";
	}

	protected OnFragmentListener mOnFragmentListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		tag = getLogTag();
		View v = null;
		try {
			v = inflater.inflate(getContentView(), container, false);
			initView(v);
			init(savedInstanceState);
		} catch (Exception e) {
			ToastManager.getInstance(getActivity()).showText(
					R.string.fm_indeterminism_error);
			e.printStackTrace();
		}
		Log.i(tag, "onCreateView");
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.i(tag, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		Log.i(tag, "onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.i(tag, "onResume");
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		Log.i(tag, "onStart");
		super.onStart();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// Log.i(tag, "onViewCreated");
		if (mOnFragmentListener != null) {
			mOnFragmentListener.onFragmentCreated(null);
		}
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		Log.i(tag, "getView");
		return super.getView();
	}

	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		// TODO Auto-generated method stub
		Log.i(tag, "onCreateAnimation");
		return super.onCreateAnimation(transit, enter, nextAnim);
	}

	@Override
	public void onInflate(Activity activity, AttributeSet attrs,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(tag, "onInflate");
		super.onInflate(activity, attrs, savedInstanceState);
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
	protected abstract void initView(View v);

	/**
	 * @comments 界面初始化数据的地方
	 * @param savedInstanceState
	 * @version 1.0
	 */
	protected void init(Bundle savedInstanceState) {

	}

	public void setOnFragmentListener(OnFragmentListener l) {
		mOnFragmentListener = l;
	}

	public boolean onBackPressed() {
		return false;
	}

	public void onShow() {
		// TODO Auto-generated method stub
		Log.i(tag, "onShow");
	}


}
