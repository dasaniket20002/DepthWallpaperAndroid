package com.dasaniket20002.utils;

import android.graphics.Bitmap;

public class BitmapUtils {

    public static Bitmap getScreenSizeBitmap(Bitmap originalMap) {
        return getScaledBitmap(originalMap, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
    }

    public static Bitmap getScaledBitmap(Bitmap originalMap, int desiredWidth, int desiredHeight) {
        int outHeight = desiredHeight;
        int outWidth = (originalMap.getWidth() * desiredHeight) / originalMap.getHeight();

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalMap, outWidth, outHeight, false);

        int overflowWidth = outWidth - desiredWidth;

        Bitmap returnImg = Bitmap.createBitmap(resizedBitmap, overflowWidth / 2, 0, outWidth - overflowWidth / 2, outHeight);

        resizedBitmap.recycle();
        return returnImg;
    }

}
