package com.vangogh.photoframe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vangogh.downloader.AsyncImageDownloader;
import com.vangogh.downloader.ImageDownloader;
import com.vangogh.photoframe.models.ImageContent;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private List<ImageContent> contents;
    private RecyclerView rvItems;
//    private AsyncImageDownloader imageDownloader;
    private ImageDownloader imageDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        rvItems = findViewById(R.id.rv_items);
        contents = new ArrayList<>();

        populateImages();

        imageDownloader = new ImageDownloader();

        ImageListAdapter adapter = new ImageListAdapter(this, contents, imageDownloader);
        GridLayoutManager llm = new GridLayoutManager(getApplicationContext(), 2);
        llm.setOrientation(RecyclerView.VERTICAL);
        rvItems.setLayoutManager(llm);
        rvItems.setItemAnimator(new DefaultItemAnimator());
        rvItems.setAdapter(adapter);
    }

    private void populateImages() {
        contents.add(new ImageContent("https://www.baanlaesuan.com/app/uploads/2016/09/141.jpg"));
        contents.add(new ImageContent("https://www.baanlaesuan.com/app/uploads/2016/09/02-4.jpg"));
        contents.add(new ImageContent("https://www.baanlaesuan.com/app/uploads/2016/09/01-5.jpg"));
        contents.add(new ImageContent("https://www.baanlaesuan.com/app/uploads/2016/09/141.jpg"));
        contents.add(new ImageContent("https://www.baanlaesuan.com/app/uploads/2016/09/mood.jpg"));
        contents.add(new ImageContent("https://www.baanlaesuan.com/app/uploads/2016/09/02-03.jpg"));
        contents.add(new ImageContent("https://www.baanlaesuan.com/app/uploads/2016/09/04-5.jpg"));
        contents.add(new ImageContent("https://i.pinimg.com/originals/bb/b4/a1/bbb4a12b56a15ee9c35a88ea039c3f47.jpg"));
        contents.add(new ImageContent("https://i.pinimg.com/originals/9f/61/b6/9f61b6c445763fa43a50a1ee73ada5aa.jpg"));
        contents.add(new ImageContent("https://i.pinimg.com/originals/e8/29/fa/e829fae1a1d8fd41d5612d57ff970e5f.jpg"));
        contents.add(new ImageContent("https://i.pinimg.com/originals/f0/ab/fa/f0abfa0c5718ee8c5f4d32665cf938e4.jpg"));
        contents.add(new ImageContent("https://i.pinimg.com/originals/f7/58/7c/f7587ca48f4d8130aa4f2aaa5df8d2b9.jpg"));
        contents.add(new ImageContent("https://i.pinimg.com/originals/ae/94/8f/ae948f2744e4da30c52c84c2ea122ddd.jpg"));
    }
}
