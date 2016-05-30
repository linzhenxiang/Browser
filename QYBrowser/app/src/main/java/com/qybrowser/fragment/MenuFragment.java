package com.qybrowser.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qybrowser.R;

import me.yokeyword.fragmentation.SwipeBackLayout;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * Created by Administrator on 2016/5/30 0030.
 */
public class MenuFragment extends SwipeBackFragment implements SwipeBackLayout.OnSwipeListener {

    /**
     * Created by Administrator on 2016/5/30 0030.
     */

    public static MenuFragment newInstance() {

        Bundle args = new Bundle();

        MenuFragment fragment = new MenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_layout, container, false);
        getSwipeBackLayout().addSwipeListener(this);
        return attachToSwipeBack(view);
    }

    @Override
    public void onDragStateChange(int state) {
    }

    @Override
    public void onEdgeTouch(int oritentationEdgeFlag) {

    }

    @Override
    public void onDragScrolled(float scrollPercent) {

    }
}
