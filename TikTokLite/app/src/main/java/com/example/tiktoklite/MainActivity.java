package com.example.tiktoklite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Fragment> list;
    private ViewPager viewPager;
    private mFragmentPagerAdapter Adapter;

    private TabLayout mTabLayout;
    private final int[] TAB_TITLES = new int[]{R.string.world,R.string.home,R.string.info};



    private PlaceholderFragmentWorld mPlaceholderFragmentWorld;
    private PlaceholderFragmentHome mPlaceholderFragmentHome;
    private PlaceholderFragmentInfo mPlaceholderFragmentInfo;





    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        list = new ArrayList<>();
        list.add(new PlaceholderFragmentWorld());
        list.add(new PlaceholderFragmentHome());
        list.add(new PlaceholderFragmentInfo());
        Adapter = new mFragmentPagerAdapter(getSupportFragmentManager(), list);
        viewPager = (ViewPager) findViewById(R.id.viewpages);
        viewPager.setAdapter(Adapter);
        viewPager.setCurrentItem(0);



        mTabLayout = (TabLayout)findViewById(R.id.tablayout);
        for (int i = 0; i < TAB_TITLES.length; i++) {
            TabLayout.Tab tab = mTabLayout.newTab();
            View view = this.getLayoutInflater().inflate(R.layout.tab,null);
            tab.setCustomView(view);

            TextView tvTitle = (TextView)view.findViewById(R.id.tv_tab);
            tvTitle.setText(TAB_TITLES[i]);
            mTabLayout.addTab(tab);
        }
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));


    }



}