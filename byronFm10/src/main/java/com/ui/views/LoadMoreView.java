package com.ui.views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.RelativeLayout;

import com.byron.framework.R;
import com.utils.LogUtils;

public class LoadMoreView extends RelativeLayout implements OnScrollListener {
    //加载更多组件
    private View footer;
    //是否支持加载更多
    private boolean mIsMoreAble = false;
    //是否正在加载中
    private boolean mIsMoreing;
    //回调函数
    private OnMoreListener moreListener;
    private View endLoading;

    public LoadMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setVisibility(View.VISIBLE);

    }

    @Override
    protected void onFinishInflate() {
        footer = findViewById(R.id.footer);
        endLoading = findViewById(R.id.endLoading);
        super.onFinishInflate();
        end();
    }

    /**
     * @param isMoreAble
     * @comments 设置是否加载更多
     * @version 1.0
     */
    public void setMoreAble(boolean isMoreAble) {
        mIsMoreAble = isMoreAble;
    }

    public Handler mHandler = new Handler();

    //加载中
    public void loading() {
        //开始加载更多，变更相应标记
        mIsMoreing = true;
        footer.setVisibility(View.VISIBLE);
        endLoading.setVisibility(View.GONE);
    }

    //加载结束，需要发起者调用
    public void end() {
        //结束加载更多，变更相应标记
        mIsMoreing = false;
        footer.setVisibility(View.GONE);
        endLoading.setVisibility(View.VISIBLE);

    }

    boolean tag = false;

    public  void setEndLoading() {

        if (!tag) {
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    LogUtils.LOGE("f", "f");
                    tag = true;
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            endLoading.setVisibility(View.GONE);
                            tag = false;
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        //&& firstVisibleItem !=0
        if (mIsMoreAble &&
                firstVisibleItem + visibleItemCount == totalItemCount) {
            //正在加载中，忽略处理
            if (!mIsMoreing) {
                if (moreListener != null) {
                    //通知发起者做相应处理
                    if (moreListener.onMore(view)) {
                        //通知本组件正在加载中
                        loading();
                    }

                }
            }
        } else if (!mIsMoreAble) {
            if (!mIsMoreing) {
                end();

                setEndLoading();
            }
        }
    }

    /**
     * @version 1.0
     * @comments 回调接口
     * @date 2014-3-21
     */
    public interface OnMoreListener {
        /**
         * @param view
         * @return true:界面已处理。 false:界面忽略处理
         * @comments 加载更多被触发
         * @version 1.0
         */
        public boolean onMore(AbsListView view);
    }

    /**
     * @param moreListener
     * @comments 注册回调
     * @version 1.0
     */
    public void setOnMoreListener(OnMoreListener moreListener) {
        this.moreListener = moreListener;
    }

    /**
     * @return
     * @comments 当前是否在加载中
     * @version 1.0
     */
    public boolean isloading() {
        return mIsMoreing;
    }

    public boolean getMoreAble() {
        return mIsMoreAble;
    }

}

