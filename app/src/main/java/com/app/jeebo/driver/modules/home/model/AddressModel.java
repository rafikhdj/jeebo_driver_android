package com.app.jeebo.driver.modules.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressModel {
    @SerializedName("zipcode")
    @Expose
    private String zipcode;
    @SerializedName("shop_number")
    @Expose
    private String shopNumber;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("locality")
    @Expose
    private String locality;
    @SerializedName("street_name")
    @Expose
    private String streetName;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("business_details_id")
    @Expose
    private Integer businessDetailsId;
    @SerializedName("full_address")
    @Expose
    private String fullAddress;
    @SerializedName("house_no")
    @Expose
    private String house_no;
    @SerializedName("state")
    @Expose
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
    }

    @SerializedName("lng")
    @Expose
    private Double lng;

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getShopNumber() {
        return shopNumber;
    }

    public void setShopNumber(String shopNumber) {
        this.shopNumber = shopNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getBusinessDetailsId() {
        return businessDetailsId;
    }

    public void setBusinessDetailsId(Integer businessDetailsId) {
        this.businessDetailsId = businessDetailsId;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
