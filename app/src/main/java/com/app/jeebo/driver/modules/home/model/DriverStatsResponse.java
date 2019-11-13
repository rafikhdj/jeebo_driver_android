package com.app.jeebo.driver.modules.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverStatsResponse {
    @SerializedName("results")
    @Expose
    private DriverStatsResult results;

    public DriverStatsResult getResults() {
        return results;
    }

    public void setResults(DriverStatsResult results) {
        this.results = results;
    }
}
