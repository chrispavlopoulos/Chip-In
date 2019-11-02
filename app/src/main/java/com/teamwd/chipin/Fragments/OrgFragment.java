package com.teamwd.chipin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.teamwd.chipin.R;

public class OrgFragment extends ChipFragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = getContext();

        root = inflater.inflate(R.layout.fragment_org, container, false);

        return root;
    }
}
