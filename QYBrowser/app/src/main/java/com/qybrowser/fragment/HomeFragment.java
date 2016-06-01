package com.qybrowser.fragment;

import android.animation.ObjectAnimator;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.qybrowser.Adapter.FGHomeRecyclerAdapter;
import com.qybrowser.MainActivity;
import com.qybrowser.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * Created by Administrator on 2016/5/30 0030.
 */
public class HomeFragment extends SwipeBackFragment {
    @Bind(R.id.fg_home_recycle_view)
    RecyclerView mRecyclerView;

    private RelativeLayout mSearchBar;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
        mSearchBar = ((MainActivity)getActivity()).getSearchBar();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_layout, container, false);
        ButterKnife.bind(this, view);
        initRecycle();
        Log.e("VV",getClass().getName()+"_____onCreateView");
        return attachToSwipeBack(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("VV",getClass().getName()+"_____onResume");

    }

    @Override
    protected void onShow() {
        super.onShow();


    }

    @Override
    protected void onHidden() {
        super.onHidden();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("VV",getClass().getName()+"_____onPause");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("VV",getClass().getName()+"_____onDestroyView");

    }

    /**
     * clk
     */
    private void initRecycle() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(new FGHomeRecyclerAdapter(_mActivity));
    }


    @Override
    public void onAnimationEnd(Animation animation) {
        super.onAnimationEnd(animation);
        Log.e("VV",getClass().getName()+"_____onAnimationEnd");

    }

    @Override
    public void onAnimationStart(Animation animation) {
        super.onAnimationStart(animation);
        ObjectAnimator animator = ObjectAnimator.ofFloat(mSearchBar,"translationX",mSearchBar.getMeasuredWidth());
        animator.setDuration(animation.getDuration());
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.start();
        Log.e("VV",getClass().getName()+"_____onAnimationStart");

    }
}
