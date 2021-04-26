package com.example.androidcarmanager.View_EXPENCES;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.androidcarmanager.Adapter.pagerAdaptwe;
import com.example.androidcarmanager.user_info.Login_Screen;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.example.androidcarmanager.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class View_Expences_Screen extends AppCompatActivity  {

    TabLayout tabLayout;
    TabItem cleaningTab,fuelTab,maintanceTab,engineTab,PurchasesTab;
    ViewPager viewPager;
    ImageButton btnsearch;
    String vehicleId;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__expences__screen);
        getSupportActionBar().hide();

        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        cleaningTab =(TabItem)findViewById(R.id.cleaning);
        fuelTab =(TabItem)findViewById(R.id.fuel);
        maintanceTab =(TabItem)findViewById(R.id.maintance);
        PurchasesTab =(TabItem)findViewById(R.id.purchases);
        engineTab =(TabItem)findViewById(R.id.engine);
        viewPager =(ViewPager)findViewById(R.id.viewpager);
        btnsearch =(ImageButton)findViewById(R.id.searchIcon);
        firebaseAuth = FirebaseAuth.getInstance();

        btnsearch .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(View_Expences_Screen.this, Search_Screen.class);
                startActivity(i);
            }
        });
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            Intent i=new Intent(View_Expences_Screen.this, Login_Screen.class);
            startActivity(i);
        }
        vehicleId = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("key", "-1");


        PagerAdapter pagerAdapter =new pagerAdaptwe(getSupportFragmentManager(),
                tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem( tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem( tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem( tab.getPosition());
            }
        });

    }
}