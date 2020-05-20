package com.safe.gallery.calculator;

import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting_preferences);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {

        if (preference.getKey().equals("pref_forgot_password")) {

            Toast.makeText(getContext(), "Change password clicked", Toast.LENGTH_SHORT).show();

        }

        return super.onPreferenceTreeClick(preference);
    }
}
