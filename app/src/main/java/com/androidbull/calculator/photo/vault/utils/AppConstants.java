package com.androidbull.calculator.photo.vault.utils;

import android.os.Environment;

import java.io.File;

public class AppConstants {

    public static final String HIDDEN_FOLDER = ".hiddendatacalculator";
    public static final String HIDDEN_INNER_FOLDER = "incognitobrowser";

    public static final String EXPORT_FOLDER = "Calculator";
    public static final String EXPORT_FOLDER_BROWSER = "Incognito Browser";
    public static final String AUDIO = "Audio";
    public static final String AUDIO_EXPORT_PATH = (Environment.getExternalStorageDirectory() + File.separator + EXPORT_FOLDER + File.separator + AUDIO);
    public static final String AUDIO_PATH = (Environment.getExternalStorageDirectory() + File.separator + HIDDEN_FOLDER + File.separator + AUDIO);

    public static final String FILES = "Files";
    public static final String FILES_PATH = (Environment.getExternalStorageDirectory() + File.separator + HIDDEN_FOLDER + File.separator + FILES);
    public static final String FILE_EXPORT_PATH = (Environment.getExternalStorageDirectory() + File.separator + EXPORT_FOLDER + File.separator + FILES);

    public static final String BROWSER_FILES_PATH = (Environment.getExternalStorageDirectory() + File.separator + HIDDEN_FOLDER + File.separator + HIDDEN_INNER_FOLDER);

    public static final String HIDDEN_RESULT = "hidden_result";
    public static final String IMAGE = "Image";
    public static final String IMAGE_EXPORT_PATH = (Environment.getExternalStorageDirectory() + File.separator + EXPORT_FOLDER + File.separator + IMAGE);


    public static final String BROWSER_FILE_EXPORT_PATH = (Environment.getExternalStorageDirectory() + File.separator + EXPORT_FOLDER_BROWSER);


    public static final String IMAGE_PATH = (Environment.getExternalStorageDirectory() + File.separator + HIDDEN_FOLDER + File.separator + IMAGE);
    public static final int REFRESH_LIST = 1233;
    public static final String VIDEO = "Video";
    public static final String VIDEO_EXPORT_PATH = (Environment.getExternalStorageDirectory() + File.separator + EXPORT_FOLDER + File.separator + VIDEO);
    public static final String VIDEO_PATH = (Environment.getExternalStorageDirectory() + File.separator + HIDDEN_FOLDER + File.separator + VIDEO);


    public static final String INTRUDER= "Intruder";
    public static final String INTRUDER_PATH= (Environment.getExternalStorageDirectory() + File.separator + HIDDEN_FOLDER + File.separator + INTRUDER);
}
