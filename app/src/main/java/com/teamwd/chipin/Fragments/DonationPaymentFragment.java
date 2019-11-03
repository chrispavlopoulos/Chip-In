package com.teamwd.chipin.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.Donation;
import com.teamwd.chipin.Models.ModelUser;
import com.teamwd.chipin.Models.OrganizationNew;
import com.teamwd.chipin.R;
import com.teamwd.chipin.Utils.UserDataProvider;
import com.teamwd.chipin.Views.ChipButton;

import java.util.ArrayList;

import static com.teamwd.chipin.Utils.SharedPrefsUtil.PREF_USER_EMAIL;
import static com.teamwd.chipin.Utils.SharedPrefsUtil.PREF_USER_FIRST_NAME;
import static com.teamwd.chipin.Utils.SharedPrefsUtil.PREF_USER_LAST_NAME;
import static com.teamwd.chipin.Utils.SharedPrefsUtil.getSharedPrefs;

/**
 * A simple {@link Fragment} subclass.
 */
public class DonationPaymentFragment extends ChipFragment {


    private OrganizationNew organization;

    public DonationPaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_donation_payment, container, false);

        buildViews();

        return root;
    }

    private void buildViews() {
        final EditText amount = root.findViewById(R.id.amount_et);
        final EditText comments = root.findViewById(R.id.comments_et);
        SharedPreferences sharedPrefs = getSharedPrefs(root.getContext());
        final String email = sharedPrefs.getString(PREF_USER_EMAIL, "");
        final String firstName = sharedPrefs.getString(PREF_USER_FIRST_NAME, "");
        final String lastName = sharedPrefs.getString(PREF_USER_LAST_NAME, "");
        final String donationTitle = firstName + " " + lastName + " dontated to " + organization.getCharityName();

        ChipButton button = root.findViewById(R.id.donate_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(amount.getText().toString().trim().equals("") || amount.getText().toString().trim().equals("")){
                    Toast.makeText(root.getContext(), "Cannot be empty fields!", Toast.LENGTH_SHORT).show();
                    return;
                }

                final Donation donation = new Donation(
                        organization.getCharityName(),
                        Double.valueOf(amount.getText().toString()),
                        donationTitle, comments.getText().toString(),
                        System.currentTimeMillis());

                final UserDataProvider dataProvider = UserDataProvider.getInstance(root.getContext());

                dataProvider.getUser(email, new Interfaces.UserCallback() {
                    @Override
                    public void onCompleted(final ModelUser user) {

                        dataProvider.getDonations(email, new Interfaces.DonationsCallback() {
                            @Override
                            public void onCompleted(ArrayList<Donation> donations) {

                                ModelUser modelUser = user;
                                modelUser.setDonationList(donations);
                                modelUser.addDonation(donation);
                                dataProvider.addDonationList(modelUser, new Interfaces.DataProviderCallback() {
                                    @Override
                                    public void onCompleted() {
                                        getActivity().finish();
                                    }

                                    @Override
                                    public void onError(String msg) {
                                        Toast.makeText(root.getContext(), "Error: "+ msg, Toast.LENGTH_SHORT).show();
                                        getActivity().finish();
                                    }
                                });


                            }

                            @Override
                            public void onError(String msg) {
                                Toast.makeText(root.getContext(), "Error: "+ msg, Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                            }
                        });

                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(root.getContext(), "Error: "+ msg, Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                });

            }
        });

    }


    public void setOrg(OrganizationNew organizationNew){
        this.organization = organizationNew;
    }

}
