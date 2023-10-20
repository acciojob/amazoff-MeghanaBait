package com.driver;

public class Order {

    private String orderId;
    private int deliveryTime;

    public Order(String orderId, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
    }

    public String getOrderId() {
        return orderId;
    }

    public int getDeliveryTime() {return deliveryTime;}
}
