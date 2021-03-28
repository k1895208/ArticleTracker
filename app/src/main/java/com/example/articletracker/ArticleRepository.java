package com.example.articletracker;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.LiveData;


import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.ArrayList;
import java.util.List;

//Can't have anything about Trackers
public class ArticleRepository {
    private ArticleDao articleDao;
    //private TrackerDao trackerDao;
    private LiveData<List<Article>> allArticles;
    //private LiveData<List<Tracker>> allTrackers;

    public ArticleRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);

        articleDao = database.articleDao();
        allArticles = articleDao.getAllArticles();
    }

    public void insertArticle(Article article){
        new InsertArticleAsyncTask(articleDao).execute(article);
    }

    public void updateArticle(Article article){
        new UpdateArticleAsyncTask(articleDao).execute(article);
    }

    public void deleteArticle(Article article){
        new DeleteArticleAsyncTask(articleDao).execute(article);
    }

    public void deleteAllArticles(){
        new DeleteAllArticleAsyncTask(articleDao).execute();
    }

    public LiveData<List<Article>> getAllArticles(){
        return allArticles;
    }

    public void scrapWebArticles(Context context){
        new ScrapWebAsyncTask(context, articleDao).execute();
    }

    private static class InsertArticleAsyncTask extends AsyncTask<Article, Void, Void> {
        private ArticleDao articleDao;

        private InsertArticleAsyncTask(ArticleDao articleDao){
            this.articleDao = articleDao;
        }

        @Override
        protected Void doInBackground(Article... articles) {
            articleDao.insertArticle(articles[0]);
            return null;
        }
    }

    private static class UpdateArticleAsyncTask extends AsyncTask<Article, Void, Void> {
        private ArticleDao articleDao;

        private UpdateArticleAsyncTask(ArticleDao articleDao){
            this.articleDao = articleDao;

        }

        @Override
        protected Void doInBackground(Article... articles) {
            articleDao.updateArticle(articles[0]);
            return null;
        }
    }

    private static class DeleteArticleAsyncTask extends AsyncTask<Article, Void, Void> {
        private ArticleDao articleDao;

        private DeleteArticleAsyncTask(ArticleDao articleDao){
            this.articleDao = articleDao;

        }

        @Override
        protected Void doInBackground(Article... articles) {
            articleDao.deleteArticle(articles[0]);
            return null;
        }
    }

    private static class DeleteAllArticleAsyncTask extends AsyncTask<Void, Void, Void> {
        private ArticleDao articleDao;

        private DeleteAllArticleAsyncTask(ArticleDao articleDao){
            this.articleDao = articleDao;

        }

        @Override
        protected Void doInBackground(Void... voids) {
            articleDao.deleteAllArticles();
            return null;
        }
    }

    private static class ScrapWebAsyncTask extends AsyncTask<Void, Void, Void> {
        private ArticleDao articleDao;
        private Context context;
        private ProgressDialog dialog;



        private ScrapWebAsyncTask(Context context, ArticleDao articleDao){
            this.articleDao = articleDao;
            this.context = context;
            dialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Processing...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<PyObject> downloadedArticles = new ArrayList<>();
            List<String> papers = new ArrayList<>();
            if (!Python.isStarted()) {
                Python.start(new AndroidPlatform(context));
            }


            Python py = Python.getInstance();
            PyObject pythonObject = py.getModule("WebScraper");
            List<PyObject> pool = pythonObject.callAttr("pool").asList();

            //We will store each source locally. We can then scrap them without timing out.
            List<PyObject> temp = pythonObject.callAttr("get_papers").asList();
            for (PyObject paper : temp){papers.add(paper.toString());}
            pythonObject.callAttr("main");

            List<PyObject> titles = pythonObject.callAttr("get_titles").asList();
            List<PyObject> text = pythonObject.callAttr("get_texts").asList();
            List<PyObject> sources = pythonObject.callAttr("get_sources").asList();

            for (int i = 0; i < titles.size(); i++) {
                try {
                    String articleTitle = titles.get(i).toString();
                    String articleURL = sources.get(i).toString();
                    String articleText = text.get(i).toString();
                    articleDao.insertArticle(new Article(articleTitle, articleURL, articleText));
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            dialog.dismiss();
        }
    }

}
