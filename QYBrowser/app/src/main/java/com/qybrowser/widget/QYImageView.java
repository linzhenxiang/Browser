package com.qybrowser.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isPressed())
            canvas.drawColor(Color.parseColor("#33000000"));
    }



    @Override
    protected void dispatchSetPressed(boolean pressed) {
        super.dispatchSetPressed(pressed);
        invalidate();
    }
}


