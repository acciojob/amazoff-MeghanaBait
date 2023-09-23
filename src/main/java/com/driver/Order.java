package com.driver;

public class Order {

    private String OrderId;
    private int deliveryTime;

    public Order(String OrderId, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
    }

    public String getOrderId() {
        return OrderId;
    }

    public int getDeliveryTime() {return deliveryTime;}
}
