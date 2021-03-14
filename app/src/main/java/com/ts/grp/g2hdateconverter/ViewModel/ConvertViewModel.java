package com.ts.grp.g2hdateconverter.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.ts.grp.g2hdateconverter.repository.G2hRepository;
import com.ts.grp.g2hdateconverter.repository.apiclient.DataState;
import com.ts.grp.g2hdateconverter.repository.apiclient.pojo.DualCalendarDate;

import java.util.Calendar;
import java.util.Date;

public class ConvertViewModel extends AndroidViewModel {
    private Date mDate;
    private final G2hRepository mRepo;
    public ConvertViewModel(@NonNull Application application) {
        super(application);
        mRepo=G2hRepository.getInstance(application);
    }
    public Date getDate(){
        return mDate;
    }
    public void setDate(Date date){
        mDate=date;
    }


    public LiveData<DataState<DualCalendarDate>> getConvertedDate() {

        Calendar calendar=Calendar.getInstance();
        calendar.setTime(mDate);
        String day=String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month=String.valueOf(calendar.get(Calendar.MONTH)+1); //Lol Calendar returns months from 0-11
        String year=String.valueOf(calendar.get(Calendar.YEAR));

        return mRepo.getDualCalendarDate(day,month,year);
    }


}
