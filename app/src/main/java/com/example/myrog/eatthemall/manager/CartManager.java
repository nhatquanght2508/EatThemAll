package com.example.myrog.eatthemall.manager;

import com.example.myrog.eatthemall.Model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thinh on 07/11/2017.
 */

public class CartManager {
    private static CartManager instance;
    private List<Order> list = new ArrayList<>();

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void setData(List<Order> lst) {
        clear();
        list.addAll(lst);
    }

    public List<Order> loadCart(){
        return list;
    }

    public Order getById(String id) {
        for (Order p : list) {
            if (p.getProductID().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public void addOrder(Order order) {
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getProductID().equals(order.getProductID())){
                int quantity = Integer.parseInt(list.get(i).getQuantity()) + Integer.parseInt(order.getQuantity());
                list.get(i).setQuantity(String.valueOf(quantity));
                return;
            }
        }
        list.add(order);
    }

    public void updateOrder(Order nameCardEntity) {
        for (int i = 0; i < list.size(); i++) {
            if (nameCardEntity.getProductID().equals(list.get(i).getProductID())) {
                list.set(i, nameCardEntity);
            }
        }
    }

    public boolean deleteOrderById(String id) {
        for (Order p : list) {
            if (p.getProductID().equals(id)) {
                list.remove(p);
                return true;
            }
        }
        return false;
    }

    public void clear() {
        list.clear();
    }

    public String getTotalString(){
        long total = 0;
        for(Order order : list){
            total += Long.parseLong(order.getPrice()) * Integer.parseInt(order.getQuantity());
        }
        return String.valueOf(total);
    }

    public long getTotal(){
        long total = 0;
        for(Order order : list){
            total += Long.parseLong(order.getPrice()) * Integer.parseInt(order.getQuantity());
        }
        return total;
    }

}
