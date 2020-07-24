package com.androidbull.calculator.photo.vault;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.os.StrictMode.VmPolicy.Builder;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;

import com.facebook.ads.AudienceNetworkActivity;
import com.facebook.ads.AudienceNetworkAds;

import java.text.DecimalFormat;

//import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MainApplication extends Application {

    private static final String CONSTANAT_SECURITY_ANSWER = "constant_answer";
    private static final String CONSTANAT_SECURITY_QUESTION = "constant_question";
    private static final String CONSTANT_AUDIO_COUNT = "constant_audio_count";
    private static final String CONSTANT_EMAIL = "constant_email";
    private static final String OPEN_COUNTER= "open_counter";
    private static final String CONSTANT_FILES_COUNT = "constant_files_count";
    private static final String CONSTANT_FP_HINT = "consatnt_fp_hint";
    private static final String CONSTANT_IMAGE_COUNT = "constant_image_count";
    private static final String CONSTANT_PASSWORD = "constant_password";
    private static final String CONSTANT_UNINSTALL = "constant_uninstall";
    private static final String CONSTANT_VIDEO_COUNT = "constant_video_count";
    private static MainApplication mInstance;
    public static  boolean theme_boolean;
    private static SharedPreferences sharedPreferences;


    public void onCreate() {
        super.onCreate();

        mInstance = this;
        MultiDex.install(this);
        StrictMode.setVmPolicy(new Builder().build());
        AudienceNetworkAds.initialize(this);

        SharedPreferences mPrefs = mInstance.getSharedPreferences("THEME", 0);
        theme_boolean = mPrefs.getBoolean("theme_boolean", true);
        if (theme_boolean) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }



        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("Montserrat-Regular.ttf").setFontAttrId(R.attr.fontPath).build());

    }

    public synchronized void savePassword(String pass) {
        sharedPreferences.edit().putString(CONSTANT_PASSWORD, pass).apply();
    }

    public synchronized String getPassword() {
        return sharedPreferences.getString(CONSTANT_PASSWORD, "");
    }


    public synchronized int getTimeOpened() {
        return sharedPreferences.getInt(OPEN_COUNTER, -1);
    }

    public synchronized void saveTimeOpened(int openedTime) {
        sharedPreferences.edit().putInt(OPEN_COUNTER, openedTime).apply();
    }


    public synchronized void saveImageCount(int pass) {
        sharedPreferences.edit().putInt(CONSTANT_IMAGE_COUNT, pass).apply();
    }


    public synchronized void saveVideoCount(int pass) {
        sharedPreferences.edit().putInt(CONSTANT_VIDEO_COUNT, pass).apply();
    }

    public synchronized void saveAudioCount(int pass) {
        sharedPreferences.edit().putInt(CONSTANT_AUDIO_COUNT, pass).apply();
    }

    public synchronized void saveFilesCount(int pass) {
        sharedPreferences.edit().putInt(CONSTANT_FILES_COUNT, pass).apply();
    }


    public static synchronized MainApplication getInstance() {
        MainApplication mainApplication;
        synchronized (MainApplication.class) {
            mainApplication = mInstance;
        }
        return mainApplication;
    }


    public synchronized boolean isUninstallProtected() {
        return sharedPreferences.getBoolean(CONSTANT_UNINSTALL, false);
    }

    public synchronized void setUninstallProtected(boolean value) {
        sharedPreferences.edit().putBoolean(CONSTANT_UNINSTALL, value).commit();
    }

    public synchronized void setSecurityQuestion(String question) {
        sharedPreferences.edit().putString(CONSTANAT_SECURITY_QUESTION, question).commit();
    }

    public synchronized String getSecurityQuestion() {
        return sharedPreferences.getString(CONSTANAT_SECURITY_QUESTION, "");
    }

    public synchronized void setSecurityAnswer(String answer) {
        sharedPreferences.edit().putString(CONSTANAT_SECURITY_ANSWER, answer).commit();
    }

    public synchronized String getSecurityAnswer() {
        return sharedPreferences.getString(CONSTANAT_SECURITY_ANSWER, "");
    }

    public synchronized String getFileSize(long size) {
        String hrSize;
        double b = (double) size;
        double k = ((double) size) / 1024.0d;
        double m = (((double) size) / 1024.0d) / 1024.0d;
        double g = ((((double) size) / 1024.0d) / 1024.0d) / 1024.0d;
        double t = (((((double) size) / 1024.0d) / 1024.0d) / 1024.0d) / 1024.0d;
        DecimalFormat dec = new DecimalFormat("0.00");
        if (t > 1.0d) {
            hrSize = dec.format(t).concat(" TB");
        } else if (g > 1.0d) {
            hrSize = dec.format(g).concat(" GB");
        } else if (m > 1.0d) {
            hrSize = dec.format(m).concat(" MB");
        } else if (k > 1.0d) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" Bytes");
        }
        return hrSize;
    }

}
