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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.Donation;
import com.teamwd.chipin.Models.Event;
import com.teamwd.chipin.Models.ModelUser;
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
    public void getAllUsers(final DataProviderCallback callback){
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<ModelUser> modelUsersList = new ArrayList<>();
                            modelUsersList = new ArrayList<>();
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
                            callback.onCompleted();
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
                                        Long.parseLong(data.get("timeInMillis").toString()),
                                        data.get("ein").toString()
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

    /*
     */
/**
 * Use this method to add badge list for the user
 * Make sure to set donation using modelUser.addBadge
 *//*

    public void addBadgeList(ModelUser modelUser, final DataProviderCallback callback){

        DocumentReference ref = db.collection("users").document("badges");
        ref.update("badge", modelUser.getBadges());
    }

    */
/**
 * Gets the list of badges for the user
 *//*

    public void getBadges(String emailID, final BadgeCallback callback){

        db.collection("users")
                .document(emailID)
                .collection("badges")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> badges = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("MSG", document.getId() + " => " + document.getData());
                                Map data = document.getData();
                                if(data == null){
                                    callback.onError("No post for user" + task.getException().getMessage());
                                    return;
                                }
                                String badge = "";

                                badges.add(badge);
                            }
                            callback.onCompleted(badges);
                        } else {
                            callback.onError("Error getting documents: " + task.getException().getMessage());
                        }
                    }
                });
    }
*/

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
                                        (long) data.get("endTime")

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

}