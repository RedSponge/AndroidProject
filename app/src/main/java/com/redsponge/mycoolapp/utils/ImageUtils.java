package com.redsponge.mycoolapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class ImageUtils {

    /**
     * Decodes a base64 string into a {@link Bitmap}
     * @param base64 The base64 string to decode (usually received from {@link com.redsponge.mycoolapp.db.DatabaseHandler#getIcon(int)}
     * @return The bitmap
     */
    public static Bitmap decode(String base64) {
        byte[] arr = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(arr, 0, arr.length);
    }

    /**
     * Scales an image down to be stored in the database and later displayed
     * Unless needed the source bitmap should be recycled with {@link Bitmap#recycle()}
     * @param bmp The bitmap to scale down
     * @return The scaled down bitmap
     */
    public static Bitmap scaleDown(Bitmap bmp) {
        float scalar = 1;
        while(bmp.getWidth() * scalar > Constants.MAX_IMAGE_SIZE) {
            scalar -= 0.01f;
        }
        return Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * scalar), (int) (bmp.getHeight() * scalar), false);
    }

    /**
     * Encodes a bitmap into a base64 string to store in the database
     * @param bmp The bitmap to encode
     * @return The encoded string
     */
    public static String encode(Bitmap bmp) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArr = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteArr, Base64.DEFAULT);
    }

}
