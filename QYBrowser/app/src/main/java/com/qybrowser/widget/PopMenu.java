package com.qybrowser.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.qybrowser.R;

/**
 * Created by Administrator on 2016/5/27 0027.
 */
public class PopMenu extends PopupWindow implements View.OnClickListener, View.OnTouchListener {
    private RelativeLayout mBackGround;
    LinearLayout mPopBtn;
   private OnPopTouchListener popTouchListener;
    private LinearLayout mContent;
    private ImageView mBtn;
    private RelativeLayout mTabMenu;
    private State mState = State.CLOSE;
    private boolean mIsLayoutEd = false;
    private OnPopMenuDragListener dragListener;
    private OnPopItemListener popItemListener;
    ObjectAnimator openAnim, closeAnim;

    public PopMenu(final Context context, final View view) {
        mContent = (LinearLayout) view.findViewById(R.id.pop_menu_content);
        mPopBtn = (LinearLayout) view.findViewById(R.id.button_bt_flat);
        mBtn = (ImageView) view.findViewById(R.id.pop_menu_btn);
        mTabMenu = (RelativeLayout) view.findViewById(R.id.tab_menu);
        mBackGround = (RelativeLayout) view.findViewById(R.id.pop_back_ground);
        setContentView(view);
        mPopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popItemListener != null)
                    popItemListener.OnItemClick(v);
            }
        });
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(true);
        setFocusable(false);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mIsLayoutEd = true;
                open();
            }
        });
        mBtn.setOnTouchListener(this);
        mBtn.setOnClickListener(this);
        initAnim();
    }
    public interface OnPopItemListener{
        void OnItemClick(View view);
    }

    public void setPopItemListener(OnPopItemListener popItemListener) {
        this.popItemListener = popItemListener;
    }

    public boolean isOpened() {
        return mState == State.OPEN;
    }

    public static Integer evaluate(float fraction, Object startValue, Integer endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;
        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;
        return (int) ((startA + (int) (fraction * (endA - startA))) << 24)
                | (int) ((startR + (int) (fraction * (endR - startR))) << 16)
                | (int) ((startG + (int) (fraction * (endG - startG))) << 8)
                | (int) ((startB + (int) (fraction * (endB - startB))));
    }

    public void setPopTouchListener(OnPopTouchListener popTouchListener) {
        this.popTouchListener = popTouchListener;
    }

    @Override
    public void onClick(View v) {
        if (isShowing()) {
            close();
        }
    }

    private void initAnim() {
        openAnim = ObjectAnimator.ofFloat(mContent, "translationY", mContent.getLayoutParams().height, 0);
        openAnim.setDuration(300);
        openAnim.setInterpolator(new FastOutSlowInInterpolator());
        openAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (dragListener != null && mState == State.DRAGGING)
                    dragListener.onDragOpen(animation.getAnimatedFraction());
                mBackGround.setBackgroundColor(evaluate(animation.getAnimatedFraction(), Color.TRANSPARENT, Color.parseColor("#66000000")));

            }
        });
        openAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mState = State.OPEN;

            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mState = State.DRAGGING;

            }
        });

        closeAnim = ObjectAnimator.ofFloat(mContent, "translationY", mContent.getLayoutParams().height);
        closeAnim.setDuration(200);
        closeAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        closeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (dragListener != null && mState == State.DRAGGING) {
                    dragListener.onDragClose(animation.getAnimatedFraction());

                }
                mBackGround.setBackgroundColor(evaluate(animation.getAnimatedFraction(), Color.parseColor("#66000000"), Color.TRANSPARENT));
            }
        });
        closeAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                dismiss();
                mState = State.CLOSE;

            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mState = State.DRAGGING;

            }
        });
    }
    private void open() {
        openAnim.start();
    }

    public void show(View view, int gravity) {
        if (mState == State.OPEN || mState == State.DRAGGING || openAnim.isRunning())
            return;

        showAtLocation(view, gravity, 0, 0);
        if (!mIsLayoutEd) return;
        open();

    }

    public void closeIn(){
        if (openAnim.isRunning())
            openAnim.cancel();
        dismiss();
        mState = State.CLOSE;
        ViewCompat.setTranslationY(mContent,mContent.getLayoutParams().height);

    }

    public void close() {

        if (mState == State.CLOSE || mState == State.DRAGGING || closeAnim.isRunning())
            return;


        closeAnim.start();


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (popTouchListener != null) {
            popTouchListener.onTouch(v, event);
        }
        return false;
    }

    public enum State {
        OPEN, CLOSE, DRAGGING
    }


    public void setDragListener(OnPopMenuDragListener dragListener) {
        this.dragListener = dragListener;
    }

    public interface OnPopTouchListener {
        boolean onTouch(View v, MotionEvent event);
    }

    public interface OnPopMenuDragListener {
        void onDragOpen(float dxY);

        void onDragClose(float dxY);
    }
}
