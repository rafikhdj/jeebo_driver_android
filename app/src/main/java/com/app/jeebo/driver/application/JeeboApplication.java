package com.app.jeebo.driver.application;

import android.app.Application;
import android.content.Context;

import com.app.jeebo.driver.api.ApiClient;
import com.app.jeebo.driver.api.IApiRequest;
import com.app.jeebo.driver.utils.AppUtils;
import com.app.jeebo.driver.utils.PreferenceKeeper;

import java.util.Locale;


public class JeeboApplication extends Application {

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        ApiClient.init(IApiRequest.class,getApplicationContext());
        PreferenceKeeper.setContext(getApplicationContext());
        PreferenceKeeper.getInstance().setLanguage(Locale.getDefault().getDisplayLanguage());
        AppUtils.getDeviceToken();
       // PreferenceKeeper.getInstance().setLanguage(Locale.getDefault().getDisplayLanguage());
    }
    //FacebookSdk.InitializeCallback

    public static Context getContext(){
        return context;
    }

}
