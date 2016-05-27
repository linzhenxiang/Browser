package com.qybrowser.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Administrator on 2016/1/11 0011.
 */
public class YRSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private final int ENLARGE = 0x01;
    private final int NARROW = 0x02;
    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    private int rippleType = ENLARGE;
    private SurfaceHolder surfaceHolder;
    private boolean isRunning;
    private Paint paint;
    private int rippleDuration = 300;
    private int timer = 0;
    private int framer = 10;
    private int radiusMax;
    private float D;
    private int x, y;
    private boolean isClear;
    private  boolean isTouchUp;


    public YRSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public YRSurfaceView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        setZOrderOnTop(true);
        surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.DKGRAY);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void run() {
        Canvas canvas = null;
        while (isRunning) {
            try {
                canvas = surfaceHolder.lockCanvas();
                drawSurface(canvas);
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

        }


        if (((ThreadPoolExecutor) cachedThreadPool).getActiveCount() == 1 && timer != 0 &&!isTouchUp) {
            rippleType = NARROW;
            isClear = true;
            isRunning = true;
            cachedThreadPool.submit(this);
        }

        Log.e("VV", "______D:" + D + "/timer:" + timer + "/" + ((ThreadPoolExecutor) cachedThreadPool).getActiveCount()+"/isTouchUp:"+isTouchUp);

    }


    private void drawSurface(Canvas canvas) {
        if (canvas == null)
            return;
        if (rippleType == ENLARGE && !isClear) {
            if (rippleDuration < timer * framer) {
                isRunning = false;
                timer--;
                return;
            }
        } else {
            if (timer < 0) {
                isRunning = false;
                rippleType = ENLARGE;
                timer = 0;
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                canvas.drawPaint(paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
                paint.setAlpha(0);
                D = radiusMax * 0.7f;
                canvas.drawCircle(x, y, radiusMax * 0.7f, paint);
                return;
            }
        }
        canvas.save();
        float per = (timer * framer) / (float) rippleDuration;
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        paint.setAlpha((int) (per * 50));
        D = radiusMax * (0.3f * per + 0.7f);
        canvas.drawCircle(x, y, radiusMax * (0.3f * per + 0.7f), paint);
        canvas.restore();
        if (rippleType == ENLARGE && !isClear) {
            timer++;
        } else {
            timer--;
        }


    }

    public void createAnim(int pointX, int pointY, int radiusMax) {
        Log.e("VV", "createAnim A");

        isTouchUp = true;
        isClear = false;
        isRunning = true;
        rippleDuration = 120;
        this.x = pointX;
        this.y = pointY;
        this.radiusMax = (int) (radiusMax * 1.4f);
        Log.e("VV", "createAnim B:____radiusMax" + (10 * framer) / (float) rippleDuration);
        cachedThreadPool.submit(this);
    }


    public void closeAnim(int pointX, int pointY, int radiusMax) {
        Log.e("VV", "closeAnim A");
        rippleType = NARROW;
        isRunning = true;
        isTouchUp = false;
        rippleDuration = 300;
        timer = (rippleDuration / framer) - 5;
        this.x = pointX;
        this.y = pointY;
        this.radiusMax = (int) (radiusMax * 1.4f);
        cachedThreadPool.submit(this);
    }

    public void setFixedSize(int width, int heght) {
        surfaceHolder.setFixedSize(width, heght);
    }
}
