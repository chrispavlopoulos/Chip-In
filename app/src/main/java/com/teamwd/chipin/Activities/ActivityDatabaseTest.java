package com.teamwd.chipin.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.teamwd.chipin.Models.Event;
import com.teamwd.chipin.Models.OrganizationNew;
import com.teamwd.chipin.Utils.UserDataProvider;
import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.Donation;
import com.teamwd.chipin.Models.ModelUser;
import com.teamwd.chipin.Models.Post;
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

        testUserData();

        userDataProvider = UserDataProvider.getInstance(this);

        ArrayList<OrganizationNew> organizationNewArrayList = new ArrayList<>();

        for(int i=0; i< 5; i++){

            organizationNewArrayList.add(new OrganizationNew(
                    getRandString(),
                    getRandString(),
                    getRandString(),
                    getRandString(),
                    getRandString(),
                    getRandString(),
                    getRandString(),
                    123456
            ));

        }

        userDataProvider.addOrgsList(organizationNewArrayList, new Interfaces.DataProviderCallback() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(String msg) {

            }
        });

        userDataProvider.getAllOrgs(new Interfaces.OrgsCallback() {
            @Override
            public void onCompleted(ArrayList<OrganizationNew> organizationNewArrayList) {
                
            }

            @Override
            public void onError(String msg) {

            }
        });

    }

    private void testUserData() {
        String emailID = getRandString() + "@iastate.edu";
        ModelUser modelUser = new ModelUser(getRandString(),getRandString(), emailID, getRandString(), false);
        userDataProvider = UserDataProvider.getInstance(this);

        userDataProvider.addUser(modelUser, new Interfaces.DataProviderCallback() {
            @Override
            public void onCompleted() {
                //Toast.makeText(ActivityDatabaseTest.this, "Added user", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String msg) {

            }
        });

        userDataProvider.getAllUsers(new Interfaces.UserListCallback() {
            @Override
            public void onCompleted(ArrayList<ModelUser> user) {

            }

            @Override
            public void onError(String msg) {

            }
        });

        userDataProvider.getUser(emailID, new Interfaces.UserCallback() {
            @Override
            public void onCompleted(ModelUser user) {
                //Toast.makeText(ActivityDatabaseTest.this, "Got user!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String msg) {

            }
        });


        for(int i=0; i< 3; i++){
            Donation donation = new Donation(getRandString(),getRandDouble(),getRandString(), getRandString(), System.currentTimeMillis());
            modelUser.addDonation(donation);
        }


        userDataProvider.addDonationList(modelUser, new Interfaces.DataProviderCallback() {
            @Override
            public void onCompleted() {
                //Toast.makeText(ActivityDatabaseTest.this, "Got donation!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String msg) {

            }
        });

        userDataProvider.getDonations(emailID, new Interfaces.DonationsCallback() {
            @Override
            public void onCompleted(ArrayList<Donation> donations) {

            }

            @Override
            public void onError(String msg) {

            }
        });



        for(int i=0; i< 3; i++){
            Post post = new Post(getRandString(),System.currentTimeMillis());
            modelUser.addPost(post);
        }


        userDataProvider.addPostList(modelUser, new Interfaces.DataProviderCallback() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(String msg) {

            }
        });

        userDataProvider.getPosts(emailID, new Interfaces.PostsCallback() {
            @Override
            public void onCompleted(ArrayList<Post> posts) {

            }

            @Override
            public void onError(String msg) {

            }
        });

        modelUser.setScore((long) getRandDouble());
        userDataProvider.addScore(modelUser, new Interfaces.DataProviderCallback() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(String msg) {

            }
        });

        userDataProvider.getScore(emailID, new Interfaces.ScoreCallback() {
            @Override
            public void onCompleted(long score) {
            }

            @Override
            public void onError(String msg) {

            }
        });

        for(int i=0; i< 3; i++){
            modelUser.addBadge(getRandString());
        }
/*
        userDataProvider.addBadgeList(modelUser, new Interfaces.DataProviderCallback() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(String msg) {

            }
        });

        userDataProvider.getBadges(emailID, new Interfaces.BadgeCallback() {
            @Override
            public void onCompleted(ArrayList<String> badges) {

            }

            @Override
            public void onError(String msg) {

            }
        });
*/

        Event event = new Event(
                getRandString(),
                getRandString(),
                getRandString(),
                (long) getRandDouble(),
                0.75f,
                Long.parseLong("1572696676059"),
                Long.parseLong("1577858400000"),
                getRandString());
        userDataProvider.addEvent(event, new Interfaces.DataProviderCallback() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(String msg) {

            }
        });

        userDataProvider.getAllEvents(new Interfaces.EventsCallback() {
            @Override
            public void onCompleted(ArrayList<Event> events) {

            }

            @Override
            public void onError(String msg) {

            }
        });


        userDataProvider.getAllDonations(new Interfaces.DonationsListCallback() {
            @Override
            public void onCompleted(ArrayList<Donation> donations) {
                Log.e("stop","here");
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

    public static double getRandDouble() {
        String SALTCHARS = "0123456789";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 4) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return Double.parseDouble(saltStr);
    }

}
