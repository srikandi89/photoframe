package com.vangogh.downloader;

import java.io.*;
import java.net.URL;

public class Downloader extends Thread {
    private String url;
    private boolean running;
    private ResultCallback callback;

    private static final int BUFFER_SIZE = 1024;

    public Downloader(String url, ResultCallback callback) {
        this.url = url;
        this.callback = callback;
        this.running = true;
    }

    @Override
    public void run() {
        super.run();

        download(callback);
    }

    /**
     * Download the file by from specified target URL
     * Given ResultCallback as the parameter to retrieve the result asynchronously
     * @param callback
     */
    private void download(ResultCallback callback) {
        try {
            BufferedInputStream inputStream = new BufferedInputStream(new URL(url).openStream());

            // Change downloaded file to the preferred location
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;


            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            while ((bytesRead = inputStream.read(buffer, 0, BUFFER_SIZE)) != -1 && running) {
                bos.write(buffer, 0, bytesRead);
            }

            running = false;

            byte[] data = bos.toByteArray();

            callback.onFinished(data);

            inputStream.close();
            bos.close();
        }
        catch (IOException e) {
            callback.onFailed(e);
        }
    }

    /**
     * Stop the loop and intercept the thread
     */
    public void cancel() {
        running = false;
    }

    /**
     * Convert file to array of byte
     * @param file with specified full file path
     * @return array of byte
     */
    private byte[] readFileToByteArray(File file){
        FileInputStream fis = null;
        // Creating a byte array using the length of the file
        // file.length returns long which is cast to int
        byte[] bArray = new byte[(int) file.length()];
        try{
            fis = new FileInputStream(file);
            fis.read(bArray);
            fis.close();

        }catch(IOException ioExp){
            ioExp.printStackTrace();
        }
        return bArray;
    }

    public interface ResultCallback {
        void onFinished(byte[] data);
        void onFailed(IOException e);
    }
}
