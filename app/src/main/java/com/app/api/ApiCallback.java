package com.app.api;

import android.util.Log;

import com.app.model.BaseResponse;
import com.app.model.Error;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
This Class is to get Api response
 */

public abstract class ApiCallback<T> implements Callback<BaseResponse<T>> {
    private final String TAG = ApiCallback.class.getSimpleName();

    public ApiCallback() {
    }

    @Override
    public void onResponse(Call<BaseResponse<T>> call, Response<BaseResponse<T>> response) {
        if (response.body() != null) {
            Log.i(TAG, "RES : " + "Success");
            if (response.body().getStatusCode() == 1) {
                onSucess(response.body().getResult());
            } else {

                if (response.body().getError() != null) {
                    Error error = response.body().getError();

                    if (error != null) {

                    }



                }
            }
        } else {
            Log.i(TAG, "ERROR : " + "Success");
            Error error = new Error();
            error.setErrMsg("");
            onError(error);
        }
    }

    @Override
    public void onFailure(Call<BaseResponse<T>> call, Throwable t) {
        Log.i(TAG, "ERROR FAIL : " + t.getMessage());
        Error error = new Error();
//        if (t != null && t.getMessage() != null) {
//            error.setMsg(t.getMessage());
//        } else {
//            error.setMsg(context.getString(R.string.error_msg));
//        }
        onError(error);
    }


    public abstract void onSucess(T t);

    public abstract void onError(Error msg);

}
