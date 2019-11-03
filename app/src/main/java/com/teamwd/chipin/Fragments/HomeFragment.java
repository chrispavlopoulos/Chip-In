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
    protected ArrayList<Event> events = new ArrayList<>();
    protected ArrayList<Donation> donations = new ArrayList<>();

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
        //eventRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //eventRecyclerView.setAdapter(new HomeFeaturedAdapter());
    }
/*
    private class HomeFeaturedAdapter extends RecyclerView.Adapter<HomeFeaturedViewHolder>{

        public HomeFeaturedAdapter(){
        }

        @NonNull
        @Override
        public HomeFeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_home_featured_item, parent, false);
            return new HomeFeaturedViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HomeFeaturedViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return events.size();
        }
    }

    private class HomeFeaturedViewHolder extends RecyclerView.ViewHolder{
        ImageView companyPic;
        TextView eventCountdown;
        TextView eventTitle;
        TextView eventDescription;

        public HomeFeaturedViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
*/
class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<Donation> donations;
    private LayoutInflater mInflater;
    private Context ctx;

    // data is passed into the constructor
    RecyclerAdapter(Context context, ArrayList<Donation> data) {
        this.mInflater = LayoutInflater.from(context);
        this.donations = data;
        ctx = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = mInflater.inflate(R.layout.view_item_donation, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Donation donation = donations.get(position);
        final Context context = getContext();
        final ViewHolder hold = holder;
        holder.title.setText(donation.getCharityName());
        holder.time.setText(donation.getCharityName());
        holder.comment.setText(donation.getCharityName());
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

    // convenience method for getting data at click position
    Donation getItem(int id) {
        return donations.get(id);
    }
}
}
