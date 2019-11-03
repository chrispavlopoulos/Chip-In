package com.teamwd.chipin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.teamwd.chipin.Fragments.HomeFragment;
import com.teamwd.chipin.Fragments.OrgFragment;
import com.teamwd.chipin.Fragments.UserFragment;
import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.ModelUser;
import com.teamwd.chipin.Models.OrganizationNew;
import com.teamwd.chipin.Models.ViewPagerAdapter;
import com.teamwd.chipin.R;
import com.teamwd.chipin.Utils.OrganizationDataProvider;
import com.teamwd.chipin.Utils.SharedPrefsUtil;
import com.teamwd.chipin.Utils.UserDataProvider;
import com.teamwd.chipin.Views.FluidSearchView;

import java.util.ArrayList;

import io.realm.Realm;

public class ActivityMain extends AppCompatActivity {

    View content;
    Toolbar toolbar;
    View navBarBackground;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    BottomNavigationView bottomNav;
    private static boolean IS_TESTING_DB = false;

    private MenuItem lastCheckedItem = null;
    private int lastPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content = findViewById(R.id.wrapper_content);
        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.search_view);
        navBarBackground = findViewById(R.id.navigation_background);
        viewPager = findViewById(R.id.view_pager_main);
        bottomNav = findViewById(R.id.bottom_nav_main);


        setSupportActionBar(toolbar);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        addMarginForNavigationBar();

        if (IS_TESTING_DB) {
            Intent intent = new Intent(this, ActivityDatabaseTest.class);
            startActivity(intent);
            return;
        }


        if (ActivityLogIn.userLoggedIn) {

            setUpApp();

        } else {
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

    private void addMarginForNavigationBar() {

        int navigationBarHeight = 0;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight)
            navigationBarHeight = realHeight - usableHeight;

        ((ViewGroup.MarginLayoutParams) content.getLayoutParams()).bottomMargin = navigationBarHeight;
        navBarBackground.getLayoutParams().height = navigationBarHeight;
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
            } else if (resultCode == Activity.RESULT_CANCELED) {
                startLogInActivity();
            }
        }
    }

    /**
     * Only sets the app up if this closes with a result code of 1
     */
    private void startLogInActivity() {
        Intent intent = new Intent(ActivityMain.this, ActivityLogIn.class);
        overridePendingTransition(R.anim.slide_in_top, 0);
        startActivityForResult(intent, 1);
    }

    private void setUpApp() {
        bottomNav.setVisibility(View.VISIBLE);
        setUpViewPager();
        setUpSearch();
        //This is for testing only; comment this if not needed
        //startActivity(new Intent(this, ActivityDatabaseTest.class));
    }

    private void setUpViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), ViewPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(viewPagerAdapter);

        lastPosition = 1;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (lastPosition != -1 && lastPosition != position) {
                    selectItem(position);
                }
                lastPosition = position;
            }

            private void selectItem(int position) {
                if (lastCheckedItem != null) {
                    lastCheckedItem.setChecked(false);
                }

                MenuItem newItem = bottomNav.getMenu().getItem(position);
                newItem.setChecked(true);
                lastCheckedItem = newItem;


                if(position == 2){
                    searchView.show();
                }else{
                    if(searchView.isOpened())
                        searchView.onClose(true);
                    searchView.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (lastCheckedItem != null && menuItem != lastCheckedItem) {
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

    public void logOut() {
        SharedPrefsUtil.saveUser(getBaseContext(), new ModelUser("", "", "", "", false));

        ActivityLogIn.userLoggedIn = false;

        startLogInActivity();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.org_menu, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Fragment fragment = viewPagerAdapter.getItem(lastPosition);
        if(fragment instanceof OrgFragment)
            return fragment.onOptionsItemSelected(item);
        else
            return super.onOptionsItemSelected(item);
    }

    FluidSearchView searchView;

    private void setUpSearch() {
        searchView.init(toolbar, this);
        searchView.hide();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.isEmpty()) return true;
                Fragment orgFrag = viewPagerAdapter.getItem(2);
                if(!(orgFrag instanceof OrgFragment))
                    return true;

                ((OrgFragment) orgFrag).onQueryTextSubmit(query);

                return true;
            }
        });

    }
}
