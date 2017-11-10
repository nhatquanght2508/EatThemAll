package com.example.myrog.eatthemall.Model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Thinh on 09/11/2017.
 */

public class Store {
    private String address;
    private String phone;
    private double latitude;
    private double longitude;

    public Store(String address, String phone, double latitude, double longitude) {
        this.address = address;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Store() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longtitude) {
        this.longitude = longtitude;
    }
}
