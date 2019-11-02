package com.safe.gallery.calculator.utils;

import android.content.Context;

import com.google.android.gms.ads.AdListener;

public class ToastAdListener extends AdListener {
    private Context mContext;

    public ToastAdListener(Context context) {
        this.mContext = context;
    }

    public void onAdLoaded() {
    }

    public void onAdFailedToLoad(int errorCode) {
        String errorReason = "";
        switch (errorCode) {
            case 0:
                errorReason = "Internal error";
                return;
            case 1:
                errorReason = "Invalid request";
                return;
            case 2:
                errorReason = "Network Error";
                return;
            case 3:
                errorReason = "No fill";
                return;
            default:
                return;
        }
    }

    public void onAdOpened() {
    }

    public void onAdClosed() {
    }

    public void onAdLeftApplication() {
    }
}
