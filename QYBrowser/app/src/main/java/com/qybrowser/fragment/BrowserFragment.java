package com.qybrowser.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qybrowser.MainActivity;
import com.qybrowser.R;
import com.qybrowser.web.utils.X5WebView;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;

import java.net.MalformedURLException;
import java.net.URL;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SwipeBackLayout;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

public class BrowserFragment extends SwipeBackFragment implements SwipeBackLayout.OnSwipeListener {
    public static final int MSG_OPEN_TEST_URL = 0;
    public static final int MSG_INIT_UI = 1;
    private static final String mHomeUrl = "http://baidu.com";
    private static final String TAG = "SdkDemo";
    private static final int MAX_LENGTH = 14;
    private final int disable = 120;
    private final int enable = 255;
    private boolean mEnableLoadUrl = true;
    private final int mUrlStartNum = 0;
    private final int mUrlEndNum = 108;

    private TextView mUrl;
    boolean[] m_selected = new boolean[]{true, true, true, true, false,
            false, true};
    private RelativeLayout mSearchBar;
    /**
     * 作为一个浏览器的示例展示出来，采用android+web的模式
     */
    private X5WebView mWebView;
    private boolean mNeedTestPage = false;
    private ValueCallback<Uri> uploadFile;
    private URL mIntentUrl;
    private TEST_ENUM_FONTSIZE m_font_index = TEST_ENUM_FONTSIZE.FONT_SIZE_NORMAL;
    private int mCurrentUrl = mUrlStartNum;
    /* private class TestHandler extends */
    private Handler mTestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_OPEN_TEST_URL:
                    if (!mNeedTestPage) {
                        return;
                    }

                    String testUrl = "file:///sdcard/outputHtml/html/"
                            + Integer.toString(mCurrentUrl) + ".html";
                    if (mWebView != null) {
                        mWebView.loadUrl(testUrl);
                    }

                    mCurrentUrl++;
                    break;
                case MSG_INIT_UI:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private ContentLoadingProgressBar progressBar;

//    private void initBtnListenser() {
//        mBack = (ImageButton) findViewById(R.id.btnBack1);
//        mForward = (ImageButton) findViewById(R.id.btnForward1);
//        mRefresh = (ImageButton) findViewById(R.id.btnRefresh1);
//        mExit = (ImageButton) findViewById(R.id.btnExit1);
//        mHome = (ImageButton) findViewById(R.id.btnHome1);
//        mTestProcesses = (ImageButton) findViewById(R.id.btnTestProcesses1);
//        mTestWebviews = (ImageButton) findViewById(R.id.btnTestWebviews1);
//        mGo = (Button) findViewById(R.id.btnGo1);
//        mUrl = (EditText) findViewById(R.id.editUrl1);
//        mMore = (ImageButton) findViewById(R.id.btnMore);
//        mMenu = (RelativeLayout) findViewById(R.id.menuMore);
//        mClearData = (ImageButton) findViewById(R.id.btnClearData);
//        mOpenFile = (ImageButton) findViewById(R.id.btnOpenFile);
//
//        if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
//        {
//            mBack.setAlpha(disable);
//            mForward.setAlpha(disable);
//            mHome.setAlpha(disable);
//        }
//        mHome.setEnabled(false);
//
//
//
//        mBack.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                moreMenuClose();
//                if (mWebView != null && mWebView.canGoBack())
//                    mWebView.goBack();
//            }
//        });
//
//        mForward.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                moreMenuClose();
//                if (mWebView != null && mWebView.canGoForward())
//                    mWebView.goForward();
//            }
//        });
//
//        mRefresh.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                moreMenuClose();
//                if (mWebView != null)
//                    mWebView.reload();
//            }
//        });
//
//
//        mGo.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                moreMenuClose();
//                String url = mUrl.getText().toString();
//                mWebView.loadUrl(url);
//                mWebView.requestFocus();
//            }
//        });
//
//        mMore.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (mMenu.getVisibility() == View.GONE)
//                {
//                    mMenu.setVisibility(View.VISIBLE);
//                    mMore.setImageDrawable(getResources().getDrawable(R.drawable.theme_toolbar_btn_menu_fg_pressed));
//                }else{
//                    mMenu.setVisibility(View.GONE);
//                    mMore.setImageDrawable(getResources().getDrawable(R.drawable.theme_toolbar_btn_menu_fg_normal));
//                }
//            }
//        });
//
//        mClearData.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                moreMenuClose();
////				QbSdk.clearAllWebViewCache(getApplicationContext(),false);
//                QbSdk.reset(getApplicationContext());
//            }
//        });
//
//        mOpenFile.setOnClickListener(new OnClickListener()
//        {
//
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("*/*");
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                try
//                {
//                    BrowserFragment.this.startActivityForResult(Intent.createChooser(intent,"choose file"), 1);
//                }
//                catch (android.content.ActivityNotFoundException ex)
//                {
//                    Toast.makeText(BrowserFragment.this, "完成", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//        mUrl.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                moreMenuClose();
//                if (hasFocus) {
//                    mGo.setVisibility(View.VISIBLE);
//                    mRefresh.setVisibility(View.GONE);
//                    if (null == mWebView.getUrl()) return;
//                    if (mWebView.getUrl().equalsIgnoreCase(mHomeUrl)) {
//                        mUrl.setText("");
//                        mGo.setText("首页");
//                        mGo.setTextColor(0X6F0F0F0F);
//                    } else {
//                        mUrl.setText(mWebView.getUrl());
//                        mGo.setText("进入");
//                        mGo.setTextColor(0X6F0000CD);
//                    }
//                } else {
//                    mGo.setVisibility(View.GONE);
//                    mRefresh.setVisibility(View.VISIBLE);
//                    String title = mWebView.getTitle();
//                    if (title != null && title.length() > MAX_LENGTH)
//                        mUrl.setText(title.subSequence(0, MAX_LENGTH) + "...");
//                    else
//                        mUrl.setText(title);
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }

