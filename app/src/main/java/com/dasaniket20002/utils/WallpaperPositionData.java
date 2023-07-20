package com.dasaniket20002.utils;

public class WallpaperPositionData {

    public static final int FONT_SIZE = 230;
    private float clockOffsetX, clockOffsetY;

    public WallpaperPositionData() {
        this.clockOffsetX = 0;
        this.clockOffsetY = 0;
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
}
