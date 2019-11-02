package com.teamwd.chipin.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.teamwd.chipin.R;

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkLogIn();
    }

    private void checkLogIn(){
        boolean loggedIn = false;

        if(!loggedIn){
            Intent loginIntent = new Intent(ActivityMain.this, ActivityLogIn.class);
            startActivity(loginIntent);
        }
    }

    public void openActivity(View view) {
        Intent myIntent = new Intent(this, ActivityDatabaseTest.class);
        this.startActivity(myIntent);
    }
}
