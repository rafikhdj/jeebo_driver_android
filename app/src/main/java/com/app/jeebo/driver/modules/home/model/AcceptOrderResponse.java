package com.app.jeebo.driver.modules.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AcceptOrderResponse {
    @SerializedName("results")
    @Expose
    private String results;
    @SerializedName("result")
    @Expose
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }
}
