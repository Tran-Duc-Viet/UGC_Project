package com.example.ugpapplication.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.ugpapplication.UGCModel;

@Database(entities = {UGCModel.class}, exportSchema = false,version = 2)
public abstract class AppDB extends RoomDatabase {
    public static final String Database_name="ugc_db1.db";
    public static AppDB instance;
    public abstract UGCDao ugcDao();
    public static synchronized AppDB getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(), AppDB.class,Database_name).fallbackToDestructiveMigration().build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };





}
