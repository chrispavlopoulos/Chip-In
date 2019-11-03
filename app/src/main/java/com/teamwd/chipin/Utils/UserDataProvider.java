package com.teamwd.chipin.Utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.Donation;
import com.teamwd.chipin.Models.Event;
import com.teamwd.chipin.Models.ModelUser;
import com.teamwd.chipin.Models.OrganizationNew;
import com.teamwd.chipin.Models.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserDataProvider extends Interfaces {

    private static FirebaseFirestore db;
    private static UserDataProvider instance = null;

    public UserDataProvider(Context context){
        db = FirebaseFirestore.getInstance();
    }

    static public UserDataProvider getInstance(Context context) {
        return instance == null ? instance = new UserDataProvider(context) : instance;
    }

    /**
     * Adds the user to the DB
     * @param modelUser The model user to add
     */
    public void addUser(ModelUser modelUser, final DataProviderCallback dataProviderCallback){
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();

        user.put("first", modelUser.getFirstName());
        user.put("last", modelUser.getLastName());
        user.put("email", modelUser.getEmail());
        user.put("password", modelUser.getPassword());
        user.put("isCompany", modelUser.isCompany());

        String documentPath = modelUser.getEmail();
        //DocumentReference documentReference = db.document(documentPath);

        // Add a new document with a generated ID
        db.collection("users").document(documentPath)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataProviderCallback.onCompleted();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dataProviderCallback.onError("Error adding document" + e.getMessage());
                    }
                });
    }

    /**
     * Get the list of all the users
     */
    public void getAllUsers(final UserListCallback callback){
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<ModelUser> modelUsersList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("MSG", document.getId() + " => " + document.getData());
                                Map data = document.getData();
                                ModelUser modelUser = new ModelUser(
                                        data.get("first").toString(),
                                        data.get("last").toString(),
                                        data.get("email").toString(),
                                        data.get("password").toString(),
                                        (Boolean) data.get("isCompany")
                                );
                                modelUsersList.add(modelUser);
                            }
                            callback.onCompleted(modelUsersList);
                        } else {
                            callback.onError("Error getting documents: " + task.getException().getMessage());
                        }
                    }
                });
    }

    public void getUser(String emailID, final UserCallback callback){
        try{
            db.collection("users")
                    .document(emailID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                Map<String, Object> data = documentSnapshot.getData();
                                if(data == null){
                                    callback.onError("No User found");
                                    return;
                                }
                                ModelUser modelUser = new ModelUser(
                                        data.get("first").toString(),
                                        data.get("last").toString(),
                                        data.get("email").toString(),
                                        data.get("password").toString(),
                                        (Boolean) data.get("isCompany")
                                );

                                callback.onCompleted(modelUser);
                            } else {
                                callback.onError("Error getting documents: " + task.getException().getMessage());
                            }
                        }
                    });
        }catch (Exception e){
            callback.onError("Error getting documents: " + e.getMessage());
        }
    }


    /**
     * Use this method to add donation for the user
     * Make sure to set donation using modelUser.setDonation
     */
    public void addDonationList(ModelUser modelUser, final DataProviderCallback callback){

        // Get a new write batch
        WriteBatch batch = db.batch();

        for(Donation donation : modelUser.getDonationList()){
            DocumentReference donationRef = db.collection("users").document(modelUser.getEmail()).collection("donations").document();
            batch.set(donationRef, donation);
        }

        // Commit the batch
        try{
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    callback.onCompleted();
                }
            });
        }catch (Exception e){
            callback.onError("Error adding document" + e.getMessage());
        }
    }

    public void addDonationItem(String email, Donation donation, final DataProviderCallback callback){

        // Get a new write batch
        WriteBatch batch = db.batch();
        DocumentReference donationRef = db.collection("users").document(email).collection("donations").document();
        batch.set(donationRef, donation);

        // Commit the batch
        try{
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    callback.onCompleted();
                }
            });
        }catch (Exception e){
            callback.onError("Error adding document" + e.getMessage());
        }
    }


    private int counter = 0 ;
    public void getAllDonations(final DonationsListCallback callback){

        getAllUsers(new UserListCallback() {
            @Override
            public void onCompleted(final ArrayList<ModelUser> users) {
                final ArrayList<Donation> allDonationsList = new ArrayList<>();
                for(int i=0; i< users.size(); i++) {

                    ModelUser modelUser = users.get(i);
                    final int numUsers = users.size();

                    getDonations(modelUser.getEmail(), new DonationsCallback() {
                        @Override
                        public void onCompleted(ArrayList<Donation> donations) {
                            allDonationsList.addAll(donations);
                            counter++;

                            if(numUsers == counter)
                                callback.onCompleted(allDonationsList);
                        }

                        @Override
                        public void onError(String msg) {

                        }
                    });

                }

            }

            @Override
            public void onError(String msg) {
                callback.onError(msg);
            }
        });
    }

    /**
     * Gets the list of donations for the user
     */
    public void getDonations(String emailID, final DonationsCallback callback){

        db.collection("users")
                .document(emailID)
                .collection("donations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Donation> donations = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("MSG", document.getId() + " => " + document.getData());
                                Map data = document.getData();
                                if(data == null){
                                    callback.onError("No donation for user" + task.getException().getMessage());
                                    return;
                                }
                                Donation donation = new Donation(
                                        data.get("charityName").toString(),
                                        Double.parseDouble(data.get("amount").toString()),
                                        data.get("donationTitle").toString(),
                                        data.get("userComment").toString(),
                                        Long.parseLong(data.get("timeInMillis").toString())
                                );
                                donations.add(donation);
                            }
                            callback.onCompleted(donations);
                        } else {
                            callback.onError("Error getting documents: " + task.getException().getMessage());
                        }
                    }
                });
    }

    /**
     * Use this method to add post list for the user
     * Make sure to set donation using modelUser.setPost
     */
    public void addPostList(ModelUser modelUser, final DataProviderCallback callback){

        // Get a new write batch
        WriteBatch batch = db.batch();

        for(Post post : modelUser.getPostList()){
            DocumentReference ref = db.collection("users").document(modelUser.getEmail()).collection("posts").document();
            batch.set(ref, post);
        }

        // Commit the batch
        try{
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    callback.onCompleted();
                }
            });
        }catch (Exception e){
            callback.onError("Error adding document" + e.getMessage());
        }
    }

    /**
     * Gets the list of posts for the user
     */
    public void getPosts(String emailID, final PostsCallback callback){

        db.collection("users")
                .document(emailID)
                .collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Post> posts = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("MSG", document.getId() + " => " + document.getData());
                                Map data = document.getData();
                                if(data == null){
                                    callback.onError("No post for user" + task.getException().getMessage());
                                    return;
                                }
                                Post post = new Post(
                                        data.get("postText").toString(),
                                        Long.parseLong(data.get("timeInMillis").toString())
                                );
                                posts.add(post);
                            }
                            callback.onCompleted(posts);
                        } else {
                            callback.onError("Error getting documents: " + task.getException().getMessage());
                        }
                    }
                });
    }

    /**
     * Adds the user's score to the DB
     * @param modelUser Send the most upto date score
     */
    public void addScore(ModelUser modelUser, final DataProviderCallback dataProviderCallback){
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();

        user.put("score", modelUser.getScore());

        String documentPath = modelUser.getEmail();
        //DocumentReference documentReference = db.document(documentPath);

        // Add a new document with a generated ID
        db.collection("users").document(documentPath)
                .update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataProviderCallback.onCompleted();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dataProviderCallback.onError("Error adding document" + e.getMessage());
                    }
                });
    }

    public void getScore(String emailID, final ScoreCallback callback){
        try{
            db.collection("users")
                    .document(emailID)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                Map<String, Object> data = documentSnapshot.getData();
                                if(data == null){
                                    callback.onError("No User found");
                                    return;
                                }
                                callback.onCompleted(Long.parseLong(data.get("score").toString()));
                            } else {
                                callback.onError("Error getting documents: " + task.getException().getMessage());
                            }
                        }
                    });
        }catch (Exception e){
            callback.onError("Error getting documents: " + e.getMessage());
        }
    }

    public void addEvent(Event event, final DataProviderCallback callback){

        // Get a new write batch
        WriteBatch batch = db.batch();
        DocumentReference ref = db.collection("events").document();
        batch.set(ref, event);

        // Commit the batch
        try{
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    callback.onCompleted();
                }
            });
        }catch (Exception e){
            callback.onError("Error adding document" + e.getMessage());
        }
    }

    public void getAllEvents(final EventsCallback eventsCallback){

        db.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Event> eventArrayList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("MSG", document.getId() + " => " + document.getData());
                                Map data = document.getData();
                                if(data == null){
                                    eventsCallback.onError("No post for user" + task.getException().getMessage());
                                    return;
                                }
                                Event event = new Event(
                                        data.get("companyName").toString(),
                                        data.get("evenTitle").toString(),
                                        data.get("eventDetails").toString(),
                                        (long) data.get("amountContributed"),
                                        Float.parseFloat(data.get("percentToMatch").toString()),
                                        (long) data.get("startTime"),
                                        (long) data.get("endTime"),
                                        data.get("goal").toString()

                                );
                                eventArrayList.add(event);
                            }
                            eventsCallback.onCompleted(eventArrayList);
                        } else {
                            eventsCallback.onError("Error getting documents: " + task.getException().getMessage());
                        }
                    }
                });
    }



    public void addOrgsList(ArrayList<OrganizationNew> organizationNewArrayList, final DataProviderCallback callback){

        // Get a new write batch
        WriteBatch batch = db.batch();

        for(OrganizationNew organizationNew : organizationNewArrayList){
            DocumentReference donationRef = db.collection("organizations").document(organizationNew.getEin());
            batch.set(donationRef, organizationNew);
        }

        // Commit the batch
        try{
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    callback.onCompleted();
                }
            });
        }catch (Exception e){
            callback.onError("Error adding document" + e.getMessage());
        }
    }

    public void getAllOrgs(final OrgsCallback orgsCallback){

        db.collection("organizations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<OrganizationNew> organizationArrayList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("MSG", document.getId() + " => " + document.getData());
                                Map data = document.getData();
                                if(data == null){
                                    orgsCallback.onError("No post for user" + task.getException().getMessage());
                                    return;
                                }
                                OrganizationNew organizationNew = new OrganizationNew(
                                        data.get("ein").toString(),
                                        data.get("categoryName").toString(),
                                        data.get("charityName").toString(),
                                        data.get("mission").toString(),
                                        data.get("categoryImage").toString(),
                                        data.get("causeName").toString(),
                                        data.get("state").toString(),
                                        Integer.parseInt(data.get("score").toString())

                                );
                                organizationArrayList.add(organizationNew);
                            }
                            orgsCallback.onCompleted(organizationArrayList);
                        } else {
                            orgsCallback.onError("Error getting documents: " + task.getException().getMessage());
                        }
                    }
                });
    }

    public void getOrg(String ein, final OrgCallback callback){

        try{
            db.collection("organizations")
                    .document(ein)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                Map<String, Object> data = documentSnapshot.getData();
                                if(data == null){
                                    callback.onError("No org found");
                                    return;
                                }
                                OrganizationNew organizationNew = new OrganizationNew(
                                        data.get("ein").toString(),
                                        data.get("categoryName").toString(),
                                        data.get("charityName").toString(),
                                        data.get("mission").toString(),
                                        data.get("categoryImage").toString(),
                                        data.get("causeName").toString(),
                                        data.get("state").toString(),
                                        Integer.parseInt(data.get("score").toString())
                                );

                                callback.onCompleted(organizationNew);
                            } else {
                                callback.onError("Error getting documents: " + task.getException().getMessage());
                            }
                        }
                    });
        }catch (Exception e){
            callback.onError("Error getting documents: " + e.getMessage());
        }
    }

}