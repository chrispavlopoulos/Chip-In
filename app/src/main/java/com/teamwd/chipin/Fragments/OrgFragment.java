package com.teamwd.chipin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.squareup.picasso.Picasso;
import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.OrganizationNew;

import com.teamwd.chipin.R;
import com.teamwd.chipin.Utils.OrganizationDataProvider;
import com.teamwd.chipin.Utils.UserDataProvider;

import java.util.ArrayList;


public class OrgFragment extends ChipFragment{

    private RecyclerView recyclerView;
    private ArrayList<OrganizationNew> organizations;
    private SwipeRefreshLayout swipeRefreshLayout;
    private OrgRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = getContext();

        root = inflater.inflate(R.layout.fragment_org, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        swipeRefreshLayout = root.findViewById(R.id.org_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                forceUpdateDatabase();
            }
        });
        setUpRecyclerView();

        return root;
    }

    public void forceUpdateDatabase() {
        OrganizationDataProvider.getInstance(getContext()).loadAllOrganizations(getContext(), new Interfaces.OrgsCallback() {
            @Override
            public void onCompleted(ArrayList<OrganizationNew> organizationNewArrayList) {
                UserDataProvider.getInstance(getContext()).addOrgsList(organizationNewArrayList, new Interfaces.DataProviderCallback() {
                    @Override
                    public void onCompleted() {
                        UserDataProvider.getInstance(getContext()).getAllOrgs(new Interfaces.OrgsCallback() {
                            @Override
                            public void onCompleted(ArrayList<OrganizationNew> organizationNewArrayList) {
                                organizations = organizationNewArrayList;
                                adapter.notifyDataSetChanged();
                                swipeRefreshLayout.setRefreshing(false);
                            }

                            @Override
                            public void onError(String msg) {
                                onError("Error loading organizations");
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }

                    @Override
                    public void onError(String msg) {
                        onError("Error loading organizations");
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onError(String msg) {
                onError("Error loading organizations");
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void setUpRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        UserDataProvider.getInstance(getContext()).getAllOrgs(new Interfaces.OrgsCallback() {
            @Override
            public void onCompleted(ArrayList<OrganizationNew> organizationNewArrayList) {
                if (organizationNewArrayList.isEmpty()) {
                    OrganizationDataProvider.getInstance(getContext()).loadAllOrganizations(getContext(), new Interfaces.OrgsCallback() {
                        @Override
                        public void onCompleted(ArrayList<OrganizationNew> organizationNewArrayList) {
                            UserDataProvider.getInstance(getContext()).addOrgsList(organizationNewArrayList, new Interfaces.DataProviderCallback() {
                                @Override
                                public void onCompleted() {
                                    UserDataProvider.getInstance(getContext()).getAllOrgs(new Interfaces.OrgsCallback() {
                                        @Override
                                        public void onCompleted(ArrayList<OrganizationNew> organizationNewArrayList) {
                                            organizations = organizationNewArrayList;
                                            adapter = new OrgRecyclerAdapter();
                                            recyclerView.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onError(String msg) {
                                            onError("Error loading organizations");
                                        }
                                    });
                                }

                                @Override
                                public void onError(String msg) {
                                    onError("Error loading organizations");
                                }
                            });
                        }

                        @Override
                        public void onError(String msg) {
                            onError("Error loading organizations");
                        }
                    });
                } else {
                    organizations = organizationNewArrayList;
                    adapter = new OrgRecyclerAdapter();
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onError(String msg) {
                onError("Error loading organizations");
            }
        });
    }

    class OrgRecyclerAdapter extends RecyclerView.Adapter<OrgRecyclerAdapter.OrgViewHolder> {

        // data is passed into the constructor
        OrgRecyclerAdapter() {

        }

        // inflates the row layout from xml when needed
        @Override
        public OrgViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_item_organization, parent, false);
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
