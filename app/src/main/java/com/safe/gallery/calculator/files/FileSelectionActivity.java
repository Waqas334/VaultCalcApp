package com.safe.gallery.calculator.files;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Files;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.safe.gallery.calculator.Constant;
import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.app.AppConstants;
import com.safe.gallery.calculator.app.BaseActivity;
import com.safe.gallery.calculator.app.MainApplication;
import com.safe.gallery.calculator.common.MergeAdapter;
import com.safe.gallery.calculator.utils.CenterTitleToolbar;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import butterknife.ButterKnife;

public class FileSelectionActivity extends BaseActivity {

    private Button btnHide;
    Button cancel;
    String choiceMode;
    private int count = 0;
    private String destPath;
    private Dialog dialog;
    private ArrayList<File> directoryList = new ArrayList();
    private ArrayList<String> directoryNames = new ArrayList();
    private ListView directoryView;
    private ArrayList<File> fileList = new ArrayList();
    private ArrayList<String> fileNames = new ArrayList();
    int index = 0;

    Button ok;
    TextView path;
    String primary_sd;
    private int progress;
    private static final Pattern DIR_SEPORATOR = Pattern.compile("/");
    public static final String FILES_TO_UPLOAD = "upload";
    private static final String TAG = "FileSelection";
    Button New;
    Boolean Switch = Boolean.valueOf(false);

    Button all;

    private boolean isFileCopied = true;
    private boolean isImageAddedToNewAlbum;
    File mainPath = new File(Environment.getExternalStorageDirectory() + "");
    private LinearLayout nativeAdContainer;

