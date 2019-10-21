package com.app.jeebo.driver.model;

/**
 * This class is used to handle api error code and error message
 */

public class Error {

    private String message;


    public String getErrMsg() {
        return message;
    }

    public void setErrMsg(String errMsg) {
        this.message = errMsg;
    }
}
