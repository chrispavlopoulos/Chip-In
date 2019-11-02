package com.teamwd.chipin.DataProviders;

import android.content.Context;

import com.teamwd.chipin.Objects.Organization;

import java.util.ArrayList;

public class CharityDataProvider {

    private Context context;

    public CharityDataProvider(Context context) {
        this.context = context;
    }

    public ArrayList<Organization> getCharityList() {
        return new ArrayList<>();
    }

}
