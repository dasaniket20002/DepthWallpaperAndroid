package com.dasaniket20002.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.service.wallpaper.WallpaperService;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.SurfaceHolder;

import com.dasaniket20002.livewallpapertest01.R;
import com.dasaniket20002.utils.BitmapUtils;
import com.dasaniket20002.utils.ScreenUtils;

import java.util.Calendar;
public class LiveWallpaperService extends WallpaperService {
    private static final int TIME_FONT_SIZE = 230;
    private static final int TEXT_HEIGHT_OFFSET = 440;
    private Bitmap bgBitmap = null;
    private Bitmap fgBitmap = null;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public Engine onCreateEngine() {
        return new MyEngine();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class MyEngine extends Engine {
        private final Handler mHandler = new Handler(Looper.getMainLooper());
        private final Runnable mDrawThread = () -> drawFrame();
        private boolean mVisible;
        private final Paint textPaintGraphics;

        MyEngine() {
            textPaintGraphics = new Paint();
            textPaintGraphics.setColor(Color.WHITE);
            textPaintGraphics.setStyle(Paint.Style.FILL);
            textPaintGraphics.setTextSize(TIME_FONT_SIZE);
            textPaintGraphics.setTextAlign(Paint.Align.CENTER);

            Typeface bold = getResources().getFont(R.font.poppins_bold);
            textPaintGraphics.setTypeface(bold);

            setSceneBackground();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
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
                mHandler.post(mDrawThread);
            } else {
                mHandler.removeCallbacks(mDrawThread);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder,
                                     int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
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
            drawFrame();
        }

        private void setSceneBackground() {
            if (null != bgBitmap)
                bgBitmap.recycle();

            Bitmap originalMap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.bg);
            bgBitmap = BitmapUtils.getScreenSizeBitmap(originalMap);

            originalMap.recycle();

            originalMap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.fg);
            fgBitmap = BitmapUtils.getScreenSizeBitmap(originalMap);

            originalMap.recycle();
        }

        void drawFrame() {
            final SurfaceHolder holder = getSurfaceHolder();
            Canvas c = null;

            try {
                c = holder.lockCanvas();
                if (c != null) {
                    c.drawBitmap(bgBitmap, 0f, 0f, null);

                    int h = Calendar.getInstance().get(Calendar.HOUR);
                    int m = Calendar.getInstance().get(Calendar.MINUTE);

                    String hh = String.format("%02d", h);
                    String mm = String.format("%02d", m);

                    float drawX = ScreenUtils.getScreenWidth() / 2.0f;
                    float drawY = ScreenUtils.getScreenHeight() / 2.0f - TEXT_HEIGHT_OFFSET;

                    c.drawText(hh, drawX, drawY, textPaintGraphics);
                    c.drawText(mm, drawX, drawY + TIME_FONT_SIZE, textPaintGraphics);

                    Log.d("TIME", hh + ":" + mm);

                    c.drawBitmap(fgBitmap, 0f, 0f, null);
                }
            } finally {
                if (c != null) holder.unlockCanvasAndPost(c);
            }

            mHandler.removeCallbacks(mDrawThread);
            if (mVisible) {
                mHandler.postDelayed(mDrawThread, DateUtils.MINUTE_IN_MILLIS - System.currentTimeMillis() % DateUtils.MINUTE_IN_MILLIS);
            }
        }
    }
}