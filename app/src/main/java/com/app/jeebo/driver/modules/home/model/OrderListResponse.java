package com.app.jeebo.driver.modules.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderListResponse {
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("page_size")
    @Expose
    private Integer pageSize;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("previous")
    @Expose
    private String previous;
    @SerializedName("next")
    @Expose
    private String next;
    @SerializedName("results")
    @Expose
    private List<OrderListResult> results = null;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<OrderListResult> getResults() {
        return results;
    }

    public void setResults(List<OrderListResult> results) {
        this.results = results;
    }

}
