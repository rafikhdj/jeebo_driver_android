package com.app.jeebo.driver.modules.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerOrderDetail {
    @SerializedName("merchant_product_order_details")
    @Expose
    private MerchantProductOrderDetails merchantProductOrderDetails;
    @SerializedName("product_order")
    @Expose
    private ProductOrder productOrder;
    @SerializedName("amount")
    @Expose
    private double amount;
    @SerializedName("quantity")
    @Expose
    private double quantity;
    @SerializedName("is_cancelled")
    @Expose
    private boolean is_cancelled;
    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        if(status==null)
            status=" ";
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isIs_cancelled() {
        return is_cancelled;
    }

    public void setIs_cancelled(boolean is_cancelled) {
        this.is_cancelled = is_cancelled;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public MerchantProductOrderDetails getMerchantProductOrderDetails() {
        return merchantProductOrderDetails;
    }

    public void setMerchantProductOrderDetails(MerchantProductOrderDetails merchantProductOrderDetails) {
        this.merchantProductOrderDetails = merchantProductOrderDetails;
    }

    public ProductOrder getProductOrder() {
        return productOrder;
    }

    public void setProductOrder(ProductOrder productOrder) {
        this.productOrder = productOrder;
    }

}
