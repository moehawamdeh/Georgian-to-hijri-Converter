package com.ts.grp.g2hdateconverter.repository.apiclient;

import com.ts.grp.g2hdateconverter.repository.apiclient.pojo.ApiResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

//I made the api client singleton to avoid making objects like Gson, RestAdapter every time the client is needed
public final class AladhanApiClient {
    public static final String BASE_URL="https://api.aladhan.com/v1/";
    private static AladhanApiClient mInstance=null;
    private GregorianToHijriService mService;
    private AladhanApiClient() {
        //build retrofit at initiating
        buildRetrofit();
    }
    private void buildRetrofit() {
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService=retrofit.create(GregorianToHijriService.class);
    }

    public static AladhanApiClient getClient() {
        if (mInstance == null) {
            mInstance = new AladhanApiClient();
        }
        return mInstance;
    }
    public  Call<ApiResponse> convertDate(String date){
        return mService.convertDate(date);
    }
    public interface GregorianToHijriService{
        @GET("gToH")
        Call<ApiResponse> convertDate(@Query("date") String date);

    }
}