package com.example.ugpapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<UGCModel> ugcModels=new ArrayList<>();
    Context context;
    View view;

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PhotoAdapter photoAdapter;

    private Button goBack;




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ugc,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UGCModel currentUGC=ugcModels.get(position);
        holder.time.setText(currentUGC.getDate());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyPage)context).onClick(currentUGC);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                View detailView=LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.detail_ugc_screen,null);
                viewPager=detailView.findViewById(R.id.image_slider);
                circleIndicator=detailView.findViewById(R.id.circle_center);
                goBack=detailView.findViewById(R.id.buttonGoBackToPreviousScreen);
                int pos= holder.getAdapterPosition();
                photoAdapter=new PhotoAdapter(detailView.getContext(), getListPhoto(pos));
                viewPager.setAdapter(photoAdapter);
                circleIndicator.setViewPager(viewPager);
                photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
                builder.setView(detailView);
                AlertDialog dialog=builder.create();
                goBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


    }

    private ArrayList<Photo> getListPhoto(int position) {
        ArrayList<Photo> photoArrayList=new ArrayList<Photo>();
        UGCModel model=ugcModels.get(position);
        photoArrayList.add(new Photo(model.getPicture1()));
        photoArrayList.add(new Photo(model.getPicture2()));
        photoArrayList.add(new Photo(model.getPicture3()));

        return photoArrayList;
    }

    @Override
    public int getItemCount() {
        return ugcModels.size();
    }

    public void setUgcModels(List<UGCModel> ugcModels){
        this.ugcModels=ugcModels;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pinPoint;
        TextView time;
        Button deleteButton;
        public LinearLayout cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pinPoint=itemView.findViewById(R.id.pinpoint);
            time=itemView.findViewById(R.id.date_time_of_location_dot);
            deleteButton=itemView.findViewById(R.id.deleteButton);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }


}
