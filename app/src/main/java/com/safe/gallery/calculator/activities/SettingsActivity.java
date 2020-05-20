package com.safe.gallery.calculator.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SettingsFragment settingsFragment = new SettingsFragment();
        getFragmentManager().beginTransaction().add(R.id.settings_fragment_container, settingsFragment).commit();
    }
}
