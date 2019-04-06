package com.vangogh.photoframe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.vangogh.downloader.ImageDownloader;

public class DoubleImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_image);

        ImageView imageView1 = findViewById(R.id.imageView1);
        ImageView imageView2 = findViewById(R.id.imageView2);

        String url1 = "https://images.unsplash.com/photo-1464550883968-cec281c19761?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=entropy\\u0026w=200\\u0026fit=max\\u0026s=9fba74be19d78b1aa2495c0200b9fbce";
        String url2 = "https://images.unsplash.com/profile-1464495186405-68089dcd96c3?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=faces\\u0026fit=crop\\u0026h=32\\u0026w=32\\u0026s=63f1d805cffccb834cf839c719d91702";

        ImageDownloader downloader = new ImageDownloader();
        downloader.toImageView(url1, imageView1);
        downloader.toImageView(url2, imageView2);
    }
}
