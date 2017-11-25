package com.example.myrog.eatthemall;

import android.app.Dialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.myrog.eatthemall.Model.Order;
import com.example.myrog.eatthemall.Model.Request;
import com.example.myrog.eatthemall.ViewHolder.Cart2Adapter;
import com.example.myrog.eatthemall.manager.CartManager;
import com.example.myrog.eatthemall.manager.UserManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    LinearLayout linearLayout ;
    private Toolbar toolbar;
    private RecyclerView rvCart;
    private Cart2Adapter adapter;
    private TextView txtTotal;
    private Button btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart2);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        txtTotal = (TextView) findViewById(R.id.txt_total);
        configToolbar();
        setTotalText();
        configRecyclerView();
        configSendRequestButton();
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

    private void configSendRequestButton() {
        btnOrder = (Button) findViewById(R.id.btn_order);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CartManager.getInstance().getCount()==0){
                    return;
                }

                final Request request= new Request();
                CartManager cartManager = CartManager.getInstance();
                final UserManager userManager = UserManager.getInstance();
                request.setFoods(cartManager.loadCart());
                request.setPhone(userManager.getPhone());
                request.setName(userManager.getName());
                request.setAddress("Nhận tại cửa hàng");
                request.setTotal(cartManager.getTotal());
                request.setDateTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                        .format(Calendar.getInstance().getTime()));
                request.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());

                final Dialog dialog = new Dialog(CartActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_order);
                RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radio_group);
                RadioButton rbAddress = (RadioButton) dialog.findViewById(R.id.rb_address);
                final EditText etDifference = (EditText) dialog.findViewById(R.id.et_difference);
                Button btnOk = (Button) dialog.findViewById(R.id.btn_ok);
                TextView txtCancel = (TextView) dialog.findViewById(R.id.txt_cancel);

                if(!userManager.getAddress().equals("Chưa có địa chỉ")){
                    rbAddress.setVisibility(View.VISIBLE);
                    rbAddress.setText(userManager.getAddress());
                }
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.rb_store:
                                etDifference.setEnabled(false);
                                request.setAddress("Nhận tại cửa hàng");
                                break;
                            case R.id.rb_address:
                                etDifference.setEnabled(false);
                                request.setAddress(userManager.getAddress());
                                break;
                            case R.id.rb_difference:
                                etDifference.setEnabled(true);
                                request.setAddress(etDifference.getText().toString());
                                break;
                        }
                    }
                });

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(TextUtils.isEmpty(etDifference.getText()) && etDifference.isEnabled()){
                            Snackbar.make(linearLayout, "Địa chỉ không được bỏ rống",
                                    Snackbar.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        ref.child("Requests").child(ref.push().getKey()).setValue(request);
                        CartManager.getInstance().clear();
                        adapter.setData(CartManager.getInstance().loadCart());
                        setTotalText();
                        Snackbar.make(linearLayout, "Đặt món ăn thành công!",
                                Snackbar.LENGTH_SHORT)
                                .show();
                        dialog.dismiss();
                    }
                });

                txtCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }
}
