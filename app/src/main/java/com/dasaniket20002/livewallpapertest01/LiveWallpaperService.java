package com.dasaniket20002.livewallpapertest01;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class LiveWallpaperService extends WallpaperService {
    static int mWallpaperWidth;
    static int mWallpaperHeight;
    static int mViewWidth;
    static int mViewHeight;
    private Bitmap mSceneBitmap = null;
    private int mFrameRate = 20;

    @Override
    public void onCreate() {
        super.onCreate();

        // Get wallpaper width and height
        WallpaperManager wpm = WallpaperManager.getInstance
                (getApplicationContext());
        mWallpaperWidth = wpm.getDesiredMinimumWidth();
        mWallpaperHeight = wpm.getDesiredMinimumHeight();
    }

    @Override
    public Engine onCreateEngine() {
        return new MyEngine();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    void setSceneBackground() {
        Bitmap b = BitmapFactory.decodeResource(getResources(),
                R.drawable.bg);

        if (null != mSceneBitmap)
            mSceneBitmap.recycle();

        Matrix m = new Matrix();
        m.setScale((float) mWallpaperWidth / (float) b.getWidth(),
                (float) mWallpaperHeight / (float) b.getHeight());
        mSceneBitmap = Bitmap.createBitmap(b, 0, 0,
                b.getWidth(), b.getHeight(), m, true);

        b.recycle();
    }

    class MyEngine extends Engine {
        private final Handler mHandler = new Handler();
        private float mOffset = 0.0f;
        private final Paint mPaint = new Paint();
        private final Runnable mDrawThread = new Runnable() {
            public void run() {
                drawFrame();
                try {
                    Thread.sleep(50);
                } catch (Exception e) {
                }
            }
        };
        private boolean mVisible;

        MyEngine() {
            setSceneBackground();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            setTouchEventsEnabled(true);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(mDrawThread);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
                drawFrame();
            } else {
                mHandler.removeCallbacks(mDrawThread);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder,
                                     int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);

            mViewWidth = width;
            mViewHeight = height;

            drawFrame();
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mDrawThread);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset,
                                     float xStep, float yStep, int xPixels, int yPixels) {
            super.onOffsetsChanged(xOffset, yOffset, xStep, yStep,
                    xPixels, yPixels);

            mOffset = 1.0f * xPixels;

            drawFrame();
        }

        void drawFrame() {
            final SurfaceHolder holder = getSurfaceHolder();
            final Rect frame = holder.getSurfaceFrame();
            final int width = frame.width();
            final int height = frame.height();
            Canvas c = null;

            try {
                c = holder.lockCanvas();
                if (c != null) {
                    c.drawBitmap(mSceneBitmap, mOffset, 00.0f, null);

                    //TODO: Draw here
                }
            } finally {
                if (c != null) holder.unlockCanvasAndPost(c);
            }

            mHandler.removeCallbacks(mDrawThread);
            if (mVisible) {
                mHandler.postDelayed(mDrawThread, 1000 /
                        mFrameRate);
            }
        }
    }
}