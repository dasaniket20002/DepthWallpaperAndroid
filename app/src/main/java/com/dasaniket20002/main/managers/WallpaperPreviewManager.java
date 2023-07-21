package com.dasaniket20002.main.managers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import androidx.appcompat.app.AppCompatActivity;

import com.dasaniket20002.main.MainActivity;
import com.dasaniket20002.main.R;
import com.dasaniket20002.utils.BitmapUtils;
import com.dasaniket20002.utils.ScreenUtils;
import com.dasaniket20002.utils.WallpaperDataHolder;
import com.google.android.material.imageview.ShapeableImageView;

public class WallpaperPreviewManager {

    private static final String HH = "01", MM = "45";
    private static final float SCALE_FACTOR = 0.7f;
    private int widthIV, heightIV;
    private final MainActivity activity;
    private final ShapeableImageView wallpaper_IV;

    private Bitmap wallpaperPreview;
    private Bitmap bg, fg;
    private Paint clockPaint;
    private boolean isXRayEnabled;

    public WallpaperPreviewManager(MainActivity activity, int resourceID) {
        this.activity = activity;
        this.wallpaper_IV = activity.findViewById(resourceID);
        isXRayEnabled = false;
    }

    public void setupWallpaperPreviewImageView() {
        wallpaper_IV.requestLayout();
        wallpaper_IV.getLayoutParams().width = widthIV =
                Math.round(ScreenUtils.getScreenWidth() * SCALE_FACTOR);
        wallpaper_IV.getLayoutParams().height = heightIV =
                Math.round(ScreenUtils.getScreenHeight() * SCALE_FACTOR);

        wallpaperPreview = Bitmap.createBitmap(
                wallpaper_IV.getLayoutParams().width,
                wallpaper_IV.getLayoutParams().height,
                Bitmap.Config.ARGB_8888
        );

        setupClockPaint();
        createIVGraphics();

        redraw();
    }

    public void createIVGraphics() {
        if(bg != null) bg.recycle();
        if(fg != null) fg.recycle();

        bg = BitmapUtils.getScaledBitmap(
                WallpaperDataHolder.getInstance().getBG(), SCALE_FACTOR);
        fg = BitmapUtils.getScaledBitmap(
                WallpaperDataHolder.getInstance().getFG(), SCALE_FACTOR);

    }

    public void redraw() {
        Canvas canvas = new Canvas(wallpaperPreview);

        canvas.drawBitmap(bg, 0, 0, null);
        drawClock(canvas, widthIV, heightIV);
        if(!isXRayEnabled) canvas.drawBitmap(fg, 0, 0, null);

        wallpaper_IV.setImageBitmap(wallpaperPreview);
    }

    private void drawClock(Canvas canvas, int width_IV, int height_IV) {
        float displacementX = WallpaperDataHolder.getInstance().getClockOffsetX() / 100.0f * width_IV;
        float displacementY = WallpaperDataHolder.getInstance().getClockOffsetY() / 100.0f * height_IV;

        int drawX = Math.round(width_IV / 2.0f + displacementX / 2.0f);
        int drawY = Math.round(height_IV / 2.0f + displacementY / 2.0f);

        canvas.drawText(HH, drawX, drawY, clockPaint);
        canvas.drawText(MM, drawX, drawY + (WallpaperDataHolder.FONT_SIZE * SCALE_FACTOR), clockPaint);
    }

    private void setupClockPaint() {
        clockPaint = new Paint();
        clockPaint.setColor(Color.WHITE);
        clockPaint.setStyle(Paint.Style.FILL);
        clockPaint.setTextSize(WallpaperDataHolder.FONT_SIZE * SCALE_FACTOR);
        clockPaint.setTextAlign(Paint.Align.CENTER);

        Typeface bold = activity.getResources().getFont(R.font.poppins_bold);
        clockPaint.setTypeface(bold);
    }

    public void setXRayEnabled(boolean b) {
        isXRayEnabled = b;
    }

    public void disposePreview() {
        wallpaperPreview.recycle();
    }

    public void onDestroy() {
        disposePreview();
        bg.recycle();
        fg.recycle();
    }
}
