package com.vangogh.downloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.vangogh.downloader.utilities.StringUtils;

import java.io.IOException;

public class ImageDownloader extends DownloadManager {
    private Downloader downloader;
    private String sourceUrl;
    private Context context;
    private Handler handler;
    private static final int BYTE_SIZE = 1024;
    private static final int DEFAULT_MAX_THREAD = 10;
    private static final int DEFAULT_MAX_TOTAL_BYTE = 500*BYTE_SIZE;

    public ImageDownloader() {
        super(DEFAULT_MAX_THREAD, DEFAULT_MAX_TOTAL_BYTE);
    }

    public ImageDownloader(int maxThread, int maxTotalBytes) {
        super(maxThread, maxTotalBytes);
    }

    @Override
    public void download(String url, Downloader.ResultCallback result) {

        if (downloaders.size() < DEFAULT_MAX_THREAD) {
            String downloaderId = StringUtils.toMD5(url);
            if (downloaders.get(downloaderId) == null) {
                downloader = new Downloader(url, result);
                downloader.start();
                downloaders.put(downloaderId, downloader);
            }
        }
        else {
            Log.i(ImageDownloader.class.getSimpleName(), "Thread capacity already full... Inserted to queue");
            downloaderQueue.addFirst(new Downloader(url, result));
        }
    }

    @Override
    public void cancel() {
        downloader.cancel();
    }

    public void toImageView(String url, final ImageView view) {

        download(url, new Downloader.ResultCallback() {
            @Override
            public void onStarted(Downloader downloader, String downloaderId) {
                // TODO : Do something before download finished
                Log.d(ImageDownloader.class.getSimpleName(), downloaderId+" starting to download image from: "+downloader.getUrl());
            }

            @Override
            public void onFinished(final byte[] data, final String downloaderId) {
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

                        if (downloaders.get(downloaderId) != null) {
                            downloaders.remove(downloaderId);

                            Log.d(ImageDownloader.class.getSimpleName(), "Removing "+downloaderId+" from worker pool");
                        }
                    }
                });
            }

            @Override
            public void onFailed(IOException e) {
                Log.d(ImageDownloader.class.getSimpleName(), e.getMessage());
            }
        });
    }

    public int getMaxThread() {
        return this.maxThread;
    }

    public int getMaxTotalBytes() {
        return this.maxTotalBytes;
    }
}
