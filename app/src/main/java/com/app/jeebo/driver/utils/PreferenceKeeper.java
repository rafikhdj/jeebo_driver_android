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

    public int getDriverStatus() {
        return prefs.getInt(AppConstant.PrefsName.DRIVER_STATUS, 3);
    }

    public void setDriverStatus(int status) {
        prefs.edit().putInt(AppConstant.PrefsName.DRIVER_STATUS, status).commit();
    }

    public String getDeviceToken() {
        return prefs.getString(AppConstant.PrefsName.DEVICE_TOKEN, "");
    }

    public void setDeviceToken(String token) {
        prefs.edit().putString(AppConstant.PrefsName.DEVICE_TOKEN, token).commit();
    }

    public String getFCMToken() {
        return "";
    }

    public String getName() {
        return prefs.getString(AppConstant.PrefsName.NAME, "");
    }

    public void setName(String name) {
        prefs.edit().putString(AppConstant.PrefsName.NAME, name).commit();
    }

    public String getImage() {
        return prefs.getString(AppConstant.PrefsName.IMAGE, "");
    }

    public void setImage(String image) {
        prefs.edit().putString(AppConstant.PrefsName.IMAGE, image).commit();
    }

    public String getEmail() {
        return prefs.getString(AppConstant.PrefsName.EMAIL, "");
    }

    public void setEmail(String email) {
        prefs.edit().putString(AppConstant.PrefsName.EMAIL, email).commit();
    }

    public String getUserId() {
        return prefs.getString(AppConstant.PrefsName.USER_ID, "");
    }

    public void setUserId(String userId) {
        prefs.edit().putString(AppConstant.PrefsName.USER_ID, userId).commit();
    }

    public String getUserPhone() {
        return prefs.getString(AppConstant.PrefsName.USER_PHONE, "");
    }

    public void setUserPhone(String userPhone) {
        prefs.edit().putString(AppConstant.PrefsName.USER_PHONE, userPhone).commit();
    }

    public boolean isLogin() {
        return prefs.getBoolean(AppConstant.PrefsName.IS_LOGIN, false);
    }

    public void setLogin(boolean islogin) {
        prefs.edit().putBoolean(AppConstant.PrefsName.IS_LOGIN, islogin).commit();
    }
}


