package com.teamwd.chipin.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.teamwd.chipin.DataProviders.UserDataProvider;
import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.ModelUser;
import com.teamwd.chipin.R;
import com.teamwd.chipin.Views.ChipButton;

public class ActivityRegister extends AppCompatActivity {

    EditText firstNameField;
    EditText lastNameField;
    EditText emailField;
    EditText passwordField;
    ChipButton registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(getSupportActionBar() != null)
            getSupportActionBar().hide();

        firstNameField = findViewById(R.id.edit_first_name);
        passwordField = findViewById(R.id.edit_password);
        registerButton = findViewById(R.id.button_register);

        setUpOnClicks();
    }

    private void setUpOnClicks() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = firstNameField.getText().toString();
                String lastName = lastNameField.getText().toString();
                String email = firstNameField.getText().toString();
                String password = passwordField.getText().toString();
                UserDataProvider.getInstance(getBaseContext()).addUser(new ModelUser(firstName, "pavs", "pavs.com"), new Interfaces.DataProviderCallback() {
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
