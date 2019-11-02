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
import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.ModelUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.teamwd.chipin.Activities.ActivityDatabaseTest.getRandString;

public class UserDataProvider extends Interfaces {

    private static FirebaseFirestore db;
    private static UserDataProvider instance = null;
    private ArrayList<ModelUser> modelUsersList = new ArrayList<>();

    private UserDataProvider(Context context){
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
                            modelUsersList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("MSG", document.getId() + " => " + document.getData());
                                Map data = document.getData();
                                ModelUser modelUser = new ModelUser(
                                        data.get("first").toString(),
                                        data.get("last").toString(),
                                        data.get("email").toString(),
                                        data.get("password").toString()
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
        db.collection("users")
                .document(emailID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            Map<String, Object> data = documentSnapshot.getData();
                            ModelUser modelUser = new ModelUser(
                                    data.get("first").toString(),
                                    data.get("last").toString(),
                                    data.get("email").toString(),
                                    data.get("password").toString()
                            );
                            callback.onCompleted(modelUser);
                        } else {
                            callback.onError("Error getting documents: " + task.getException().getMessage());
                        }
                    }
                });

    }

    public ArrayList<ModelUser> getModelUsersList() {
        return modelUsersList;
    }
}
