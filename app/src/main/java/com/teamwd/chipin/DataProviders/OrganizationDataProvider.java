package com.teamwd.chipin.DataProviders;

import android.content.Context;

import com.teamwd.chipin.Objects.Organization;

import java.util.ArrayList;

public class OrganizationDataProvider {

    private Context context;

    public OrganizationDataProvider(Context context) {
        this.context = context;
    }

    public ArrayList<Organization> loadAllOrganizations() {
        return new ArrayList<>();
    }

    public Organization getOrganization(String ein) {
        return new Organization();
    }

}
