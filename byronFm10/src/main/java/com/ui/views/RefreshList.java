package com.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.byron.framework.R;

/**
 * 
 * @comments 可上拉自动加载，下拉刷新的listview
 * @vsersion 1.0
 */
public class RefreshList extends ListView implements OnScrollListener {
	
	/**滑动控制*/
	private Scroller mScroller;
	/**滑动监听*/
	private OnScrollListener mScrollListener;
	/**回调控制*/
	private IRefreshListViewListener mListViewListener;
	/**listview头部*/
	private ListHeader mHeaderView;
	/**listview头部的高度*/
	private int mHeaderViewHeight;
	/**手指触摸屏幕点的坐标的Y值*/
	private float mLastY = -1; 
	/**手下拉时候，head的变化率，这里设置为2表示，变化是手下拉距离的一半*/
	private final static float OFFSET_RADIO = 1.5f; 
	/**listview头部文字部分*/
	private RelativeLayout mHeaderViewContent;
	/**当前是否是刷新状态，初始值不是false*/
	private boolean mPullRefreshing = false;
	/**所有列表的个数*/
	private int mTotalItemCount;
	/**head高度变化是的时间*/
	private final static int SCROLL_DURATION = 100; 

	public RefreshList(Context context) {
		super(context);
		initView(context);
	}

	public RefreshList(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	/**
	 * 
	 * @comments 初始化listview
	 * @param context Context
	 * @version 1.0
	 */
	public void initView(Context context) {
		super.setOnScrollListener(this);
		
		mScroller = new Scroller(context);
		mHeaderView = new ListHeader(context);
		addHeaderView(mHeaderView, null, false);
		
		mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(R.id.xlistview_header_content);
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				//view布局完成时调用，每次view改变时都会调用  
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mHeaderViewHeight = mHeaderViewContent.getHeight();
						getViewTreeObserver().removeGlobalOnLayoutListener(this);
					}
				});
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
			reSet();
			if (mLastY == -1) {
				mLastY = ev.getRawY();
			}
			switch (ev.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mLastY = ev.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					final float deltaY = ev.getRawY() - mLastY;
					mLastY = ev.getRawY();
					if (getFirstVisiblePosition() == 0&& (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
						updateHeaderHeight(deltaY/OFFSET_RADIO);
					}
					break;
				case MotionEvent.ACTION_UP:
					mLastY = -1; 
					if (getFirstVisiblePosition() == 0) {
						if (!mPullRefreshing && mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
							startRefresh();
						}
						resetHeaderHeight();
					} 
					
					break;
			}
			
		
		return super.onTouchEvent(ev);
	}
	
	private float xDistance;
	private float yDistance;
	private float xLast;
	private float yLast;
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDistance = yDistance = 0.0f;
			xLast = ev.getX();
			yLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();
			
			xDistance += Math.abs(curX - xLast);
			yDistance += Math.abs(curY - yLast);
			
			if(xDistance > yDistance)
				return false;
			
			break;
		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	/**
	 * 
	 * @comments 恢复head初始状态
	 * @version 1.0
	 */
	public void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();
		if (height == 0) 
			return;
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0; 
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScroller.startScroll(0, height, 0, finalHeight - height,SCROLL_DURATION);
		invalidate();
	}
	
	/**
	 * 
	 * @comments 更新head的高度
	 * @param delta
	 * @version 1.0
	 */
	public void updateHeaderHeight(float delta) {
		mHeaderView.setVisiableHeight((int) delta+ mHeaderView.getVisiableHeight());
		if (!mPullRefreshing) {
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mHeaderView.setState(ListHeader.STATE_READY);
			} else {
				mHeaderView.setState(ListHeader.STATE_NORMAL);
			}
		}
		setSelection(0);
	}
	
	/**
	 * 
	 * @comments 刷新成功
	 * @version 1.0
	 */
	public void refreshOk(){
		mHeaderView.refreshOk();
	}
	
	/**
	 * 
	 * @comments 刷新失败
	 * @version 1.0
	 */
	public void refreshFail(){
		mHeaderView.refreshFail();
	}
	
	/**
	 * 
	 * @comments 重置
	 * @version 1.0
	 */
	public void reSet(){
		mHeaderView.reSet();
	}
	
	
	/**
	 * 
	 * @comments 停止下拉刷新
	 * @version 1.0
	 */
	public void stopRefresh() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}
	
	/**
	 * 
	 * @comments 开始下拉刷新
	 * @version 1.0
	 */
	public void startRefresh(){
		if (mPullRefreshing == false) {
			mPullRefreshing = true;
			mHeaderView.setState(ListHeader.STATE_REFRESHING);
			if (mListViewListener != null) {
				mListViewListener.onRefresh();
			}
		}
	}
	

	
	@Override
	public void computeScroll() {
		super.computeScroll();
		//如果mScroller没有调用startScroll，这里将会返回false
		if (mScroller.computeScrollOffset()) {
			mHeaderView.setVisiableHeight(mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}
	

	/**
	 * @comments 滚动时一直回调，直到停止滚动时才停止回调。单击时回调一次
	 * @param firstVisibleItem 当前能看见的第一个列表项ID（从0开始）
	 * @param visibleItemCount 当前能看见的列表项个数（小半个也算）
	 * @param totalItemCount 列表项个数
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,totalItemCount);
		}
	}
	
	/**
	 * @comments 滚动时回调
	 * @param scrollState 
	 * 		     第1次：scrollState = SCROLL_STATE_TOUCH_SCROLL(1) 正在滚动
	 *		     第2次：scrollState = SCROLL_STATE_FLING(2) 手指做了抛的动作
	 * 		     第3次：scrollState = SCROLL_STATE_IDLE(0) 停止滚动
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
		if(getLastVisiblePosition() == mTotalItemCount -1 ) {
			
		}
	}


	/**
	 * 
	 * @comments 给回调赋值
	 * @param l
	 * @version 1.0
	 */
	public void setRefreshListListener(IRefreshListViewListener l) {
		mListViewListener = l;
	}
	
	/**
	 * 
	 *@comments 定义回调接口
	 *@author Administrator
	 *@date 2014-5-4
	 *@version 1.0
	 */
	public interface IRefreshListViewListener {
		public void onRefresh();
	}
}
