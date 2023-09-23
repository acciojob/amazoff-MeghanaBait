package com.driver;

public class DeliveryPartner {

    private String partnerId;
    private int numberOfOrders;

    public DeliveryPartner(String partnerId) {
        this.partnerId = partnerId;
        this.numberOfOrders = 0;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public Integer getNumberOfOrders(){
        return numberOfOrders;
    }

    public void setNumberOfOrders(Integer numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }
}