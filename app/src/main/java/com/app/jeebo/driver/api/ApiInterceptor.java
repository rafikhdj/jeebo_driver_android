package com.app.jeebo.driver.api;

import android.text.TextUtils;
import android.util.Log;

import com.app.jeebo.driver.utils.AppConstant;
import com.app.jeebo.driver.utils.PreferenceKeeper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Class is used to passing user token at central level.
 */
public class ApiInterceptor implements Interceptor {
private static final String TAG = ApiInterceptor.class.getSimpleName();
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        originalRequest = originalRequest.newBuilder().addHeader("app_id",AppConstant.APPID)
                .addHeader("language", PreferenceKeeper.getInstance().getLanguage())
                .build();
               // .addHeader("deviceId", PreferenceKeeper.getInstance().getFCMToken())
              //  .addHeader("deviceOS", AppConstant.DEVICE_OS).build();
        Log.d(TAG, "deviceId = [" +  PreferenceKeeper.getInstance().getFCMToken() + "]");
        String token = PreferenceKeeper.getInstance().getAccessToken();

        if (TextUtils.isEmpty(token)) {
            return chain.proceed(originalRequest);
        }

        Request newRequest = originalRequest.newBuilder().addHeader("x-access-token", token).addHeader("deviceId",PreferenceKeeper.getInstance().getFCMToken())
                .addHeader("language", PreferenceKeeper.getInstance().getLanguage())
                .addHeader("deviceOS", AppConstant.DEVICE_OS).build();


        Log.d("Request token ", token);
        Log.d("Request URL ", newRequest.url().toString());

        Response response = chain.proceed(newRequest);
        Log.d("Response ", response.body().source().toString());

        return response;
    }

}
