package com.vangogh.downloader;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.vangogh.downloader.utilities.ImageUtils;

import java.io.IOException;

public class AsyncImageDownloader extends AsyncDownloadManager {
    private Downloader downloader;
    private String sourceUrl;
    private Handler handler;
    private static final int BYTE_SIZE = 1024;
    private static final int DEFAULT_MAX_THREAD = 10;
    private static final int DEFAULT_MAX_TOTAL_BYTE = 500*BYTE_SIZE;

    public AsyncImageDownloader() {
        super(DEFAULT_MAX_THREAD, DEFAULT_MAX_TOTAL_BYTE);
    }

    public AsyncImageDownloader(int maxThread, int maxTotalBytes) {
        super(maxThread, maxTotalBytes);
    }

    @Override
    public void download(String url, AsyncDownloader.AsyncResultCallback result) {
        this.sourceUrl = url;

        AsyncDownloader downloader = new AsyncDownloader(url, result);

        manageDownloader(downloader);
    }

    public void toImageView(String url, final ImageView view) {

        download(url, new AsyncDownloader.AsyncResultCallback() {
            @Override
            public void onStarted(AsyncDownloader downloader, final String encodedUrl) {
                Log.d(AsyncImageDownloader.class.getSimpleName(), encodedUrl+" starting to download image from: "+downloader.getUrl());

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
            public void onFinished(byte[] data, String encodedUrl) {
                ImageUtils.setImage(data, view);

                // Cache data if this byte never been cached
                if (cachedData.get(encodedUrl) == null) {
                    cacheByteData(encodedUrl, data);
                }

                if (downloaders.get(encodedUrl) != null) {
                    downloaders.remove(encodedUrl);

                    Log.d(AsyncImageDownloader.class.getSimpleName(), "Removing "+encodedUrl+" from worker pool");
                }
            }

            @Override
            public void onFailed(IOException e) {
                Log.d(AsyncImageDownloader.class.getSimpleName(), e.getMessage());
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
