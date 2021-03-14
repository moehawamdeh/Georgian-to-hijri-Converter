package com.ts.grp.g2hdateconverter.repository.apiclient.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DualCalendarDate {
    @SerializedName("hijri")
    @Expose
    private CalendarDate hijri;
    @SerializedName("gregorian")
    @Expose
    private CalendarDate gregorian;

    public CalendarDate getHijri() {
        return hijri;
    }

    public void setHijri(CalendarDate hijri) {
        this.hijri = hijri;
    }

    public CalendarDate getGregorian() {
        return gregorian;
    }

    public void setGregorian(CalendarDate gregorian) {
        this.gregorian = gregorian;
    }

}
