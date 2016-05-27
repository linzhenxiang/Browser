package com.qybrowser.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2016/1/11 0011.
 */
public class YRSurfaceLayout extends RelativeLayout {

    private YRSurfaceView yrSurfaceView;

    public YRSurfaceLayout(Context context) {
        this(context, null);
    }

    public YRSurfaceLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YRSurfaceLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        yrSurfaceView = new YRSurfaceView(getContext());
        yrSurfaceView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(yrSurfaceView);
    }

    public YRSurfaceView getSurfaceView() {
        return yrSurfaceView;
    }
}
