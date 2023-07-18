package com.example.ugpapplication.Repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.ugpapplication.Database.AppDB;
import com.example.ugpapplication.Database.UGCDao;
import com.example.ugpapplication.UGCModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppRepo {
    private UGCDao ugcDao;
    private LiveData<List<UGCModel>> allUGCs;

    private Executor executor= Executors.newSingleThreadExecutor();

    public AppRepo(Application application){
        AppDB appDB=AppDB.getInstance(application);
        ugcDao=appDB.ugcDao();
        allUGCs=ugcDao.getAll();
    }


    public void insertUGC(UGCModel ugcModel){
        new InsertUGCAsyncTask(ugcDao).execute(ugcModel);


    }

    public void deleteUGC(UGCModel ugcModel){
        new DeleteUGCAsyncTask(ugcDao).execute(ugcModel);
    }

    public LiveData<List<UGCModel>> getAllUGC(){
        return allUGCs;
    }

    private static class InsertUGCAsyncTask extends AsyncTask<UGCModel, Void, Void>{
        private UGCDao ugcDao;
        private InsertUGCAsyncTask(UGCDao ugcDao){
            this.ugcDao=ugcDao;
        }

        @Override
        protected Void doInBackground(UGCModel... ugcModels) {
            ugcDao.insertAll(ugcModels[0]);
            return null;
        }
    }

    private static class DeleteUGCAsyncTask extends AsyncTask<UGCModel, Void, Void>{
        private UGCDao ugcDao;
        private DeleteUGCAsyncTask(UGCDao ugcDao){
            this.ugcDao=ugcDao;
        }

        @Override
        protected Void doInBackground(UGCModel... ugcModels) {
            ugcDao.deleteUGC(ugcModels[0]);
            return null;
        }
    }
}
