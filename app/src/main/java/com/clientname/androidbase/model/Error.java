package com.clientname.androidbase.model;

/**
 * This class is used to handle api error code and error message
 */

public class Error {

    private String errMsg;
    private String erroCode;


    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErroCode() {
        return erroCode;
    }

    public void setErroCode(String erroCode) {
        this.erroCode = erroCode;
    }
}
