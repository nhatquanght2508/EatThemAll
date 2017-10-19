package com.example.myrog.eatthemall;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private CategoryFragment categoryFragment;
    private LocationFragment locationFragment;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        categoryFragment = categoryFragment.newInstance();
        locationFragment = locationFragment.newInstance();
        initView();

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
                R.drawable.ic_food,
                R.drawable.ic_location,
                R.drawable.ic_person
        };


        for (int i=0; i<tabLayout.getTabCount(); i++){
            TabLayout.Tab tabCall = tabLayout.getTabAt(i);
            tabCall.setIcon(tabIcons[i]);
            tabCall.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            if(i==0)
                tabCall.getIcon().setAlpha(255);
            else
                tabCall.getIcon().setAlpha(80);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(categoryFragment);
        adapter.addFragment(locationFragment);
        viewPager.setAdapter(adapter);
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

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "THỨC ĂN";
                case 1:
                    return "ĐỊA ĐIỂM";
                case 2:
                    return "TÀI KHOẢN";
            }
            return mFragmentList.get(position).getClass().getSimpleName();
        }
    }
}
