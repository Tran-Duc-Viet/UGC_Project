package com.example.ugpapplication.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomDatabase;
import androidx.room.Update;

import com.example.ugpapplication.UGCModel;

import java.util.List;

@Dao
public interface UGCDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(UGCModel ugcModel);

    @Query("SELECT * FROM UGC_TABLE ORDER BY date DESC")
    LiveData<List<UGCModel>> getAll();

    @Delete
    void deleteUGC(UGCModel ugcModel);


}
