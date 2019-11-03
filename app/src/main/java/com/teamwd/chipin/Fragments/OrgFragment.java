package com.teamwd.chipin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.teamwd.chipin.Activities.ActivityMain;
import com.teamwd.chipin.Activities.ActivityOrgDetail;
import com.teamwd.chipin.Objects.Organization;
import com.teamwd.chipin.Objects.OrganizationCategory;
import com.teamwd.chipin.Objects.OrganizationCause;
import com.teamwd.chipin.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
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

        setUpRecyclerView();

        return root;
    }

    public void setUpRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        organizations = getRealm().where(Organization.class).findAll();

        recyclerView.setAdapter(new OrgAdapter(organizations, true));



    }

    private void startOrgDetailActivity(String ein){
        Bundle orgBundle = new Bundle();
        orgBundle.putString("ein", ein);

        startActivity(new Intent(getActivity(), ActivityOrgDetail.class), orgBundle);
    }

    private class OrgAdapter extends RealmRecyclerViewAdapter<Organization, OrgViewHolder>{


        public OrgAdapter(@Nullable OrderedRealmCollection<Organization> data, boolean autoUpdate) {
            super(data, autoUpdate);

        }

        @NonNull
        @Override
        public OrgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_item_organization, parent, false);
            return new OrgViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final OrgViewHolder holder, int position) {
            final Organization org = organizations.get(position);
            if(org == null || !org.isValid())
                return;

            OrganizationCategory category = org.getCategory();
            OrganizationCause cause = org.getCause();

            if(category != null){
                holder.orgCategoryText.setText(category.getCategoryName());
            }

            if(cause != null) {
                holder.orgCauseText.setText(cause.getCauseName());
                Picasso.with(context).load(cause.getImage()).into(holder.orgImage);
            }else {
                holder.orgCauseText.setText("General");
            }

            holder.orgNameText.setText(org.getCharityName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startOrgDetailActivity(org.getEin());
                }
            });
        }

        @Override
        public int getItemCount() {
            return organizations.size();
        }
    }

    private class OrgViewHolder extends RecyclerView.ViewHolder{

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
