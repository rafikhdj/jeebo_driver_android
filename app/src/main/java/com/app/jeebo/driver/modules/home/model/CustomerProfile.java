package com.app.jeebo.driver.modules.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerProfile {

    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("gender")
    @Expose
    private String gender;

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
