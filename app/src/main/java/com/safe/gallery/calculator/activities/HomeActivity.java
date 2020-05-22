package com.safe.gallery.calculator.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;

import android.widget.ImageView;

import com.safe.gallery.calculator.MyBassActivity;
import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.fragment.HomeFragment;

public class HomeActivity extends MyBassActivity {

    ImageView iv_settings;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));


        iv_settings = findViewById(R.id.hint);
        iv_settings.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, SettingsActivity.class)));


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add((int) R.id.frame, new HomeFragment());
        ft.commit();
    }


}

