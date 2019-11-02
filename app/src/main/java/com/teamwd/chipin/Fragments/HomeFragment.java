package com.teamwd.chipin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.api.Distribution;
import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.Event;
import com.teamwd.chipin.R;
import com.teamwd.chipin.Utils.UserDataProvider;

import java.util.ArrayList;

public class HomeFragment extends ChipFragment{

    private RecyclerView mainRecyclerView;
    private RecyclerView eventRecyclerView;
    private UserDataProvider userDataProvider;
    private ArrayList<Event> events = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = getContext();

        root = inflater.inflate(R.layout.fragment_home, container, false);
        mainRecyclerView = root.findViewById(R.id.recycler_home_view);
        eventRecyclerView = root.findViewById(R.id.recycler_home_featured);
        userDataProvider = UserDataProvider.getInstance(getContext());
        userDataProvider.getAllEvents(new Interfaces.EventsCallback() {
            @Override
            public void onCompleted(ArrayList<Event> events) {

            }

            @Override
            public void onError(String msg) {

            }
        });
        return root;
    }

    public void setUpRecyclerViews(){
        //mainRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //mainRecyclerView.setAdapter(new HomeAdapter());
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        eventRecyclerView.setAdapter(new HomeFeaturedAdapter());
    }

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
/*
    private class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder>{

        public HomeAdapter(){
        }

        @NonNull
        @Override
        public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_item_donation, parent, false);
            return new HomeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return organizations.size();
        }
    }

    private class HomeViewHolder extends RecyclerView.ViewHolder{

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    */
     */
}
