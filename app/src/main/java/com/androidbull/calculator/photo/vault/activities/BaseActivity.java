package com.androidbull.calculator.photo.vault.activities;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.util.Log;

import androidx.annotation.Nullable;

import com.androidbull.calculator.photo.vault.MyBassActivity;
import com.androidbull.calculator.photo.vault.callbacks.OnAllAudiosLoadedListener;
import com.androidbull.calculator.photo.vault.callbacks.OnAllImagesLoadedListener;
import com.androidbull.calculator.photo.vault.callbacks.OnAllVideosLoadedListener;
import com.androidbull.calculator.photo.vault.callbacks.OnAudioLoadedListener;
import com.androidbull.calculator.photo.vault.callbacks.OnFilesLoadedListener;
import com.androidbull.calculator.photo.vault.callbacks.OnImagesLoadedListener;
import com.androidbull.calculator.photo.vault.callbacks.OnVideosLoadedListener;
import com.androidbull.calculator.photo.vault.model.AllAudioModel;
import com.androidbull.calculator.photo.vault.model.AllFilesModel;
import com.androidbull.calculator.photo.vault.model.AllImagesModel;
import com.androidbull.calculator.photo.vault.model.AllVideosModel;
import com.androidbull.calculator.photo.vault.utils.AppConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;


public class BaseActivity extends MyBassActivity {

    public class GetAllAudiosAsyncTask extends AsyncTask<Void, Void, ArrayList<AllAudioModel>> {
        public OnAllAudiosLoadedListener onAllAudiosLoadedListener;

        protected ArrayList<AllAudioModel> doInBackground(Void... voids) {
            ArrayList<AllAudioModel> arrayList = new ArrayList<>();
            Cursor songCursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            if (songCursor == null || !songCursor.moveToFirst()) {
                return arrayList;
            }
            int column_index = songCursor.getColumnIndex("_data");
            int last_modified = songCursor.getColumnIndex("date_modified");
            do {
                String currentPath = songCursor.getString(column_index);
                if (currentPath.contains(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                    arrayList.add(new AllAudioModel(currentPath, (long) last_modified));
                }
            } while (songCursor.moveToNext());
            return arrayList;
        }

        protected void onPostExecute(ArrayList<AllAudioModel> allAudioModels) {
            super.onPostExecute(allAudioModels);
            if (this.onAllAudiosLoadedListener != null) {
                this.onAllAudiosLoadedListener.onAllAudiosLoaded(allAudioModels);
            }
        }
    }

    public class GetAllImagesAsyncTask extends AsyncTask<Void, Void, ArrayList<AllImagesModel>> {
        public OnAllImagesLoadedListener onAllImagesLoadedListener;

