package com.app.jeebo.driver.modules.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CancelCategoriesResponse {
    @SerializedName("results")
    @Expose
    private List<CancelCategoriesResult> results;

    public List<CancelCategoriesResult> getResults() {
        return results;
    }

    public void setResults(List<CancelCategoriesResult> results) {
        this.results = results;
    }
}
