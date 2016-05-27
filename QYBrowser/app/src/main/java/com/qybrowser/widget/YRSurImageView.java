package com.qybrowser.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/1/11 0011.
 */
public class YRSurImageView extends ImageView {
    private YRSurImageView view;
    private SurfaceViewInterface surfaceViewInterface;
    private GestureDetector gestureDetector;
    private boolean isShowPress = false;

    public YRSurImageView(Context context) {
        this(context, null);
    }

    public YRSurImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YRSurImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        view = this;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {


            @Override
            public void onShowPress(MotionEvent e) {
                super.onShowPress(e);
                isShowPress = true;
                if (surfaceViewInterface != null) {
                    Log.e("VV","onShowPress");

                    surfaceViewInterface.onDown(e, view);
                }
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (surfaceViewInterface != null) {
                    Log.e("VV","onSingleTapUp");

                    surfaceViewInterface.onUp(e, view);
                }

                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                Log.e("VV","onDown");

                return super.onDown(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {

                return super.onSingleTapConfirmed(e);
            }
        });
    }

    public void setSurfaceViewInterface(SurfaceViewInterface surfaceViewInterface) {
        this.surfaceViewInterface = surfaceViewInterface;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (gestureDetector.onTouchEvent(event)) {
            Log.e("VV","gestureDetector ");

            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.e("VV","ACTION_UP ");

            if (surfaceViewInterface != null) {
                Log.e("VV","MotionEvent.ACTION_UP");
                surfaceViewInterface.onUp(event, this);
            }
            return true;
        }
        return super.onTouchEvent(event);
    }


    public interface SurfaceViewInterface {
        void onUp(MotionEvent event, YRSurImageView yrSurfaceItem);

        void onDown(MotionEvent event, YRSurImageView yrSurfaceItem);
    }
}
