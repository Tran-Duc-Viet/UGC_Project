package com.example.ugpapplication;

import android.net.Uri;
import android.widget.ImageView;

public class ImageInfo {
    ImageView imageView;
    String imageURI;

    public ImageInfo(ImageView imageView) {
        this.imageView = imageView;
    }

    public ImageInfo(){

    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }
}
