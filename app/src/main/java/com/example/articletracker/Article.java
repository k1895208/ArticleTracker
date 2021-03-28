package com.example.articletracker;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "article_table")
public class Article {
    @PrimaryKey(autoGenerate = true)
    private int id ;
    private String title;
    private String URL;
    private String text;

    public Article(String title, String URL, String text) {
        this.title = title;
        this.URL = URL;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
