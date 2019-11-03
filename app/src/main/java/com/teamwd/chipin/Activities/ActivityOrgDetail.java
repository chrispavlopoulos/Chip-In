package com.teamwd.chipin.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.R;

public class ActivityOrgDetail extends AppCompatActivity {

    private TextView orgNameText;
    private TextView orgCategoryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_detail);

        if(getIntent().getExtras() != null)
            getIntent().getExtras().getString("ein");

        if(getSupportActionBar() != null)
            getSupportActionBar().hide();

        orgNameText = findViewById(R.id.tv_org_name);
        orgCategoryText = findViewById(R.id.tv_org_category);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
