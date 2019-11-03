package com.teamwd.chipin.Utils;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.ModelUser;
import com.teamwd.chipin.Models.OrganizationNew;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import io.realm.Realm;

public class OrganizationDataProvider extends Interfaces {

    private static OrganizationDataProvider instance = null;
    private final static String ALL_ORGANIZATIONS_URL = "https://api.data.charitynavigator.org/v2/Organizations?app_id=3b2cf536&app_key=9f4111a9d6a9fe6034f7854d4ce07828&sort=RATING:DESC&pageSize=420";
    private final static String APP_ID = "3b2cf536";
    private final static String APP_KEY = "9f4111a9d6a9fe6034f7854d4ce07828";

    private OrganizationDataProvider(Context context){
    }

    static public OrganizationDataProvider getInstance(Context context) {
        return instance == null ? instance = new OrganizationDataProvider(context) : instance;
    }

    /**
     * Calls api to load in the organizations and save to realm to be accessed by the user
     */
    public void loadAllOrganizations(final Context context, final OrgsCallback orgsCallback) {
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_ORGANIZATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<OrganizationNew> organizationList = new ArrayList<>();
                try {
                    JSONArray organizations = new JSONArray(response);
                    for (int i = 0; i < organizations.length(); i++) {
                        JSONObject object = organizations.getJSONObject(i);
                        OrganizationNew organization = new OrganizationNew();
                        organization.setMission(object.getString("mission"));
                        organization.setCharityName(object.getString("charityName"));
                        organization.setEin(object.getString("ein"));
                        organization.setState(object.getJSONObject("mailingAddress").getString("stateOrProvince"));
                        if (object.has("cause")) {
                            organization.setCauseName(object.getJSONObject("cause").getString("causeName"));
                        }
                        if (object.has("category")) {
                            organization.setCategoryImage(object.getJSONObject("category").getString("image"));
                            organization.setCategoryName(object.getJSONObject("category").getString("categoryName"));
                        }

                        if (object.has("currentRating")) {
                            organization.setScore(object.getJSONObject("currentRating").getInt("score"));
                        }
                        organizationList.add(organization);
                    }
                    orgsCallback.onCompleted(organizationList);
                } catch (JSONException e) {
                    e.printStackTrace();
                    orgsCallback.onError("Error loading organizations");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                orgsCallback.onError("Error loading organizations");
            }
        });

        queue.add(stringRequest);
    }
}
