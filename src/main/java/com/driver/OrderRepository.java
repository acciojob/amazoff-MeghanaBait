package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {
    private static final HashMap<String, Order> orderMap = new HashMap<>();
    private static final HashMap<String, DeliveryPartner>partnerMap = new HashMap<>();
    private static final HashMap<String, List<String>> partnerOrderMap = new HashMap<>();

    public static void addUser(Order order) {
        String key = order.getOrderId();
        orderMap.put(key,order);
    }

    public void addPartner(String partnerId) {
        DeliveryPartner deliveryPartner = new DeliveryPartner(partnerId);
        partnerMap.put(partnerId,deliveryPartner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        List<String> orderMap = partnerOrderMap.getOrDefault(partnerId,new ArrayList<>());
        orderMap.add(orderId);
        partnerOrderMap.put(partnerId,orderMap);
    }

    public Order getOrderById(String orderId) {
        return orderMap.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return partnerMap.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        if(partnerOrderMap.containsKey(partnerId)){
            return partnerOrderMap.get(partnerId).size();
        }else{
            return 0;
        }
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
       return partnerOrderMap.getOrDefault(partnerId,new ArrayList<>());
    }


    public List<String> getAllOrders() {
        List<String> orderList = new ArrayList<>();
        for(Order order: orderMap.values()){
            orderList.add(order.getOrderId());
        }
        return orderList;
    }

    public Integer getCountOfUnassignedOrders() {
      return  orderMap.size() - partnerOrderMap.values().stream().mapToInt(List::size).sum();
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
        partnerOrderMap.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {
        orderMap.remove(orderId);
        partnerOrderMap.values().forEach(partnerMap -> partnerMap.remove(orderId));
    }
}
