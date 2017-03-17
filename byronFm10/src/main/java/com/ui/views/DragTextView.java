package com.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/3/15.
 */

public class DragTextView extends TextView {

        private float startx;// down事件发生时，手指相对于view左上角x轴的距离
        private float starty;// down事件发生时，手指相对于view左上角y轴的距离
        private float endx; // move事件发生时，手指相对于view左上角x轴的距离
        private float endy; // move事件发生时，手指相对于view左上角y轴的距离
        private int left; // DragTV左边缘相对于父控件的距离
        private int top; // DragTV上边缘相对于父控件的距离
        private int right; // DragTV右边缘相对于父控件的距离
        private int bottom; // DragTV底边缘相对于父控件的距离
        private int hor; // 触摸情况下，手指在x轴方向移动的距离
        private int ver; // 触摸情况下，手指在y轴方向移动的距离

        public DragTextView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public DragTextView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public DragTextView(Context context) {
            this(context, null);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            // TODO Auto-generated method stub
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 手指刚触摸到屏幕的那一刻，手指相对于View左上角水平和竖直方向的距离:startX startY
                    startx = event.getX();
                    starty = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    // 手指停留在屏幕或移动时，手指相对与View左上角水平和竖直方向的距离:endX endY
                    endx = event.getX();
                    endy = event.getY();
                    // 获取此时刻 View的位置。
                    left = getLeft();
                    top = getTop();
                    right = getRight();
                    bottom = getBottom();
                    // 手指移动的水平距离
                    hor = (int) (endx - startx);
                    // 手指移动的竖直距离
                    ver = (int) (endy - starty);
                    // 当手指在水平或竖直方向上发生移动时，重新设置View的位置（layout方法）
                    if (hor != 0 || ver != 0) {
                        layout(left + hor, top + ver, right + hor, bottom + ver);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;
            }
            return true;
        }

    }