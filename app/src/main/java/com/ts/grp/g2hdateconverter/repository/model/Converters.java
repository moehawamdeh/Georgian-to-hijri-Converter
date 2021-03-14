package com.ts.grp.g2hdateconverter.repository.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.ts.grp.g2hdateconverter.repository.apiclient.pojo.CalendarDate;

import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    //used Gson as a converter, since I need most date at the ConvertedDate to display with events
    @TypeConverter
    public static CalendarDate fromString(String date  ){ return new Gson().fromJson(date, CalendarDate.class);}
    @TypeConverter
    public static String convertedDateToString(CalendarDate date){ return new Gson().toJson(date);}
}