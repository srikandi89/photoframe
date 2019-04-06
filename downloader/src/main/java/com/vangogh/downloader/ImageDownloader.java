package com.vangogh.downloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;

public class ImageDownloader extends DownloadManager {
    private Downloader downloader;
    private String sourceUrl;
    private Context context;
    private Handler handler;
    private static final int BYTE_SIZE = 1024;
    private static final int MAX_THREAD = 10;
    private static final int MAX_TOTAL_BYTE = 500*BYTE_SIZE;

    public ImageDownloader() {
        super(MAX_THREAD, MAX_TOTAL_BYTE);
    }

    public ImageDownloader(int maxThread, int maxTotalBytes) {
        super(maxThread, maxTotalBytes);
    }

    @Override
    public void download(String url, Downloader.ResultCallback result) {
        downloader = new Downloader(url, result);
        downloader.start();
    }

    @Override
    public void cancel() {
        downloader.cancel();
    }

    public void toImageView(String url, final ImageView view) {

        download(url, new Downloader.ResultCallback() {
            @Override
            public void onFinished(final byte[] data) {
                handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        view.setImageBitmap(Bitmap.createScaledBitmap(
                                bmp,
                                view.getWidth(),
                                view.getHeight(),
                                false)
                        );
                    }
                });
            }

            @Override
            public void onFailed(IOException e) {
                Log.d("GOGH_DOWNLOADER", e.getMessage());
            }
        });
    }
}
