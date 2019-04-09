package com.vangogh.photoframe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int i) {
        final ImageContent content = contents.get(i);
        Log.d(ImageListAdapter.class.getSimpleName(), "IMAGE #"+i);

        imageDownloader.toImageView(content.getUrl(), holder.imageView);

        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.cancelBtn.getText().toString().toLowerCase().equals("cancel")) {
                    Log.d(ImageListAdapter.class.getSimpleName(), "Download Image from "+content.getUrl()+" about to stop");

                    imageDownloader.cancel(content.getUrl());

                    holder.cancelBtn.setText("Restart");
                }
                else if (holder.cancelBtn.getText().toString().toLowerCase().equals("restart")) {
                    Log.d(ImageListAdapter.class.getSimpleName(), "Download image from "+content.getUrl()+" about to started");
                    imageDownloader.toImageView(content.getUrl(), holder.imageView);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return contents != null ? contents.size() : 0;
    }

    /**
     * Use static to keep each of items will displayed consistently
     */
    static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public Button cancelBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            cancelBtn = itemView.findViewById(R.id.btn_cancel);
        }
    }
}
