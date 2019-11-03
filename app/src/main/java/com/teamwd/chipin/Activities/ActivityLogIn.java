package com.teamwd.chipin.Activities;

import android.content.Context;
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
import com.teamwd.chipin.Utils.Validator;
import com.teamwd.chipin.Views.ChipButton;

public class ActivityLogIn extends AppCompatActivity {

    public static boolean userLoggedIn = false;

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

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

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
                //overridePendingTransition(R.anim.slide_in_bottom, R.anim.hold);
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
        String error = "";
        if(email.isEmpty()){
            error = "Please enter an email";
        }else if(password.isEmpty()){
            error = "Please enter your password";
        }else if(!Validator.validEmail(email)){
            error = "Please enter a valid email.";
        }

        if(!error.isEmpty()){
            showError(error);
            return;
        }


        UserDataProvider.getInstance(getBaseContext()).getUser(email, new Interfaces.UserCallback() {
            @Override
            public void onCompleted(ModelUser user) {
                if (user.getPassword().equals(password)){
                    emailField.setText(user.getEmail());
                    passwordField.setText(user.getPassword());

                    SharedPrefsUtil.saveUser(getBaseContext(), user);

                    userLoggedIn = true;

                    Intent result = new Intent();
                    result.putExtra("result", "granted");
                    setResult(RESULT_OK, result);
                    finish();
                    overridePendingTransition(0, R.anim.slide_out_top);
                }
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

    public static void asyncLogIn(final Context context, final Interfaces.Callback callback){
        final ModelUser savedUser = SharedPrefsUtil.getSavedUser(context);
        if (savedUser == null) {
            callback.onError();
            return;
        }


        UserDataProvider.getInstance(context).getUser(savedUser.getEmail(), new Interfaces.UserCallback() {
            @Override
            public void onCompleted(ModelUser user) {
                if(user.getPassword().equals(savedUser.getPassword()))
                    callback.onSuccess();
                else {
                    SharedPrefsUtil.clearSavedUser(context);
                    callback.onError();
                }
            }

            @Override
            public void onError(String msg) {
                SharedPrefsUtil.clearSavedUser(context);
                callback.onError();
            }
        });
    }
}
