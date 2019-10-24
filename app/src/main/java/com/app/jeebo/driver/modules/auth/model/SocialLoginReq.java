package com.app.jeebo.driver.modules.auth.model;

public class SocialLoginReq {
    private String access_token;
    // fb =1 g+ =2
    private String device_token;
    private int platform;

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
