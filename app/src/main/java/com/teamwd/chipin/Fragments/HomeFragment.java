package com.teamwd.chipin.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.Donation;
import com.teamwd.chipin.Models.Event;
import com.teamwd.chipin.R;
import com.teamwd.chipin.Utils.UserDataProvider;

import java.util.ArrayList;

public class HomeFragment extends ChipFragment{

    private RecyclerView mainRecyclerView;
    private RecyclerView eventRecyclerView;
    private UserDataProvider userDataProvider;
    protected ArrayList<Event> eventsList = new ArrayList<>();
    protected ArrayList<Donation> donationsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = getContext();

        root = inflater.inflate(R.layout.fragment_home, container, false);
        mainRecyclerView = root.findViewById(R.id.recycler_home_view);
        eventRecyclerView = root.findViewById(R.id.recycler_home_featured);
        userDataProvider = UserDataProvider.getInstance(getContext());
        setUpRecyclerViews();
        return root;
    }

    public void setUpRecyclerViews(){
        userDataProvider.getAllDonations(new Interfaces.DonationsListCallback() {
            @Override
            public void onCompleted(ArrayList<Donation> donations) {
                mainRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                mainRecyclerView.setAdapter(new RecyclerAdapter(getContext(), donations));
            }

            @Override
            public void onError(String msg) {
                onError("Error loading donations.");
            }
        });
        userDataProvider.getAllEvents(new Interfaces.EventsCallback() {
            @Override
            public void onCompleted(ArrayList<Event> events) {
                eventsList = events;
            }

            @Override
            public void onError(String msg) {
                onError("Error loading events.");
            }
        });
        //eventRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //eventRecyclerView.setAdapter(new HomeFeaturedAdapter());
    }

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    // data is passed into the constructor
    RecyclerAdapter() {

    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = null;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.fragment_home_featured, parent, false);
                break;
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.view_item_donation, parent, false);
                break;
        }
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Donation donation = donationsList.get(position);
        holder.title.setText(donation.getCharityName());
        holder.time.setText(donation.getCharityName());
        holder.comment.setText(donation.getCharityName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return donationsList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView time;
        TextView comment;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.donation_name);
            time = itemView.findViewById(R.id.donation_time_stamp);
            comment = itemView.findViewById(R.id.donation_comment);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Donation donation = getItem(getAdapterPosition());
            //showDetailView(result);
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        RecyclerView recyclerView;

        ViewHolder2(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycler_home_featured);
        }

        @Override
        public void onClick(View view) {
            Donation donation = getItem(getAdapterPosition());
            //showDetailView(result);
        }
    }

    // convenience method for getting data at click position
    Donation getItem(int id) {
        return donationsList.get(id);
    }
}
}
