package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {
    HashMap<String, Order> orderMap = new HashMap<>();
    HashMap<String, DeliveryPartner>partnerMap = new HashMap<>();
    HashMap<String, List<String>> partnerOrderMap = new HashMap<>();
    HashMap<String,String> orderPartnerMap = new HashMap<>();

    public void addOrder(Order order) {
        String key = order.getOrderId();
        orderMap.put(key,order);
    }

    public void addPartner(String partnerId) {
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        partnerMap.put(partnerId,deliveryPartner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)){
            //adding into o-p map
            orderPartnerMap.put(orderId,partnerId);

            //adding into p-o map
            List<String>currentOrders = new ArrayList<>();
            if(partnerOrderMap.containsKey(partnerId)){
                currentOrders = partnerOrderMap.get(partnerId);
            }
            currentOrders.add(orderId);
            partnerOrderMap.put(partnerId,currentOrders);

            //increase the no. of orders of partner
            DeliveryPartner deliveryPartner = partnerMap.get(partnerId);
            deliveryPartner.setNumberOfOrders(currentOrders.size());
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
        int count = 0;
        int timeInMinutes = convertTimeToMinutes(time);
        for(Order order : orderMap.values()){
            if(timeInMinutes > order.getDeliveryTime())
            {
                count++;
            }
        }
        return count;
    }

    private int convertTimeToMinutes(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int mins = Integer.parseInt((parts[1]));
        return hours * 60 + mins;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        List<String>odrerList = partnerOrderMap.get(partnerId);
        String lastOrderId = odrerList.get(odrerList.size() - 1);
        int ordertime = orderMap.get(lastOrderId).getDeliveryTime();
        int hours = ordertime/60;
        int mins = ordertime% 60;
        return ""+hours+":"+mins;
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