        protected ArrayList<AllImagesModel> doInBackground(Void... voids) {
            Uri uri = Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = new String[]{"_data", "date_modified"};
            Cursor cursor = null;
            SortedSet<String> dirList = new TreeSet();
            ArrayList<AllImagesModel> resultIAV = new ArrayList<>();
            String[] directories = null;
            if (uri != null) {
                cursor = getContentResolver().query(uri, projection, null, null, null);
            }
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String tempDir = cursor.getString(0);
                    try {
                        dirList.add(tempDir.substring(0, tempDir.lastIndexOf("/")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (cursor.moveToNext());
                directories = new String[dirList.size()];
                dirList.toArray(directories);
            }
            for (int i = 0; i < dirList.size(); i++) {
                File[] listFiles = new File(directories[i]).listFiles();
                if (listFiles != null) {
                    int length = listFiles.length;
                    int i2 = 0;
                    File[] imageList = listFiles;
                    while (i2 < length) {
                        File imagePath = listFiles[i2];
                        try {
                            if (imagePath.isDirectory()) {
                                imageList = imagePath.listFiles();
                            }
                            if (imagePath.getName().contains(".jpg")
                                    || imagePath.getName().contains(".JPG")
                                    || imagePath.getName().contains(".jpeg")
                                    || imagePath.getName().contains(".JPEG")
                                    || imagePath.getName().contains(".png")
                                    || imagePath.getName().contains(".PNG")
                                    || imagePath.getName().contains(".gif")
                                    || imagePath.getName().contains(".GIF")
                                    || imagePath.getName().contains(".bmp")
                                    || imagePath.getName().contains(".BMP")) {
                                String path = imagePath.getAbsolutePath();
                                long lastModified = imagePath.lastModified();
//                                if (path.contains(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                                resultIAV.add(new AllImagesModel(path, lastModified));
//                                }
                                i2++;
                            } else {
                                i2++;
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                    listFiles = imageList;
                }
            }
            return resultIAV;
        }

        protected void onPostExecute(ArrayList<AllImagesModel> allImageModels) {
            super.onPostExecute(allImageModels);
            if (this.onAllImagesLoadedListener != null) {
                this.onAllImagesLoadedListener.onAllImagesLoaded(allImageModels);
            }
        }
    }

    public class GetAllVideosAsyncTask extends AsyncTask<Void, Void, ArrayList<AllVideosModel>> {

        public OnAllVideosLoadedListener onAllVideosLoadedListener;

        protected ArrayList<AllVideosModel> doInBackground(Void... voids) {
            ArrayList<AllVideosModel> videos = new ArrayList<>();
            Cursor cursor = BaseActivity.this.getContentResolver().query(Video.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "date_modified"}, null, null, null);
            try {
                cursor.moveToFirst();
                do {
                    String path = cursor.getString(cursor.getColumnIndex("_data"));
                    String lastmodified = cursor.getString(cursor.getColumnIndex("_data"));
                    if (path.contains(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                        videos.add(new AllVideosModel(path, lastmodified));
                    }
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return videos;
        }

        protected void onPostExecute(ArrayList<AllVideosModel> allImages) {
            super.onPostExecute(allImages);
            if (this.onAllVideosLoadedListener != null) {
                this.onAllVideosLoadedListener.onAllVideosLoaded(allImages);
            }
        }
    }

    public class GetHiddenAudio extends AsyncTask<Void, Void, ArrayList<AllAudioModel>> {

        public OnAudioLoadedListener onAudioLoadedListener;

        protected ArrayList<AllAudioModel> doInBackground(Void... voids) {
            ArrayList<AllAudioModel> resultIAV = new ArrayList<>();
            File file = new File(AppConstants.AUDIO_PATH);
            if (!file.exists()) {
                return resultIAV;
            }
            File[] imageList = file.listFiles();
            if (imageList == null) {
                return resultIAV;
            }
            for (File imagePath : imageList) {
                Log.e("PATH", "" + imagePath.getAbsolutePath());
                try {
                    resultIAV.add(new AllAudioModel(imagePath.getAbsolutePath(), imagePath.lastModified()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return resultIAV;
        }

        protected void onPostExecute(ArrayList<AllAudioModel> allAudioModels) {
            super.onPostExecute(allAudioModels);
            if (this.onAudioLoadedListener != null) {
                this.onAudioLoadedListener.onAudiosLoaded(allAudioModels);
            }
        }
    }

    public class GetHiddenBrowserFiles extends AsyncTask<Void, Void, ArrayList<AllFilesModel>> {

        public OnFilesLoadedListener onFilesLoadedListener;

        protected ArrayList<AllFilesModel> doInBackground(Void... voids) {
            ArrayList<AllFilesModel> resultIAV = new ArrayList<>();
            File file = new File(AppConstants.BROWSER_FILES_PATH);
            if (!file.exists()) {
                return resultIAV;
            }
            File[] imageList = file.listFiles();
            if (imageList == null) {
                return resultIAV;
            }
            for (File imagePath : imageList) {
                Log.e("PATH", "" + imagePath.getAbsolutePath());
                try {
                    resultIAV.add(new AllFilesModel(imagePath.getAbsolutePath(), imagePath.lastModified()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return resultIAV;
        }


        protected void onPostExecute(ArrayList<AllFilesModel> allFileModels) {
            super.onPostExecute(allFileModels);
            if (this.onFilesLoadedListener != null) {
                this.onFilesLoadedListener.onFilesLoaded(allFileModels);
            }
        }
    }

    public class GetHiddenFiles extends AsyncTask<Void, Void, ArrayList<AllFilesModel>> {

        public OnFilesLoadedListener onFilesLoadedListener;

        protected ArrayList<AllFilesModel> doInBackground(Void... voids) {
            ArrayList<AllFilesModel> resultIAV = new ArrayList<>();
            File file = new File(AppConstants.FILES_PATH);
            if (!file.exists()) {
                return resultIAV;
            }
            File[] imageList = file.listFiles();
            if (imageList == null) {
                return resultIAV;
            }
            for (File imagePath : imageList) {
                Log.e("PATH", "" + imagePath.getAbsolutePath());
                try {
                    resultIAV.add(new AllFilesModel(imagePath.getAbsolutePath(), imagePath.lastModified()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return resultIAV;
        }

        protected void onPostExecute(ArrayList<AllFilesModel> allFileModels) {
            super.onPostExecute(allFileModels);
            if (this.onFilesLoadedListener != null) {
                this.onFilesLoadedListener.onFilesLoaded(allFileModels);
            }
        }
    }

    public class GetHiddenImages extends AsyncTask<Void, Void, ArrayList<AllImagesModel>> {

        public OnImagesLoadedListener onImagesLoadedListener;

        protected ArrayList<AllImagesModel> doInBackground(Void... voids) {
            ArrayList<AllImagesModel> resultIAV = new ArrayList<>();
            File file = new File(AppConstants.IMAGES_PATH);
            if (!file.exists()) {
                return resultIAV;
            }
            File[] imageList = file.listFiles();
            if (imageList == null) {
                return resultIAV;
            }
            for (File imagePath : imageList) {
                Log.e("PATH", "" + imagePath.getAbsolutePath());
                try {
                    resultIAV.add(new AllImagesModel(imagePath.getAbsolutePath(), imagePath.lastModified()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return resultIAV;
        }

        protected void onPostExecute(ArrayList<AllImagesModel> allImageModels) {
            super.onPostExecute(allImageModels);
            if (this.onImagesLoadedListener != null) {
                this.onImagesLoadedListener.onImagesLoaded(allImageModels);
            }
        }
    }

    public class GetHiddenVideos extends AsyncTask<Void, Void, ArrayList<AllVideosModel>> {
        public OnVideosLoadedListener onVideosLoadedListener;

        protected ArrayList<AllVideosModel> doInBackground(Void... voids) {
            ArrayList<AllVideosModel> resultIAV = new ArrayList<>();
            File file = new File(AppConstants.VIDEO_PATH);
            if (!file.exists()) {
                return resultIAV;
            }
            File[] imageList = file.listFiles();
            if (imageList == null) {
                return resultIAV;
            }
            for (File imagePath : imageList) {
                Log.e("PATH", "" + imagePath.getAbsolutePath());
                try {
                    resultIAV.add(new AllVideosModel(imagePath.getAbsolutePath(), imagePath.lastModified()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return resultIAV;
        }

        protected void onPostExecute(ArrayList<AllVideosModel> allVideoModels) {
            super.onPostExecute(allVideoModels);
            if (this.onVideosLoadedListener != null) {
                this.onVideosLoadedListener.onVideosLoaded(allVideoModels);
            }
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
