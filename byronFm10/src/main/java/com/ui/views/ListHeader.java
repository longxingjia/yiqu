package com.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.byron.framework.R;
/**
 * 
 * @comments 下拉时listview 的head
 * @vsersion 1.0
 */
public class ListHeader extends LinearLayout {
	/**正常阶段，箭头朝下*/
	public final static int STATE_NORMAL = 0;
	/**准备阶段，箭头朝上*/
	public final static int STATE_READY = 1;
	/**刷新阶段*/
	public final static int STATE_REFRESHING = 2;
	/**控制head的高度*/
	private LinearLayout mContainer;
	/**箭头显示*/
	private ImageView mArrowImageView;
	/**进度圈*/
	private ProgressBar mProgressBar;
	/**显示下拉刷新的文字*/
	private TextView mHintTextView;
	/**控制当前的状态*/
	private int mState = STATE_NORMAL;
	/**箭头变成上的动画*/
	private Animation mRotateUpAnim;
	/**箭头变成下的动画*/
	private Animation mRotateDownAnim;
	/**动画时间*/
	private final int ROTATE_ANIM_DURATION = 100;


	public ListHeader(Context context) {
		super(context);
		initView(context);
	}

	public ListHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	/**
	 * 
	 * @comments 初始化下拉时listview 的head
	 * @param context Context
	 * @version 1.0
	 */
	public void initView(Context context) {
		// 初始情况，设置下拉刷新view高度为0
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.list_header, null);
		addView(mContainer, lp);
		setGravity(Gravity.BOTTOM);

		mArrowImageView = (ImageView)findViewById(R.id.xlistview_header_arrow);
		mHintTextView = (TextView)findViewById(R.id.xlistview_header_hint_textview);
		mProgressBar = (ProgressBar)findViewById(R.id.xlistview_header_progressbar);
		
		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
	}
	
	/**
	 * 
	 * @comments 设置当前状态
	 * @param state 当前顶部状态值
	 * @version 1.0
	 */
	public void setState(int state) {
		if (state == mState) return ;
		
		if (state == STATE_REFRESHING) {	
			// 显示进度
			mArrowImageView.clearAnimation();
			mArrowImageView.setImageResource(R.drawable.arrow);
			mArrowImageView.setVisibility(View.INVISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
		} else {	
			// 显示箭头图片
			mArrowImageView.setImageResource(R.drawable.arrow);
			mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
		}
		
		switch(state){
			case STATE_NORMAL:
				if (mState == STATE_READY) {
					mArrowImageView.startAnimation(mRotateDownAnim);
				}
				if (mState == STATE_REFRESHING) {
					mArrowImageView.clearAnimation();
				}
				mHintTextView.setText(R.string.header_refresh);
				break;
			case STATE_READY:
				if (mState != STATE_READY) {
					mArrowImageView.clearAnimation();
					mArrowImageView.startAnimation(mRotateUpAnim);
					mHintTextView.setText(R.string.released_more);
				}
				break;
			case STATE_REFRESHING:
				mHintTextView.setText(R.string.list_loading);
				break;
			default:
				break;
		}
		
		mState = state;
	}
	
	
	/**
	 * 
	 * @comments 刷新成功
	 * @version 1.0
	 */
	public void refreshOk(){
		mHintTextView.setText(R.string.refreshOk);
		mArrowImageView.setImageResource(R.drawable.icon_ok);
		mProgressBar.setVisibility(View.INVISIBLE);
		mArrowImageView.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 
	 * @comments 刷新失败
	 * @version 1.0
	 */
	public  void refreshFail(){
		mHintTextView.setText(R.string.refreshFail);
		mArrowImageView.setImageResource(R.drawable.icon_wrong);
		mProgressBar.setVisibility(View.INVISIBLE);
		mArrowImageView.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 
	 * @comments 重置
	 * @version 1.0
	 */
	public void reSet(){
		mArrowImageView.setImageResource(R.drawable.arrow);
	}
	
	/**
	 * 
	 * @comments 设置head高度
	 * @param height head高度值
	 * @version 1.0
	 */
	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

	/**
	 * 
	 * @comments 获取head高度
	 * @return head高度
	 * @version 1.0
	 */
	public int getVisiableHeight() {
		return mContainer.getHeight();
	}

}
