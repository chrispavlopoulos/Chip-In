package com.teamwd.chipin.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.teamwd.chipin.Models.ModelUser;

import javax.annotation.Nullable;

public class SharedPrefsUtil {

    public static final String DEFAULT_PREFS = "default_prefs";
    public static final String PREF_USER_FIRST_NAME = "user_first_name";
    public static final String PREF_USER_LAST_NAME = "user_last_name";
    public static final String PREF_USER_EMAIL = "user_email";
    public static final String PREF_USER_PASSWORD = "user_password";


    public static void saveUser(Context context, ModelUser user){
        getSharedPrefs(context).edit()
                .putString(PREF_USER_FIRST_NAME, user.getFirstName())
                .putString(PREF_USER_LAST_NAME, user.getLastName())
                .putString(PREF_USER_EMAIL, user.getEmail())
                .putString(PREF_USER_PASSWORD, user.getPassword())
                .apply();
    }

    @Nullable
    public static ModelUser getSavedUser(Context context){
        SharedPreferences sharedPrefs = getSharedPrefs(context);
        String firstName = sharedPrefs.getString(PREF_USER_FIRST_NAME, "");
        String secondName = sharedPrefs.getString(PREF_USER_LAST_NAME, "");
        String email = sharedPrefs.getString(PREF_USER_EMAIL, "");
        String password = sharedPrefs.getString(PREF_USER_PASSWORD, "");

        return email.isEmpty() || password.isEmpty()? null: new ModelUser(firstName, secondName, email, password, false);
    }

    public static void clearSavedUser(Context context){
        saveUser(context, new ModelUser("", "", "", "", false));
    }

    public static SharedPreferences getSharedPrefs(Context context){
        return context.getSharedPreferences(SharedPrefsUtil.DEFAULT_PREFS, Context.MODE_PRIVATE);
    }
}
