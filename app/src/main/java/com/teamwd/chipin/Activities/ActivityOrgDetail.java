package com.teamwd.chipin.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.R;

public class ActivityOrgDetail extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getSupportActionBar() != null)
            getSupportActionBar().hide();


    }

    @Override
    protected void onResume() {
        super.onResume();


    }

}
