package com.teamwd.chipin.Fragments;

import android.content.Context;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.teamwd.chipin.Activities.ActivityMain;

import io.realm.Realm;

public class ChipFragment extends Fragment {

    Context context;
    private Realm realm;

    View root;


    Realm getRealm(){
        return realm == null? (realm = Realm.getDefaultInstance()): realm;
    }

    Toolbar getToolbar(){
        return ((ActivityMain) getActivity()).getToolbar();
    }

}
