package com.ts.grp.g2hdateconverter.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ts.grp.g2hdateconverter.R;
import com.ts.grp.g2hdateconverter.repository.G2hRepository;
import com.ts.grp.g2hdateconverter.repository.apiclient.DataState;
import com.ts.grp.g2hdateconverter.repository.model.Event;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;

public class EventsViewModel extends AndroidViewModel {
    LiveData<List<Event>> mEvents;
    MutableLiveData<DataState<Void>> mDeleteEvents;
    private List<Event>mSelected;
    public EventsViewModel(@NonNull Application application) {
        super(application);
        mEvents= G2hRepository.getInstance(application).getAllEvents();
        mDeleteEvents=new MutableLiveData<>();
    }

    public LiveData<List<Event>>getEvents(){
        return mEvents;
    }

    public void setSelected(List<Event> selected) {
        mSelected=new ArrayList<>(selected); //no need to keep reference to list from view
    }

    public void removeSelected() {
        mSelected=null;
    }

    public LiveData<DataState<Void>> deleteSelected() {
        G2hRepository.getInstance(getApplication()).delete(mSelected, new CompletableObserver() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                mDeleteEvents.postValue(new DataState<Void>().loading());
            }

            @Override
            public void onComplete() {
                mDeleteEvents.postValue(new DataState<Void>().success());
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                mDeleteEvents.postValue(new DataState<Void>().error(DataState.Errors.TRANSACTION_FAILED));
            }
        });
        return mDeleteEvents;
    }
}
