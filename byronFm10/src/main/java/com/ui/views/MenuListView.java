package com.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.byron.framework.R;

/**
 *@comments 分享组件
 */
public class MenuListView extends FrameLayout implements View.OnClickListener,
		AnimationListener {
	//遮罩层
	private View mShadeView;
	//菜单
	private View mMenuView;
	//菜单动画 进入
	private Animation mAnimationIn;
	//菜单动画 出去
	private Animation mAnimationOut;
	//遮罩层动画 进入
	private Animation mAnimationShadeIn;
	//遮罩层动画 出去
	private Animation mAnimationShadeOut;
	//是否在动画中
	private boolean isAniming;
	//是否显示
	private boolean isShowing;
	private OnClickListener mOtherClick;
	
	public MenuListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		View v = LayoutInflater.from(context).inflate(R.layout.menu_list, null);
		addView(v);		
		LinearLayout menuContent = (LinearLayout) v.findViewById(R.id.menu_content);
		
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.Menu); 
		int i = a.getResourceId(R.styleable.Menu_content, 0); 
		a.recycle();
		if(i != 0){
			View content = LayoutInflater.from(context).inflate(i, null);
			menuContent.addView(content);
		}
		
		setFocusable(true);
		setClickable(true);
		setOnClickListener(this);
		//初始化组件。注册事件
		mShadeView = v.findViewById(R.id.shade);
		mMenuView = v.findViewById(R.id.menu);	
		
		findViewById(R.id.menu_cancel).setOnClickListener(this);
		//初始化动画
		mAnimationIn = AnimationUtils.loadAnimation(context,
				R.anim.anim_menu_enter);
		mAnimationOut = AnimationUtils.loadAnimation(context,
				R.anim.anim_menu_exit);
		mAnimationIn.setAnimationListener(this);
		mAnimationOut.setAnimationListener(this);

		mAnimationShadeIn = AnimationUtils.loadAnimation(context,
				R.anim.anim_menu_bg_enter);
		mAnimationShadeOut = AnimationUtils.loadAnimation(context,
				R.anim.anim_menu_bg_exit);
		setVisibility(View.INVISIBLE);
	}

	/**
	 * @comments 显示菜单
	 * @version 1.0
	 */
	public void show(View.OnClickListener l) {
		setVisibility(View.VISIBLE);
		findFocus();
		mMenuView.startAnimation(mAnimationIn);
		mShadeView.startAnimation(mAnimationShadeIn);
		isShowing = true;
		mOtherClick = l;
	}
	
	/**
	 * @comments 退出菜单
	 * @version 1.0
	 */
	public boolean back(){
		if(isShowing){
			complete();
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(!isShowing){
			//菜单未显示不做任何处理
			return;
		}
		if(v == this){
			//触摸空白区域退出
			complete();
			return;
		}
		if(isAniming){
			//动画中不做分享
			return;
		}
		try {
			if(mOtherClick != null){
				mOtherClick.onClick(v);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		//完毕退出
		complete();
	}
	
	
	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		//动画中
		isAniming = true;
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		isAniming = false;
		if(mAnimationOut == animation){
			this.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
	}
	//分享组件处理完毕
	private void complete(){
		if(isShowing){
			mMenuView.startAnimation(mAnimationOut);
			mShadeView.startAnimation(mAnimationShadeOut);
			isShowing = false;
		}
	}

}
