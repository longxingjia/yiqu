package com.ui.abs;

import android.R.drawable;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.byron.framework.R;
import com.jauker.widget.BadgeView;

public abstract class AbsTitleFragment extends AbsFragment {

	/** 无标题栏 */
	public static final int FLAG_NONE = 0;
	/** 有文本 */
	public static final int FLAG_TXT = 1;
	/** 有返回 */
	public static final int FLAG_BACK = 2;
	/** 有按钮 */
	public static final int FLAG_BTN = 4;
	/** 有按钮 */
	public static final int FLAG_ALL = FLAG_TXT | FLAG_BACK | FLAG_BTN;

	private boolean isNexting = false;
	private View mRootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = null;
		try {
			v = inflater.inflate(getContentView(), null);
			initView(v);
			setTitleType(v, getTitleBarType());
			initTitle();
			init(savedInstanceState);
		} catch (Exception e) {
			ToastManager.getInstance(getActivity()).showText(
					R.string.fm_indeterminism_error);
			e.printStackTrace();
		}
		Log.i(tag, "onCreateView");
		
		return v;
	}

	/**
	 * @comments 界面初始化组件的地方
	 * @version 1.0
	 */
	protected abstract int getTitleBarType();

	protected void setTitleText(String t) {
		if (mRootView == null) {
			return;
		}
		View txt = mRootView.findViewById(R.id.title_text);
		if (txt != null) {
			((TextView) txt).setText(t);
		}
	}

	protected void setTitleBtn(String t) {
		if (mRootView == null) {
			return;
		}
		View btn = mRootView.findViewById(R.id.title_btn);
		if (btn != null) {
			if (btn instanceof TextView) {
				((TextView) btn).setText(t);
			}
		}
	}

	protected void setTitleBtnImg(int res) {
		if (mRootView == null) {
			return;
		}
		View btn = mRootView.findViewById(R.id.title_btn);
		if (btn != null) {
			if (btn instanceof ImageView) {
				((ImageView) btn).setImageResource(res);
			}
		}
	}

	// 设置标题栏
	protected void setTitleType(View root, int flag) {
		mRootView = root;
		if (flag == FLAG_NONE) {
			View v = root.findViewById(R.id.title_bar);
			if (v != null) {
				v.setVisibility(View.GONE);
			}
		} else {
			if ((flag & FLAG_TXT) == FLAG_TXT) {
				View v = root.findViewById(R.id.title_text);
				if (v != null) {
					v.setVisibility(View.VISIBLE);

				}
			}
			if ((flag & FLAG_BACK) == FLAG_BACK) {
				menuOrBack = root.findViewById(R.id.title_back);
				if (menuOrBack != null) {

					menuOrBack.setVisibility(View.VISIBLE);

					menuOrBack.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							try {
								if (!onPageBack()) {
									getActivity().finish();
								}
							} catch (Exception e) {
								e.printStackTrace();
								ToastManager
										.getInstance(getActivity())
										.showText(
												R.string.fm_indeterminism_error);
							}
						}
					});
				}
			}
			if ((flag & FLAG_BTN) == FLAG_BTN) {
				View v = root.findViewById(R.id.title_btn);
				if (v != null) {

					Log.e("he","fs");
					v.setVisibility(View.VISIBLE);
					v.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
						//	Log.e("","ssss");
							try {
								onPageNext();
//								if (!isNexting) {
//									isNexting = true;
//									onPageNext();
//								}
							} catch (Exception e) {
								e.printStackTrace();
								ToastManager
										.getInstance(getActivity())
										.showText(
												R.string.fm_indeterminism_error);
							}
						}
					});
				}
			}
		}
	}

	/**
	 * @comments 返回按键
	 * @return true:子类已处理，false：子类不处理，交由父类处理,父类执行finish();
	 * @version 1.0
	 */
	protected abstract boolean onPageBack();

	/**
	 * @comments 下一步
	 * @return true:子类已处理，false：子类不处理，交由父类处理
	 * @version 1.0
	 */
	protected abstract boolean onPageNext();

	/**
	 * @comments 初始化title信息
	 * @version 1.0
	 */
	protected abstract void initTitle();

	protected void pageNextComplete() {
		isNexting = false;
	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return onPageBack();
	}


	
	
}
