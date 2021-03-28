package com.example.articletracker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ArticleDao {

    @Insert
    void insertArticle(Article article);

    @Update
    void updateArticle(Article article);

    @Delete
    void deleteArticle(Article article);

    @Query("DELETE FROM article_table")
    void deleteAllArticles();

    @Query("SELECT * FROM article_table")
    LiveData<List<Article>>getAllArticles();

}
