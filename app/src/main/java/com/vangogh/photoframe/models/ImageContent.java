package com.vangogh.photoframe.models;

public class ImageContent {
    private String url;

    public ImageContent() {}

    public ImageContent(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
