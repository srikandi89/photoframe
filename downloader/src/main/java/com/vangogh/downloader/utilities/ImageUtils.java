package com.vangogh.downloader.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class ImageUtils {
    public static void setImage(byte[] data, ImageView view) {
        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        view.setImageBitmap(Bitmap.createScaledBitmap(
                bmp,
                view.getWidth(),
                view.getHeight(),
                false)
        );
    }
}
