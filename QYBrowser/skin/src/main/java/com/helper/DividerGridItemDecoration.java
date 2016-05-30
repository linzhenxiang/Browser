package com.helper;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

/**
 * @author zhy
 */
public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {

    private int mDivideWidth;
    public void setDivideWidth(int mDivideWidth) {
        this.mDivideWidth = mDivideWidth;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        outRect.set(16, 16, 16,
                16);
    }


}