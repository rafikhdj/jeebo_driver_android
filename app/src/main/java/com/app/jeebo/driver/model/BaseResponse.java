package com.app.jeebo.driver.model;

public class BaseResponse<T> {

    private int statusCode;
    private T results;
    private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public T getResult() {
        return results;
    }

    public void setResult(T result) {
        this.results = result;
    }

}
