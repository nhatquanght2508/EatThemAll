package com.example.myrog.eatthemall;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.myrog.eatthemall.Database.Database;
import com.example.myrog.eatthemall.Interface.ItemClickListener;
import com.example.myrog.eatthemall.Model.Food;
import com.example.myrog.eatthemall.Model.Order;
import com.example.myrog.eatthemall.ViewHolder.FoodViewHolder;
import com.example.myrog.eatthemall.manager.CartManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {

    RelativeLayout relativeLayout;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodlist;

    String categoryId ="";

    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;

    //Search
    FirebaseRecyclerAdapter<Food,FoodViewHolder> searchadapter;
    List<String> suggestlist =new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Load Firebase
        database = FirebaseDatabase.getInstance();
        foodlist  = database.getReference("Foods");

        //RecyclerView
        relativeLayout = (RelativeLayout) findViewById(R.id.rootView);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Lấy intend

        if (getIntent() != null){
            categoryId = getIntent().getStringExtra("CategoryId");
        }
        if (!categoryId.isEmpty() && categoryId != null){
            loadListFood(categoryId);
        }
        materialSearchBar = (MaterialSearchBar) findViewById(R.id.seachbar);
        materialSearchBar.setHint("Tìm kiếm");
        LoadSuggest();

        materialSearchBar.setLastSuggestions(suggestlist);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Khi khách hàng nhập text , chuyển nó lên suggest list
                List<String> suggest = new ArrayList<String>();
                for (String search:suggestlist){
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())){
                        suggest.add(search);
                    }
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //Đóng searching bar
                // Restore suggest adapter gốc
                if (!enabled){
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //Khi search xong
                //Lấy kết quả adapter
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {
        searchadapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodlist.orderByChild("Name").equalTo(text.toString()) // so sánh tên
        )
        {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, final Food model, final int position) {
                viewHolder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_img);
                viewHolder.txtPrice.setText(model.getPrice());
                viewHolder.btnCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        new Database(getBaseContext()).addtoCart(new Order(
//                                adapter.getRef(position).getKey(),
//                                model.getName(),
//                                "1",
//                                model.getPrice(),
//                                model.getDiscount()
//
//                        ));
                        CartManager.getInstance().addOrder(new Order(
                                adapter.getRef(position).getKey(),
                                model.getName(),
                                "1",
                                model.getPrice(),
                                model.getDiscount()
                        ));
                        Snackbar.make(relativeLayout, "Đã thêm 1 "+ model.getName()+ " vào giỏ hàng",
                                Snackbar.LENGTH_SHORT)
                                .show();
                    }
                });

                final  Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
//                        //Mở activity mới -> food detail
//                        Intent foodDetail = new Intent(FoodList.this,FoodDetail.class);
//                        //Gửi id của food để bắt key
//                        foodDetail.putExtra("FoodId",searchadapter.getRef(position).getKey());
//                        startActivity(foodDetail);
                        configDialogFoodDetail(model, searchadapter.getRef(position).getKey());
                    }
                });
            }
        };
        recyclerView.setAdapter(searchadapter ); // set adapter cho list recycler lấy kết quả search
    }

    private void LoadSuggest() {
        foodlist.orderByChild("MenuId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Food item = postSnapshot.getValue(Food.class);
                    suggestlist.add(item.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadListFood(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodlist.orderByChild("MenuId").equalTo(categoryId)) { //   Select * from Foods where Menuid =
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, final Food model, final int position) {
                viewHolder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_img);
                viewHolder.txtPrice.setText(model.getPrice());
                viewHolder.btnCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        new Database(getBaseContext()).addtoCart(new Order(
//                                adapter.getRef(position).getKey(),
//                                model.getName(),
//                                "1",
//                                model.getPrice(),
//                                model.getDiscount()
//
//                        ));
                        CartManager.getInstance().addOrder(new Order(
                                adapter.getRef(position).getKey(),
                                model.getName(),
                                "1",
                                model.getPrice(),
                                model.getDiscount()
                        ));
                        Snackbar.make(relativeLayout, "Đã thêm 1 "+ model.getName()+ " vào giỏ hàng",
                                Snackbar.LENGTH_SHORT)
                                .show();
                    }
                });

                final  Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
//                        //Mở activity mới -> food detail
//                        Intent foodDetail = new Intent(FoodList.this,FoodDetail.class);
//                        //Gửi id của food để bắt key
//                        foodDetail.putExtra("FoodId",adapter.getRef(position).getKey());
//                        startActivity(foodDetail);
                        configDialogFoodDetail(model, adapter.getRef(position).getKey());

                    }
                });
            }
        };
        // nhớ

        // Luôn phải nhớ set Adapter , khổ lắm T.T
        recyclerView.setAdapter(adapter);
    }

    private void configDialogFoodDetail(final Food currentFood, final String foodId) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_food_detail);
        final ElegantNumberButton numberButton = (ElegantNumberButton)
                dialog.findViewById(R.id.number_button);
        Button btncart = (Button) dialog.findViewById(R.id.btn_cart);
        ImageButton btnClose = (ImageButton) dialog.findViewById(R.id.btn_close);

        TextView food_name = (TextView) dialog.findViewById(R.id.txt_foodname);
        TextView food_description = (TextView) dialog.findViewById(R.id.txt_food_description);
        final TextView food_price = (TextView) dialog.findViewById(R.id.txt_foodprice);
        ImageView food_img = (ImageView) dialog.findViewById(R.id.img_food);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)
                dialog.findViewById(R.id.collapsing);

        //Set Image
        Picasso.with(getBaseContext()).load(currentFood.getImage()).into(food_img);

        // đổ dữ liệu
        collapsingToolbarLayout.setTitle(currentFood.getName());
        food_price.setText(currentFood.getPrice());
        food_name.setText(currentFood.getName());
        food_description.setText(currentFood.getDescription());

        numberButton.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                long price = Long.parseLong(currentFood.getPrice()) *
                        Integer.parseInt(numberButton.getNumber());
                food_price.setText(String.valueOf(price));
            }
        });

        btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new Database(getBaseContext()).addtoCart(new Order(
//                        foodId,
//                        currentFood.getName(),
//                        numberButton.getNumber(),
//                        currentFood.getPrice(),
//                        currentFood.getDiscount()
//
//                ));
                CartManager.getInstance().addOrder(new Order(
                        foodId,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()
                ));
                Snackbar.make(relativeLayout, "Đã thêm " + numberButton.getNumber() + " " +
                                currentFood.getName()+ " vào giỏ hàng",
                        Snackbar.LENGTH_SHORT)
                        .show();
                //Toast.makeText(FoodList.this, "Đã thêm"+ currentFood.getName()+ "vào giỏ hàng", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
