package com.vangogh.downloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.vangogh.downloader.utilities.ImageUtils;
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

        Downloader downloader = new Downloader(url, result);

        manageDownloader(downloader);
    }

    @Override
    public void cancel() {
        downloader.cancel();
    }

    public void toImageView(String url, final ImageView view) {

        download(url, new Downloader.ResultCallback() {
            @Override
            public void onStarted(Downloader downloader, final String encodedUrl) {
                // TODO : Do something before download finished
                Log.d(ImageDownloader.class.getSimpleName(), encodedUrl+" starting to download image from: "+downloader.getUrl());

                // Read from cache if exist
                if (cachedData.get(encodedUrl) != null) {
                    handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            byte[] data = cachedData.get(encodedUrl);
                            ImageUtils.setImage(data, view);
                        }
                    });
                }
            }

            @Override
            public void onFinished(final byte[] data, final String encodedUrl) {
                handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Re-render with the newest one
                        ImageUtils.setImage(data, view);

                        // Cache data if this byte never been cached
                        if (cachedData.get(encodedUrl) == null) {
                            cacheByteData(encodedUrl, data);
                        }

                        if (downloaders.get(encodedUrl) != null) {
                            downloaders.remove(encodedUrl);

                            Log.d(ImageDownloader.class.getSimpleName(), "Removing "+encodedUrl+" from worker pool");
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
