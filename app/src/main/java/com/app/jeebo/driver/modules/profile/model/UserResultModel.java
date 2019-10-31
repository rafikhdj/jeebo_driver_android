package com.app.jeebo.driver.modules.profile.model;

import com.app.jeebo.driver.modules.auth.model.UserModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserResultModel {
    @SerializedName("result")
    @Expose
    private UserModel result;

    public UserModel getResult() {
        return result;
    }

    public void setResult(UserModel result) {
        this.result = result;
    }
}
