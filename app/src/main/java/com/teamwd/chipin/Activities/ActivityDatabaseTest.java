package com.teamwd.chipin.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.teamwd.chipin.DataProviders.UserDataProvider;
import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.ModelUser;
import com.teamwd.chipin.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * This activity is to test the DB!
 */
public class ActivityDatabaseTest extends AppCompatActivity {

    private UserDataProvider userDataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_test);

        String emailID = getRandString() + "@iastate.edu";
        ModelUser modelUser = new ModelUser(getRandString(),getRandString(), emailID, getRandString());
        userDataProvider = UserDataProvider.getInstance(this);

        userDataProvider.addUser(modelUser, new Interfaces.DataProviderCallback() {
            @Override
            public void onCompleted() {
                Toast.makeText(ActivityDatabaseTest.this, "Added user", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String msg) {

            }
        });

        userDataProvider.getAllUsers(new Interfaces.DataProviderCallback() {
            @Override
            public void onCompleted() {
                ArrayList<ModelUser> modelUsers = userDataProvider.getModelUsersList();

            }

            @Override
            public void onError(String msg) {
                Toast.makeText(ActivityDatabaseTest.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        userDataProvider.getUser(emailID, new Interfaces.UserCallback() {
            @Override
            public void onCompleted(ModelUser user) {
                Toast.makeText(ActivityDatabaseTest.this, "Got user!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String msg) {

            }
        });

    }


    /**
     * Creates stub code for us to use
     * @return random 4 char string
     */
    public static String getRandString() {
        String SALTCHARS = "qwertyuioplkjhgfdsazxcvbnm";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 4) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

}
