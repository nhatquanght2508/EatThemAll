package com.example.myrog.eatthemall.Model;

/**
 * Created by My Rog on 9/27/2017.
 */

public class User {
    private String Ten;
    private String Matkhau;
    private String Phone;

    public User() {
    }

    public User(String ten, String matkhau) {
        Ten = ten;
        Matkhau = matkhau;

    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public String getMatkhau() {
        return Matkhau;
    }

    public void setMatkhau(String matkhau) {
        Matkhau = matkhau;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
