package com.ts.grp.g2hdateconverter.ViewModel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ts.grp.g2hdateconverter.repository.G2hRepository;
import com.ts.grp.g2hdateconverter.repository.model.Event;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarViewModel extends AndroidViewModel {

    private MutableLiveData<String> mMonthYearTitle;
    private final LiveData<List<Event>> mEvents;

    public CalendarViewModel(@NonNull Application application) {
        super(application);
        mMonthYearTitle = new MutableLiveData<>();
        mEvents= G2hRepository.getInstance(application).getAllEvents();
    }
    public LiveData<List<Event>>getEvents(){
        return mEvents;
    }

    public void setSelectedDate(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mMonthYearTitle.setValue(calendar.get(Calendar.YEAR) +" - "+ (calendar.getDisplayName(Calendar.MONTH,Calendar.SHORT_FORMAT, Locale.US) ));
        }else{
            mMonthYearTitle.setValue(calendar.get(Calendar.YEAR) +" - "+ String.format(Locale.US,"%tB",calendar));

        }
    }
    public LiveData<String> getMonthTitle() {
        return mMonthYearTitle;
    }
}
