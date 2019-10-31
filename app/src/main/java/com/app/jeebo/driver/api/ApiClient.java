package com.app.jeebo.driver.api;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.app.jeebo.driver.BuildConfig;
import com.app.jeebo.driver.R;
import com.app.jeebo.driver.application.JeeboApplication;
import com.app.jeebo.driver.utils.AppUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static IApiRequest apiRequest;

    public static void init(Class<IApiRequest> requestClass) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // setup timeout vale offset
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new ApiInterceptor()).addInterceptor(interceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.URL)
                .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient)
                .build();

        Log.d("url" , BuildConfig.URL);
        apiRequest = retrofit.create(requestClass);

    }
    public static IApiRequest getRequest() {
        if(AppUtils.isNetworkAvailable(JeeboApplication.getContext()))
            return apiRequest;
        else{
            Toast.makeText(JeeboApplication.getContext(),JeeboApplication.getContext().getResources().getString(R.string.network_error) , Toast.LENGTH_LONG).show();
        }
        return apiRequest;
        //return apiRequest;
    }
}
