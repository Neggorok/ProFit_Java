package com.example.profit_java;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class Util {

    public static Bitmap getBitmapFromDrawable(Activity activity, int drawableResId){
        Bitmap bitmap = BitmapFactory.decodeResource(activity.getApplicationContext().getResources(),
                drawableResId);
        return bitmap;
    }

    public static byte[] getBitmapData(Bitmap bitmap) {
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, blob);

        byte[] bitmapdata = blob.toByteArray();
        //System.out.println(blob.toByteArray());


        try {
            blob.close();
            blob = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmapdata; //als byte []
    }

    public static String getBase64StringFromBitmap(Bitmap bitmap) {
        String base64StringOfBitmap = Base64.encodeToString(getBitmapData(bitmap), 1); // die 1 ist ein Flag
        return base64StringOfBitmap;
    }

    public static Bitmap getBitmapFromBase64String(String base64String) {
        byte[] decoded = Base64.decode(base64String, Base64.DEFAULT); // die 1 ist ein Flag
        Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length); //Returns the decoded Bitmap, or null if the image could not be decoded.
        return bitmap;
    }
}
