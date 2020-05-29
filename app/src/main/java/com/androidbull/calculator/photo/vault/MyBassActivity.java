package com.androidbull.calculator.photo.vault;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MyBassActivity extends AppCompatActivity {


    private static final String TAG = "MyBaseActivity";

    private static int onStartCount = 0;
    private static int onStopCount = 0;


    @Override
    protected void onStop() {
        super.onStop();
        onStopCount++;
        Log.i(TAG, "onStop: count: " + onStopCount);
        //If user is online then we gonna save the online value as true, if not than time stemp of when he went offline

        if (onStopCount >= onStartCount) {
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        onStartCount++;
        Log.i(TAG, "onStart: count: " + onStartCount);
    }
}
