package com.qybrowser;

import android.app.Application;

import com.skin.SkinManager;

/**
 * Created by Administrator on 2016/5/26 0026.
 */
public class QYApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.getInstance().init(this,null);
    }
}
