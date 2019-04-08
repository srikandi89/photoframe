package com.vangogh.downloader;

import android.util.Log;

import com.vangogh.downloader.utilities.TimeUtils;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Invalidator extends Thread {
    private static final long SECOND = 1000;
    private static final long MINUTE = 60*SECOND;
    private static final long LIFE_TIME_MILLIS = MINUTE;

    private boolean running;
    private DownloadManager manager;

    public Invalidator(DownloadManager manager) {
        this.manager = manager;
        this.running = true;
    }

    @Override
    public void run() {
        super.run();

        try {
            while (running) {
                invalidate(manager);
                Thread.sleep(MINUTE);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopInvalidator() {
        running = false;
    }

    private void invalidate(DownloadManager manager) {
        ConcurrentHashMap<String, Date> timeCached = manager.getTimeCachedData();

        for (Map.Entry<String, Date> entry : timeCached.entrySet()) {
            Date date = entry.getValue();
            String key = entry.getKey();

            long timeDiff = TimeUtils.timeDiffMillis(date, new Date());

            if (timeDiff > LIFE_TIME_MILLIS) {
                Log.d(Invalidator.class.getSimpleName(), "Remove data for "+key+" cache document");
                manager.getCachedData().remove(key);
            }
        }
    }
}
