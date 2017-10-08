package com.example.myrog.eatthemall.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.ParcelUuid;

import com.example.myrog.eatthemall.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by My Rog on 10/3/2017.
 */
//Table Database đặt tên sai : OrderDetailt
public class Database extends SQLiteAssetHelper {
    private static final String DB_Name = "EatThemAll.db";
    private static final int DB_VER =1;
    public Database(Context context) {
        super(context, DB_Name, null, DB_VER);
    }
    public List<Order> getCarts(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect ={"ProductID","ProductName","Quantity","Price","Discount"};
        String sqlTable = "OrderDetailt";
        qb.setTables(sqlTable);

        //Con trỏ
        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null);

        final List<Order> result = new ArrayList<>();
        if (c.moveToFirst()){
            do {
                result.add(new Order(c.getString(c.getColumnIndex("ProductID")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Discount"))
                        ));
            }while (c.moveToNext());
        }
        return result;
    }

    public void addtoCart(Order order){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetailt(ProductID,ProductName,Quantity,Price,Discount) VALUES('%s','%s','%s','%s','%s');",
                order.getProductID(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount());
        db.execSQL(query);
    }

    public void cleanCart(){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetailt");
        db.execSQL(query);
    }
}
