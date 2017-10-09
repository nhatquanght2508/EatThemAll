package com.example.myrog.eatthemall.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.myrog.eatthemall.Interface.ItemClickListener;
import com.example.myrog.eatthemall.R;

/**
 * Created by My Rog on 10/9/2017.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_Orderid,txt_Status , txt_Phone, txt_Address;
    private ItemClickListener itemClickListener;


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public OrderViewHolder(View itemView) {
        super(itemView);
        txt_Orderid = (TextView) itemView.findViewById(R.id.order_id);
        txt_Status = (TextView) itemView.findViewById(R.id.order_status);
        txt_Phone = (TextView) itemView.findViewById(R.id.order_phone);
        txt_Address = (TextView) itemView.findViewById(R.id.order_address);
        itemView.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
