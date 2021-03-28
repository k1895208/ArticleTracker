package com.example.articletracker;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "tracker_table")
public class Tracker {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo
    private String URL;
    @ColumnInfo
    private String title;
    private List<Integer> relatedArticlesId;


    public Tracker(String URL, String title) {
        this.URL = URL;
        this.title = title;
        this.relatedArticlesId = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Integer> getRelatedArticlesId() {
        return relatedArticlesId;
    }

    public void setRelatedArticlesId(List<Integer> relatedArticlesId) {
        this.relatedArticlesId = relatedArticlesId;
    }

}
