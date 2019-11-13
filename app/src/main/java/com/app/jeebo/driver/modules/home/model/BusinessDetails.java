package com.app.jeebo.driver.modules.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BusinessDetails {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sell_type")
    @Expose
    private List<String> sellType = null;
    @SerializedName("service_type")
    @Expose
    private String serviceType;
    @SerializedName("categories")
    @Expose
    private List<CategoryModel> categories = null;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("merchant_address")
    @Expose
    private List<AddressModel> merchantAddress = null;
    @SerializedName("business_name")
    @Expose
    private String businessName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getSellType() {
        return sellType;
    }

    public void setSellType(List<String> sellType) {
        this.sellType = sellType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public List<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryModel> categories) {
        this.categories = categories;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<AddressModel> getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(List<AddressModel> merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
}
