package com.qybrowser.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import com.qybrowser.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/1/11 0011.
 */
public class YRTabLayout extends RelativeLayout implements YRSurImageView.SurfaceViewInterface {
    @Bind(R.id.main_tab_surface_view)
    YRSurfaceLayout surfaceLayout;
    @Bind(R.id.main_tab_average_layout)
    YRAverage yrAverage;
    private OnTabDrawListener drawListener;

    public YRTabLayout(Context context) {
        this(context, null);
    }

    public YRTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public YRTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        for (int i = 0; i < yrAverage.getChildCount(); i++) {
            View child = yrAverage.getChildAt(i);
            if (child instanceof YRSurImageView) {
                ((YRSurImageView) child).setSurfaceViewInterface(this);
            } else if (child instanceof ViewGroup) {
                ((YRSurImageView) ((ViewGroup) child).getChildAt(0)).setSurfaceViewInterface(this);
            }
        }
        ViewGroup.LayoutParams lp = getLayoutParams();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawListener != null) {
            drawListener.onDrawEnd();
        }

    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    public void onUp(MotionEvent event, YRSurImageView yrSurfaceItem) {
        if (yrSurfaceItem.getParent() instanceof YRAverage) {
            surfaceLayout.getSurfaceView().closeAnim(yrSurfaceItem.getLeft() + yrSurfaceItem.getMeasuredWidth() / 2, yrSurfaceItem.getMeasuredHeight() / 2, yrSurfaceItem.getMeasuredWidth() / 2);

        } else {
            ViewGroup group = (ViewGroup) yrSurfaceItem.getParent();
            surfaceLayout.getSurfaceView().closeAnim(group.getLeft() + group.getMeasuredWidth() / 2, group.getMeasuredHeight() / 2, group.getMeasuredWidth() / 2);

        }
    }

    @Override
    public void onDown(MotionEvent event, YRSurImageView yrSurfaceItem) {
        if (yrSurfaceItem.getParent() instanceof YRAverage) {
            surfaceLayout.getSurfaceView().createAnim(yrSurfaceItem.getLeft() + yrSurfaceItem.getMeasuredWidth() / 2, yrSurfaceItem.getMeasuredHeight() / 2, yrSurfaceItem.getMeasuredWidth() / 2);

        } else {
            ViewGroup group = (ViewGroup) yrSurfaceItem.getParent();
            surfaceLayout.getSurfaceView().createAnim(group.getLeft() + group.getMeasuredWidth() / 2, group.getMeasuredHeight() / 2, group.getMeasuredWidth() / 2);

        }

    }

    public interface OnTabDrawListener {
        void onDrawEnd();
    }
}
