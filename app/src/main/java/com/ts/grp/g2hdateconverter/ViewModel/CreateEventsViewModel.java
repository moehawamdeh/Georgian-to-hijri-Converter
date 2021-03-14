package com.ts.grp.g2hdateconverter.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ts.grp.g2hdateconverter.repository.G2hRepository;
import com.ts.grp.g2hdateconverter.repository.apiclient.DataState;
import com.ts.grp.g2hdateconverter.repository.apiclient.pojo.DualCalendarDate;
import com.ts.grp.g2hdateconverter.repository.model.Event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;

public class CreateEventsViewModel extends AndroidViewModel {
    MutableLiveData<DataState<Void>> mState;
    public CreateEventsViewModel(@NonNull Application application) {
        super(application);
            mState =new MutableLiveData<>();
    }
    public LiveData<DataState<Void>>createEvent(DualCalendarDate date, String name, String description, int color){
        Event event= new Event();
        event.name=name;
        event.gregorianDate=date.getGregorian();
        event.hijriDate=date.getHijri();
        event.color=color;
        event.description=description;
        event.timestamp=0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        try {
            Date d = sdf.parse(event.gregorianDate.getDate());
            if (d != null) {
                event.timestamp=d.getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        G2hRepository.getInstance(getApplication()).insert(event, new CompletableObserver() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                    mState.postValue(new DataState<Void>().loading());
            }

            @Override
            public void onComplete() {
                    mState.postValue(new DataState<Void>().success());
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                    mState.postValue(new DataState<Void>().error(DataState.Errors.TRANSACTION_FAILED));
            }
        });
        return mState;
    }

}
