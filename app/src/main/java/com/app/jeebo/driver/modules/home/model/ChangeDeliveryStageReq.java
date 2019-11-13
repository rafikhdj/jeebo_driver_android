package com.app.jeebo.driver.modules.home.model;

public class ChangeDeliveryStageReq {
    private int stage;
    private int order_id;

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
}
