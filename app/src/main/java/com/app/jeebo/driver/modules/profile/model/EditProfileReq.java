package com.app.jeebo.driver.modules.profile.model;

public class EditProfileReq {
    private String name;
    private String email;
    private String phone_number;

    public String getPhone() {
        return phone_number;
    }

    public void setPhone(String phone) {
        this.phone_number = phone;
    }

    private String driver_image_url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage_url() {
        return driver_image_url;
    }

    public void setImage_url(String image_url) {
        this.driver_image_url = image_url;
    }
}
