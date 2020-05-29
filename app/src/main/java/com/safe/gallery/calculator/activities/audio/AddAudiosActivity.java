package com.safe.gallery.calculator.activities.audio;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.activities.BaseActivity;
import com.safe.gallery.calculator.adapters.AddAudiosAdapter;
import com.safe.gallery.calculator.callbacks.OnAllAudiosLoadedListener;
import com.safe.gallery.calculator.db.DBHelper;
import com.safe.gallery.calculator.model.AllAudioModel;
import com.safe.gallery.calculator.utils.AppConstants;

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

public class AddAudiosActivity extends BaseActivity implements OnAllAudiosLoadedListener {

    AddAudiosAdapter adapter;
    //@BindView(R.id.banner_container)
    LinearLayout bannerContainer;
    //@BindView(R.id.btn_hide)
    Button btnHide;
    private int count;
    DBHelper dbHelper;
    private String destPath;
    private Dialog dialog;
    private boolean isAllSelected;
    private boolean isFileCopied = true;
    private boolean isImageAddedToNewAlbum;
    private MenuItem itemSelectAll;
    private int progress;
    private ProgressBar progressbar;
    //@BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    /* renamed from: t */
    private Timer f12t;
    //@BindView(R.id.toolbar)
    Toolbar toolbar;
    private TextView txtCount;
    //@BindView(R.id.txt_error)
    TextView txtError;
    //@BindView(R.id.viewanimator)
    ViewAnimator viewanimator;


    class C05092 implements Runnable {
        C05092() {
        }

        public void run() {
            AddAudiosActivity.this.progressbar.setProgress(AddAudiosActivity.this.progress);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adds_image);
        ButterKnife.bind(this);
        this.dbHelper = new DBHelper(this);
        findViews();
        setHeaderInfo();
        Init();
    }

    private void findViews() {

        bannerContainer = findViewById(R.id.banner_container);
        recyclerview = findViewById(R.id.recyclerview);
        toolbar = findViewById(R.id.toolbar);
        txtError = findViewById(R.id.txt_error);
        viewanimator = findViewById(R.id.viewanimator);
        btnHide = findViewById(R.id.btn_hide);
    }


    private void setHeaderInfo() {
        //this.toolbar.setNavigationIcon((int) R.drawable.ic_close);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setTitle(getString(R.string.add_audios));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
    }

