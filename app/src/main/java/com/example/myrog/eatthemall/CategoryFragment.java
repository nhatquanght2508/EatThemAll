package com.example.myrog.eatthemall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myrog.eatthemall.Interface.ItemClickListener;
import com.example.myrog.eatthemall.Model.Category;
import com.example.myrog.eatthemall.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class CategoryFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference category;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;

    public CategoryFragment() {
        // Required empty public constructor
    }


    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        // Inflate the layout for this fragment
        database = FirebaseDatabase.getInstance();
        category = database.getReference(Constants.CATEGORY_REFERENCE);
        recycler_menu = (RecyclerView) view.findViewById(R.id.recycle_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recycler_menu.setLayoutManager(layoutManager);
        loadMenu();
        return view;
    }
    private void loadMenu() {
        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class,
                R.layout.menu_item,MenuViewHolder.class,category ) {
            @Override
            protected void populateViewHolder(final MenuViewHolder viewHolder, Category model, int position) {
                viewHolder.txtmenuname.setText(model.getName());
                Picasso.with(getActivity().getBaseContext()).load(model.getImage())
                        .into(viewHolder.imageView);
                final Category clickitem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Lấy idcategory và gửi đến activity mới
                        Intent foodlist = new Intent(getActivity(),FoodList.class);

                        // CategoryID là khóa nên chỉ cần lấy id của category
                        foodlist.putExtra(Constants.KEY_FOODLIST,adapter.getRef(viewHolder.getAdapterPosition()).getKey());
                        startActivity(foodlist);
                    }
                });
            }
        };
        recycler_menu.setAdapter(adapter);
    }


}
