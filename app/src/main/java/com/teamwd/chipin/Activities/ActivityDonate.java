package com.teamwd.chipin.Activities;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.teamwd.chipin.Fragments.DonationSearchFragment;
import com.teamwd.chipin.R;

public class ActivityDonate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        buildViews();
    }

    private void buildViews() {
        ImageView close = findViewById(R.id.close);
        final ActivityDonate activityDonate = this;
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityDonate.finish();
            }
        });

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, new DonationSearchFragment(),"DonationSearch");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed(){
        finish();
    }

}
