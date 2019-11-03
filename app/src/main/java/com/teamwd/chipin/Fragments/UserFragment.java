package com.teamwd.chipin.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.auth.User;
import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.Donation;
import com.teamwd.chipin.Models.ModelUser;
import com.teamwd.chipin.Objects.Organization;
import com.teamwd.chipin.R;
import com.teamwd.chipin.Utils.UserDataProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.realm.Realm;

import static com.teamwd.chipin.Activities.ActivityDatabaseTest.getRandDouble;
import static com.teamwd.chipin.Activities.ActivityDatabaseTest.getRandString;
import static com.teamwd.chipin.Utils.SharedPrefsUtil.PREF_USER_EMAIL;
import static com.teamwd.chipin.Utils.SharedPrefsUtil.getSharedPrefs;

public class UserFragment extends ChipFragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = getContext();

        root = inflater.inflate(R.layout.fragment_user, container, false);

        SharedPreferences sharedPrefs = getSharedPrefs(context);
        String email = sharedPrefs.getString(PREF_USER_EMAIL, "");
        UserDataProvider dataProvider = UserDataProvider.getInstance(root.getContext());
        dataProvider.getDonations(email, new Interfaces.DonationsCallback() {
            @Override
            public void onCompleted(ArrayList<Donation> donations) {
                setAdapter(donations);
            }

            @Override
            public void onError(String msg) {

            }
        });
        dataProvider.getUser(email, new Interfaces.UserCallback() {
            @Override
            public void onCompleted(ModelUser user) {
                buildViews(user);
            }

            @Override
            public void onError(String msg) {

            }
        });

        return root;
    }

    private void buildViews(ModelUser user) {

        TextView name= root.findViewById(R.id.user_name);

        name.setText(user.getFirstName().substring(0, 1).toUpperCase() + user.getFirstName().substring(1) + " " + user.getLastName().substring(0, 1).toUpperCase() + user.getLastName().substring(1));

        TextView xpTV= root.findViewById(R.id.xpTV);
        xpTV.setText("Earned " + user.getScore()+"xp");


    }

    private void setAdapter(ArrayList<Donation> donations) {

        Collections.sort(donations, new Comparator<Donation>() {
            @Override
            public int compare(Donation donation, Donation t1) {
                return Long.compare(donation.getTimeInMillis(), t1.getTimeInMillis());
            }
        });
        RecyclerView recyclerView = root.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        UserDonationsAdapter adapter = new UserDonationsAdapter(root.getContext(), donations);
        recyclerView.setAdapter(adapter);

    }


    public class UserDonationsAdapter extends RecyclerView.Adapter<UserDonationsAdapter.ViewHolder> {

        private LayoutInflater mInflater;
        ArrayList<Donation> donations;

        UserDonationsAdapter(Context context, ArrayList<Donation> donations) {
            this.mInflater = LayoutInflater.from(context);
            this.donations = donations;
        }

        // inflates the row layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_user_donation, parent, false);
            return new ViewHolder(view);
        }

        // binds the data to the TextView in each row
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.amountTV.setText("$"+donations.get(position).getAmount());
            holder.charityNameTV.setText(donations.get(position).getCharityName());
        }

        // total number of rows
        @Override
        public int getItemCount() {
            return donations.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView charityNameTV;
            TextView amountTV;

            ViewHolder(View itemView) {
                super(itemView);
                charityNameTV = itemView.findViewById(R.id.charity_name);
                amountTV = itemView.findViewById(R.id.donation_amount);
            }
        }
    }
}
