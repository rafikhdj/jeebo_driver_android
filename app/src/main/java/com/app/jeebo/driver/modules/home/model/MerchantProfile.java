package com.app.jeebo.driver.modules.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MerchantProfile {

    @SerializedName("profile_image_url")
    @Expose
    private String profileImageUrl;
    @SerializedName("business_details")
    @Expose
    private BusinessDetails businessDetails;

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public BusinessDetails getBusinessDetails() {
        return businessDetails;
    }

    public void setBusinessDetails(BusinessDetails businessDetails) {
        this.businessDetails = businessDetails;
    }
}
