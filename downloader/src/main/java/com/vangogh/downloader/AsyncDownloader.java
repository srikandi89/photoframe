package com.vangogh.downloader;

import android.os.AsyncTask;
import android.util.Log;

import com.vangogh.downloader.utilities.StringUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

public class AsyncDownloader extends AsyncTask<byte[], Void, byte[]> {
    private String url;
    private AsyncResultCallback callback;
    private boolean running;
    private BufferedInputStream inputStream;
    private ByteArrayOutputStream bos;
    private static final int BUFFER_SIZE = 1024;

    public AsyncDownloader(String url, AsyncResultCallback callback) {
        this.url = url;
        this.callback = callback;
        this.running = true;
    }

    public String getUrl() {
        return url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        callback.onStarted(AsyncDownloader.this, StringUtils.toMD5(url));
    }

    @Override
    protected byte[] doInBackground(byte[]... bytes) {
        download();

        byte[] data = bos.toByteArray();

        return data;
    }

    @Override
    protected void onPostExecute(byte[] s) {
        try {
            super.onPostExecute(s);

            Log.d(AsyncDownloader.class.getSimpleName(), "Byte data :"+s);

            callback.onFinished(s, StringUtils.toMD5(url));

            inputStream.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Download the file by from specified target URL
     * Given ResultCallback as the parameter to retrieve the result asynchronously
     */
    private void download() {
        try {
            inputStream = new BufferedInputStream(new URL(url).openStream());

            // Change downloaded file to the preferred location
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            bos = new ByteArrayOutputStream();

            while ((bytesRead = inputStream.read(buffer, 0, BUFFER_SIZE)) != -1 && running) {
                bos.write(buffer, 0, bytesRead);
            }

            running = false;

        }
        catch (IOException e) {
            callback.onFailed(e);
        }
    }

    public interface AsyncResultCallback {
        void onStarted(AsyncDownloader downloader, String encodedUrl);
        void onFinished(byte[] data, String encodedUrl);
        void onFailed(IOException e);
    }
}
