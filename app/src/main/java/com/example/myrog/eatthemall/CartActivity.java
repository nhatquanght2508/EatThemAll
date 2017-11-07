package com.example.myrog.eatthemall;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myrog.eatthemall.Model.Order;
import com.example.myrog.eatthemall.ViewHolder.Cart2Adapter;
import com.example.myrog.eatthemall.manager.CartManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView rvCart;
    private Cart2Adapter adapter;
    private TextView txtTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart2);
        txtTotal = (TextView) findViewById(R.id.txt_total);
        configToolbar();
        setTotalText();
        configRecyclerView();
    }

    private void setTotalText() {
        Locale locale = new Locale("vi","VN");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        txtTotal.setText(format.format(CartManager.getInstance().getTotal()));
    }

    private void configRecyclerView() {
        rvCart = (RecyclerView) findViewById(R.id.rv_cart);
        adapter = new Cart2Adapter(CartManager.getInstance().loadCart(),
                new Cart2Adapter.OnNumberClickListener() {
            @Override
            public void onItemClick(Order item) {
                CartManager.getInstance().updateOrder(item);
                setTotalText();
            }
        },
                new Cart2Adapter.OnDeleteClickedListener() {
            @Override
            public void onDeleteClick(Order item) {
                CartManager.getInstance().deleteOrderById(item.getProductID());
                adapter.setData(CartManager.getInstance().loadCart());
                setTotalText();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvCart.setLayoutManager(layoutManager);
        rvCart.setItemAnimator(new DefaultItemAnimator());
        rvCart.setAdapter(adapter);
    }

    private void configToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_dashboard);
        this.setSupportActionBar(toolbar);

        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView imgDelete = (ImageView) findViewById(R.id.img_delete);
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartManager.getInstance().clear();
                adapter.setData(CartManager.getInstance().loadCart());
                setTotalText();
            }
        });

    }
}
