package com.androidbull.calculator.photo.vault;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.SwitchPreference;

import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import androidx.multidex.BuildConfig;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;


import com.androidbull.calculator.photo.R;
import com.androidbull.calculator.photo.vault.activities.NewChangePasswordActivity;
import com.androidbull.calculator.photo.vault.activities.SecurityQuestionActivity;
import com.androidbull.calculator.photo.vault.dialog.AboutUsDialog;
import com.androidbull.calculator.photo.vault.activities.PrivacyPolicyActivity;
import com.androidbull.calculator.photo.vault.utils.Utils;

import static com.androidbull.calculator.photo.vault.MainApplication.theme_boolean;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    public Context mContext;
    private Activity mActivity;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting_preferences);
        mContext = this.getActivity();
        mActivity = this.getActivity();
        SharedPreferences mPrefs = mContext.getSharedPreferences("THEME", 0);
        theme_boolean = mPrefs.getBoolean("theme_boolean", true);
        if (theme_boolean) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();
        switch (key) {
            case "pref_change_security_question":
                Intent changeSecurityQuestion = new Intent(getContext(), SecurityQuestionActivity.class);
                changeSecurityQuestion.putExtra(SecurityQuestionActivity.TYPE, SecurityQuestionActivity.CHANGE);
                startActivity(changeSecurityQuestion);
                return true;

            case "pref_forgot_password":
                showForgotPasswordDialog();
                return true;

            case "pref_change_password":
                startActivity(new Intent(getContext(), NewChangePasswordActivity.class));
                return true;

            case "pref_dark_mode":
                return true;

            case "pref_about_us":
                AboutUsDialog aboutUsDialog = new AboutUsDialog();
                aboutUsDialog.show(getChildFragmentManager(), "ABOUT_US");
                return true;

            case "pref_share_app":
                share();
                return true;

            case "pref_rate_app":
                rateUs();
                return true;

            case "pref_contact_us":
                openEmail("Contact");
                return true;

            case "pref_privacy_policy":
                startActivity(new Intent(getContext(), PrivacyPolicyActivity.class));
                return true;

            default:
                return super.onPreferenceTreeClick(preference);

        }


    }

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.forgot_password));
        builder.setMessage(getResources().getString(R.string.hint));
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();

    }


    private void rateUs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.rate_app));
        builder.setMessage(getResources().getString(R.string.rate_app_desc));
        builder.setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> Utils.gotoPlayStore(getContext().getPackageName(), getContext()));
        builder.setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> letUsKnow());
        builder.show();
    }

    private void letUsKnow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.inform_us));
        builder.setMessage(getResources().getString(R.string.inform_us_description));
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> openEmail("Not Enjoying App"));
        builder.setNegativeButton(getString(R.string.later), (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void openEmail(String subject) {
        String deviceInfo = "\n\n\n\n\n\nDevice Info:";
        deviceInfo += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        deviceInfo += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
        deviceInfo += "\n Device: " + android.os.Build.DEVICE;
        deviceInfo += "\n Model (and Product): " + android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")";
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "andbullofficial@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject + ": " + getResources().getString(R.string.app_name));
        emailIntent.putExtra(Intent.EXTRA_TEXT, deviceInfo);
        startActivity(emailIntent);

    }


    private void share() {
        try {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String shareMessage = getString(R.string.app_name) + "\n\n" + getString(R.string.share_app_desc);
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(shareIntent);

        } catch (Exception e) {
        }

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equalsIgnoreCase("pref_dark_mode")) {
            boolean theme_boolean = MainApplication.theme_boolean;
            if (theme_boolean && sharedPreferences.getBoolean(key, false)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                theme_boolean = false;

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                theme_boolean = true;
            }
            SharedPreferences mPrefs = mContext.getSharedPreferences("THEME", 0);
            SharedPreferences.Editor mEditor = mPrefs.edit();
            mEditor.putBoolean("theme_boolean", theme_boolean).apply();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

    }
}
