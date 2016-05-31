package com.qybrowser;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

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

    private PopMenu popMenu;
    private static boolean main_initialized = false;

    private volatile boolean isX5WebViewEnabled = false;
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

        preinitX5WebCore();
        preinitX5WithService();// 此方法必须在非主进程执行才会有效
    }
    /**
     * 开启额外进程 服务 预加载X5内核， 此操作必须在主进程调起X5内核前进行，否则将不会实现预加载
     */
    private void preinitX5WithService() {
        Intent intent = new Intent(getBaseContext(), FirstLoadingX5Service.class);
        startService(intent);
    }
    private QbSdk.PreInitCallback myCallback = new QbSdk.PreInitCallback() {

        @Override
        public void onViewInitFinished() {// 当X5webview 初始化结束后的回调
            new WebView(getBaseContext());
            MainActivity.this.isX5WebViewEnabled = true;
        }

        @Override
        public void onCoreInitFinished() {
        }
    };
    /**
     * X5内核在使用preinit接口之后，对于首次安装首次加载没有效果
     * 实际上，X5webview的preinit接口只是降低了webview的冷启动时间；
     * 因此，现阶段要想做到首次安装首次加载X5内核，必须要让X5内核提前获取到内核的加载条件
     */
    private void preinitX5WebCore() {
        if (!QbSdk.isTbsCoreInited()) {// preinit只需要调用一次，如果已经完成了初始化，那么就直接构造view
            QbSdk.preInit(MainActivity.this, myCallback);// 设置X5初始化完成的回调接口
            // 第三个参数为true：如果首次加载失败则继续尝试加载；
        }
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
        start(MenuFragment.newInstance());

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
