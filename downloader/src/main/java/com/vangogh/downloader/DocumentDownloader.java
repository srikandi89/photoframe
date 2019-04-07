package com.vangogh.downloader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class DocumentDownloader extends DownloadManager {
    private static final int BYTE_SIZE = 1024;
    private static final int DEFAULT_MAX_THREAD = 10;
    private static final int DEFAULT_MAX_TOTAL_BYTE = 500*BYTE_SIZE;

    public DocumentDownloader() {
        super(DEFAULT_MAX_THREAD, DEFAULT_MAX_TOTAL_BYTE);
    }

    public DocumentDownloader(int maxThread, int maxTotalBytes) {
        super(maxThread, maxTotalBytes);
    }

    @Override
    public void download(String url, Downloader.ResultCallback result) {
        Downloader downloader = new Downloader(url, result);

        manageDownloader(downloader);
    }

    public void toJSON(String url, final DocumentResponse response) {
        download(url, new Downloader.ResultCallback() {
            @Override
            public void onStarted(Downloader downloader, String encodedUrl) {
                if (cachedData.get(encodedUrl) != null) {
                    cachedData.put(encodedUrl, null);
                }

                response.onStart();
            }

            @Override
            public void onFinished(byte[] data, String encodedUrl) {
                String rawResponse = new String(data);
                try {
                    JSONObject jsonResponse = new JSONObject(rawResponse);

                    response.onSuccess(rawResponse, jsonResponse);
                } catch (JSONException e) {
                    response.onFailed(e);
                }
            }

            @Override
            public void onFailed(IOException e) {
                response.onFailed(e);
            }
        });
    }

    public interface DocumentResponse {
        void onStart();
        void onSuccess(String raw, JSONObject jsonObject);
        void onFailed(Exception e);
    }
}
