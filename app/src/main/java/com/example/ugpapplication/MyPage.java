package com.example.ugpapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ugpapplication.Database.AppDB;
import com.example.ugpapplication.Database.UGCDao;

import java.util.ArrayList;
import java.util.List;

public class MyPage extends AppCompatActivity implements OnClickListener{

    RecyclerView recyclerView;
    ArrayList<UGCModel> arrayList;
    UgcViewModel ugcViewModel;
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        recyclerView=findViewById(R.id.recyclerviewforlistugcinmypage);
        adapter=new MainAdapter();
        recyclerView.setAdapter(adapter);
        registerForContextMenu(recyclerView);
        ugcViewModel = new ViewModelProvider(this).get(UgcViewModel.class);
        ugcViewModel.getAllUGCs().observe(this, new Observer<List<UGCModel>>() {
            @Override
            public void onChanged(List<UGCModel> ugcModels) {
                adapter.setUgcModels(ugcModels);
            }
        });


    }

    public void GoBackToMenu(View view) {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }

    @Override
    public void onClick(UGCModel ugcModel) {
        ugcViewModel.delete(ugcModel);
    }
}