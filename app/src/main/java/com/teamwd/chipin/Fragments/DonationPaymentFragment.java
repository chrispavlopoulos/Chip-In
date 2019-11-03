package com.teamwd.chipin.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamwd.chipin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DonationPaymentFragment extends ChipFragment {


    public DonationPaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_donation_payment, container, false);

        return root;
    }

}
