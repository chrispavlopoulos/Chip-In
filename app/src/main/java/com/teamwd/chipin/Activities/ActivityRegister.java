package com.teamwd.chipin.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.teamwd.chipin.Utils.SharedPrefsUtil;
import com.teamwd.chipin.Utils.UserDataProvider;
import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.ModelUser;
import com.teamwd.chipin.R;
import com.teamwd.chipin.Utils.Validator;
import com.teamwd.chipin.Views.ChipButton;

public class ActivityRegister extends AppCompatActivity {

    View root;
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

        root = findViewById(R.id.root);
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
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                String error = "";
                if(firstName.isEmpty())
                    error = "Enter your first name";
                else if(lastName.isEmpty())
                    error = "Enter your last name";
                else if(email.isEmpty()){
                    error = "Enter an email";
                }else if(password.isEmpty()){
                    error = "Enter a password";
                }else if(!Validator.validEmail(email)){
                    error = "Please enter a valid email";
                }

                if(!error.isEmpty()){
                    showError(error);
                    return;
                }

                final ModelUser newUser = new ModelUser(firstName, lastName, email, password, false);

                UserDataProvider.getInstance(getBaseContext()).addUser(newUser, new Interfaces.DataProviderCallback() {
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

    public void showError(String errorText) {

        final Snackbar snackbar = Snackbar.make(root, errorText, Snackbar.LENGTH_SHORT);

        snackbar
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
