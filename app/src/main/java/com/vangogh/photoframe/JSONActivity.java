package com.vangogh.photoframe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.vangogh.downloader.DocumentDownloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONActivity extends AppCompatActivity {

    private DocumentDownloader downloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);

        String url = "http://pastebin.com/raw/wgkJgazE";

        downloader = new DocumentDownloader();
        downloader.toJSON(url, new DocumentDownloader.DocumentResponse<JSONArray>() {
            @Override
            public void onStart() {
                Log.d(JSONActivity.class.getSimpleName(), "*** Start Downloading ... ***");
            }

            @Override
            public void onSuccess(String raw, JSONArray response) {
                Log.d(JSONActivity.class.getSimpleName(), raw);

                try {
                    Log.d(JSONActivity.class.getSimpleName(), response.get(0).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }
}
