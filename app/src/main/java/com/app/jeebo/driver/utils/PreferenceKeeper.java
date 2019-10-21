package com.app.jeebo.driver.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Class is used to save user data in preference.
 */
public class PreferenceKeeper {

    private static PreferenceKeeper keeper;
    private static Context context;
    private SharedPreferences prefs;

    private PreferenceKeeper(Context context) {
        if (context != null)
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PreferenceKeeper getInstance() {
        if (keeper == null) {
            keeper = new PreferenceKeeper(context);
        }
        return keeper;
    }

    public static void setContext(Context ctx) {
        context = ctx;
    }

    public void clearData() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    public void setCurrentUserName(String userId) {
        prefs.edit().putString(AppConstant.PrefsName.USERNAME, userId).commit();
    }

    public String getCurrentUserName() {
        return prefs.getString(AppConstant.PrefsName.USERNAME, null);
    }


    public String getAccessToken() {
        return prefs.getString(AppConstant.PrefsName.ACCESS_TOKEN, "");
    }

    public void setAccessToken(String token) {
        prefs.edit().putString(AppConstant.PrefsName.ACCESS_TOKEN, token).commit();
    }

    public String getFCMToken() {
        return null;
    }
}


