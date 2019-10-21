package com.app.jeebo.driver.api;

import android.util.Log;

import com.app.jeebo.driver.BuildConfig;

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
        return apiRequest;
    }
}
