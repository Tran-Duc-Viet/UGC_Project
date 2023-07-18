package com.example.ugpapplication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ugpapplication.Repository.AppRepo;

import java.util.List;

public class UgcViewModel extends AndroidViewModel {
    private AppRepo repo;
    private LiveData<List<UGCModel>> allUGCs;

    public UgcViewModel(@NonNull Application application) {
        super(application);
        repo=new AppRepo(application);
        allUGCs= repo.getAllUGC();
    }
    public void insert(UGCModel ugcModel){
        repo.insertUGC(ugcModel);
    }

    public void delete(UGCModel ugcModel){
        repo.deleteUGC(ugcModel);
    }

    public LiveData<List<UGCModel>> getAllUGCs() {
        return allUGCs;
    }
}
