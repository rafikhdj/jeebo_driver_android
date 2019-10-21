package com.app.jeebo.driver.modules.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("current_role")
    @Expose
    private String currentRole;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("isProfile_updated")
    @Expose
    private Boolean isProfileUpdated;
    @SerializedName("is_phone_verified")
    @Expose
    private Boolean isPhoneVerified;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("phone_number")
    @Expose
    private String phone_number;

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("otp")
    @Expose
    private Integer otp;

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    @SerializedName("file_url")
    @Expose
    private String file_url;

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsProfileUpdated() {
        return isProfileUpdated;
    }

    public void setIsProfileUpdated(Boolean isProfileUpdated) {
        this.isProfileUpdated = isProfileUpdated;
    }

    public Boolean getIsPhoneVerified() {
        return isPhoneVerified;
    }

    public void setIsPhoneVerified(Boolean isPhoneVerified) {
        this.isPhoneVerified = isPhoneVerified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
