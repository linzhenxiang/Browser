package com.qybrowser.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2016/5/26 0026.
 */
public class QYNestedScrollView extends ScrollView {

    private static final String TAG = "CRScrollView";
    private static final boolean DEBUG = true;

    /**
     * Sentinel value for no current active pointer.
     * Used by {@link #mActivePointerId}.
     */
    private static final int INVALID_POINTER = -1;
    ObjectAnimator animator;
    private int mTouchSlop;
    /**
     * ID of the active pointer. This is used to retain consistency during
     * drags/flings if multiple pointers are used.
     */
    private int mActivePointerId = INVALID_POINTER;
    private boolean isFirstScroll = true;
    /**
     * Position of the last motion event.
     */
    private float mLastMotionY, mLastMotionX;
    /**
     * True if the user is currently dragging this ScrollView around. This is
     * not the same as 'is being flinged', which can be checked by
     * mScroller.isFinished() (flinging begins when the user lifts his finger).
     */
    private boolean mIsBeingDragged = false;
    private boolean mIsUnableToDrag;
    private int mScrollY;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private float mDexY;
    private float mOldDexY;
    private int downX;
    private int downY;
    private boolean mEnableLayout = true;

    public QYNestedScrollView(Context context) {
        this(context,null);

    }

    public QYNestedScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public QYNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();

    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mEnableLayout) {
            super.onLayout(changed, l, t, r, b);
            mEnableLayout = false;
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mEnableLayout = true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

       /*
         * This method JUST determines whether we want to intercept the motion.
         * If we return true, onMotionEvent will be called and we do the actual
         * scrolling there.
         */

        final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;

        // Always take care of the touch gesture being complete.
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            // Release the drag.
            resetTouch();
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                /*
                 * mIsBeingDragged == false, otherwise the shortcut would have caught it. Check
                 * whether the user has moved far enough from his original down touch.
                 */

                /*
                * Locally do absolute value. mLastMotionY is set to the y value
                * of the down event.
                */
                final int activePointerId = mActivePointerId;
                if (activePointerId == INVALID_POINTER) {
                    // If we don't have a valid id, the touch down wasn't on content.
                    break;
                }

                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, activePointerId);
                if (pointerIndex == INVALID_POINTER) {
                    // If we don't have a valid id, the touch down wasn't on content.
                    return true;
                }
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float dx = x - mLastMotionX;
                final float xDiff = Math.abs(dx);
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float yDiff = Math.abs(y - mInitialMotionY);
                if (DEBUG)
                    Log.v(TAG, "Moved x to " + x + "," + y + " diff=xDiff:" + xDiff + ",yDiff:" + yDiff + "/mTouchSlop:" + mTouchSlop);

                if (xDiff > mTouchSlop) {

                    if (DEBUG) Log.v(TAG, "Starting drag!");
                    mIsBeingDragged = true;
                    requestParentDisallowInterceptTouchEvent(true);
                    mLastMotionX = dx > 0 ? mInitialMotionX + mTouchSlop :
                            mInitialMotionX - mTouchSlop;
                    mLastMotionY = y;
                    return true;
                } else if (yDiff > mTouchSlop) {

                    // The finger has moved enough in the vertical
                    // direction to be counted as a drag...  abort
                    // any attempt to drag horizontally, to work correctly
                    // with children that have scrolling containers.
                    if (DEBUG) Log.v(TAG, "Starting unable to drag!");
                    return true;
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                /*
                 * Remember location of down touch.
                 * ACTION_DOWN always refers to pointer index 0.
                 */
                mLastMotionX = mInitialMotionX = ev.getX();
                mLastMotionY = mInitialMotionY = ev.getY();
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                break;
            }
            case MotionEventCompat.ACTION_POINTER_UP:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e("VV", "___________onTouchEvent______:" + ev.getAction());

        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:

                    /*
                 * Remember location of down touch.
                 * ACTION_DOWN always refers to pointer index 0.
                 */
                mLastMotionX = mInitialMotionX = ev.getX();
                mLastMotionY = mInitialMotionY = ev.getY();


                break;
            case MotionEvent.ACTION_MOVE:


                if (getTranslationY() == 0) {
                    if (getScrollY() != 0) {
                        mDexY = 0;
                        mInitialMotionY = ev.getY();
                    } else {
                        mDexY = ev.getY() - mInitialMotionY;
                        if (mDexY > 0) {
                            Log.e("VV","___________mDexY>0:"+mDexY+"/mInitialMotionY:"+mInitialMotionY+"/ev.getY():"+ev.getY());

                            ViewCompat.setTranslationY(getChildAt(0), Math.max((int) (mDexY / 4), 0));
                            return true;
                        } else {
                            Log.e("VV", "___________mDexY<0:" + mDexY + "/mInitialMotionY:" + mInitialMotionY + "/ev.getY():" + ev.getY());

                            ViewCompat.setTranslationY(getChildAt(0), 0);
                            return super.onTouchEvent(ev);
                        }
                    }
                } else {
                    Log.e("VV","___________mDexY:"+mDexY+"/mInitialMotionY:"+mInitialMotionY+"/ev.getY():"+ev.getY());

                    mDexY = ev.getY() - mInitialMotionY;
                    if (mDexY > 0) {
                        ViewCompat.setTranslationY(getChildAt(0), Math.max((int) (mDexY / 4), 0));
                        return true;
                    } else {
                        ViewCompat.setTranslationY(getChildAt(0), 0);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                ObjectAnimator animator = ObjectAnimator.ofFloat(getChildAt(0), "translationY", 0);
                animator.setDuration(200);
                animator.setInterpolator(new FastOutSlowInInterpolator());
                animator.start();
               invalidate();

                break;
        }
        return super.onTouchEvent(ev);
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        final ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    private void resetTouch() {
        mActivePointerId = INVALID_POINTER;
        endDrag();
    }

    private void endDrag() {
        mIsBeingDragged = false;
        mIsUnableToDrag = false;
    }


}
