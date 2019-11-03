package com.teamwd.chipin.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.Donation;
import com.teamwd.chipin.Models.Event;
import com.teamwd.chipin.R;
import com.teamwd.chipin.Utils.UserDataProvider;
import com.teamwd.chipin.Views.ChipButton;

import static com.teamwd.chipin.Utils.SharedPrefsUtil.PREF_USER_EMAIL;
import static com.teamwd.chipin.Utils.SharedPrefsUtil.PREF_USER_FIRST_NAME;
import static com.teamwd.chipin.Utils.SharedPrefsUtil.PREF_USER_LAST_NAME;
import static com.teamwd.chipin.Utils.SharedPrefsUtil.getSharedPrefs;


public class EventDonationFragment extends ChipFragment {

    private Event event;
    private boolean isButtonEnabled = true;

    public EventDonationFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_event_donation, container, false);
        if (getArguments() != null) {
            event = (Event) getArguments().getSerializable("event");
        }

        if(event!= null){
            buildViews();
        }
        return root;
    }

    private void buildViews() {
        TextView companyName = root.findViewById(R.id.event_org_name);
        companyName.setText(event.getCompanyName());

        TextView eventDesc = root.findViewById(R.id.event_desc);
        eventDesc.setText(event.getEventDetails());

        final TextView eventTitle = root.findViewById(R.id.event_title);
        eventTitle.setText(event.getEvenTitle());

        TextView collected = root.findViewById(R.id.event_collected);
        collected.setText(String.valueOf(event.getAmountContributed()));

        TextView match = root.findViewById(R.id.event_match);
        match.setText("Matching "+(event.getPercentToMatch()*100)+"% of all donations");

        TextView goal = root.findViewById(R.id.event_goal);
        goal.setText(event.getGoal());


        final EditText amount = root.findViewById(R.id.event_donate_amt);
        final EditText comments = root.findViewById(R.id.event_comments);

        SharedPreferences sharedPrefs = getSharedPrefs(root.getContext());
        final String email = sharedPrefs.getString(PREF_USER_EMAIL, "");
        final String firstName = sharedPrefs.getString(PREF_USER_FIRST_NAME, "");
        final String lastName = sharedPrefs.getString(PREF_USER_LAST_NAME, "");

        ChipButton chipButton = root.findViewById(R.id.donate_button);
        chipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amount.getText().toString().trim().equals("") || amount.getText().toString().trim().equals("")){
                    Toast.makeText(root.getContext(), "Cannot be empty fields!", Toast.LENGTH_SHORT).show();
                    return;
                } else if(!isButtonEnabled){
                    return;
                }

                isButtonEnabled = false;
                final String donationTitle = firstName + " " + lastName + " donated to " + event.getEvenTitle();
                final Donation donation = new Donation(
                        event.getCompanyName(),
                        Double.valueOf(amount.getText().toString()),
                        donationTitle, comments.getText().toString(),
                        System.currentTimeMillis());

                final UserDataProvider dataProvider = UserDataProvider.getInstance(root.getContext());

                dataProvider.addDonationItem(email, donation, new Interfaces.DataProviderCallback() {
                    @Override
                    public void onCompleted() {
                        try{
                            getActivity().finish();
                        }catch (Exception e){}
                    }

                    @Override
                    public void onError(String msg) {
                        try{
                            Toast.makeText(root.getContext(), "Error: "+ msg, Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }catch (Exception e){}

                    }
                });

            }
        });



    }


}
