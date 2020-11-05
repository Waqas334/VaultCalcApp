package com.androidbull.calculator.photo.vault.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.androidbull.calculator.photo.vault.MainApplication;
import com.androidbull.calculator.photo.vault.MyBassActivity;
import com.androidbull.calculator.photo.R;
import com.androidbull.calculator.photo.vault.fragment.HomeFragment;

public class HomeActivity extends MyBassActivity {

    private static final String TAG = "HomeActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));


        ImageView ivSettings = findViewById(R.id.hint);
        ivSettings.setOnClickListener(v -> startActivityForResult(new Intent(HomeActivity.this, SettingsActivity.class), 11));


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frame, new HomeFragment());
        ft.commit();

        int counter = MainApplication.getInstance().getTimeOpened() == -1 ? 0 : MainApplication.getInstance().getTimeOpened();
        counter++;

        MainApplication.getInstance().saveTimeOpened(counter);
        if (counter == 1) {
            //App Opened for the very first time
            showWelcomeDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == RESULT_OK) {
            recreate();
        }
    }

    private void showWelcomeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.welcome));
        String hint = getString(R.string.hint);
        builder.setMessage(getResources().getString(R.string.welcome_message, hint));
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();

    }


}

