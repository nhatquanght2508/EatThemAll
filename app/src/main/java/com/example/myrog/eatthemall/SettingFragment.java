package com.example.myrog.eatthemall;


import android.content.Context;
import android.icu.text.StringSearch;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myrog.eatthemall.manager.UserManager;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {
    LinearLayout linearLayout;

    private CircleImageView imgUser;
    private TextView txtName;
    private TextView txtPhone;
    private EditText etName;
    private ImageView imgEditName;
    private ImageView imgSaveName;

    private ImageView imgEditAddress;
    private ImageView imgSaveAddress;
    private TextView txtAddress;
    private EditText etAddress;

    private CardView cvFavorite;
    private CardView cvHistory;



    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        initControl(view);
        configInfoUser();
        return view;
    }

    private void initControl(View view) {
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        imgUser = (CircleImageView) view.findViewById(R.id.img_user) ;
        txtName = (TextView) view.findViewById(R.id.txt_name) ;
        txtPhone = (TextView) view.findViewById(R.id.txt_phone) ;
        etName = (EditText) view.findViewById(R.id.et_name);
        imgEditName = (ImageView) view.findViewById(R.id.img_edit_name) ;
        imgSaveName = (ImageView) view.findViewById(R.id.img_save_name);

        imgEditAddress = ( ImageView) view.findViewById(R.id.img_edit_address);
        imgSaveAddress = (ImageView) view.findViewById(R.id.img_save_address);
        txtAddress = (TextView) view.findViewById(R.id.txt_address);
        etAddress = (EditText) view.findViewById(R.id.et_address);

        cvFavorite = (CardView) view.findViewById(R.id.cv_favorite);
        cvHistory = (CardView) view.findViewById(R.id.cv_history);

    }

    private void configInfoUser() {
        if(UserManager.getInstance().getImageUrl() != null){
            Picasso.with(getActivity()).load(UserManager.getInstance().getImageUrl());
        }
        txtName.setText(UserManager.getInstance().getName());
        txtPhone.setText(UserManager.getInstance().getPhone());

        imgEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgEditName.setVisibility(View.GONE);
                imgSaveName.setVisibility(View.VISIBLE);
                txtName.setVisibility(View.GONE);
                etName.setVisibility(View.VISIBLE);
                if(!txtName.getText().equals("Chưa có tên")){
                    etName.setText(txtName.getText());
                }
            }
        });

        imgSaveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getActivity().getCurrentFocus();
                if( view != null){
                    InputMethodManager imm = (InputMethodManager)getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (TextUtils.isEmpty(etName.getText())){
                    Snackbar.make(linearLayout, "Tên không được bỏ trống",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                UserManager.getInstance().setName(etName.getText().toString());
                txtName.setText(etName.getText());

                imgEditName.setVisibility(View.VISIBLE);
                imgSaveName.setVisibility(View.GONE);
                txtName.setVisibility(View.VISIBLE);
                etName.setVisibility(View.GONE);

            }
        });

        imgEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgEditAddress.setVisibility(View.GONE);
                imgSaveAddress.setVisibility(View.VISIBLE);
                txtAddress.setVisibility(View.GONE);
                etAddress.setVisibility(View.VISIBLE);
                if(!txtAddress.getText().equals("Chưa có địa chỉ")){
                    etAddress.setText(txtAddress.getText());
                }
            }
        });

        imgSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getActivity().getCurrentFocus();
                if( view != null){
                    InputMethodManager imm = (InputMethodManager)getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (TextUtils.isEmpty(etAddress.getText())){
                    Snackbar.make(linearLayout, "Địa chỉ không được bỏ trống",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                UserManager.getInstance().setAddress(etAddress.getText().toString());
                txtAddress.setText(etAddress.getText());

                imgEditAddress.setVisibility(View.VISIBLE);
                imgSaveAddress.setVisibility(View.GONE);
                txtAddress.setVisibility(View.VISIBLE);
                etAddress.setVisibility(View.GONE);

            }
        });

        txtAddress.setText(UserManager.getInstance().getAddress());
    }
}
