package com.vangogh.downloader;

import com.vangogh.downloader.utilities.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

public abstract class AsyncDownloadManager {
    protected int maxThread;
    protected int maxTotalBytes;

    // Considered as priority-based, so this collection will use list-based collection (thread safe)
    protected LinkedBlockingDeque<AsyncDownloader> downloaderQueue;
    protected ConcurrentHashMap<String, byte[]> cachedData;
    protected ConcurrentHashMap<String, AsyncDownloader> downloaders;

    public AsyncDownloadManager(int maxThread, int maxTotalBytes) {
        this.maxThread = maxThread;
        this.maxTotalBytes = maxTotalBytes;
        cachedData = new ConcurrentHashMap<>();
        downloaders = new ConcurrentHashMap<>();
        downloaderQueue = new LinkedBlockingDeque<>();
    }

    public abstract void download(String url, AsyncDownloader.AsyncResultCallback result);

    public void cancel(String url) {
        String encodedUrl = StringUtils.toMD5(url);
        if (downloaders.get(encodedUrl) != null) {
            AsyncDownloader downloader = downloaders.get(encodedUrl);
            downloader.cancel(true);
        }
    }

    protected void cacheByteData(String key, byte[] data) {
        if(getTotalCachedDataSize() <= maxTotalBytes) {
            cachedData.put(key, data);
        }
    }

    /**
     * check if downloader map reached its max limit.
     * put downloader to the queue if downloader list exceed its max thread limit.
     * remove downloader from list whenever its about to be executed
     * if downloaderQueue size > 0, then pop from the queue, add into downloader list
     * @param downloader
     */
    protected void manageDownloader(AsyncDownloader downloader) {
        String encodedUrl = StringUtils.toMD5(downloader.getUrl());

        if (downloaders.size() >= maxThread) {
            downloaderQueue.add(downloader);
        }
        else {
            downloaders.put(encodedUrl, downloader);
        }

        if (downloaders.size() > 0){
            AsyncDownloader worker = downloaders.remove(encodedUrl);
            worker.execute();

            if (downloaderQueue.size() > 0) {
                AsyncDownloader fromQueue = downloaderQueue.pop();
                downloaders.put(encodedUrl, fromQueue);
            }
        }
    }

    private int getTotalCachedDataSize() {
        int size = 0;

        if (cachedData != null) {
            for (Map.Entry<String, byte[]> entry : cachedData.entrySet()) {
                byte[] data = entry.getValue();

                for(int i=0; i<data.length; i++) {
                    size += data[i];
                }
            }
        }

        return size;
    }
}
