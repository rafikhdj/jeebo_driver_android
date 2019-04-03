package com.app.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class GsonUtils {

    public static <T> T parseJson(String json, Class<T> tClass) {
        return new Gson().fromJson(json, tClass);
    }

    public static <T> T parseJson(String json, Type type) {
        return new Gson().fromJson(json, type);
    }

    public static String getJson(Object profile) {
        return new Gson().toJson(profile);
    }

}