    private void Init() {
        File file = new File(AppConstants.AUDIO_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        this.destPath = file.getAbsolutePath();
        this.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        this.adapter = new AddAudiosAdapter(this);
        this.recyclerview.setAdapter(this.adapter);
        GetAllAudiosAsyncTask task = new GetAllAudiosAsyncTask();
        task.onAllAudiosLoadedListener = this;
        task.execute(new Void[0]);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_image_menu, menu);
        this.itemSelectAll = menu.findItem(R.id.action_select_all);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_select_all:
                if (!this.isAllSelected) {
                    if (this.adapter != null) {
                        this.adapter.selectAllItem();
                    }
                    this.isAllSelected = true;
                    item.setIcon(R.drawable.ic_check_box_white_48dp);
                    showHideButton(true);
                    break;
                }
                if (this.adapter != null) {
                    this.adapter.deSelectAllItem();
                }
                this.isAllSelected = false;
                item.setIcon(R.drawable.ic_check_box_outline_white_48dp);
                showHideButton(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showHideButton(boolean value) {
        this.btnHide.setVisibility(value ? View.VISIBLE : View.GONE);
    }

    public void setSelectAll(boolean selectAll) {
        if (this.itemSelectAll == null) {
            return;
        }
        if (selectAll) {
            this.itemSelectAll.setIcon(R.drawable.ic_check_box_white_48dp);
        } else {
            this.itemSelectAll.setIcon(R.drawable.ic_check_box_outline_white_48dp);
        }
    }

    @OnClick({R.id.btn_hide})
    public void onClick() {
        if (this.adapter != null) {
            final List<String> selectedFiles = this.adapter.getSelectedImages();
            if (selectedFiles == null || selectedFiles.size() <= 0) {
                Toast.makeText(this, getString(R.string.select_atleat_1_image), Toast.LENGTH_SHORT).show();
                return;
            }
            showProgressDialog(selectedFiles);
            this.f12t = new Timer();
            this.f12t.scheduleAtFixedRate(new TimerTask() {

                class C05061 implements Runnable {
                    C05061() {
                    }

                    public void run() {
                        AddAudiosActivity.this.publishProgress(selectedFiles.size());
                    }
                }

                class C05072 implements Runnable {
                    C05072() {
                    }

                    public void run() {
                        AddAudiosActivity.this.hideProgressDialog();
                        AddAudiosActivity.this.setBackData();
                    }
                }

                public void run() {
                    if (AddAudiosActivity.this.count == selectedFiles.size()) {
                        AddAudiosActivity.this.f12t.cancel();
                        AddAudiosActivity.this.isImageAddedToNewAlbum = true;
                        AddAudiosActivity.this.runOnUiThread(new C05072());
                    } else if (AddAudiosActivity.this.isFileCopied) {
                        AddAudiosActivity.this.runOnUiThread(new C05061());
                        AddAudiosActivity.this.isFileCopied = false;
                        File src = new File((String) selectedFiles.get(AddAudiosActivity.this.count));
                        AddAudiosActivity.this.moveFile(src, new File(AddAudiosActivity.this.destPath, src.getName()));
                        AddAudiosActivity.this.count = AddAudiosActivity.this.count + 1;
                        AddAudiosActivity.this.isImageAddedToNewAlbum = true;
                    }
                }
            }, 0, 200);
        }
    }

    private void setBackData() {
        if (!this.isImageAddedToNewAlbum) {
            File file = new File(this.destPath);
            if (file.exists()) {
                file.delete();
            }
        }
        Intent intent = new Intent();
        intent.putExtra(AppConstants.HIDDEN_RESULT, this.isImageAddedToNewAlbum);
        setResult(-1, intent);
        finish();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
    }

    public void onBackPressed() {
        setBackData();
    }

    private void showProgressDialog(List<String> files) {
        this.dialog = new Dialog(this);
        this.dialog.requestWindowFeature(1);
        this.dialog.setContentView(R.layout.dialog_progress);
        if (this.dialog.getWindow() != null) {
            this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            this.dialog.getWindow().setLayout(-1, -2);
        }
        this.progressbar = this.dialog.findViewById(R.id.progress_bar);
        this.txtCount = this.dialog.findViewById(R.id.txt_count);
        ((TextView) this.dialog.findViewById(R.id.txt_title)).setText(getString(R.string.moving_audios));
        this.txtCount.setText(getString(R.string.moving_1_of,files.size()));
        int totalFileSize = 0;
        for (String ss : files) {
            totalFileSize += (int) new File(ss).length();
        }
        this.progressbar.setMax(totalFileSize);
        this.dialog.show();
    }

    private void hideProgressDialog() {
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
        }
    }

    private void publishProgress(int size) {
        if (this.dialog != null && this.dialog.isShowing()) {
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
                        this.progress += len;
                        runOnUiThread(new C05092());
                    } else {
                        out.close();
                        this.isFileCopied = true;
                        runOnUiThread(() -> AddAudiosActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(dst))));
                        in.close();
                        runOnUiThread(() -> AddAudiosActivity.this.deleteFilePath(src));
                        this.isFileCopied = true;
                        return;
                    }
                }
            } catch (Throwable th) {
                in.close();
                runOnUiThread(/* anonymous class already generated */);
                this.isFileCopied = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.isFileCopied = true;
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

    public void onAllAudiosLoaded(ArrayList<AllAudioModel> allAudioModels) {
        if (allAudioModels == null || allAudioModels.size() <= 0) {
            this.viewanimator.setDisplayedChild(2);
            return;
        }
        this.adapter.addItems(allAudioModels);
        this.viewanimator.setDisplayedChild(1);
    }


    protected void onStop() {
        super.onStop();
        if (this.f12t != null) {
            this.f12t.cancel();
        }
        hideProgressDialog();
    }
}
