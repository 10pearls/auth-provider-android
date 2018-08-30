package com.tenpearls.auth.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.tenpearls.auth.constants.KeyConstants;
import com.tenpearls.auth.core.AuthProvider;
import com.tenpearls.auth.entities.User;

public class PreferenceUtility {
    public static void setUser(User user) {
        SharedPreferences sharedPreferences = AuthProvider.getInstance().getContext().getSharedPreferences(KeyConstants.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KeyConstants.SHARED_PREFS_USER, user.toString());
        editor.apply();
    }

    public static User getUser(){
        SharedPreferences sharedPreferences = AuthProvider.getInstance().getContext().getSharedPreferences(KeyConstants.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(KeyConstants.SHARED_PREFS_USER, "");
        return User.fromString(json);
    }

    public static void clearAllPreferences(){
        SharedPreferences sharedPreferences = AuthProvider.getInstance().getContext().getSharedPreferences(KeyConstants.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }
}
