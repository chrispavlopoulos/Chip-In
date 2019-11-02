package com.teamwd.chipin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.teamwd.chipin.Fragments.HomeFragment;
import com.teamwd.chipin.Fragments.SearchFragment;
import com.teamwd.chipin.Fragments.UserFragment;
import com.teamwd.chipin.R;

public class ActivityMain extends AppCompatActivity {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLogIn();
        BottomNavigationView btmNav = findViewById(R.id.bottom_nav_main);
        btmNav.setSelectedItemId(R.id.feed_tab);
        viewPager = findViewById(R.id.view_pager_main);
        btmNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.user_tab:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.feed_tab:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.organizations_tab:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });
    }

    private void checkLogIn(){
        boolean loggedIn = false;

        if(!loggedIn){
            Intent loginIntent = new Intent(ActivityMain.this, ActivityLogIn.class);
            startActivity(loginIntent);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        UserFragment userFragment = new UserFragment();
        HomeFragment homeFragment = new HomeFragment();
        SearchFragment searchFragment = new SearchFragment();
    }

    public void openActivity(View view) {
        Intent myIntent = new Intent(this, ActivityDatabaseTest.class);
        this.startActivity(myIntent);
    }

}
