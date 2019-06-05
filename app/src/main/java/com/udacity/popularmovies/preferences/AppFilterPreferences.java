package com.udacity.popularmovies.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.utilities.NetworkUtils;


public class AppFilterPreferences {
    public static String getSorting(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(context.getString(R.string.pref_filter_key), NetworkUtils.NOW_PLAYING);
    }

    public static void setSorting(Context context, String selectedFilterType) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(context.getString(R.string.pref_filter_key), selectedFilterType);
        editor.apply();
    }
}