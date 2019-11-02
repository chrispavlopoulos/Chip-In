package com.teamwd.chipin.DataProviders;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
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
import com.teamwd.chipin.Objects.IrsClassification;
import com.teamwd.chipin.Objects.Organization;
import com.teamwd.chipin.Objects.OrganizationAddress;
import com.teamwd.chipin.Objects.OrganizationCause;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class OrganizationDataProvider extends Interfaces {

    private static OrganizationDataProvider instance = null;
    private ArrayList<Organization> organizations = new ArrayList<>();
    private final static String ALL_ORGANIZATIONS_URL = "https://api.data.charitynavigator.org/v2/Organizations?app_id=3b2cf536&app_key=9f4111a9d6a9fe6034f7854d4ce07828";
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
    public void loadAllOrganizations() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_ORGANIZATIONS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray organizations = new JSONArray(response);
                    for (int i = 0; i < organizations.length(); i++) {
                        JSONObject object = organizations.getJSONObject(i);
                        Organization organization = new Organization();
                        organization.setCharityNavigatorUrl(object.getString("charityNavigationURL"));
                        organization.setMission(object.getString("mission"));
                        organization.setWebsiteUrl(object.getString("websiteURL"));
                        organization.setTagLine(object.getString("tagLine"));
                        organization.setCharityName(object.getString("charityName"));
                        organization.setEin(object.getString("ein"));
                        organization.setOrgId(object.getInt("orgID"));

                        JSONObject irsObject = object.getJSONObject("irsClassification");
                        IrsClassification irsClassification = new IrsClassification();
                        irsClassification.setDeductibility(irsObject.getString("deductibility"));
                        irsClassification.setSubSection(irsObject.getString("subsection"));
                        irsClassification.setAssetAmount(irsObject.getInt("assetAmount"));
                        irsClassification.setIncomeAmount(irsObject.getInt("incomeAmount"));
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
                            cause.setImage(causeObj.getString(causeObj.getString("")));
                        }

                        if (object.has("category")) {

                        }

                        if (object.has("currentRating")) {

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }
}
