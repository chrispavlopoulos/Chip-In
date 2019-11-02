package com.teamwd.chipin.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.ModelUser;
import com.teamwd.chipin.R;
import com.teamwd.chipin.Utils.SharedPrefsUtil;
import com.teamwd.chipin.Utils.UserDataProvider;
import com.teamwd.chipin.Views.ChipButton;

public class ActivityLogIn extends AppCompatActivity {

    View root;
    EditText emailField;
    EditText passwordField;
    ChipButton loginButton;
    TextView registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        root = findViewById(R.id.root);
        emailField = findViewById(R.id.edit_email);
        passwordField = findViewById(R.id.edit_password);
        loginButton = findViewById(R.id.button_login);
        registerButton = findViewById(R.id.button_register);

        setUpOnClicks();
    }

    private void setUpOnClicks() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                attemptLogIn(email, password);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityLogIn.this, ActivityRegister.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        ModelUser savedUser = SharedPrefsUtil.getSavedUser(getBaseContext());
        if (savedUser != null)
            attemptLogIn(savedUser.getEmail(), savedUser.getPassword());
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private void attemptLogIn(String email, final String password) {

        UserDataProvider.getInstance(getBaseContext()).getUser(email, new Interfaces.UserCallback() {
            @Override
            public void onCompleted(ModelUser user) {
                if (user.getPassword().equals(password))
                    finish();
                else
                    showError("Wrong password.");
            }

            @Override
            public void onError(String msg) {
                if(msg.equals("Null"))
                    return;

                showError("Email not found, sign up below!");
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
}
