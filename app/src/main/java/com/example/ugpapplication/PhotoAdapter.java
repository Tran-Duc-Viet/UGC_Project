package com.example.ugpapplication;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PhotoAdapter extends PagerAdapter {
    private Context mContext;
    ArrayList<Photo> mListPhoto;

    public PhotoAdapter(Context mContext, ArrayList<Photo> mListPhoto) {
        this.mContext = mContext;
        this.mListPhoto = mListPhoto;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_photo,container,false);
        ImageView imagePhoto=view.findViewById(R.id.image_Photo);
        Photo photo=mListPhoto.get(position);
        if(photo!=null){
            Glide.with(mContext).load(photo.getuRI()).into(imagePhoto);
        }

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return mListPhoto.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
