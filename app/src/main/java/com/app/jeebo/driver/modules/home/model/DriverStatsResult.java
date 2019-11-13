package com.app.jeebo.driver.modules.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverStatsResult {
    @SerializedName("earnings")
    @Expose
    private Double earnings;
    @SerializedName("return_to_jeebo")
    @Expose
    private Double return_to_jeebo;

    public Double getEarnings() {
        return earnings;
    }

    public void setEarnings(Double earnings) {
        this.earnings = earnings;
    }

    public Double getReturn_to_jeebo() {
        return return_to_jeebo;
    }

    public void setReturn_to_jeebo(Double return_to_jeebo) {
        this.return_to_jeebo = return_to_jeebo;
    }
}
