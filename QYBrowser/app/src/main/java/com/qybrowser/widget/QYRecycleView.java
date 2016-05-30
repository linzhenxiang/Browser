package com.qybrowser.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/5/30 0030.
 */
public class QYRecycleView extends RecyclerView {
    public QYRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public QYRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public QYRecycleView(Context context) {
        super(context);
    }
//s
    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return false;
    }
}
