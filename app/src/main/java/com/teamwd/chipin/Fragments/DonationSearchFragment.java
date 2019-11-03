package com.teamwd.chipin.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.OrganizationNew;
import com.teamwd.chipin.R;
import com.teamwd.chipin.Utils.UserDataProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

        UserDataProvider userDataProvider = UserDataProvider.getInstance(root.getContext());
        userDataProvider.getAllOrgs(new Interfaces.OrgsCallback() {
            @Override
            public void onCompleted(ArrayList<OrganizationNew> organizationNewArrayList) {
                setUpAdapter(organizationNewArrayList);
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(root.getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private void setUpAdapter(ArrayList<OrganizationNew> organizationNewArrayList) {

        Collections.sort(organizationNewArrayList, new Comparator<OrganizationNew>() {
            @Override
            public int compare(OrganizationNew organizationNew, OrganizationNew t1) {
                return organizationNew.getCharityName().compareTo(t1.getCharityName());
            }
        });

        RecyclerView recyclerView = root.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        OrgSearchAdapter adapter = new OrgSearchAdapter(organizationNewArrayList);
        recyclerView.setAdapter(adapter);
    }

    class OrgSearchAdapter extends RecyclerView.Adapter<OrgSearchAdapter.OrgViewHolder> {

        private ArrayList<OrganizationNew> organizations;

        // data is passed into the constructor
        OrgSearchAdapter(ArrayList<OrganizationNew> organizations) {
            this.organizations = organizations;
        }

        // inflates the row layout from xml when needed
        @Override
        public OrgViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
            View view = LayoutInflater.from(root.getContext()).inflate(R.layout.view_item_organization, parent, false);
            return new OrgViewHolder(view);
        }

        // binds the data to the TextView in each row
        @Override
        public void onBindViewHolder(final OrgViewHolder holder, int position) {
            final OrganizationNew organizationNew = organizations.get(position);
            try {
                Picasso.with(getContext()).load(organizationNew.getCategoryImage()).into(holder.orgImage);
                holder.orgCategoryText.setText(organizationNew.getCategoryName());
                holder.orgCauseText.setText(organizationNew.getCauseName());
                holder.orgNameText.setText(organizationNew.getCharityName());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        // total number of rows
        @Override
        public int getItemCount() {
            return organizations.size();
        }


        // stores and recycles views as they are scrolled off screen
        private class OrgViewHolder extends RecyclerView.ViewHolder {

            TextView orgCategoryText;
            TextView orgCauseText;
            ImageView orgImage;
            TextView orgNameText;
            //rating

            public OrgViewHolder(@NonNull View itemView) {
                super(itemView);

                orgCategoryText = itemView.findViewById(R.id.tv_org_category);
                orgCauseText = itemView.findViewById(R.id.tv_org_cause);
                orgImage = itemView.findViewById(R.id.iv_org_image);
                orgNameText = itemView.findViewById(R.id.tv_org_name);

            }
        }
    }

}
