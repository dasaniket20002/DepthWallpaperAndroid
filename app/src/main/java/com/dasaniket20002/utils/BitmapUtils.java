package com.dasaniket20002.utils;

import android.graphics.Bitmap;

public class BitmapUtils {

    public static Bitmap getScreenSizeBitmap(Bitmap originalMap) {
        int outHeight = ScreenUtils.getScreenHeight();
        int outWidth = (originalMap.getWidth() * ScreenUtils.getScreenHeight()) / originalMap.getHeight();

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalMap, outWidth, outHeight, false);

        int overflowWidth = outWidth - ScreenUtils.getScreenWidth();

        Bitmap returnImg = Bitmap.createBitmap(resizedBitmap, overflowWidth / 2, 0, outWidth - overflowWidth / 2, outHeight);

        resizedBitmap.recycle();
        return returnImg;
    }

}
