package com.app.jeebo.driver.api;

import com.app.jeebo.driver.model.Error;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorBase {
    @SerializedName("error")
    @Expose
    private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
