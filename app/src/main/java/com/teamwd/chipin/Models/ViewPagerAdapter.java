package com.teamwd.chipin.Models;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.teamwd.chipin.Fragments.HomeFragment;
import com.teamwd.chipin.Fragments.OrgFragment;
import com.teamwd.chipin.Fragments.UserFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private UserFragment userFragment;
    private HomeFragment homeFragment;
    private OrgFragment orgFragment;


    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);

        userFragment = new UserFragment();
        homeFragment = new HomeFragment();
        orgFragment = new OrgFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return userFragment;
            case 1:
                return homeFragment;
            default:
                return orgFragment;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