//        });

//        mUrl.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // TODO Auto-generated method stub
//
//                String url = null;
//                if (mUrl.getText() != null) {
//                    url = mUrl.getText().toString();
//                }
//
//                if (url == null
//                        || mUrl.getText().toString().equalsIgnoreCase("")) {
//                    mGo.setText("请输入网址");
//                    mGo.setTextColor(0X6F0F0F0F);
//                } else {
//                    mGo.setText("进入");
//                    mGo.setTextColor(0X6F0000CD);
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//                // TODO Auto-generated method stub
//
//            }
//
//        });
//
//        mHome.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                moreMenuClose();
//                if (mWebView != null)
//                    mWebView.loadUrl(mHomeUrl);
//            }
//        });
//
//
//
//
//        mExit.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                android.os.Process.killProcess(Process.myPid());
//            }
//
//        });
//    }

    public static BrowserFragment newInstance() {

        Bundle args = new Bundle();

        BrowserFragment fragment = new BrowserFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.web_activity_main, container, false);
        Log.e("VV", "______onCreateView");
        init(view);

        return attachToSwipeBack(view);
    }

    ;

    @Override
    public void onResume() {
        super.onResume();
        Log.e("VV", "______onResume:");

    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (mWebView != null && mWebView.canGoBack()) {
//                mWebView.goBack();
//                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
//                    changGoForwardButton(mWebView);
//                return true;
//            } else
//                return super.onKeyDown(keyCode, event);
//        }
//        return super.onKeyDown(keyCode, event);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        TbsLog.d(TAG, "onActivityResult, requestCode:" + requestCode
//                + ",resultCode:" + resultCode);
//
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case 0:
//                    if (null != uploadFile) {
//                        Uri result = data == null || resultCode != RESULT_OK ? null
//                                : data.getData();
//                        uploadFile.onReceiveValue(result);
//                        uploadFile = null;
//                    }
//                    break;
//                case 1:
//
//                    Uri uri = data.getData();
//                    String path = uri.getPath();
//
//
//                    break;
//                default:
//                    break;
//            }
//        }
//        else if (resultCode == RESULT_CANCELED) {
//            if (null != uploadFile) {
//                uploadFile.onReceiveValue(null);
//                uploadFile = null;
//            }
//
//        }
//
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        if (intent == null || mWebView == null || intent.getData() == null)
//            return;
//        mWebView.loadUrl(intent.getData().toString());
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (mWebView != null)
//            mWebView.destroy();
//        super.onDestroy();
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            try {
                mIntentUrl = new URL(intent.getData().toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //
        try {
            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
                getActivity().getWindow()
                        .setFlags(
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSwipeBackLayout().addSwipeListener(this);
        progressBar = ((MainActivity) getActivity()).getProgressBar();
        mUrl = ((MainActivity) getActivity()).getSearchTitle();
        mSearchBar = ((MainActivity) getActivity()).getSearchBar();
        /*getWindow().addFlags(
                android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
//        mViewParent = (ViewGroup) findViewById(R.id.web_view);

//        initBtnListenser();

        // preloadX5Check -- call this before webview creation
        //QbSdk.preloadX5Check(this);

        QbSdk.preInit(getActivity());

//        this.webViewTransportTest();

//        mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);// �ӳ�1.5s����webview

    }

    private void webViewTransportTest() {
        X5WebView.setSmallWebViewEnabled(true);
    }

    /**
     * 前进后退判断，修改图片状态
     *
     * @param view
     */
    private void changGoForwardButton(WebView view) {
//        if (view.canGoBack())
//            mBack.setAlpha(enable);
//        else
//            mBack.setAlpha(disable);
//        if (view.canGoForward())
//            mForward.setAlpha(enable);
//        else
//            mForward.setAlpha(disable);
//        if (view.getUrl()!=null && view.getUrl().equalsIgnoreCase(mHomeUrl)) {
//            mHome.setAlpha(disable);
//            mHome.setEnabled(false);
//        } else {
//            mHome.setAlpha(enable);
//            mHome.setEnabled(true);
//        }
    }

    private void initProgressBar() {
//        mPageLoadingProgressBar = (ProgressBar) findViewById(R.id.progressBar1);// new
//        // ProgressBar(getApplicationContext(),
//        // null,
//        // android.R.attr.progressBarStyleHorizontal);
//        mPageLoadingProgressBar.setMax(100);
//        mPageLoadingProgressBar.setProgressDrawable(this.getResources()
//                .getDrawable(R.drawable.color_progressbar));
    }

    private void init(View view) {
        // ========================================================
        //
        //mWebView = new DemoWebView(this);
        mWebView = (X5WebView) view.findViewById(R.id.web_view);


        Log.w("grass", "Current SDK_INT:" + Build.VERSION.SDK_INT);


//        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.FILL_PARENT,
//                FrameLayout.LayoutParams.FILL_PARENT));

        initProgressBar();
        // ����Client
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;

            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view,
                                                              WebResourceRequest request) {
                // TODO Auto-generated method stub

                Log.e("should", "request.getUrl().toString() is " + request.getUrl().toString());

                return super.shouldInterceptRequest(view, request);
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                moreMenuClose();
//                 mTestHandler.sendEmptyMessage(MSG_OPEN_TEST_URL);
//                mTestHandler.sendEmptyMessageDelayed(MSG_OPEN_TEST_URL, 5000);// 5s?
                if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
                    changGoForwardButton(view);
				/* mWebView.showLog("test Log"); */

            }


        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                TbsLog.d(TAG, "title: " + title);
                if (mUrl == null)
                    return;
                if (!mWebView.getUrl().equalsIgnoreCase(mHomeUrl)) {
                    if (title != null && title.length() > MAX_LENGTH)
                        mUrl.setText(title.subSequence(0, MAX_LENGTH) + "...");
                    else
                        mUrl.setText(title);
                } else {
                    mUrl.setText(view.getUrl());
                }
            }


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress > 60 && mWebView.getVisibility() == View.GONE) {
                    mWebView.setVisibility(View.VISIBLE);

                }

                progressBar.setProgress(newProgress);
                if (progressBar != null && newProgress != 100) {
                    progressBar.setVisibility(View.VISIBLE);
                } else if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        mWebView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String arg0, String arg1, String arg2,
                                        String arg3, long arg4) {
                TbsLog.d(TAG, "url: " + arg0);
//                new AlertDialog.Builder(BrowserFragment.this)
//                        .setTitle("�Ƿ�����")
//                        .setPositiveButton("yes",
//                                new DialogInterface.OnClickListener() {
//
//                                    @Override
//                                    public void onClick(DialogInterface dialog,
//                                                        int which) {
//                                        Toast.makeText(
//                                                BrowserFragment.this,
//                                                "fake message: i'll download...",
//                                                1000).show();
//                                    }
//                                })
//                        .setNegativeButton("no",
//                                new DialogInterface.OnClickListener() {
//
//                                    @Override
//                                    public void onClick(DialogInterface dialog,
//                                                        int which) {
//                                        // TODO Auto-generated method stub
//                                        Toast.makeText(
//                                                BrowserFragment.this,
//                                                "fake message: refuse download...",
//                                                1000).show();
//                                    }
//                                })
//                        .setOnCancelListener(
//                                new DialogInterface.OnCancelListener() {
//
//                                    @Override
//                                    public void onCancel(DialogInterface dialog) {
//                                        // TODO Auto-generated method stub
//                                        Toast.makeText(
//                                                BrowserFragment.this,
//                                                "fake message: refuse download...",
//                                                1000).show();
//                                    }
//                                }).show();
            }
        });


        WebSettings webSetting = mWebView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(getActivity().getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(getActivity().getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(getActivity().getDir("geolocation", 0)
                .getPath());
//         webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
//         webSetting.setPreFectch(true);/
        long time = System.currentTimeMillis();
//        if (mIntentUrl == null) {
//            mWebView.loadUrl(mHomeUrl);
//        } else {
//            mWebView.loadUrl(mIntentUrl.toString());
//        }
//        TbsLog.d("time-cost", "cost time: "
//                + (System.currentTimeMillis() - time));
//        CookieSyncManager.createInstance(getActivity());
//        CookieSyncManager.getInstance().sync();
    }

    /**
     * 更多菜单
     */
    private void moreMenuClose() {
//        if (mMenu!=null && mMenu.getVisibility()==View.VISIBLE)
//        {
//            mMenu.setVisibility(View.GONE);
//            mMore.setImageDrawable(getResources().getDrawable(R.drawable.theme_toolbar_btn_menu_fg_normal));
//        }
    }

    @Override
    public void onDestroy() {
        Log.e("VV", getClass().getName() + "___________onDestroy" + ((SupportActivity) getActivity()).getFragmentRecords() + "/" + mSearchBar.getTranslationX());
        int size = ((SupportActivity) getActivity()).getFragmentRecords();
        if (size == 2 && mSearchBar.getTranslationX() == 0) {
            ViewCompat.setTranslationY(mSearchBar,-mSearchBar.getMeasuredHeight());
        }
        if (mWebView != null)
            mWebView.destroy();
        super.onDestroy();
    }

    @Override
    public void onDragStateChange(int state) {
        Log.e("VV", "______onDragStateChange:" + state);
    }

    @Override
    public void onEdgeTouch(int oritentationEdgeFlag) {

    }


    @Override
    public void onDragScrolled(float scrollPercent) {

    }

    @Override
    public void onAnimationStart(Animation animation) {
        super.onAnimationStart(animation);
        Log.e("VV", "______onAnimationStart:");
        ViewCompat.setTranslationX(mSearchBar, 0);
        ObjectAnimator animator = ObjectAnimator.ofFloat(mSearchBar, "translationY", 0);
        animator.setDuration(animation.getDuration());
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.start();

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        super.onAnimationEnd(animation);
        Log.e("VV", "onAnimationEnd:");
        ViewCompat.setPaddingRelative((View) mWebView.getParent(), 0, mSearchBar.getMeasuredHeight(), 0, 0);
        long time = System.currentTimeMillis();
        if (mEnableLoadUrl) {
            if (mIntentUrl == null) {
                mWebView.loadUrl(mHomeUrl);
            } else {
                mWebView.loadUrl(mIntentUrl.toString());
            }
            mEnableLoadUrl = false;
        }

        TbsLog.d("time-cost", "cost time: "
                + (System.currentTimeMillis() - time));
        CookieSyncManager.createInstance(getActivity());
        CookieSyncManager.getInstance().sync();
    }

    @Override
    protected void onHidden() {
        super.onHidden();
        Log.e("VV", "onHidden:");

    }

    @Override
    protected void onShow() {
        super.onShow();

    }

    @Override
    public boolean onBackPressedSupport() {
        Log.e("VV", "onBackPressedSupport:");

        mEnableLoadUrl = false;
        return super.onBackPressedSupport();

    }

    @Override
    protected FragmentAnimator onCreateFragmentAnimation() {
        return new DefaultHorizontalAnimator();
    }

    private enum TEST_ENUM_FONTSIZE {
        FONT_SIZE_SMALLEST, FONT_SIZE_SMALLER, FONT_SIZE_NORMAL, FONT_SIZE_LARGER, FONT_SIZE_LARGEST
    }


}
