package com.example.myrog.eatthemall.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.myrog.eatthemall.Model.Order;
import com.example.myrog.eatthemall.R;

import java.util.List;

/**
 * Created by Thinh on 07/11/2017.
 */

public class Cart2Adapter  extends RecyclerView.Adapter<Cart2Adapter.Cart2ViewHolder>{

    private final OnNumberClickListener itemClickListener;
    private OnDeleteClickedListener deleteListener;

    private List<Order> list;

    public Cart2Adapter(List<Order> list, OnNumberClickListener itemClickListener,
                        OnDeleteClickedListener deleteListener) {
        this.list = list;
        this.itemClickListener = itemClickListener;
        this.deleteListener = deleteListener;
    }

    public void setData(List<Order> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public Cart2ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        Cart2ViewHolder holder = new Cart2ViewHolder(view);
        return holder;    }

    @Override
    public void onBindViewHolder(Cart2Adapter.Cart2ViewHolder holder, int position) {
        if (list != null && 0 <= position && position < list.size()) {

            // Bind your data here
            setDataToViews(holder, position);
            holder.bind(list.get(position), itemClickListener, deleteListener);
        }

    }

    private void setDataToViews(Cart2ViewHolder holder, int position) {
        holder.txtNumber.setText(String.valueOf(position+1));
        holder.txtName.setText(list.get(position).getProductName());
        long price = Long.parseLong(list.get(position).getPrice()) *
                Integer.parseInt(list.get(position).getQuantity());
        holder.txtPrice.setText(String.valueOf(price));

        holder.btnNumber.setNumber(list.get(position).getQuantity());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnNumberClickListener {
        void onItemClick(Order item);
    }

    public interface OnDeleteClickedListener {
        void onDeleteClick(Order item);
    }

    public class Cart2ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNumber;
        private TextView txtName;
        private TextView txtPrice;
        ElegantNumberButton btnNumber;
        private ImageView imgClear;

        private Cart2ViewHolder(View v) {
            super(v);
            txtNumber = (TextView) v.findViewById(R.id.txt_number);
            txtName = (TextView) v.findViewById(R.id.txt_name);
            txtPrice = (TextView) v.findViewById(R.id.txt_price);
            btnNumber = (ElegantNumberButton) v.findViewById(R.id.btn_number);
            imgClear = (ImageView) v.findViewById(R.id.img_clear);


        }

        private void bind(final Order item, final OnNumberClickListener itemListener, final OnDeleteClickedListener deleteListener) {

            btnNumber.setOnClickListener(new ElegantNumberButton.OnClickListener() {
                @Override
                public void onClick(View view) {
                    list.get(list.indexOf(item)).setQuantity(btnNumber.getNumber());
                    long price = Long.parseLong(item.getPrice()) * Integer.parseInt(btnNumber.getNumber());
                    txtPrice.setText(String.valueOf(price));
                    itemListener.onItemClick(item);
                }
            });

            imgClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteListener.onDeleteClick(item);
                }
            });

        }
    }
}
