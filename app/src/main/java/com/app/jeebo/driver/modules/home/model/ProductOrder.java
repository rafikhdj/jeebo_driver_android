package com.app.jeebo.driver.modules.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductOrder {
    @SerializedName("product_title")
    @Expose
    private String productTitle;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("parent_category_id")
    @Expose
    private Integer parent_category_id;
    @SerializedName("parent_category_url")
    @Expose
    private String parent_category_url;
    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getParent_category_id() {
        return parent_category_id;
    }

    public void setParent_category_id(Integer parent_category_id) {
        this.parent_category_id = parent_category_id;
    }

    public String getParent_category_url() {
        return parent_category_url;
    }

    public void setParent_category_url(String parent_category_url) {
        this.parent_category_url = parent_category_url;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
