package com.yiqu.iyijiayi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by Administrator on 2017/5/24.
 */

public class NoScrollGridView  extends GridView {

    private static final String TAG = "NoScrollGridView";

    public NoScrollGridView(Context context) {
        super(context);
    }

    public NoScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    // 通过重新dispatchTouchEvent方法来禁止滑动
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        Log.v(TAG, "dispatchTouchEvent....4444444444........." + ev.getAction());
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;// 禁止Gridview进行滑动
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 重写的onTouchEvent回调方法
        Log.v(TAG, "dispatchTouchEvent...5555........" + event.getAction());
        switch (event.getAction()) {
            // 按下
            case MotionEvent.ACTION_DOWN:
                return super.onTouchEvent(event);
            // 滑动
            case MotionEvent.ACTION_MOVE:
                break;
            // 离开
            case MotionEvent.ACTION_UP:
                return super.onTouchEvent(event);
        }
        // 注意：返回值是false
        return false;
    }
//    @SuppressLint({ "NewApi", "NewApi" })
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        // 重写的onTouchEvent回调方法
        switch (event.getAction()) {
            // 按下
            case MotionEvent.ACTION_DOWN:
                return super.onGenericMotionEvent(event);
            // 滑动
            case MotionEvent.ACTION_MOVE:
                break;
            // 离开
            case MotionEvent.ACTION_UP:
                return super.onGenericMotionEvent(event);
        }
        // 注意：返回值是false
        return false;

    }
}