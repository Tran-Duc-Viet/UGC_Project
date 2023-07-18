package com.example.ugpapplication;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
@Entity(tableName = "UGC_TABLE")
public class UGCModel {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "latitude")
    private Double latitude;
    @ColumnInfo(name = "longtitude")
    private Double longitude;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "category")
    private String category;
    @ColumnInfo(name = "memo")
    private String memo;
    @ColumnInfo(name = "image1")
    private String picture1;
    @ColumnInfo(name = "image2")
    private String picture2;
    @ColumnInfo(name = "image3")
    private String picture3;

    public UGCModel( Double latitude, Double longitude, String date, String category, String memo, String picture1, String picture2, String picture3) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.category = category;
        this.memo = memo;
        this.picture1 = picture1;
        this.picture2 = picture2;
        this.picture3 = picture3;
    }



    public void setPicture1(String picture1) {
        this.picture1 = picture1;
    }

    public void setPicture2(String picture2) {
        this.picture2 = picture2;
    }

    public void setPicture3(String picture3) {
        this.picture3 = picture3;
    }

    public String getPicture1() {
        return picture1;
    }

    public String getPicture2() {
        return picture2;
    }

    public String getPicture3() {
        return picture3;
    }




    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
