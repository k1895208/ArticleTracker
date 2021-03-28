package com.example.articletracker;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TrackerRepository {
    private TrackerDao trackerDao;
    private LiveData<List<Tracker>> allTrackers;

    public TrackerRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        trackerDao = database.trackerDao();
        allTrackers = trackerDao.getAllTrackers();
    }

    public void insertTracker(Tracker tracker){
        new InsertTrackerAsyncTask(trackerDao).execute(tracker);
    }

    public void updateTracker(Tracker tracker){
        new UpdateTrackerAsyncTask(trackerDao).execute(tracker);
    }

    public void deleteTracker(Tracker tracker){
        new DeleteTrackerAsyncTask(trackerDao).execute(tracker);
    }

    public void deleteAllTrackers(){
        new DeleteAllTrackerAsyncTask(trackerDao).execute();
    }

    public LiveData<List<Tracker>> getAllTrackers(){
        return allTrackers;
    }

    private static class InsertTrackerAsyncTask extends AsyncTask<Tracker, Void, Void> {
        private TrackerDao trackerDao;

        private InsertTrackerAsyncTask(TrackerDao trackerDao){
            this.trackerDao = trackerDao;
        }

        @Override
        protected Void doInBackground(Tracker... trackers) {
            trackerDao.insert(trackers[0]);
            return null;
        }
    }

    private static class UpdateTrackerAsyncTask extends AsyncTask<Tracker, Void, Void> {
        private TrackerDao trackerDao;

        private UpdateTrackerAsyncTask(TrackerDao trackerDao){
            this.trackerDao = trackerDao;
        }

        @Override
        protected Void doInBackground(Tracker... trackers) {
            trackerDao.update(trackers[0]);
            return null;
        }
    }

    private static class DeleteTrackerAsyncTask extends AsyncTask<Tracker, Void, Void> {
        private TrackerDao trackerDao;

        private DeleteTrackerAsyncTask(TrackerDao trackerDao){
            this.trackerDao = trackerDao;
        }

        @Override
        protected Void doInBackground(Tracker... trackers) {
            trackerDao.delete(trackers[0]);
            return null;
        }
    }


    private static class DeleteAllTrackerAsyncTask extends AsyncTask<Void, Void, Void> {
        private TrackerDao trackerDao;

        private DeleteAllTrackerAsyncTask(TrackerDao trackerDao) {
            this.trackerDao = trackerDao;

        }

        @Override
        protected Void doInBackground(Void... voids) {
            trackerDao.deleteAllTrackers();
            return null;
        }
    }


}
