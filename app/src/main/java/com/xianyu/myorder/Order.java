package com.xianyu.myorder;

public class Order {
    private int id;
    private String orderName;
    private int orderPrice;
    private String orderImage;
    private int orderType;

    public Order() {
    }

    public Order(int id, String orderName, int orderPrice, String orderImage, int orderType) {
        this.id = id;
        this.orderName = orderName;
        this.orderPrice = orderPrice;
        this.orderImage = orderImage;
        this.orderType = orderType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public int getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(int orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderImage() {
        return orderImage;
    }

    public void setOrderImage(String orderImage) {
        this.orderImage = orderImage;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
}
