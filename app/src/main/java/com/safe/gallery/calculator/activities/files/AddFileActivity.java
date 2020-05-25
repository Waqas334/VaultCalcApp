package com.safe.gallery.calculator.activities.files;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Files;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.activities.BaseActivity;
import com.safe.gallery.calculator.adapters.files.FolderAdapter;
import com.safe.gallery.calculator.adapters.files.SingleFileAdapter;
import com.safe.gallery.calculator.common.MergeAdapter;
import com.safe.gallery.calculator.utils.AppConstants;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

public class AddFileActivity extends BaseActivity {

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
    Button New;
    Boolean Switch = false;

    Button all;

    private boolean isFileCopied = true;
    private boolean isImageAddedToNewAlbum;
    File mainPath = new File(Environment.getExternalStorageDirectory() + "");

    private ProgressBar progressbar;
    String secondary_sd;
    private ArrayList<File> selectedFiles = new ArrayList();
    Button storage;
    Boolean switcher = false;
    private Timer timer;
    Toolbar toolbar;
    int top = 0;
    private TextView txtCount;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_selection);
        ButterKnife.bind(this);

        findViews();
        setHeaderInfo();

    }

    private void findViews() {


        if (getIntent().getExtras() != null) {
            choiceMode = getIntent().getStringExtra("choiceMode");
        }
        directoryView = findViewById(R.id.directorySelectionList);
        btnHide = findViewById(R.id.btn_hide);
        if (choiceMode != null) {
            directoryView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
        ok = findViewById(R.id.ok);
        all = findViewById(R.id.all);
        cancel = findViewById(R.id.cancel);
        storage = findViewById(R.id.storage);
        New = findViewById(R.id.New);
        path = findViewById(R.id.folderpath);
        toolbar = findViewById(R.id.toolbar);
        loadLists();
        New.setEnabled(false);
        directoryView.setOnItemClickListener(new C05511());
        ok.setOnClickListener(new C05522());
        btnHide.setOnClickListener(new C05533());
        cancel.setOnClickListener(new C05544());
        storage.setOnClickListener(new C05555());
        all.setOnClickListener(new C05566());

    }


    class C05511 implements OnItemClickListener {
        C05511() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            int i = 0;
            index = directoryView.getFirstVisiblePosition();
            View v = directoryView.getChildAt(0);
            AddFileActivity addFileActivity = AddFileActivity.this;
            if (v != null) {
                i = v.getTop();
            }
            addFileActivity.top = i;
            try {
                if (position < directoryList.size()) {
                    mainPath = directoryList.get(position);
                    loadLists();
                }
            } catch (Throwable th) {
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
                if (switcher) {
                    mainPath = new File(primary_sd);
                    loadLists();
                    switcher = false;
                    storage.setText(getString(R.string.ext));
                    return;
                }
                mainPath = new File(secondary_sd);
                loadLists();
                switcher = true;
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
            if (!Switch) {
                for (i = directoryList.size(); i < directoryView.getCount(); i++) {
                    directoryView.setItemChecked(i, true);
                }

                all.setText(getString(R.string.none));
                Switch = true;

            } else if (Switch) {

                for (i = directoryList.size(); i < directoryView.getCount(); i++) {
                    directoryView.setItemChecked(i, false);

                }
                all.setText(getString(R.string.all));
                Switch = false;
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
            btnHide.setVisibility(View.GONE);
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
            Toast.makeText(this, getString(R.string.select_1_file), Toast.LENGTH_SHORT).show();
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
        progressbar = dialog.findViewById(R.id.progress_bar);
        txtCount = dialog.findViewById(R.id.txt_count);
        ((TextView) dialog.findViewById(R.id.txt_title)).setText(getString(R.string.moving_files));
        txtCount.setText(getString(R.string.moving_1_of) + " " + files.size());
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
            txtCount.setText(getString(R.string.moving) + " " + (count + 1) + " " + getString(R.string.of) + " " + size);
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

                        runOnUiThread(() -> sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(dst))));
                        in.close();
                        runOnUiThread(() -> deleteFilePath(src));
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
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
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
        int i = 0;
        FileFilter fileFilter = file -> file.isFile();
        File[] tempDirectoryList = mainPath.listFiles(file -> file.isDirectory());
        if (tempDirectoryList != null && tempDirectoryList.length > 1) {
            Arrays.sort(tempDirectoryList, (object1, object2) -> object1.getName().compareTo(object2.getName()));
        }
        directoryList = new ArrayList();
        directoryNames = new ArrayList();
        for (File file2 : tempDirectoryList) {
            directoryList.add(file2);
            directoryNames.add(file2.getName());
        }
        File[] tempFileList = mainPath.listFiles(fileFilter);
        if (tempFileList != null && tempFileList.length > 1) {
            Arrays.sort(tempFileList, (object1, object2) -> object1.getName().compareTo(object2.getName()));
        }
        fileList = new ArrayList();
        fileNames = new ArrayList();
        int length = tempFileList.length;
        while (i < length) {
            File file2 = tempFileList[i];
            if (file2.getAbsolutePath().endsWith(".pps") ||
                    file2.getAbsolutePath().endsWith(".ppt") ||
                    file2.getAbsolutePath().endsWith(".pptx") ||
                    file2.getAbsolutePath().endsWith(".xls") ||
                    file2.getAbsolutePath().endsWith(".xlsx") ||
                    file2.getAbsolutePath().endsWith(".pdf") ||
                    file2.getAbsolutePath().endsWith(".doc") ||
                    file2.getAbsolutePath().endsWith(".docx") ||
                    file2.getAbsolutePath().endsWith(".rtf") ||
                    file2.getAbsolutePath().endsWith(".apk") ||
                    file2.getAbsolutePath().endsWith(".zip") ||
                    file2.getAbsolutePath().endsWith(".rar") ||
                    file2.getAbsolutePath().endsWith(".rtf") ||
                    file2.getAbsolutePath().endsWith(".txt")) {
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
        String[] filenames = fileNames.toArray(new String[fileNames.size()]);
        FolderAdapter adapter1 = new FolderAdapter(this, directoryNames.toArray(directoryNames.toArray(new String[directoryNames.size()])), mainPath.getPath());
        SingleFileAdapter adapter2 = new SingleFileAdapter(this, fileNames.toArray(filenames), mainPath.getPath());
        MergeAdapter adap = new MergeAdapter();
        adap.addAdapter(adapter1);
        adap.addAdapter(adapter2);
        directoryView.setAdapter(adap);
    }
    public void setFiles(boolean isSelected, String fileName) {
        int i;
        if (isSelected) {
            for (i = 0; i < fileList.size(); i++) {
                if (fileList.get(i).getName().equals(fileName)) {
                    selectedFiles.add(fileList.get(i));
                    Log.e("FILE SELECTED ", fileName);
                }
            }
        } else {
            for (i = 0; i < fileList.size(); i++) {
                if (fileList.get(i).getName().equals(fileName)) {
                    selectedFiles.remove(fileList.get(i));
                    Log.e("FILE REMOVED ", fileName);
                }
            }
        }
        btnHide.setVisibility(selectedFiles.size() > 0 ? View.VISIBLE : View.GONE);
    }

}
