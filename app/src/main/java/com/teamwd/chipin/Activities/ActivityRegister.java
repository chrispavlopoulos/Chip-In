package com.teamwd.chipin.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.teamwd.chipin.Utils.SharedPrefsUtil;
import com.teamwd.chipin.Utils.UserDataProvider;
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
        setContentView(R.layout.activity_register);

        if(getSupportActionBar() != null)
            getSupportActionBar().hide();

        firstNameField = findViewById(R.id.edit_first_name);
        lastNameField = findViewById(R.id.edit_last_name);
        emailField = findViewById(R.id.edit_email);
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


                final ModelUser newUser = new ModelUser(firstName, lastName, email, password);

                UserDataProvider.getInstance().addUser(newUser, new Interfaces.DataProviderCallback() {
                    @Override
                    public void onCompleted() {
                        SharedPrefsUtil.saveUser(getBaseContext(), newUser);
                        finish();
                    }

                    @Override
                    public void onError(String msg) {

                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
