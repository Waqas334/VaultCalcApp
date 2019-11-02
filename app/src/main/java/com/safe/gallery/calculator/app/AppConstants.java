package com.safe.gallery.calculator.app;

import android.os.Environment;

import java.io.File;

public class AppConstants {

    public static final String HIDDEN_FOLDER = ".hiddendatacalculator";
    public static final String EXPORT_FOLDER = "Calculator";
    public static final String AUDIO = "Audio";
    public static final String AUDIO_EXPORT_PATH = (Environment.getExternalStorageDirectory() + File.separator + EXPORT_FOLDER + File.separator + AUDIO);
    public static final String AUDIO_PATH = (Environment.getExternalStorageDirectory() + File.separator + HIDDEN_FOLDER + File.separator + AUDIO);

    public static final String FILES = "Files";
    public static final String FILES_PATH = (Environment.getExternalStorageDirectory() + File.separator + HIDDEN_FOLDER + File.separator + FILES);
    public static final String FILE_EXPORT_PATH = (Environment.getExternalStorageDirectory() + File.separator + EXPORT_FOLDER + File.separator + FILES);

    public static final String HIDDEN_RESULT = "hidden_result";
    public static final String IMAGE = "Image";
    public static final String IMAGE_EXPORT_PATH = (Environment.getExternalStorageDirectory() + File.separator + EXPORT_FOLDER + File.separator + IMAGE);
    public static final String IMAGE_PATH = (Environment.getExternalStorageDirectory() + File.separator + HIDDEN_FOLDER + File.separator + IMAGE);
    public static final int REFRESH_LIST = 1233;
    public static final String VIDEO = "Video";
    public static final String VIDEO_EXPORT_PATH = (Environment.getExternalStorageDirectory() + File.separator + EXPORT_FOLDER + File.separator + VIDEO);
    public static final String VIDEO_PATH = (Environment.getExternalStorageDirectory() + File.separator + HIDDEN_FOLDER + File.separator + VIDEO);
}
