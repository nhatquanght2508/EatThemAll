package com.example.myrog.eatthemall.ViewHolder;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myrog.eatthemall.Interface.ItemClickListener;
import com.example.myrog.eatthemall.R;

/**
 * Created by My Rog on 10/2/2017.
 */

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView food_name;
    public ImageView food_img;
    public TextView txtPrice;
    public FloatingActionButton btnCart;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FoodViewHolder(View itemView) {
        super(itemView);

        food_name = (TextView) itemView.findViewById(R.id.food_name);
        food_img = (ImageView) itemView.findViewById(R.id.food_image);
        txtPrice = (TextView) itemView.findViewById(R.id.txt_price);
        btnCart = (FloatingActionButton) itemView.findViewById(R.id.btn_cart);

        btnCart.setOnClickListener(this);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
