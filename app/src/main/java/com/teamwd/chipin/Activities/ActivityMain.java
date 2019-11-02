package com.teamwd.chipin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.teamwd.chipin.Fragments.HomeFragment;
import com.teamwd.chipin.Fragments.OrgFragment;
import com.teamwd.chipin.Fragments.UserFragment;
import com.teamwd.chipin.Models.ViewPagerAdapter;
import com.teamwd.chipin.R;

public class ActivityMain extends AppCompatActivity {

    ViewPager viewPager;
    BottomNavigationView bottomNav;

    private MenuItem lastCheckedItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getSupportActionBar() != null)
            getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.gradient_primary_dark_light));

        viewPager = findViewById(R.id.view_pager_main);
        bottomNav = findViewById(R.id.bottom_nav_main);

        checkLogIn();

        setUpRealm();
        setUpViewPager();

        //This is for testing only; comment this if not needed
        startActivity(new Intent(this, ActivityDatabaseTest.class));
    }

    private void checkLogIn(){
        boolean loggedIn = false;

        if(!loggedIn){
            Intent loginIntent = new Intent(ActivityMain.this, ActivityLogIn.class);
            startActivity(loginIntent);
        }
    }

    private void setUpRealm(){
        //Realm
    }

    private void setUpViewPager() {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), ViewPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int lastPosition = -1;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(lastPosition != -1 && lastPosition != position){
                    selectItem(position);
                }

                lastPosition = position;
            }

            @Override
            public void onPageSelected(int position) {
                if(lastPosition != -1 && lastPosition != position){
                    selectItem(position);
                }

                lastPosition = position;
            }

            private void selectItem(int position){
                if(lastCheckedItem != null){
                    lastCheckedItem.setChecked(false);
                }

                MenuItem newItem = bottomNav.getMenu().getItem(position);
                newItem.setChecked(true);
                lastCheckedItem = newItem;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(lastCheckedItem != null && menuItem != lastCheckedItem){
                    lastCheckedItem.setChecked(false);
                }

                menuItem.setChecked(true);
                lastCheckedItem = menuItem;

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

        bottomNav.setSelectedItemId(R.id.feed_tab);
        viewPager.setCurrentItem(1);
    }

    public void openActivity(View view) {
        Intent myIntent = new Intent(this, ActivityDatabaseTest.class);
        this.startActivity(myIntent);
    }

}
