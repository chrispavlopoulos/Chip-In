package com.teamwd.chipin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.teamwd.chipin.Fragments.HomeFragment;
import com.teamwd.chipin.Fragments.OrgFragment;
import com.teamwd.chipin.Fragments.UserFragment;
import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.ViewPagerAdapter;
import com.teamwd.chipin.R;
import com.teamwd.chipin.Utils.OrganizationDataProvider;

import io.realm.Realm;

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

        if(ActivityLogIn.userLoggedIn) {

            setUpApp();

        }else{
            bottomNav.setVisibility(View.INVISIBLE);

            ActivityLogIn.asyncLogIn(getBaseContext(), new Interfaces.Callback() {
                @Override
                public void onSuccess() {
                    setUpApp();
                }

                @Override
                public void onError() {
                    startLogInActivity();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                setUpApp();
            }
            else if(resultCode == Activity.RESULT_CANCELED){
                startLogInActivity();
            }
        }
    }

    /**
     *  Only sets the app up if this closes with a result code of 1
     */
    private void startLogInActivity(){
        Intent intent = new Intent(ActivityMain.this, ActivityLogIn.class);
        overridePendingTransition(R.anim.slide_in_top, 0);
        startActivityForResult(intent, 1);
    }

    private void setUpApp(){
        bottomNav.setVisibility(View.VISIBLE);

        setUpRealm();
        setUpViewPager();

        //This is for testing only; comment this if not needed
        //startActivity(new Intent(this, ActivityDatabaseTest.class));
    }


    private void setUpRealm(){
        Realm.init(getBaseContext());
        //OrganizationDataProvider.getInstance(getBaseContext()).loadAllOrganizations();
    }

    private void setUpViewPager() {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), ViewPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int lastPosition = -1;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

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
