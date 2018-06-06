package com.example.anu.smartbudgettracker;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;

public class UserMainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        int user_id= 12;
        toolbar= (Toolbar) findViewById(R.id.tbToolbar);
        setSupportActionBar(toolbar);
        tabLayout=(TabLayout) findViewById(R.id.tbLayout);
        ViewPager viewPager= (ViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new Dashboard(),"Home");
        viewPagerAdapter.addFragments(new Categories(),"Categories");
        viewPagerAdapter.addFragments(new Expenses(),"Expenses");
        viewPagerAdapter.addFragments(new Goal(), "Goals");
        viewPagerAdapter.addFragments(new History(),"History");
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(0);
        tabLayout.setupWithViewPager(viewPager);
        mViewPager = viewPager;


    }



}


