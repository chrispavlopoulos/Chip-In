package com.teamwd.chipin.Utils;

import android.content.Context;
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
import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.ModelUser;
import com.teamwd.chipin.Objects.CurrentRating;
import com.teamwd.chipin.Objects.IrsClassification;
import com.teamwd.chipin.Objects.Organization;
import com.teamwd.chipin.Objects.OrganizationAddress;
import com.teamwd.chipin.Objects.OrganizationCategory;
import com.teamwd.chipin.Objects.OrganizationCause;

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
    private ArrayList<Organization> organizations = new ArrayList<>();
    private final static String ALL_ORGANIZATIONS_URL = "https://api.data.charitynavigator.org/v2/Organizations?app_id=3b2cf536&app_key=9f4111a9d6a9fe6034f7854d4ce07828&sort=RATING:DESC&pageSize=1000";
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
    public void loadAllOrganizations(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_ORGANIZATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                try {
                    JSONArray organizations = new JSONArray(response);
                    for (int i = 0; i < organizations.length(); i++) {
                        JSONObject object = organizations.getJSONObject(i);
                        Organization organization = new Organization();
                        if(object.has("charityNavigationURL"))
                            organization.setCharityNavigatorUrl(object.getString("charityNavigationURL"));
                        organization.setMission(object.getString("mission"));
                        organization.setWebsiteUrl(object.getString("websiteURL"));
                        organization.setTagLine(object.getString("tagLine"));
                        organization.setCharityName(object.getString("charityName"));
                        organization.setEin(object.getString("ein"));
                        if(object.has("orgID") && !object.isNull("orgID"))
                            organization.setOrgId(object.getInt("orgID"));

                        JSONObject irsObject = object.getJSONObject("irsClassification");
                        IrsClassification irsClassification = new IrsClassification();
                        irsClassification.setDeductibility(irsObject.getString("deductibility"));
                        irsClassification.setSubSection(irsObject.getString("subsection"));
                        if(object.has("assetAmount") && !object.isNull("assetAmount"))
                            irsClassification.setAssetAmount(irsObject.getInt("assetAmount"));
                        if(object.has("incomeAmount") && !object.isNull("incomeAmount"))
                            irsClassification.setIncomeAmount(irsObject.getInt("incomeAmount"));
                        if(object.has("filingRequirement") && !object.isNull("filingRequirement"))
                            irsClassification.setFilingRequirement(irsObject.getString("filingRequirement"));
                        irsClassification.setClassification(irsObject.getString("classification"));
                        irsClassification.setDeductibilityCode(irsObject.getString("deductibilityCode"));
                        organization.setIrsClassification(irsClassification);

                        JSONObject addressObj = object.getJSONObject("mailingAddress");
                        OrganizationAddress organizationAddress = new OrganizationAddress();
                        organizationAddress.setCountry(addressObj.getString("country"));
                        organizationAddress.setStateOrProvince(addressObj.getString("stateOrProvince"));
                        organizationAddress.setCity(addressObj.getString("city"));
                        organizationAddress.setPostalCode(addressObj.getString("postalCode"));
                        organizationAddress.setStreetAddress1(addressObj.getString("streetAddress1"));
                        organizationAddress.setStreetAddress2(addressObj.getString("streetAddress2"));
                        organization.setMailingAddress(organizationAddress);

                        if (object.has("cause")) {
                            JSONObject causeObj = object.getJSONObject("cause");
                            OrganizationCause cause = new OrganizationCause();
                            cause.setCauseId(causeObj.getInt("causeID"));
                            cause.setCauseName(causeObj.getString("causeName"));
                            cause.setImage(causeObj.getString(causeObj.getString("image")));
                            organization.setCause(cause);
                        }

                        if (object.has("category")) {
                            JSONObject categoryObj = object.getJSONObject("category");
                            OrganizationCategory category = new OrganizationCategory();
                            category.setImage(categoryObj.getString("image"));
                            category.setCategoryName(categoryObj.getString("categoryName"));
                            category.setCategoryId(categoryObj.getInt("categoryID"));
                            organization.setCategory(category);
                        }

                        if (object.has("currentRating")) {
                            JSONObject ratingObj = object.getJSONObject("currentRating");
                            JSONObject ratingImgs = ratingObj.getJSONObject("ratingImage");
                            CurrentRating rating = new CurrentRating();
                            rating.setScore(ratingObj.getInt("score"));
                            rating.setRatingId(ratingObj.getInt("ratingID"));
                            rating.setPublicationDate(ratingObj.getString("publicationDate"));
                            rating.setLargeImg(ratingImgs.getString("large"));
                            rating.setSmallImg(ratingImgs.getString("small"));
                            rating.setRating(ratingObj.getInt("rating"));
                            organization.setCurrentRating(rating);
                        }


                        realm.copyToRealmOrUpdate(organization);

                    }
                    realm.commitTransaction();
                    realm.close();

                } catch (JSONException e) {
                    realm.close();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(stringRequest);
    }
}
