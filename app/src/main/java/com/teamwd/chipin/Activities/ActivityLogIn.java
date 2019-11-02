package com.teamwd.chipin.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

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

                //login
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
