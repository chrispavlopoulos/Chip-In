package com.teamwd.chipin.Activities;

import android.content.Intent;
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

import com.teamwd.chipin.Fragments.DonationPaymentFragment;
import com.teamwd.chipin.Fragments.DonationSearchFragment;
import com.teamwd.chipin.Fragments.EventDonationFragment;
import com.teamwd.chipin.Models.Event;
import com.teamwd.chipin.R;

public class ActivityDonate extends AppCompatActivity {

    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        Intent intent = getIntent();
        event = (Event) intent.getSerializableExtra("event");
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

        if(event!= null){
            Bundle bundle = new Bundle();
            bundle.putSerializable("event", event);
            EventDonationFragment eventDonationFragment = new EventDonationFragment();
            eventDonationFragment.setArguments(bundle);
            transaction.add(R.id.container, eventDonationFragment,"EventDonation");
            transaction.commit();
            return;
        }

        String ein = null;
        if(getIntent().getExtras() != null)
            ein = getIntent().getExtras().getString("ein");
        if(ein == null){
            transaction.add(R.id.container, new DonationSearchFragment(),"DonationSearch");
            transaction.addToBackStack(null);
            transaction.commit();
        }else{
            DonationPaymentFragment donationPaymentFragment = new DonationPaymentFragment();
            Bundle bundle = new Bundle();
            bundle.putString("ein", ein);
            donationPaymentFragment.setArguments(bundle);
            transaction.add(R.id.container, donationPaymentFragment,"DonationPayment");
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    public Event getEvent() {
        return event;
    }

}
