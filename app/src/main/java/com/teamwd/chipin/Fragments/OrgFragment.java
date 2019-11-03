package com.teamwd.chipin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.teamwd.chipin.Activities.ActivityDonate;
import com.teamwd.chipin.Interfaces.Interfaces;
import com.teamwd.chipin.Models.OrganizationNew;

import com.teamwd.chipin.R;
import com.teamwd.chipin.Utils.OrganizationDataProvider;
import com.teamwd.chipin.Utils.UserDataProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class OrgFragment extends ChipFragment{

    private RecyclerView recyclerView;
    private ArrayList<OrganizationNew> organizations;
    private ArrayList<OrganizationNew> filterOrganizations;
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


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                //((ActivityMain) getActivity()).openSearch();
                break;
            case R.id.filter:
                break;
        }
        return super.onOptionsItemSelected(item);
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
                                filterOrganizations = new ArrayList<>(organizations);

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

                                            Collections.sort(organizationNewArrayList, new Comparator<OrganizationNew>() {
                                                @Override
                                                public int compare(OrganizationNew organizationNew, OrganizationNew t1) {
                                                    return organizationNew.getCharityName().compareTo(t1.getCharityName());
                                                }
                                            });

                                            organizations = organizationNewArrayList;
                                            filterOrganizations = new ArrayList<>(organizations);
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
                    Collections.sort(organizationNewArrayList, new Comparator<OrganizationNew>() {
                        @Override
                        public int compare(OrganizationNew organizationNew, OrganizationNew t1) {
                            return organizationNew.getCharityName().compareTo(t1.getCharityName());
                        }
                    });

                    organizations = organizationNewArrayList;
                    filterOrganizations = new ArrayList<>(organizations);
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
            final OrganizationNew organizationNew = filterOrganizations.get(position);
            try {
                Picasso.with(getContext()).load(organizationNew.getCategoryImage()).into(holder.orgImage);
                holder.orgCategoryText.setText(organizationNew.getCategoryName());
                holder.orgCauseText.setText(organizationNew.getCauseName());
                holder.orgNameText.setText(organizationNew.getCharityName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ActivityDonate.class);
                        intent.putExtra("ein",organizations.get(holder.getAdapterPosition()).getEin());
                        startActivity(intent);
                    }
                });
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        // total number of rows
        @Override
        public int getItemCount() {
            return filterOrganizations.size();
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


    public void onQueryTextSubmit(String query){
        filterOrganizations.clear();

        for(OrganizationNew org: organizations){
            if(org.getCharityName().toLowerCase().contains(query.toLowerCase()))
                filterOrganizations.add(org);
        }

        adapter.notifyDataSetChanged();
    }
}
