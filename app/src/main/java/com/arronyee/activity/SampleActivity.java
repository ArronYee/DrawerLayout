package com.arronyee.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.arronyee.drawerlayout.DrawerWrapperLayout;


public class SampleActivity extends AppCompatActivity {

    private DrawerWrapperLayout drawerWrapperLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        drawerWrapperLayout = findViewById(R.id.drawer_layout);
        drawerWrapperLayout.addDrawerSwiperLayout();

    }



}
