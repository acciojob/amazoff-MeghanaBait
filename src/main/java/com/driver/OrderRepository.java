package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {
    HashMap<String, Order> orderMap;
    HashMap<String, DeliveryPartner>partnerMap;
    HashMap<String, List<String>> partnerOrderMap;
    HashMap<String,String> orderPartnerMap;

    public OrderRepository() {
        this.orderMap = new HashMap<>();
        this.partnerMap = new HashMap<>();
        this.partnerOrderMap = new HashMap<>();
        this.orderPartnerMap = new HashMap<>();
    }

    public void addOrder(Order order) {
        String key = order.getOrderId();
        orderMap.put(key,order);
        orderPartnerMap.put(key,"Not Assigned");
    }

    public void addPartner(String partnerId) {
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        partnerMap.put(partnerId,deliveryPartner);
        partnerOrderMap.put(partnerId,new ArrayList<>());
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)){
            //adding into o-p map
            orderPartnerMap.put(orderId,partnerId);

            //adding into p-o map

            if(partnerOrderMap.containsKey(partnerId)){
                List<String>currentOrders = partnerOrderMap.get(partnerId);
                currentOrders.add(orderId);
                partnerOrderMap.put(partnerId,currentOrders);

                //increase the no. of orders of partner
                DeliveryPartner deliveryPartner = partnerMap.get(partnerId);
                deliveryPartner.setNumberOfOrders(currentOrders.size());
            }
        }
    }

    public Order getOrderById(String orderId) {
        return orderMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return partnerMap.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        return partnerOrderMap.get(partnerId).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return partnerOrderMap.get(partnerId);
    }


    public List<String> getAllOrders() {
        List<String> orderList = new ArrayList<>();
        for(Order order: orderMap.values()){
            orderList.add(order.getOrderId());
        }
        return orderList;

        //using keyset
        //for(String order : orderMap.keySet(){
        //orderList.add(order);
        //}
    }

    public Integer getCountOfUnassignedOrders() {
        return  orderMap.size() - orderPartnerMap.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        Integer count=0;
        //converting given string time to integer
        String arr[]=time.split(":"); //12:45
        int hr=Integer.parseInt(arr[0]);
        int min=Integer.parseInt(arr[1]);

        int total=(hr*60+min);

        List<String> list=partnerOrderMap.getOrDefault(partnerId,new ArrayList<>());
        if(list.size()==0)return 0; //no order assigned to partnerId

        for(String s: list){
            Order currentOrder=orderMap.get(s);
            if(currentOrder.getDeliveryTime()>total){
                count++;
            }
        }

        return count;
    }



    public int getLastDeliveryTimeByPartnerId(String partnerId) {
        int maxTime = 0;
        List<String> orders = partnerOrderMap.getOrDefault(partnerId,new ArrayList<>());
        if(orders.size() == 0) return maxTime;
        for (String orderId : orders){
            int currentTime = orderMap.get(orderId).getDeliveryTime();
            maxTime = Math.max(maxTime, currentTime);
        }
        return maxTime;
    }

    public void deletePartnerById(String partnerId) {
        partnerMap.remove(partnerId);

        List<String>orderList = partnerOrderMap.get(partnerId);
        for(String order : orderList){
            orderPartnerMap.remove(order);
        }

        partnerOrderMap.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {
        orderMap.remove(orderId);

        String partnerId = orderPartnerMap.get(orderId);

        orderPartnerMap.remove(orderId);

        partnerOrderMap.get(partnerId).remove(orderId);

        partnerMap.get(partnerId).setNumberOfOrders(partnerOrderMap.get(partnerId).size());

    }
}
