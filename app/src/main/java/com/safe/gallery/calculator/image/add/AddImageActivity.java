package com.safe.gallery.calculator.image.add;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Files;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.app.AppConstants;
import com.safe.gallery.calculator.app.BaseActivity;
import com.safe.gallery.calculator.callbacks.OnAllImagesLoadedListener;
import com.safe.gallery.calculator.db.DBHelper;
import com.safe.gallery.calculator.image.add.adapter.AllImageAdapter;
import com.safe.gallery.calculator.model.AllImagesModel;
import com.safe.gallery.calculator.utils.CenterTitleToolbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddImageActivity extends BaseActivity implements OnAllImagesLoadedListener {

    private static final String TAG = "AddImageActivity";
    private LinearLayout adView;
    AllImageAdapter adapter;
    @BindView(R.id.banner_container)
    LinearLayout bannerContainer;
    @BindView(R.id.btn_hide)
    Button btnHide;
    private int count;
    DBHelper dbHelper;
    private String destPath;
    private Dialog dialog;
    private boolean isAllSelected;
    private boolean isFileCopied = true;
    private boolean isImageAddedToNewAlbum;
    private MenuItem itemSelectAll;
    private LinearLayout nativeAdContainer;
    private int progress;
    private ProgressDialog progressDialog;
    private ProgressBar progressbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    /* renamed from: t */
    private Timer f17t;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private TextView txtCount;
    @BindView(R.id.txt_error)
    TextView txtError;
    @BindView(R.id.viewanimator)
    ViewAnimator viewanimator;


    class C06092 implements Runnable {
        C06092() {
        }

        public void run() {
            AddImageActivity.this.progressbar.setProgress(AddImageActivity.this.progress);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_adds_image);
        ButterKnife.bind((Activity) this);
        this.dbHelper = new DBHelper(this);
        setHeaderInfo();
        Init();
    }

    private void setHeaderInfo() {
        //this.toolbar.setNavigationIcon((int) R.drawable.ic_close);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setTitle(getString(R.string.add_image));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);

    }

    private void Init() {
        File file = new File(AppConstants.IMAGE_PATH);
        Log.i(TAG, "Init: Desctination path: " + file.getAbsolutePath());
        if (!file.exists()) {
            file.mkdirs();
        }
        this.destPath = file.getAbsolutePath();
        this.recyclerview.setLayoutManager(new GridLayoutManager(this, 3));
        this.adapter = new AllImageAdapter(this);
        this.recyclerview.setAdapter(this.adapter);
        GetAllImagesAsyncTask task = new GetAllImagesAsyncTask();
        task.onAllImagesLoadedListener = this;
        task.execute(new Void[0]);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_image_menu, menu);
        this.itemSelectAll = menu.findItem(R.id.action_select_all);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
            case R.id.action_select_all:
                if (!this.isAllSelected) {
                    if (this.adapter != null) {
                        this.adapter.selectAllItem();
                    }
                    this.isAllSelected = true;
                    item.setIcon(R.drawable.ic_check_filled);
                    showHideButton(true);
                    break;
                }
                if (this.adapter != null) {
                    this.adapter.deSelectAllItem();
                }
                this.isAllSelected = false;
                item.setIcon(R.drawable.ic_check_box_outline);
                showHideButton(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showHideButton(boolean value) {
        this.btnHide.setVisibility(value ? 0 : 8);
    }

    public void setSelectAll(boolean selectAll) {
        if (this.itemSelectAll == null) {
            return;
        }
        if (selectAll) {
            this.itemSelectAll.setIcon(R.drawable.ic_check_filled);
        } else {
            this.itemSelectAll.setIcon(R.drawable.ic_check_box_outline);
        }
    }

    public void onAllImagesLoaded(ArrayList<AllImagesModel> allImageModels) {
        if (allImageModels == null || allImageModels.size() <= 0) {
            this.viewanimator.setDisplayedChild(2);
            return;
        }
        this.adapter.addItems(allImageModels);
        this.viewanimator.setDisplayedChild(1);
    }

    @OnClick({R.id.btn_hide})
    public void onClick() {
        if (this.adapter != null) {
            final List<String> selectedFiles = this.adapter.getSelectedImages();
            if (selectedFiles == null || selectedFiles.size() <= 0) {
                Toast.makeText(this, "Please select at least one image!", 0).show();
                return;
            }
            if (!isFinishing()) {
                showProgressDialog(selectedFiles);
            }
            this.f17t = new Timer();
            this.f17t.scheduleAtFixedRate(new TimerTask() {

                class C06061 implements Runnable {
                    C06061() {
                    }

                    public void run() {
                        AddImageActivity.this.publishProgress(selectedFiles.size());
                    }
                }

                class C06072 implements Runnable {
                    C06072() {
                    }

                    public void run() {
                        AddImageActivity.this.hideProgressDialog();
                        AddImageActivity.this.setBackData();
                    }
                }

                public void run() {
                    if (AddImageActivity.this.count == selectedFiles.size()) {
                        AddImageActivity.this.f17t.cancel();
                        AddImageActivity.this.isImageAddedToNewAlbum = true;
                        AddImageActivity.this.runOnUiThread(new C06072());
                    } else if (AddImageActivity.this.isFileCopied) {
                        AddImageActivity.this.runOnUiThread(new C06061());
                        AddImageActivity.this.isFileCopied = false;
                        File src = new File((String) selectedFiles.get(AddImageActivity.this.count));
                        AddImageActivity.this.moveFile(src, new File(AddImageActivity.this.destPath, src.getName()));
                        AddImageActivity.this.count = AddImageActivity.this.count + 1;
                        AddImageActivity.this.isImageAddedToNewAlbum = true;
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
        this.progressbar = (ProgressBar) this.dialog.findViewById(R.id.progress_bar);
        this.txtCount = (TextView) this.dialog.findViewById(R.id.txt_count);
        this.nativeAdContainer = (LinearLayout) this.dialog.findViewById(R.id.native_ad_container);
        ((TextView) this.dialog.findViewById(R.id.txt_title)).setText("Moving Image(s)");
        this.txtCount.setText("Moving 1 of " + files.size());
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
            this.txtCount.setText("Moving " + (this.count + 1) + " of " + size);
        }
    }

    private void moveFile(final File src, final File dst) {
        OutputStream out;
        try {
            InputStream in = new FileInputStream(src);
            try {
                out = new FileOutputStream(dst);
                byte[] buf = new byte[1024];
                while (true) {
                    int len = in.read(buf);
                    if (len > 0) {
                        out.write(buf, 0, len);
                        this.progress += len;
                        runOnUiThread(new C06092());
                    } else {
                        out.close();
                        this.isFileCopied = true;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                AddImageActivity.this.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(dst)));
                            }
                        });
                        in.close();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                AddImageActivity.this.deleteFilePath(src);
                            }
                        });
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
            Toast.makeText(this, "" + e.getMessage(), 1).show();
        }
    }


    protected void onStop() {
        super.onStop();
        if (this.f17t != null) {
            this.f17t.cancel();
        }
        hideProgressDialog();
    }
}
