package com.teamwd.chipin.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.Donation;
import com.teamwd.chipin.Models.ModelUser;
import com.teamwd.chipin.R;
import com.teamwd.chipin.Utils.UserDataProvider;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import static com.teamwd.chipin.Utils.SharedPrefsUtil.PREF_USER_EMAIL;
import static com.teamwd.chipin.Utils.SharedPrefsUtil.getSharedPrefs;

public class UserFragment extends ChipFragment{

    private TextView noDonationsView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private UserDataProvider dataProvider;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = getContext();

        root = inflater.inflate(R.layout.fragment_user, container, false);
        noDonationsView = root.findViewById(R.id.no_donations_text);
        recyclerView = root.findViewById(R.id.rv);
        swipeRefreshLayout = root.findViewById(R.id.user_swipe);

        SharedPreferences sharedPrefs = getSharedPrefs(context);
        final String email = sharedPrefs.getString(PREF_USER_EMAIL, "");
        dataProvider = UserDataProvider.getInstance(root.getContext());
        dataProvider.getDonations(email, new Interfaces.DonationsCallback() {
            @Override
            public void onCompleted(ArrayList<Donation> donations) {
                setAdapter(donations);
                if (donations.isEmpty()) {
                    noDonationsView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    noDonationsView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                dataProvider.getDonations(email, new Interfaces.DonationsCallback() {
                    @Override
                    public void onCompleted(ArrayList<Donation> donations) {
                        setAdapter(donations);
                        if (donations.isEmpty()) {
                            noDonationsView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            noDonationsView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(String msg) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                dataProvider.getUser(email, new Interfaces.UserCallback() {
                    @Override
                    public void onCompleted(ModelUser user) {
                        buildViews(user);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(String msg) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        return root;
    }

    private void buildViews(final ModelUser user) {

        TextView name= root.findViewById(R.id.user_name);

        name.setText(user.getFirstName().substring(0, 1).toUpperCase() + user.getFirstName().substring(1) + " " + user.getLastName().substring(0, 1).toUpperCase() + user.getLastName().substring(1));

        final TextView pointsTV= root.findViewById(R.id.points_tv);


        UserDataProvider userDataProvider = UserDataProvider.getInstance(root.getContext());
        userDataProvider.getDonations(user.getEmail(), new Interfaces.DonationsCallback() {
            @Override
            public void onCompleted(ArrayList<Donation> donations) {
                double value = 0;
                for(Donation donation : donations){
                    value += donation.getAmount();
                }

                pointsTV.setText(Math.round(value)+" points");

            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    private void setAdapter(ArrayList<Donation> donations) {

        Collections.sort(donations, new Comparator<Donation>() {
            @Override
            public int compare(Donation donation, Donation t1) {
                return Long.compare(donation.getTimeInMillis(), t1.getTimeInMillis());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        UserDonationsAdapter adapter = new UserDonationsAdapter(donations);
        recyclerView.setAdapter(adapter);

    }


    public class UserDonationsAdapter extends RecyclerView.Adapter<UserDonationsAdapter.ViewHolder> {

        ArrayList<Donation> donations;

        UserDonationsAdapter(ArrayList<Donation> donations) {
            this.donations = donations;
        }

        // inflates the row layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_item_donation, parent, false);
            return new ViewHolder(view);
        }

        // binds the data to the TextView in each row
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final Donation donation = donations.get(position);
            holder.title.setText(donation.getDonationTitle());
            Calendar cal = Calendar.getInstance();
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss aaa");
            cal.setTimeInMillis(donation.getTimeInMillis());
            holder.time.setText(df.format(cal.getTime()));
            holder.comment.setText(donation.getUserComment());
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            holder.donation.setText(formatter.format(donation.getAmount()));
        }

        // total number of rows
        @Override
        public int getItemCount() {
            return donations.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView title;
            TextView time;
            TextView comment;
            TextView donation;

            ViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.donation_name);
                time = itemView.findViewById(R.id.donation_time_stamp);
                comment = itemView.findViewById(R.id.donation_comment);
                donation = itemView.findViewById(R.id.donation_amount);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
            }
        }
    }
}
