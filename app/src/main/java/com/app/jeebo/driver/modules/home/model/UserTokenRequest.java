package com.app.jeebo.driver.modules.home.model;

public class UserTokenRequest {
    private String device_token;
    private int platform=3;

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }
}
