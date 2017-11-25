package com.example.myrog.eatthemall.manager;

import com.example.myrog.eatthemall.Model.UserFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Thinh on 24/11/2017.
 */

public class UserManager {
    private static UserManager instance;
    private UserFirebase user;
    DatabaseReference database;

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void getUserFromFirebase(){
        database = FirebaseDatabase.getInstance().getReference();
        database.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        user = dataSnapshot.getValue(UserFirebase.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public String getPhone(){
        return user.getPhone();
    }

    public String getName(){
        return user.getName() == null ? "Chưa có tên" : user.getName();
    }

    public void setName(String name){
        user.setName(name);
        database.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child("name").setValue(name);
    }

    public String getAddress(){
        return user.getAddress() == null ? "Chưa có địa chỉ" : user.getAddress();
    }

    public void setAddress(String address){
        user.setAddress(address);
        database.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child("address").setValue(address);
    }

    public String getImageUrl(){
        return user.getImageUrl();
    }
}
