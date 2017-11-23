package com.example.myrog.eatthemall;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myrog.eatthemall.manager.CartManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    private CategoryFragment categoryFragment;
    private LocationFragment locationFragment;
    private SettingFragment settingFragment;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        categoryFragment = categoryFragment.newInstance();
        locationFragment = locationFragment.newInstance();
        settingFragment = settingFragment.newInstance();
        initView();
        //configToolbar();

    }

    private void configToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_dashboard);
        this.setSupportActionBar(toolbar);

        ImageView ivCart = (ImageView) toolbar.findViewById(R.id.ivCart);
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartIntent = new Intent(DashboardActivity.this,CartActivity.class);
                startActivity(cartIntent);
            }
        });

        Locale locale = new Locale("vi","VN");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        TextView total = (TextView) toolbar.findViewById(R.id.txt_total);
        total.setText(format.format(CartManager.getInstance().getTotal()));

        TextView txtNumberCart = (TextView) toolbar.findViewById(R.id.txtNumberCart);
        txtNumberCart.setText(String.valueOf(CartManager.getInstance().getCount()));

    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabItem(tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setAlpha(255);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setAlpha(80);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupTabItem(TabLayout tabLayout){
        int[] tabIcons = {
                R.drawable.ic_food_green,
                R.drawable.ic_location,
                R.drawable.ic_person
        };


        for (int i=0; i<tabLayout.getTabCount(); i++){
            TabLayout.Tab tabCall = tabLayout.getTabAt(i);
            tabCall.setIcon(tabIcons[i]);
            tabCall.getIcon().setColorFilter(Color.argb(255,0,77,64),
                    PorterDuff.Mode.SRC_IN);
            if(i==0)
                tabCall.getIcon().setAlpha(255);
            else
                tabCall.getIcon().setAlpha(180);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(categoryFragment);
        adapter.addFragment(locationFragment);
        adapter.addFragment(settingFragment);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        configToolbar();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "THỨC ĂN";
//                case 1:
//                    return "ĐỊA ĐIỂM";
//                case 2:
//                    return "TÀI KHOẢN";
//            }
//            return mFragmentList.get(position).getClass().getSimpleName();
//        }
    }
}
