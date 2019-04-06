package com.vangogh.downloader;

import com.vangogh.downloader.utilities.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

public abstract class DownloadManager {
    protected int maxThread;
    protected int maxTotalBytes;

    // Considered as priority-based, so this collection will use list-based collection (thread safe)
    protected LinkedBlockingDeque<Downloader> downloaderQueue;
    protected ConcurrentHashMap<String, byte[]> cachedData;
    protected ConcurrentHashMap<String, Downloader> downloaders;

    public DownloadManager(int maxThread, int maxTotalBytes) {
        this.maxThread = maxThread;
        this.maxTotalBytes = maxTotalBytes;
        cachedData = new ConcurrentHashMap<>();
        downloaders = new ConcurrentHashMap<>();
        downloaderQueue = new LinkedBlockingDeque<>();
    }

    public abstract void download(String url, Downloader.ResultCallback result);

    public void cancel(String url) {
        String encodedUrl = StringUtils.toMD5(url);
        if (downloaders.get(encodedUrl) != null) {
            Downloader downloader = downloaders.get(encodedUrl);
            downloader.cancel();
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
    protected void manageDownloader(Downloader downloader) {
        String encodedUrl = StringUtils.toMD5(downloader.getUrl());

        if (downloaders.size() >= maxThread) {
            downloaderQueue.add(downloader);
        }
        else {
            downloaders.put(encodedUrl, downloader);
        }

        if (downloaders.size() > 0){
            Downloader worker = downloaders.remove(encodedUrl);
            worker.start();

            if (downloaderQueue.size() > 0) {
                Downloader fromQueue = downloaderQueue.pop();
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
