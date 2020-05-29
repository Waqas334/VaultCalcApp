package com.androidbull.calculator.photo.vault.activities.video;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Files;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidbull.calculator.photo.R;
import com.androidbull.calculator.photo.vault.activities.BaseActivity;
import com.androidbull.calculator.photo.vault.adapters.video.AddVideoAdapter;
import com.androidbull.calculator.photo.vault.callbacks.OnAllVideosLoadedListener;
import com.androidbull.calculator.photo.vault.model.AllVideosModel;
import com.androidbull.calculator.photo.vault.utils.AppConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddVideoActivity extends BaseActivity implements OnAllVideosLoadedListener {


    private Toolbar toolbar;
    private TextView txtCount;

    ViewAnimator viewanimator;
    AddVideoAdapter adapter;

    private boolean isFileCopied = true;
    Button btnHide;
    private int count;

    private String destPath;
    private Dialog dialog;
    private boolean isAllSelected;

    private boolean isImageAddedToNewAlbum;
    private MenuItem itemSelectAll;
    private int progress = 0;
    private ProgressBar progressbar;

    private Timer timer;
    RecyclerView recyclerview;


    class C06512 implements Runnable {
        C06512() {
        }

        public void run() {
            progressbar.setProgress(progress);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adds_image);
        ButterKnife.bind(this);

        findViews();
        setHeaderInfo();
        Init();
    }

    private void findViews() {

        btnHide = findViewById(R.id.btn_hide);
        recyclerview = findViewById(R.id.recyclerview);
        toolbar = findViewById(R.id.toolbar);
        viewanimator = findViewById(R.id.viewanimator);
    }


    private void setHeaderInfo() {
        // toolbar.setNavigationIcon((int) R.drawable.ic_close);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.add_videos));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);

    }

    private void Init() {

        File file = new File(AppConstants.VIDEO_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        destPath = file.getAbsolutePath();
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddVideoAdapter(this);
        recyclerview.setAdapter(adapter);
        GetAllVideosAsyncTask task = new GetAllVideosAsyncTask();
        task.onAllVideosLoadedListener = this;
        task.execute(new Void[0]);

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_image_menu, menu);
        itemSelectAll = menu.findItem(R.id.action_select_all);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_select_all:
                if (!isAllSelected) {
                    if (adapter != null) {
                        adapter.selectAllItem();
                    }
                    isAllSelected = true;
                    item.setIcon(R.drawable.ic_check_box_white_48dp);
                    showHideButton(true);
                    break;
                }
                if (adapter != null) {
                    adapter.deSelectAllItem();
                }
                isAllSelected = false;
                item.setIcon(R.drawable.ic_check_box_outline_white_48dp);
                showHideButton(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showHideButton(boolean value) {
        btnHide.setVisibility(value ? View.VISIBLE : View.GONE);
    }

    public void setSelectAll(boolean selectAll) {
        if (itemSelectAll == null) {
            return;
        }
        if (selectAll) {
            itemSelectAll.setIcon(R.drawable.ic_check_box_white_48dp);
        } else {
            itemSelectAll.setIcon(R.drawable.ic_check_box_outline_white_48dp);
        }
    }

    public void onAllVideosLoaded(ArrayList<AllVideosModel> allVideoModels) {
        if (allVideoModels == null || allVideoModels.size() <= 0) {
            viewanimator.setDisplayedChild(2);
            return;
        }
        adapter.addItems(allVideoModels);
        viewanimator.setDisplayedChild(1);
    }

    @OnClick({R.id.btn_hide})
    public void onClick() {
        if (adapter != null) {
            final List<String> selectedFiles = adapter.getSelectedImages();
            if (selectedFiles == null || selectedFiles.size() <= 0) {
                Toast.makeText(this, getString(R.string.select_1_video), Toast.LENGTH_SHORT).show();
                return;
            }
            count = 0;
            showProgressDialog(selectedFiles);
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {

                class C06481 implements Runnable {
                    C06481() {
                    }

                    public void run() {
                        publishProgress(selectedFiles.size());
                    }
                }

                class C06492 implements Runnable {
                    C06492() {
                    }

                    public void run() {
                        hideProgressDialog();
                        setBackData();
                    }
                }

                public void run() {
                    if (count == selectedFiles.size()) {
                        timer.cancel();
                        isImageAddedToNewAlbum = true;
                        runOnUiThread(new C06492());
                    } else if (isFileCopied) {
                        runOnUiThread(new C06481());
                        isFileCopied = false;
                        File src = new File(selectedFiles.get(count));
                        moveFile(src, new File(destPath, src.getName()));
                        count = count + 1;
                        isImageAddedToNewAlbum = true;
                    }
                }
            }, 0, 200);
        }
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

    public void onBackPressed() {
        setBackData();
    }

    private void showProgressDialog(List<String> files) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_progress);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setLayout(-1, -2);
        }
        progressbar = dialog.findViewById(R.id.progress_bar);
        txtCount = dialog.findViewById(R.id.txt_count);
        TextView txtTitle = dialog.findViewById(R.id.txt_title);
        txtTitle.setText(getString(R.string.moving_videos));
        this.txtCount.setText(getString(R.string.moving_1_of,files.size()));
        int totalFileSize = 0;
        for (String ss : files) {
            totalFileSize += (int) new File(ss).length();
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
            txtCount.setText(getString(R.string.moving_dash_of_dash_size,(count + 1),size));

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
                        runOnUiThread(new C06512());
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
                runOnUiThread();
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
            e.printStackTrace();
        }
    }

    protected void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
        }
        hideProgressDialog();
    }
}
