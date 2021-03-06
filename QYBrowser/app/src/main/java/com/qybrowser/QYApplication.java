package com.qybrowser;

import android.app.Application;
import android.content.Intent;

import com.qybrowser.web.utils.FirstLoadingX5Service;
import com.skin.SkinManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by Administrator on 2016/5/26 0026.
 */
public class QYApplication extends Application {
    private static boolean main_initialized = false;

    private volatile boolean isX5WebViewEnabled = false;
    private QbSdk.PreInitCallback myCallback = new QbSdk.PreInitCallback() {

        @Override
        public void onViewInitFinished() {// 当X5webview 初始化结束后的回调
            new WebView(getBaseContext());
            isX5WebViewEnabled = true;
        }

        @Override
        public void onCoreInitFinished() {
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.getInstance().init(this,null);
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

    /**
     * X5内核在使用preinit接口之后，对于首次安装首次加载没有效果
     * 实际上，X5webview的preinit接口只是降低了webview的冷启动时间；
     * 因此，现阶段要想做到首次安装首次加载X5内核，必须要让X5内核提前获取到内核的加载条件
     */
    private void preinitX5WebCore() {
        if (!QbSdk.isTbsCoreInited()) {// preinit只需要调用一次，如果已经完成了初始化，那么就直接构造view
            QbSdk.preInit(getBaseContext(), myCallback);// 设置X5初始化完成的回调接口
            // 第三个参数为true：如果首次加载失败则继续尝试加载；
        }
    }
}
