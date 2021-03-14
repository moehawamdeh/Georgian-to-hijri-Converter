package com.ts.grp.g2hdateconverter.repository.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.ts.grp.g2hdateconverter.repository.apiclient.pojo.CalendarDate;

import java.util.Date;
@Entity(tableName = "event")
public class Event {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "event_name")
    public String name;
    @ColumnInfo(name = "event_description")
    public String description;
    @ColumnInfo(name = "gregorian_date")
    //for storing converted dates, its stored in json, to keep all formatting data and to avoid hijri conversion(Feb 28 days)
    @TypeConverters({Converters.class})
    public CalendarDate gregorianDate;
    @ColumnInfo(name = "hijri_date")
    @TypeConverters({Converters.class})
    public CalendarDate hijriDate;
    @ColumnInfo(name = "server_datetime")
    @TypeConverters({Converters.class})
    public Date serverDate;
    @ColumnInfo(name = "event_color")
    public int color;
    @ColumnInfo(name = "event_timestamp")
    public long timestamp;
    public Event(){

    }

}
