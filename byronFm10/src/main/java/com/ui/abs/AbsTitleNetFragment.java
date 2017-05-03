package com.ui.abs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.utils.ToastManager;
import com.byron.framework.R;
import com.fwrestnet.NetCallBack;
import com.fwrestnet.NetResponse;

public abstract class AbsTitleNetFragment extends AbsTitleFragment implements
		NetCallBack {

	private View mTouchMask;
	private View mProgress;
	public View mNext;
	private String tag="AbsTitleNetFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// return
		View v = null;
		try {
			// 初始化基层组件
			v = inflater.inflate(getContentView(), null);

			// 加载标题组件
			ViewGroup root = (ViewGroup) v.findViewById(R.id.fm_root);
			View title = inflater.inflate(getTitleView(), root, false);
			mProgress = title.findViewById(R.id.title_progress);
			mNext = title.findViewById(R.id.title_btn);
			root.addView(title, 0);

			// 加载正文组件
			ViewGroup content = (ViewGroup) v.findViewById(R.id.fm_content);
			View body = inflater.inflate(getBodyView(), content, false);

			content.addView(body, 0);

			// 触摸遮罩层组件
			mTouchMask = (ViewGroup) v.findViewById(R.id.fm_touch_mask);

			// 页面初始化自己的组件
			initView(root);
			// 初始化标题类型
			setTitleType(root, getTitleBarType());
			// 初始化标题
			initTitle();
			// 其他初始化
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
	protected int getContentView() {
		return R.layout.abs_net_fragment;
	}

	/**
	 * @comments 标题的布局文件
	 * @return
	 * @version 1.0
	 */
	protected abstract int getTitleView();

	/**
	 * @comments 正文的布局文件
	 * @return
	 * @version 1.0
	 */
	protected abstract int getBodyView();

	/**
	 * @comments 加载组件
	 * @return
	 * @version 1.0
	 */
	protected void setProgressView(View v) {
		mProgress = v;
	}

	@Override
	public void onNetNoStart(String id) {
		// TODO Auto-generated method stub

	}

	protected boolean isTouchMaskForNetting() {
		return false;
	}

	@Override
	public void onNetStart(String id) {
		// TODO Auto-generated method stub
		// if(mProgress != null){
		// mProgress.setVisibility(View.VISIBLE);
		// }
		// if(mNext!= null){
		// mNext.setVisibility(View.INVISIBLE);
		// }
		// if(isTouchMaskForNetting()){
		// mTouchMask.setVisibility(View.VISIBLE);
		// }
	}

	@Override
	public void onNetEnd(String id, int type, NetResponse netResponse) {
		// TODO Auto-generated method stub
//		 if(mProgress!= null){
//		 mProgress.setVisibility(View.INVISIBLE);
//		 }
//		 if(mNext!= null){
//		 mNext.setVisibility(View.INVISIBLE);
//		 }
//		 mTouchMask.setVisibility(View.INVISIBLE);

		// switch(type){
		// case TYPE_SUCCESS:
		//
		// break;
		// case TYPE_TOKEN_INVALID:
		//
		// break;
		// case TYPE_ERROR:
		// if(!isDetached()){
		// ToastManager.getInstance(getActivity()).showText(netResponse.statusDesc);
		// }
		// break;
		// case TYPE_CANCEL:
		// break;
		// case TYPE_TIMEOUT:
		// if(!isDetached()){
		// ToastManager.getInstance(getActivity()).showText(R.string.fm_net_call_timeout);
		// }
		// break;
		// }
//		if (type == TYPE_SUCCESS) {
//
//		} else if (type == TYPE_TOKEN_INVALID) {
//
//		} else if (type == TYPE_ERROR) {
//			if (!isDetached()) {
//				ToastManager.getInstance(getActivity()).showText(
//						netResponse.statusDesc);
//			}
//		} else if (type == TYPE_CANCEL) {
//
//		} else if (type == TYPE_TIMEOUT) {
//			if (!isDetached()) {
//				ToastManager.getInstance(getActivity()).showText(
//						R.string.fm_net_call_timeout);
//			}
//		}

	}

}
