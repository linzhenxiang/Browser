package com.qybrowser.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/5/27 0027.
 */
public class QYImageView extends ImageView {
    public QYImageView(Context context) {
        super(context);
    }

    public QYImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QYImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}

