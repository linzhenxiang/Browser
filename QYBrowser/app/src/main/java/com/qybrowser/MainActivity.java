package com.qybrowser;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qybrowser.fragment.BrowserFragment;
import com.qybrowser.fragment.HomeFragment;
import com.qybrowser.fragment.MenuFragment;
import com.qybrowser.web.utils.FirstLoadingX5Service;
import com.qybrowser.widget.PopMenu;
import com.qybrowser.widget.YRSurImageView;
import com.skin.SkinManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.IOverScrollDecoratorAdapter;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;

public class MainActivity extends SwipeBackActivity implements PopMenu.OnPopTouchListener, PopMenu.OnPopMenuDragListener, PopMenu.OnPopItemListener {
    @Bind(R.id.tab_menu)
    YRSurImageView mTabMenu;
    @Bind(R.id.tab_go_back)
    YRSurImageView mTabBack;
    @Bind(R.id.tab_for_word)
    YRSurImageView mTabForWord;
    @Bind(R.id.tab_home)
    YRSurImageView mTabHome;
    @Bind(R.id.tab_manager)
    YRSurImageView mTabManager;
    @Bind(R.id.main_content)
    FrameLayout mContent;
    @Bind(R.id.fg_home_progress_bar)
    ContentLoadingProgressBar progressBar;
    @Bind(R.id.fg_home_search_title)
    TextView mSearchTitle;
    @Bind(R.id.fg_home_search_bar)
    RelativeLayout mSearchBar;

    private PopMenu popMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        SkinManager.getInstance().register(this);
        View view = LayoutInflater.from(this).inflate(R.layout.pop_menu, null);
        popMenu = new PopMenu(this, view);
        popMenu.setPopTouchListener(this);
        popMenu.setDragListener(this);
//        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SkinManager.getInstance().changeSkinColor(Color.parseColor("#9c55ff"));
//            }
//        });
//
//        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SkinManager.getInstance().changeSkinColor(Color.parseColor("#7155ff"));
//                if (!popMenu.isShowing()) {
//                    popMenu.show(getWindow().getDecorView(), Gravity.NO_GRAVITY);
//                } else {
//                    popMenu.close();
//                }
//            }
//        });

        findViewById(R.id.tab_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!popMenu.isShowing()) {
                    popMenu.show(getWindow().getDecorView(), Gravity.NO_GRAVITY);
                } else {
                    popMenu.close();
                }
            }
        });

        popMenu.setPopItemListener(this);

        if (savedInstanceState == null) {
            start(HomeFragment.newInstance());
        }

        getSwipeBackLayout().setSwipeEnable(false);


    }

    @Override
    protected int setContainerId() {
        return R.id.main_content;
    }

    @Override
    protected void onDestroy() {
        SkinManager.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        findViewById(R.id.tab_menu).onTouchEvent(event);
        return false;
    }

    public RelativeLayout getSearchBar() {
        return mSearchBar;
    }

    @Override
    public void onDragOpen(float per) {
        Log.e("VV", "__________onDragOpen:" + per);
        setTabAlpha(1 - per);
    }

    @Override
    public void onDragClose(float per) {
        setTabAlpha(per);
    }


    private void setTabAlpha(float per) {
        ViewCompat.setAlpha(mTabBack, per);
        ViewCompat.setAlpha(mTabForWord, per);
        ViewCompat.setAlpha(mTabHome, per);
        ViewCompat.setAlpha(mTabManager, per);
    }

    @Override
    public void OnItemClick(View view) {
        if(popMenu.isShowing()){
            popMenu.closeIn();
            setTabAlpha(1);
        }
        start(BrowserFragment.newInstance());

    }

    public ContentLoadingProgressBar getProgressBar() {
        return progressBar;
    }

    public TextView getSearchTitle() {
        return mSearchTitle;
    }

    @Override
    protected FragmentAnimator onCreateFragmentAnimator() {
        // 设置默认Fragment动画  默认竖向(和安卓5.0以上的动画相同)
//        return super.onCreateFragmentAnimator();
        // 设置横向(和安卓4.x动画相同)
//        return new DefaultHorizontalAnimator();
        // 设置自定义动画
        return new FragmentAnimator(R.anim.h_fragment_enter, R.anim.h_fragment_exit, R.anim.h_fragment_pop_enter, R.anim.h_fragment_pop_exit);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (popMenu.isShowing()) {
                popMenu.close();
                return false;
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
