package com.dasaniket20002.main;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.service.wallpaper.WallpaperService;
import android.text.format.DateUtils;
import android.view.SurfaceHolder;

import com.dasaniket20002.utils.ScreenUtils;
import com.dasaniket20002.utils.WallpaperDataHolder;

import java.util.Calendar;
public class LiveWallpaperService extends WallpaperService {
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
        private final Runnable mDrawThread = this::drawFrame;
        private boolean mVisible;
        private final Paint textPaintGraphics;

        MyEngine() {
            textPaintGraphics = new Paint();
            textPaintGraphics.setColor(Color.WHITE);
            textPaintGraphics.setStyle(Paint.Style.FILL);
            textPaintGraphics.setTextSize(WallpaperDataHolder.FONT_SIZE);
            textPaintGraphics.setTextAlign(Paint.Align.CENTER);

            Typeface bold = getResources().getFont(R.font.poppins_bold);
            textPaintGraphics.setTypeface(bold);
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

        void drawFrame() {
            final SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;

            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    canvas.drawBitmap(WallpaperDataHolder.getInstance().getBG(), 0f, 0f, null);
                    drawClock(canvas);
                    canvas.drawBitmap(WallpaperDataHolder.getInstance().getFG(), 0f, 0f, null);
                }
            } finally {
                if (canvas != null) holder.unlockCanvasAndPost(canvas);
            }

            mHandler.removeCallbacks(mDrawThread);
            if (mVisible) {
                mHandler.postDelayed(mDrawThread, DateUtils.MINUTE_IN_MILLIS - System.currentTimeMillis() % DateUtils.MINUTE_IN_MILLIS);
            }
        }

        private void drawClock(Canvas canvas) {
            int width = ScreenUtils.getScreenWidth();
            int height = ScreenUtils.getScreenHeight();

            float displacementX = WallpaperDataHolder.getInstance().getClockOffsetX() / 100.0f * width;
            float displacementY = WallpaperDataHolder.getInstance().getClockOffsetY() / 100.0f * height;

            int drawX = Math.round(width / 2.0f + displacementX / 2.0f);
            int drawY = Math.round(height / 2.0f + displacementY / 2.0f);

            int h = Calendar.getInstance().get(Calendar.HOUR);
            int m = Calendar.getInstance().get(Calendar.MINUTE);

            String hh = String.format("%02d", h);
            String mm = String.format("%02d", m);

            canvas.drawText(hh, drawX, drawY, textPaintGraphics);
            canvas.drawText(mm, drawX, drawY + WallpaperDataHolder.FONT_SIZE, textPaintGraphics);
        }
    }
}