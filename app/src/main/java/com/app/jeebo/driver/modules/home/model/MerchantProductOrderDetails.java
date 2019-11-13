package com.app.jeebo.driver.modules.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MerchantProductOrderDetails {
    @SerializedName("delivery_address")
    @Expose
    private List<AddressModel> deliveryAddress = null;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("brand_profile")
    @Expose
    private Object brandProfile;
    @SerializedName("is_Private")
    @Expose
    private String isPrivate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("merchant_profile")
    @Expose
    private MerchantProfileModel merchantProfile;
    @SerializedName("current_role")
    @Expose
    private String currentRole;
    @SerializedName("location")
    @Expose
    private Object location;
    @SerializedName("customer_profile")
    @Expose
    private Object customerProfile;

    public List<AddressModel> getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(List<AddressModel> deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getBrandProfile() {
        return brandProfile;
    }

    public void setBrandProfile(Object brandProfile) {
        this.brandProfile = brandProfile;
    }

    public String getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MerchantProfileModel getMerchantProfile() {
        return merchantProfile;
    }

    public void setMerchantProfile(MerchantProfileModel merchantProfile) {
        this.merchantProfile = merchantProfile;
    }

    public String getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }

    public Object getCustomerProfile() {
        return customerProfile;
    }

    public void setCustomerProfile(Object customerProfile) {
        this.customerProfile = customerProfile;
    }

}
