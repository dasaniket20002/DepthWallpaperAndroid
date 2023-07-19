package com.dasaniket20002.utils;

import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class ScreenUtils {

    private static int SCREEN_WIDTH, SCREEN_HEIGHT;

    public static void init(AppCompatActivity activity) {
        WindowManager windowManager = activity.getWindowManager();
        final Display display = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        display.getRealSize(outPoint);
        if (outPoint.y > outPoint.x) {
            SCREEN_HEIGHT = outPoint.y;
            SCREEN_WIDTH = outPoint.x;
        } else {
            SCREEN_HEIGHT = outPoint.x;
            SCREEN_WIDTH = outPoint.y;
        }
    }

    public static int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    public static int getScreenHeight() {
        return SCREEN_HEIGHT;
    }
}
