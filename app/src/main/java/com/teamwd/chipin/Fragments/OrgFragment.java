package com.teamwd.chipin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.teamwd.chipin.Objects.Organization;
import com.teamwd.chipin.R;

import io.realm.Realm;
import io.realm.RealmResults;

public class OrgFragment extends ChipFragment{

    private RecyclerView recyclerView;
    private RealmResults<Organization> organizations;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = getContext();

        root = inflater.inflate(R.layout.fragment_org, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);

        return root;
    }

    public void setUpRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new OrgAdapter());

    }

    private class OrgAdapter extends RecyclerView.Adapter<OrgViewHolder>{

        public OrgAdapter(){
            Realm realm = Realm.getDefaultInstance();
            organizations = realm.where(Organization.class).findAll();
            realm.close();
        }

        @NonNull
        @Override
        public OrgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_item_organization, parent, false);
            return new OrgViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull OrgViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return organizations.size();
        }
    }

    private class OrgViewHolder extends RecyclerView.ViewHolder{

        public OrgViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
