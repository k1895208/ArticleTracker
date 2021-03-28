package com.example.articletracker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TrackerDao {
    @Insert
    void insert(Tracker tracker);

    @Update
    void update(Tracker tracker);

    @Delete
    void delete(Tracker tracker);

    @Query("DELETE FROM tracker_table")
    void deleteAllTrackers();

    @Query("SELECT * FROM tracker_table")
    LiveData<List<Tracker>> getAllTrackers();

}
