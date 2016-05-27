package com.qybrowser;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.qybrowser.widget.PopMenu;
import com.qybrowser.widget.YRSurImageView;
import com.skin.SkinManager;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements PopMenu.OnPopTouchListener, PopMenu.OnPopMenuDragListener {
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
    private PopMenu popMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        SkinManager.getInstance().register(this);
        View view = LayoutInflater.from(this).inflate(R.layout.pop_menu, null);
        popMenu = new PopMenu(this, view);
        popMenu.setPopTouchListener(this);
        popMenu.setDragListener(this);
        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkinManager.getInstance().changeSkinColor(Color.parseColor("#9c55ff"));
            }
        });

        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkinManager.getInstance().changeSkinColor(Color.parseColor("#7155ff"));
                if (!popMenu.isShowing()) {
                    popMenu.show(getWindow().getDecorView(), Gravity.NO_GRAVITY);
                } else {
                    popMenu.close();
                }
            }
        });

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
}
