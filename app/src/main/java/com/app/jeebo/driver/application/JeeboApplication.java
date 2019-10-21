package com.app.jeebo.driver.application;

import android.app.Application;

import com.app.jeebo.driver.api.ApiClient;
import com.app.jeebo.driver.api.IApiRequest;
import com.app.jeebo.driver.utils.PreferenceKeeper;

import java.util.Locale;


public class JeeboApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ApiClient.init(IApiRequest.class);
        PreferenceKeeper.setContext(getApplicationContext());
       // PreferenceKeeper.getInstance().setLanguage(Locale.getDefault().getDisplayLanguage());
    }
    //FacebookSdk.InitializeCallback

}
