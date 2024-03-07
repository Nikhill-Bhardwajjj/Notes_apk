package com.lihkin16.notes_apk.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPrefrences {

    private static final String PREF_NAME = "AppPreferences";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private final SharedPreferences preferences;

    public AppPrefrences(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void saveLoginStatus(boolean isLoggedIn) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }
}
