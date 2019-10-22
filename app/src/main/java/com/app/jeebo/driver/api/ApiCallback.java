package com.app.jeebo.driver.api;

import android.util.Log;

import com.app.jeebo.driver.model.Error;
import com.app.jeebo.driver.utils.GsonUtils;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
This Class is to get Api response
 */

public abstract class ApiCallback<T> implements Callback<T> {
    private final String TAG = ApiCallback.class.getSimpleName();

    public ApiCallback() {
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.body() != null) {
            Log.i(TAG, "RES : " + "Success");
            onSuccess(response.body());
        } else {
            ErrorBase error = null;
            try {
                error = GsonUtils.getObject(new String(response.errorBody().bytes()), ErrorBase.class);
                onError(error.getError());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Error errorModel=new Error();
       /* errorModel.setMessage(t.toString());
        onError(errorModel);*/
    }


    public abstract void onSuccess(T t);

    public abstract void onError(Error error);

}
