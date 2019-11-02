package com.teamwd.chipin.Activities;

import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.teamwd.chipin.DataProviders.UserDataProvider;
import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.ModelUser;
import com.teamwd.chipin.R;
import com.teamwd.chipin.Views.ChipButton;

public class ActivityLogIn extends AppCompatActivity {

    EditText usernameField;
    EditText passwordField;
    ChipButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(getSupportActionBar() != null)
            getSupportActionBar().hide();

        usernameField = findViewById(R.id.edit_username);
        passwordField = findViewById(R.id.edit_password);
        loginButton = findViewById(R.id.button_login);

        setUpOnClicks();
    }

    private void setUpOnClicks() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                UserDataProvider.getInstance(getBaseContext()).addUser(new ModelUser(username, "pavs", "pavs.com"), new Interfaces.DataProviderCallback() {
                    @Override
                    public void onCompleted() {
                        finish();
                    }

                    @Override
                    public void onError(String msg) {

                    }
                });
                //login
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
