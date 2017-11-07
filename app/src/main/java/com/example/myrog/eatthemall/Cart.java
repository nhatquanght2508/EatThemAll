package com.example.myrog.eatthemall;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myrog.eatthemall.Common.Common;
import com.example.myrog.eatthemall.Database.Database;
import com.example.myrog.eatthemall.Model.Order;
import com.example.myrog.eatthemall.Model.Request;
import com.example.myrog.eatthemall.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    Button btnplace;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = (RecyclerView) findViewById(R.id.list_cart);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView) findViewById(R.id.total);
        btnplace = (Button) findViewById(R.id.btnplaceorder);



        btnplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showAlertDialog();
            }
        });
        loadListFood();
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(Cart.this);
        alertdialog.setTitle("Bước cuối cùng rồi !!!");
        alertdialog.setMessage("Cho chúng tôi biết địa chỉ giao hàng");

        final EditText editaddress = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        editaddress.setLayoutParams(lp);
        alertdialog.setView(editaddress);
        alertdialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alertdialog.setPositiveButton("Đồng ý!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getTen(),
                        editaddress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        cart
                );
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Cám ơn vì đã đặt hàng", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        alertdialog.setNegativeButton("Quay lại", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertdialog.show();
    }

    private void loadListFood() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        // tinh tong gia
        int total = 0;
        for (Order order:cart){
            total+= (Integer.parseInt(order.getPrice())*(Integer.parseInt(order.getQuantity())));
            Locale locale = new Locale("en","US");
            NumberFormat format = NumberFormat.getCurrencyInstance(locale);

            txtTotalPrice.setText(format.format(total));
        }
    }
}
