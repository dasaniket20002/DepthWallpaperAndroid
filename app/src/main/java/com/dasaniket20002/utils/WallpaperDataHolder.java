package com.dasaniket20002.utils;

import android.graphics.Bitmap;

public class WallpaperDataHolder {

    private static WallpaperDataHolder instance = null;
    public static WallpaperDataHolder getInstance() {
        if(instance == null) instance = new WallpaperDataHolder();
        return instance;
    }

    public static final int FONT_SIZE = 230;
    private float clockOffsetX, clockOffsetY;

    private Bitmap bg, fg;

    public WallpaperDataHolder() {
        this.clockOffsetX = 0;
        this.clockOffsetY = 0;
        bg = fg = null;
    }

    public void setClockOffsetX(float clockOffsetX) {
        this.clockOffsetX = clockOffsetX;
    }

    public void setClockOffsetY(float clockOffsetY) {
        this.clockOffsetY = clockOffsetY;
    }

    public float getClockOffsetX() {
        return clockOffsetX;
    }

    public float getClockOffsetY() {
        return clockOffsetY;
    }

    public Bitmap getBG() {
        return bg;
    }

    public void setBG(Bitmap bg) {
        this.bg = bg;
    }

    public Bitmap getFG() {
        return fg;
    }

    public void setFG(Bitmap fg) {
        this.fg = fg;
    }

    public void onDestroy() {
        if(bg != null) bg.recycle();
        if(fg != null) fg.recycle();
    }
}
