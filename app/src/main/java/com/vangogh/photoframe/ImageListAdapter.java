package com.vangogh.photoframe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vangogh.downloader.AsyncImageDownloader;
import com.vangogh.downloader.ImageDownloader;
import com.vangogh.photoframe.models.ImageContent;

import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MyViewHolder> {
    private List<ImageContent> contents;
    private Context context;
    private ImageDownloader imageDownloader;

    public ImageListAdapter(Context context, List<ImageContent> contents, ImageDownloader imageDownloader) {
        this.context = context;
        this.contents = contents;
        this.imageDownloader = imageDownloader;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.content_list_item, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        ImageContent content = contents.get(i);
        Log.d(ImageListAdapter.class.getSimpleName(), "IMAGE #"+i);

        imageDownloader.toImageView(content.getUrl(), holder.imageView);
    }

    @Override
    public int getItemCount() {
        return contents != null ? contents.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
