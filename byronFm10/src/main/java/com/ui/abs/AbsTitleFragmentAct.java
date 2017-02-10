package com.ui.abs;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.utils.ToastManager;
import com.byron.framework.R;


public abstract class AbsTitleFragmentAct extends AbsFragmentAct {

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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			setTitleType(getTitleBarType());
			initTitle();
		} catch (Exception e) {
			ToastManager.getInstance(this).showText(
					R.string.fm_indeterminism_error);
			e.printStackTrace();
		}
	}
	
	/**
	 * @comments 界面初始化组件的地方
	 * @version 1.0
	 */
	protected abstract int getTitleBarType();
	protected abstract void initTitle();
	
	protected void setTitleText(String t) {
		View txt = findViewById(R.id.title_text);
		if (txt != null) {
			((TextView) txt).setText(t);
		}
	}

	protected void setTitleBtn(String t) {
		View btn = findViewById(R.id.title_btn);
		if (btn != null) {
			if (btn instanceof TextView) {
				((TextView) btn).setText(t);
			}
		}
	}
	
	protected void setTitleBtnImg(int res) {
		View btn = findViewById(R.id.title_btn);
		if (btn != null) {
			if (btn instanceof ImageView) {
				((ImageView) btn).setImageResource(res);
			}
		}
	}
	
	// 设置标题栏
	private void setTitleType(int flag) {
		if (flag == FLAG_NONE) {
			View v = findViewById(R.id.title_bar);
			if (v != null) {
				v.setVisibility(View.GONE);
			}
		} else {
			if ((flag & FLAG_TXT) == FLAG_TXT) {
				View v = findViewById(R.id.title_text);
				if (v != null) {
					v.setVisibility(View.VISIBLE);
					if (v instanceof TextView) {
						((TextView) v).setText(getTitle());
					}
				}
			}
			if ((flag & FLAG_BACK) == FLAG_BACK) {
				View v = findViewById(R.id.title_back);
				if (v != null) {
					v.setVisibility(View.VISIBLE);
					v.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							try{
								if(!onPageBack()){
									finish();
								}
							}catch(Exception e){
								e.printStackTrace();
								ToastManager.getInstance(AbsTitleFragmentAct.this).showText(
										R.string.fm_indeterminism_error);
							}
						}
					});
				}
			}
			if ((flag & FLAG_BTN) == FLAG_BTN) {
				View v = findViewById(R.id.title_btn);
				if (v != null) {
					v.setVisibility(View.VISIBLE);
					v.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							try{
								if (!isNexting) {
									isNexting = true;
									onPageNext();
								}
							}catch(Exception e){
								e.printStackTrace();
								ToastManager.getInstance(AbsTitleFragmentAct.this).showText(
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
	
	protected void pageNextComplete(){
		isNexting = false;
	}
	
}
