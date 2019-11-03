package com.teamwd.chipin.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.teamwd.chipin.Models.OrganizationNew;
import com.teamwd.chipin.R;

import java.util.ArrayList;

public class DonationSearchFragment extends ChipFragment {


    public DonationSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_donation_search, container, false);

        /*Button button = root.findViewById(R.id.temp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DonationPaymentFragment frag =new DonationPaymentFragment();
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,frag,"DonationPaymentFragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });*/

        return root;
    }

    public class OrganizationSearchAdapter extends RecyclerView.Adapter<OrganizationSearchAdapter.ViewHolder> {

        private LayoutInflater mInflater;
        ArrayList<OrganizationNew> organizations;

        OrganizationSearchAdapter(Context context, ArrayList<OrganizationNew> organizations) {
            this.mInflater = LayoutInflater.from(context);
            this.organizations = organizations;
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
            final OrganizationNew organization = organizations.get(position);
/*            holder.title.setText(donation.getCharityName());
            holder.time.setText(donation.getCharityName());
            holder.comment.setText(donation.getCharityName());
            holder.donation.setText("$"+ donation.getAmount());*/
        }

        // total number of rows
        @Override
        public int getItemCount() {
            return organizations.size();
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