    private ProgressBar progressbar;
    String secondary_sd;
    private ArrayList<File> selectedFiles = new ArrayList();
    Button storage;
    Boolean switcher = Boolean.valueOf(false);
    private Timer timer;
    CenterTitleToolbar toolbar;
    int top = 0;
    private TextView txtCount;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_file_selection);
        ButterKnife.bind((Activity) this);

        findViews();
        initViews();

        setHeaderInfo();

    }

    private void findViews() {


        MainApplication.getInstance().LogFirebaseEvent("9", "AddFile");
        if (getIntent().getExtras() != null) {
            choiceMode = getIntent().getStringExtra("choiceMode");
        }
        directoryView = (ListView) findViewById(R.id.directorySelectionList);
        btnHide = (Button) findViewById(R.id.btn_hide);
        if (choiceMode != null) {
            directoryView.setChoiceMode(1);
        }
        ok = (Button) findViewById(R.id.ok);
        all = (Button) findViewById(R.id.all);
        cancel = (Button) findViewById(R.id.cancel);
        storage = (Button) findViewById(R.id.storage);
        New = (Button) findViewById(R.id.New);
        path = (TextView) findViewById(R.id.folderpath);
        toolbar =findViewById(R.id.toolbar);
        loadLists();
        New.setEnabled(false);
        directoryView.setOnItemClickListener(new C05511());
        ok.setOnClickListener(new C05522());
        btnHide.setOnClickListener(new C05533());
        cancel.setOnClickListener(new C05544());
        storage.setOnClickListener(new C05555());
        all.setOnClickListener(new C05566());
        addBanner();

    }

    private void initViews() {

    }


    class C05511 implements OnItemClickListener {
        C05511() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            int i = 0;
            index = directoryView.getFirstVisiblePosition();
            View v = directoryView.getChildAt(0);
            FileSelectionActivity fileSelectionActivity = FileSelectionActivity.this;
            if (v != null) {
                i = v.getTop();
            }
            fileSelectionActivity.top = i;
            try {
                if (position < directoryList.size()) {
                    mainPath = (File) directoryList.get(position);
                    loadLists();
                }
            } catch (Throwable th) {
                mainPath = mainPath;
                loadLists();
            }
        }
    }

    class C05522 implements OnClickListener {
        C05522() {
        }

        public void onClick(View v) {
            ok();
        }
    }

    class C05533 implements OnClickListener {
        C05533() {
        }

        public void onClick(View view) {
            ok();
        }
    }

    class C05544 implements OnClickListener {
        C05544() {
        }

        public void onClick(View v) {
            finish();
        }
    }

    class C05555 implements OnClickListener {
        C05555() {
        }

        public void onClick(View v) {
            try {
                if (switcher.booleanValue()) {
                    mainPath = new File(primary_sd);
                    loadLists();
                    switcher = Boolean.valueOf(false);
                    storage.setText(getString(R.string.ext));
                    return;
                }
                mainPath = new File(secondary_sd);
                loadLists();
                switcher = Boolean.valueOf(true);
                storage.setText(getString(R.string.Int));
            } catch (Throwable th) {
            }
        }
    }

    class C05566 implements OnClickListener {
        C05566() {
        }

        public void onClick(View v) {
            int i;
            if (!Switch.booleanValue()) {
                for (i = directoryList.size(); i < directoryView.getCount(); i++) {
                    directoryView.setItemChecked(i, true);
                }

                all.setText(getString(R.string.none));
                Switch = Boolean.valueOf(true);

            } else if (Switch.booleanValue()) {

                for (i = directoryList.size(); i < directoryView.getCount(); i++) {
                    directoryView.setItemChecked(i, false);

                }
                all.setText(getString(R.string.all));
                Switch = Boolean.valueOf(false);
            }
        }
    }

    class C05597 extends TimerTask {

        class C05571 implements Runnable {
            C05571() {
            }

            public void run() {
                publishProgress(selectedFiles.size());
            }
        }

        class C05582 implements Runnable {
            C05582() {
            }

            public void run() {
                hideProgressDialog();
                setBackData();
            }
        }

        C05597() {
        }

        public void run() {
            if (count == selectedFiles.size()) {
                timer.cancel();
                isImageAddedToNewAlbum = true;
                runOnUiThread(new C05582());
            } else if (isFileCopied) {
                runOnUiThread(new C05571());
                isFileCopied = false;
                File src = (File) selectedFiles.get(count);
                moveFile(src, new File(destPath, src.getName()));
                count = count + 1;
                isImageAddedToNewAlbum = true;
            }
        }
    }

    class C05608 implements Runnable {
        C05608() {
        }

        public void run() {
            progressbar.setProgress(progress);
        }
    }


    public void addBanner() {

        final AdView mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.BANNER);
        final View adContainer = findViewById(R.id.layoutViewAdd);

        mAdView.setAdUnitId(Constant.bannerId);

        ((LinearLayout) adContainer).addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("EA965DE183B804F71E5E6D353E6607DE")
                .addTestDevice("5CE992DB43E8F2B50F7D2201A724526D")
                .addTestDevice("6E5543AE954EAD6702405BFCCC34C9A2")
                .addTestDevice("28373E4CC308EDBD5C5D39795CD4956A")
                .addTestDevice("3C5740EB2F36FB5F0FEFA773607D27CE") // mi white
                .addTestDevice("79E8DED973BDF7477739501E228D88E1") //samsung max
                .build();

        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);


                //adContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                adContainer.setVisibility(View.VISIBLE);

            }
        });
    }

    private void setHeaderInfo() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.select_file));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onBackPressed() {
            try {
            if (mainPath.equals(Environment.getExternalStorageDirectory())) {
                finish();
                return;
            }
            mainPath = mainPath.getParentFile();
            loadLists();
            directoryView.setSelectionFromTop(index, top);
            btnHide.setVisibility(8);
        } catch (Throwable th) {
        }
    }

    public void ok() {
        File file = new File(AppConstants.FILES_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        destPath = file.getAbsolutePath();
        if (selectedFiles == null || selectedFiles.size() <= 0) {
            Toast.makeText(this, "Please select at least one file!", 0).show();
            return;
        }
        showProgressDialog(selectedFiles);
        timer = new Timer();
        timer.scheduleAtFixedRate(new C05597(), 0, 200);
    }

    private void setBackData() {
        if (!isImageAddedToNewAlbum) {
            File file = new File(destPath);
            if (file.exists()) {
                file.delete();
            }
        }
        Intent intent = new Intent();
        intent.putExtra(AppConstants.HIDDEN_RESULT, isImageAddedToNewAlbum);
        setResult(-1, intent);
        finish();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
    }

    private void showProgressDialog(ArrayList<File> files) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_progress);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setLayout(-1, -2);
        }
        progressbar = (ProgressBar) dialog.findViewById(R.id.progress_bar);
        txtCount = (TextView) dialog.findViewById(R.id.txt_count);
        nativeAdContainer = (LinearLayout) dialog.findViewById(R.id.native_ad_container);
        ((TextView) dialog.findViewById(R.id.txt_title)).setText("Moving File(s)");
        txtCount.setText("Moving 1 of " + files.size());
        int totalFileSize = 0;
        Iterator it = files.iterator();
        while (it.hasNext()) {
            totalFileSize += (int) ((File) it.next()).length();
        }
        progressbar.setMax(totalFileSize);
        dialog.show();
    }

    private void hideProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void publishProgress(int size) {
        if (dialog != null && dialog.isShowing()) {
            txtCount.setText("Moving " + (count + 1) + " of " + size);
        }
    }

    private void moveFile(final File src, final File dst) {
        try {
            InputStream in = new FileInputStream(src);
            OutputStream out;
            try {
                out = new FileOutputStream(dst);
                byte[] buf = new byte[1024];
                while (true) {
                    int len = in.read(buf);
                    if (len > 0) {
                        out.write(buf, 0, len);
                        progress += len;
                        runOnUiThread(new C05608());
                    } else {
                        out.close();
                        isFileCopied = true;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(dst)));
                            }
                        });
                        in.close();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                deleteFilePath(src);
                            }
                        });
                        isFileCopied = true;
                        return;
                    }
                }
            } catch (Throwable th) {
                in.close();
                runOnUiThread(/* anonymous class already generated */);
                isFileCopied = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            isFileCopied = true;
        }
    }

    private void runOnUiThread() {
    }

    private void deleteFilePath(File file) {
        try {
            String where = "_data=?";
            String[] selectionArgs = new String[]{file.getAbsolutePath()};
            ContentResolver contentResolver = getContentResolver();
            Uri filesUri = Files.getContentUri("external");
            contentResolver.delete(filesUri, "_data=?", selectionArgs);
            if (file.exists()) {
                contentResolver.delete(filesUri, "_data=?", selectionArgs);
                file.delete();
            }
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), 1).show();
        }
    }

    protected void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
        }
        hideProgressDialog();
    }

    private void loadLists() {
        File file;
        int i = 0;
        FileFilter fileFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        };
        File[] tempDirectoryList = mainPath.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        if (tempDirectoryList != null && tempDirectoryList.length > 1) {
            Arrays.sort(tempDirectoryList, new Comparator<File>() {
                public int compare(File object1, File object2) {
                    return object1.getName().compareTo(object2.getName());
                }
            });
        }
        directoryList = new ArrayList();
        directoryNames = new ArrayList();
        for (File file2 : tempDirectoryList) {
            directoryList.add(file2);
            directoryNames.add(file2.getName());
        }
        ArrayAdapter<String> directoryAdapter = new ArrayAdapter(this, 17367043, directoryNames);
        File[] tempFileList = mainPath.listFiles(fileFilter);
        if (tempFileList != null && tempFileList.length > 1) {
            Arrays.sort(tempFileList, new Comparator<File>() {
                public int compare(File object1, File object2) {
                    return object1.getName().compareTo(object2.getName());
                }
            });
        }
        fileList = new ArrayList();
        fileNames = new ArrayList();
        int length = tempFileList.length;
        while (i < length) {
            File file2 = tempFileList[i];
            if (file2.getAbsolutePath().endsWith(".pps") || file2.getAbsolutePath().endsWith(".ppt") || file2.getAbsolutePath().endsWith(".pptx") || file2.getAbsolutePath().endsWith(".xls") || file2.getAbsolutePath().endsWith(".xlsx") || file2.getAbsolutePath().endsWith(".pdf") || file2.getAbsolutePath().endsWith(".doc") || file2.getAbsolutePath().endsWith(".docx") || file2.getAbsolutePath().endsWith(".rtf") || file2.getAbsolutePath().endsWith(".txt")) {
                fileList.add(file2);
                fileNames.add(file2.getName());
            }
            i++;
        }
        path.setText(mainPath.toString());
        iconload();
        setTitle(mainPath.getName());
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void iconload() {
        String[] filenames = (String[]) fileNames.toArray(new String[fileNames.size()]);
        CustomListSingleOnly adapter1 = new CustomListSingleOnly(this, (String[]) directoryNames.toArray((String[]) directoryNames.toArray(new String[directoryNames.size()])), mainPath.getPath());
        CustomList adapter2 = new CustomList(this, (String[]) fileNames.toArray(filenames), mainPath.getPath());
        MergeAdapter adap = new MergeAdapter();
        adap.addAdapter(adapter1);
        adap.addAdapter(adapter2);
        directoryView.setAdapter(adap);
    }

    public boolean hasStorage() {
        String state = Environment.getExternalStorageState();
        Log.v(TAG, "storage state is " + state);
        if ("mounted".equals(state)) {
            return true;
        }
        return false;
    }

    public void ExtStorageSearch() {
        primary_sd = System.getenv("EXTERNAL_STORAGE");
        if (primary_sd == null) {
            primary_sd = Environment.getExternalStorageDirectory() + "";
        }
        if (Boolean.valueOf(Environment.getExternalStorageState().equals("mounted")).booleanValue()) {
            for (String string : getStorageDirectories(this)) {
                if (new File(string).exists() && new File(string).isDirectory() && !string.equals(primary_sd)) {
                    secondary_sd = string;
                    return;
                }
            }
            return;
        }
        secondary_sd = null;
    }

    public static String[] getStorageDirectories(Context context) {

        Set<String> rv = new HashSet();
        String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
        String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
        String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
        if (!TextUtils.isEmpty(rawEmulatedStorageTarget)) {
            String rawUserId;
            if (VERSION.SDK_INT < 17) {
                rawUserId = "";
            } else {
                String[] folders = DIR_SEPORATOR.split(Environment.getExternalStorageDirectory().getAbsolutePath());
                String lastFolder = folders[folders.length - 1];
                boolean isDigit = false;
                try {
                    Integer.valueOf(lastFolder);
                    isDigit = true;
                } catch (NumberFormatException e) {
                }
                rawUserId = isDigit ? lastFolder : "";
            }
            if (TextUtils.isEmpty(rawUserId)) {
                rv.add(rawEmulatedStorageTarget);
            } else {
                rv.add(rawEmulatedStorageTarget + File.separator + rawUserId);
            }
        } else if (VERSION.SDK_INT >= 23) {
            for (File file : context.getExternalFilesDirs(null)) {
                String applicationSpecificAbsolutePath = file.getAbsolutePath();
                rv.add(applicationSpecificAbsolutePath.substring(0, applicationSpecificAbsolutePath.indexOf("Android/data")));
            }
        } else if (TextUtils.isEmpty(rawExternalStorage)) {
            rv.addAll(Arrays.asList(getPhysicalPaths()));
        } else {
            rv.add(rawExternalStorage);
        }
        if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
            Collections.addAll(rv, rawSecondaryStoragesStr.split(File.pathSeparator));
        }
        return (String[]) rv.toArray(new String[rv.size()]);
    }

    private static String[] getPhysicalPaths() {
        return new String[]{"/storage/sdcard0", "/storage/sdcard1", "/storage/extsdcard", "/storage/sdcard0/external_sdcard", "/mnt/extsdcard", "/mnt/sdcard/external_sd", "/mnt/external_sd", "/mnt/media_rw/sdcard1", "/removable/microsd", "/mnt/emmc", "/storage/external_SD", "/storage/ext_sd", "/storage/removable/sdcard1", "/data/sdext", "/data/sdext2", "/data/sdext3", "/data/sdext4", "/sdcard1", "/sdcard2", "/storage/microsd"};
    }

    public void setFiles(boolean isSelected, String fileName) {
        int i;
        if (isSelected) {
            for (i = 0; i < fileList.size(); i++) {
                if (((File) fileList.get(i)).getName().equals(fileName)) {
                    selectedFiles.add(fileList.get(i));
                    Log.e("FILE SELECTED ", fileName);
                }
            }
        } else {
            for (i = 0; i < fileList.size(); i++) {
                if (((File) fileList.get(i)).getName().equals(fileName)) {
                    selectedFiles.remove(fileList.get(i));
                    Log.e("FILE REMOVED ", fileName);
                }
            }
        }
        btnHide.setVisibility(selectedFiles.size() > 0 ? 0 : 8);
    }

}
