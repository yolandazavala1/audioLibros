package com.example.audiolibros_zeuz.fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.audiolibros_zeuz.R;

public class PreferenciasFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

    }
}
