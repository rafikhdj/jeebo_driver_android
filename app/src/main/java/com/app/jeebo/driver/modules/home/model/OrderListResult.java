package com.app.jeebo.driver.modules.home.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderListResult {
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("delivery_address")
    @Expose
    private String delivery_address;
    @SerializedName("longitude")
    @Expose
    private String longitude;

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("cancel_subcategory_details")
    @Expose
    private CategoryModel cancel_subcategory_details;

    public CategoryModel getCancel_subcategory_details() {
        return cancel_subcategory_details;
    }

    public void setCancel_subcategory_details(CategoryModel cancel_subcategory_details) {
        this.cancel_subcategory_details = cancel_subcategory_details;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @SerializedName("id")
    @Expose
    private int id;

    public int getDelivery_stage() {
        return delivery_stage;
    }

    public void setDelivery_stage(int delivery_stage) {
        this.delivery_stage = delivery_stage;
    }

    @SerializedName("delivery_stage")
    @Expose
    private int delivery_stage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @SerializedName("payment_type")
    @Expose
    private String payment_type;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("collect_from_client")
    @Expose
    private double collect_from_client;
    @SerializedName("pay_to_merchant")
    @Expose
    private Double pay_to_merchant;
    @SerializedName("delivery_charge")
    @Expose
    private Double delivery_charge;
    @SerializedName("order_total")
    @Expose
    private Double order_total;

    public Double getOrder_total() {
        return order_total;
    }

    public void setOrder_total(Double order_total) {
        this.order_total = order_total;
    }

    public Double getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(Double delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public Double getPay_to_merchant() {
        return pay_to_merchant;
    }

    public void setPay_to_merchant(Double pay_to_merchant) {
        this.pay_to_merchant = pay_to_merchant;
    }

    public double getCollect_from_client() {
        return collect_from_client;
    }

    public void setCollect_from_client(double collect_from_client) {
        this.collect_from_client = collect_from_client;
    }

    @SerializedName("customer_order_details")
    @Expose
    private List<CustomerOrderDetail> customerOrderDetails = null;
    @SerializedName("cancel_subcategory")
    @Expose
    private Object cancelSubcategory;
    @SerializedName("user_order_details")
    @Expose
    private UserOrderDetails userOrderDetails;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<CustomerOrderDetail> getCustomerOrderDetails() {
        return customerOrderDetails;
    }

    public void setCustomerOrderDetails(List<CustomerOrderDetail> customerOrderDetails) {
        this.customerOrderDetails = customerOrderDetails;
    }

    public Object getCancelSubcategory() {
        return cancelSubcategory;
    }

    public void setCancelSubcategory(Object cancelSubcategory) {
        this.cancelSubcategory = cancelSubcategory;
    }

    public UserOrderDetails getUserOrderDetails() {
        return userOrderDetails;
    }

    public void setUserOrderDetails(UserOrderDetails userOrderDetails) {
        this.userOrderDetails = userOrderDetails;
    }
}
