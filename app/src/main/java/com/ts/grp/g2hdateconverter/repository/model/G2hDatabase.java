package com.ts.grp.g2hdateconverter.repository.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(entities = {Event.class},version = 1,exportSchema = false) //Not interested in saving schema
public abstract class G2hDatabase extends RoomDatabase {
    public abstract EventDao eventDao();
}
