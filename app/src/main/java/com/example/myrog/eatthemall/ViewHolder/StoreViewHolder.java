package com.example.myrog.eatthemall.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myrog.eatthemall.Interface.ItemClickListener;
import com.example.myrog.eatthemall.R;

/**
 * Created by Thinh on 09/11/2017.
 */

public class StoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtAddress;
    public ImageView imgCall;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public StoreViewHolder(View itemView) {
        super(itemView);

        txtAddress = (TextView) itemView.findViewById(R.id.txt_address);
        imgCall = (ImageView) itemView.findViewById(R.id.img_call);

        imgCall.setOnClickListener(this);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
