package com.teamwd.chipin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.teamwd.chipin.Models.Event;
import com.teamwd.chipin.R;


public class EventDonationFragment extends ChipFragment {

    private Event event;

    public EventDonationFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_event_donation, container, false);
        if (getArguments() != null) {
            event = (Event) getArguments().getSerializable("event");
        }
        return root;
    }


}
