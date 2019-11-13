package com.app.jeebo.driver.modules.home.model;

public class CancelOrderRequest {
    private int order_id;

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getCancel_subcategory() {
        return cancel_subcategory;
    }

    public void setCancel_subcategory(int cancel_subcategory) {
        this.cancel_subcategory = cancel_subcategory;
    }

    private int cancel_subcategory;
}
