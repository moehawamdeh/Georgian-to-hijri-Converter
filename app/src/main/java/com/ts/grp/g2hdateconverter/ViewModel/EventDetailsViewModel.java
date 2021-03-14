package com.ts.grp.g2hdateconverter.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ts.grp.g2hdateconverter.repository.G2hRepository;
import com.ts.grp.g2hdateconverter.repository.apiclient.DataState;
import com.ts.grp.g2hdateconverter.repository.model.Event;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;

public class EventDetailsViewModel extends AndroidViewModel {
    private LiveData<Event>mEvent=null;
    private final MutableLiveData<DataState<TransactionType>>mTransaction;
    public EventDetailsViewModel(@NonNull Application application) {
        super(application);
        mTransaction=new MutableLiveData<>();
    }

    public LiveData<Event> getEventById(String id) {
        if(mEvent==null)
            mEvent= G2hRepository.getInstance(getApplication()).getEventById(id);
        return mEvent;
    }

    public LiveData<DataState<TransactionType>> getTransactionState() {
        return mTransaction;
    }

    public void deleteEvent(){
        if(mEvent==null)
            return;
        List<Event> container=new ArrayList<Event>();
        container.add(mEvent.getValue());
        G2hRepository.getInstance(getApplication()).delete(container, new CompletableObserver() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                mTransaction.postValue(new DataState<TransactionType>().loading());
            }

            @Override
            public void onComplete() {
                mTransaction.postValue(new DataState<TransactionType>().success(TransactionType.TRANSACTION_DELETE));
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                mTransaction.postValue(new DataState<TransactionType>().error(DataState.Errors.TRANSACTION_FAILED));
            }
        });
    }
    public void updateEvent(Event event){
        if(mEvent==null)
            return;

        G2hRepository.getInstance(getApplication()).update(event, new CompletableObserver() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                mTransaction.postValue(new DataState<TransactionType>().loading());
            }

            @Override
            public void onComplete() {
                mTransaction.postValue(new DataState<TransactionType>().success(TransactionType.TRANSACTION_UPDATE));
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                mTransaction.postValue(new DataState<TransactionType>().error(DataState.Errors.TRANSACTION_FAILED));
            }
        });
    }
    public static enum TransactionType{
        TRANSACTION_DELETE,TRANSACTION_UPDATE
    }
}
