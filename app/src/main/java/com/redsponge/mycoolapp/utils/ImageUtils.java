package com.redsponge.mycoolapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class ImageUtils {

    public static Bitmap decode(String base64) {
        byte[] arr = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(arr, 0, arr.length);
    }

    public static Bitmap scaleDown(Bitmap bmp) {
        float scalar = 1;
        while(bmp.getWidth() * scalar > Constants.MAX_IMAGE_SIZE) {
            scalar -= 0.01f;
        }
        return Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scalar), (int) (bmp.getHeight() * scalar), false);
    }

    public static String encode(Bitmap bmp) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArr = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteArr, Base64.DEFAULT);
    }

}
