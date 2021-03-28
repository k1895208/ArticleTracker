package com.example.articletracker;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ArticleViewModel extends AndroidViewModel {

    private ArticleRepository repository;
    private LiveData<List<Article>>  allArticles;

    public ArticleViewModel(@NonNull Application application) {
        super(application);
        repository = new ArticleRepository(application);
        allArticles = repository.getAllArticles();
        System.out.println(allArticles.getValue());

    }

    public void insertArticle(Article article){
        repository.insertArticle(article);
    }

    public void updateArticle(Article article){
        repository.updateArticle(article);
    }

    public void deleteArticle(Article article){
        repository.deleteArticle(article);
    }

    public void deleteAllArticles(){
        repository.deleteAllArticles();
    }

    public LiveData<List<Article>> getAllArticles() {
        return allArticles;
    }

    public void scrapWebArticles(Context context){
        repository.scrapWebArticles(context);
    }
}
