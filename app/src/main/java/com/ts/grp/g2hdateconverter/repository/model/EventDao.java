package com.ts.grp.g2hdateconverter.repository.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;


@Dao
public interface EventDao {
    @Query("SELECT * FROM Event  ORDER BY event_timestamp")
    LiveData<List<Event>> getAll();
    @Query("SELECT * FROM Event WHERE id = :eventID")
    LiveData<Event> getByID(int eventID);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(Event... events);
    @Update
    Completable  update(Event event);
    @Delete
    Completable  delete(Event... events);

}
