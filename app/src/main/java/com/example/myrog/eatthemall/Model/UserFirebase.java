package com.example.myrog.eatthemall.Model;

import java.util.ArrayList;

/**
 * Created by Thinh on 24/11/2017.
 */

public class UserFirebase {
    private String phone;
    private String name;
    private String address;
    private String imageUrl;
//    private ArrayList<Food> favorites;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

//    public ArrayList<Food> getFavorites() {
//        return favorites;
//    }
//
//    public void setFavorites(ArrayList<Food> favorites) {
//        this.favorites = favorites;
//    }
}
