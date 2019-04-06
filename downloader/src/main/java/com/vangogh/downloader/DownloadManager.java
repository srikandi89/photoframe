package com.vangogh.downloader;

import java.util.ArrayList;
import java.util.List;
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

    public abstract void cancel();

    protected void cacheByteData(String key, byte[] data) {
        if(getTotalCachedDataSize() <= maxTotalBytes) {
            cachedData.put(key, data);
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
