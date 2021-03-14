package com.ts.grp.g2hdateconverter.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.ts.grp.g2hdateconverter.repository.apiclient.AladhanApiClient;
import com.ts.grp.g2hdateconverter.repository.apiclient.DataState;
import com.ts.grp.g2hdateconverter.repository.apiclient.pojo.ApiResponse;
import com.ts.grp.g2hdateconverter.repository.apiclient.pojo.DualCalendarDate;
import com.ts.grp.g2hdateconverter.repository.model.Event;
import com.ts.grp.g2hdateconverter.repository.model.G2hDatabase;

import java.io.IOException;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class G2hRepository {
    public static final String TAG="G2h-Repository";
    private static G2hRepository mInstance=null;
    private final AladhanApiClient mApiClient;
    private final G2hDatabase mDatabase;
    private MutableLiveData<DataState<DualCalendarDate>> mConvertedDateState;
    private G2hRepository(Context appContext){

        mApiClient=AladhanApiClient.getClient();
        mDatabase= Room.databaseBuilder(appContext,G2hDatabase.class,"G2hDB").build();
        mConvertedDateState=new MutableLiveData<>();
    }

    public static G2hRepository getInstance(@NonNull Context appContext){
        if(mInstance==null)
            mInstance= new G2hRepository(appContext);
        return mInstance;
    }

    /**
     getConvertedDate
     Description:
     Convert Hijri Dates To Gregorian
     Parameters:
     [date] : com.ts.grp.g2hdateconverter.repository.apiclient.pojo.Date
     **/
    public LiveData<DataState<DualCalendarDate>> getDualCalendarDate(String day, String month, String year){
        mConvertedDateState.postValue(new DataState<DualCalendarDate>().loading());
        mApiClient.convertDate(String.format("%s-%s-%s",day,month,year)).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@Nullable Call<ApiResponse> call,@Nullable Response<ApiResponse> response) {
                if(response.isSuccessful() ){
                    if(response.body()!=null){
                    //Server returned data, we wrap it in a data state, and add Server Datetime from headers since its a requirement
                    DataState<DualCalendarDate> stateDate= new DataState<DualCalendarDate>()
                            .setServerDateTime(response.headers().getDate("date"))
                            .success(response.body().getData());
                    mConvertedDateState.postValue(stateDate);
                    }
                    else
                    {
                        mConvertedDateState.postValue(new DataState<DualCalendarDate>().error(DataState.Errors.API_NO_RESPONSE));
                    }
                }else{
                    //Server returned an error
                    mConvertedDateState.postValue(new DataState<DualCalendarDate>().error(DataState.Errors.BAD_REQUEST));
                }

            }

            @Override
            public void onFailure(@Nullable Call<ApiResponse> call,@Nullable Throwable t) {
                //Post state liveData holding error for LiveData being observed
                    if(t instanceof IOException){
                        //Network Failure
                        mConvertedDateState.postValue(new DataState<DualCalendarDate>().error(DataState.Errors.NETWORK_ERROR));

                    }else{
                        //Converter Failure ( Gson parsing json or whatever )
                        mConvertedDateState.postValue(new DataState<DualCalendarDate>().error(DataState.Errors.CONVERTER_ERROR));
                    }


            }
        });
        return mConvertedDateState;
    }
    public LiveData<List<Event>> getAllEvents(){
        return mDatabase.eventDao().getAll();
    }
    public LiveData<Event> getEventById(String id) { return mDatabase.eventDao().getByID(Integer.parseInt(id));}
    public void insert(Event entity, CompletableObserver observer){
        mDatabase.eventDao()
                .insertAll(entity)
                .subscribeOn(Schedulers.io())
                .subscribe(observer);

    }
    public void update(Event entity, CompletableObserver observer){
        mDatabase.eventDao()
                .update(entity)
                .subscribeOn(Schedulers.io())
                .subscribe(observer);

    }
    public void delete(List<Event> entities, CompletableObserver observer){
        Event[] events=new Event[entities.size()];
        entities.toArray(events);
        mDatabase.eventDao()
                .delete(events)
                .subscribeOn(Schedulers.io())
                .subscribe(observer);

    }



}
