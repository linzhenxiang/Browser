package com.qybrowser.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

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

    @Override
    public void smoothScrollBy(int dx, int dy) {
        super.smoothScrollBy(dx, 0);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
    }

}
