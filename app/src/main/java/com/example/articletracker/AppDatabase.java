package com.example.articletracker;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;


import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.List;

//For some reason, the app fails if I have more than one entity. No, it's fine if you uninstall and reinstall the app.
//Looks like you just can't have Tracker data in the repository.
@Database(entities = {Article.class, Tracker.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    private int scrapCount;

    public abstract ArticleDao articleDao();
    public abstract TrackerDao trackerDao();

    public static synchronized AppDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "app_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private ArticleDao articleDao;

        private PopulateDbAsyncTask(AppDatabase db){
            articleDao = db.articleDao();
        }

        //Maybe you can run python script from here and initialise. Maybe you should make context an argument.
        @Override
        protected Void doInBackground(Void... voids) {
            articleDao.insertArticle(new Article("Title 1", "Https://website1", "text1"));
            articleDao.insertArticle(new Article("Title 2", "Https://website2", "text2"));
            articleDao.insertArticle(new Article("Title 3", "Https://website3", "text3"));
            return null;
        }
    }
}
















