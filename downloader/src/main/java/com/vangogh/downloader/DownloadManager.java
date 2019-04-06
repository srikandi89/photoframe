package com.vangogh.downloader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class DownloadManager {
    protected int maxThread;
    protected int maxTotalBytes;
    protected ConcurrentHashMap<String, byte[]> cachedData;
    protected List<Downloader> downloaders;

    public DownloadManager(int maxThread, int maxTotalBytes) {
        this.maxThread = maxThread;
        this.maxTotalBytes = maxTotalBytes;
        cachedData = new ConcurrentHashMap<>();
        downloaders = new ArrayList<>();
    }

    public abstract void download(String url, Downloader.ResultCallback result);

    public abstract void cancel();
}
