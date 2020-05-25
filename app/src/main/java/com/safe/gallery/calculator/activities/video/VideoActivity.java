package com.safe.gallery.calculator.activities.video;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Files;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.utils.AppConstants;
import com.safe.gallery.calculator.activities.BaseActivity;
import com.safe.gallery.calculator.MainApplication;
import com.safe.gallery.calculator.callbacks.OnVideosLoadedListener;
import com.safe.gallery.calculator.db.DBHelper;
import com.safe.gallery.calculator.activities.FullScreenImageActivity;
import com.safe.gallery.calculator.model.AllImagesModel;
import com.safe.gallery.calculator.model.AllVideosModel;
import com.safe.gallery.calculator.adapters.video.VideoAdapter;

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

public class VideoActivity extends BaseActivity implements OnVideosLoadedListener {


    String TAG = "TAG";

    private VideoAdapter adapter;

    Button btnUnhide;
    private int count;
    DBHelper dbHelper;
    private Dialog dialog;
    FloatingActionButton fabAdd;
    private boolean isEditable;
    private boolean isFileCopied = true;
    private boolean isSelectAll;
    private MenuItem menuItemDelete;
    private MenuItem menuItemEdit;
    private MenuItem menuItemSelect;
    private int progress;
    private ProgressBar progressbar;
    RecyclerView recyclerview;

    private Timer timer;
    Toolbar toolbar;
    private TextView txtCount;
    TextView txtError;
    ViewAnimator viewanimator;


    class C06392 implements Runnable {
        C06392() {
        }

        public void run() {
            progressbar.setProgress(progress);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        dbHelper = new DBHelper(this);

        findViews();
        setHeaderInfo();

        Init();
    }

    private void findViews() {

        btnUnhide = findViewById(R.id.btn_unhide);
        fabAdd = findViewById(R.id.fab_add);
        recyclerview = findViewById(R.id.recyclerview);
        toolbar = findViewById(R.id.toolbar);
        txtError = findViewById(R.id.txt_error);
        viewanimator = findViewById(R.id.viewanimator);
    }

    private void setHeaderInfo() {
        //toolbar.setNavigationIcon((int) R.drawable.ic_arrow);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.video));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void Init() {

        //LoadBannerAd();
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setAdapter() {
        adapter = new VideoAdapter(this);
        recyclerview.setAdapter(adapter);
        GetHiddenVideos task = new GetHiddenVideos();
        task.onVideosLoadedListener = this;
        task.execute(new Void[0]);
    }

    @OnClick({R.id.fab_add, R.id.btn_unhide})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_unhide:
                recoverFiles();
                return;
            case R.id.fab_add:

                startActivityForResult(new Intent(this, AddVideoActivity.class), 1012);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                return;

            default:
                return;
        }
    }

    private void recoverFiles() {
        if (adapter != null) {
            final List<String> selectedFiles = adapter.getSelectedImages();
            if (selectedFiles == null || selectedFiles.size() <= 0) {
                Toast.makeText(this, getString(R.string.select_1_video), Toast.LENGTH_LONG).show();
                return;
            }
            showProgressDialog(selectedFiles);
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {

                class C06361 implements Runnable {
                    C06361() {
                    }

                    public void run() {
                        publishProgress(selectedFiles.size());
                    }
                }

                class C06372 implements Runnable {
                    C06372() {
                    }

                    public void run() {
                        hideProgressDialog();
                        btnUnhide.setVisibility(View.GONE);
                        if (menuItemEdit != null) {
                            menuItemEdit.setVisible(true);
                        }
                        if (menuItemSelect != null) {
                            menuItemSelect.setVisible(false);
                            menuItemSelect.setIcon(R.drawable.ic_check_box_outline_white_48dp);
                        }
                        if (menuItemDelete != null) {
                            menuItemDelete.setVisible(false);
                        }
                        isEditable = false;
                        if (getSupportActionBar() != null) {
//                            Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow);
//                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//                            Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 70, 70, true));
//                            newdrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

                        }
                        //toolbar.setNavigationIcon((int) R.drawable.ic_arrow);
                        setAdapter();
                    }
                }

                public void run() {
                    if (count == selectedFiles.size()) {
                        timer.cancel();
                        count = 0;
                        runOnUiThread(new C06372());
                    } else if (isFileCopied) {
                        runOnUiThread(new C06361());
                        isFileCopied = false;
                        File src = new File(selectedFiles.get(count));
                        File file = new File(AppConstants.VIDEO_EXPORT_PATH);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        moveFile(src, new File(AppConstants.VIDEO_EXPORT_PATH, src.getName()));
                        count = count + 1;
                    }
                }
            }, 0, 200);
        }
    }

    private void showProgressDialog(List<String> files) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_progress);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setLayout(-1, -2);
        }
        progressbar = dialog.findViewById(R.id.progress_bar);
        txtCount = dialog.findViewById(R.id.txt_count);
        ((TextView) dialog.findViewById(R.id.txt_title)).setText(getString(R.string.moving_videos));
        txtCount.setText(getString(R.string.moving_1_of)+" " + files.size());
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
                        runOnUiThread(new C06392());
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1012 && resultCode == -1) {
            viewanimator.setDisplayedChild(0);
            setAdapter();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_menu, menu);
        menuItemSelect = menu.findItem(R.id.itm_select);
        menuItemDelete = menu.findItem(R.id.itm_delete);
        menuItemEdit = menu.findItem(R.id.itm_edit);
        setAdapter();
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.itm_delete:
                final AlertDialog alertDialog = new Builder(this).create();
                alertDialog.setTitle(getString(R.string.alert));
                alertDialog.setMessage(getString(R.string.delete_file_desc));
                alertDialog.setCancelable(false);
                alertDialog.setButton(-1, getString(R.string.yes), (dialog, which) -> {
                    deleteSelectedFiles();
                    onBackPressed();
                    alertDialog.dismiss();
                });
                alertDialog.setButton(-2, getString(R.string.no), (dialog, which) -> alertDialog.dismiss());
                alertDialog.show();
                break;
            case R.id.itm_edit:
                isEditable = true;
                item.setVisible(false);
                if (menuItemSelect != null) {
                    menuItemSelect.setVisible(true);
                }
                if (adapter != null) {
                    adapter.isItemEditable(true);
                }
                //toolbar.setNavigationIcon((int) R.drawable.ic_close);
                if (getSupportActionBar() != null) {
//                    Drawable drawable = getResources().getDrawable(R.drawable.ic_close);
//                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//                    Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 70, 70, true));
//                    newdrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);

                }
                break;
            case R.id.itm_select:
                if (menuItemSelect != null) {
                    if (!isSelectAll) {
                        menuItemSelect.setIcon(R.drawable.ic_check_box_white_48dp);
                        if (adapter != null) {
                            adapter.selectAllItem();
                        }
                        showDeleteButton(true);
                        isSelectAll = true;
                        break;
                    }
                    menuItemSelect.setIcon(R.drawable.ic_check_box_outline_white_48dp);
                    if (adapter != null) {
                        adapter.deSelectAllItem();
                    }
                    showDeleteButton(false);
                    isSelectAll = false;
                    break;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteSelectedFiles() {
        if (adapter != null) {
            List<String> selectedFiles = adapter.getSelectedImagePaths();
            if (selectedFiles != null && selectedFiles.size() > 0) {
                for (String selectedFile : selectedFiles) {
                    new File(selectedFile).delete();
                }
            }
            showDeleteButton(false);
            setAdapter();
        }
    }

    public void showDeleteButton(boolean needToshow) {
        if (menuItemDelete != null) {
            menuItemDelete.setVisible(needToshow);
        }
        btnUnhide.setVisibility(needToshow ? View.INVISIBLE : View.GONE);
    }

    public void showSelectAllButton(boolean needToShow) {
        if (menuItemSelect != null) {
            menuItemSelect.setIcon(needToShow ? R.drawable.ic_check_box_white_48dp : R.drawable.ic_check_box_outline_white_48dp);
            isSelectAll = needToShow;
        }
    }

    public void onBackPressed() {
        if (isEditable) {
            if (menuItemEdit != null) {
                menuItemEdit.setVisible(true);
            }
            if (menuItemSelect != null) {
                menuItemSelect.setVisible(false);
                menuItemSelect.setIcon(R.drawable.ic_check_box_outline_white_48dp);
            }
            if (menuItemDelete != null) {
                menuItemDelete.setVisible(false);
            }
            isEditable = false;
            if (adapter != null) {
                adapter.isItemEditable(false);
            }
            if (adapter != null) {
                adapter.deSelectAllItem();
            }
            if (getSupportActionBar() != null) {
//                Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow);
//                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//                Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 70, 70, true));
//                newdrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

            }
            //toolbar.setNavigationIcon((int) R.drawable.ic_arrow);
            btnUnhide.setVisibility(View.INVISIBLE);
            return;
        }
        setBackData();
    }

    private void setBackData() {
        setResult(-1, new Intent());
        finish();
    }

    public void onVideosLoaded(ArrayList<AllVideosModel> allVideoModels) {
        if (allVideoModels == null || allVideoModels.size() <= 0) {
            MainApplication.getInstance().saveVideoCount(0);
            enableMenuItems(false);
            viewanimator.setDisplayedChild(2);
            return;
        }
        adapter.addItems(allVideoModels);
        MainApplication.getInstance().saveVideoCount(allVideoModels.size());
        enableMenuItems(true);
        viewanimator.setDisplayedChild(1);
    }

    private void enableMenuItems(boolean isEnabled) {
        if (menuItemEdit != null) {
            menuItemEdit.setVisible(isEnabled);
        }
    }

    public void openVideo(final String videoPath) {


        try {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoPath));
            intent.setDataAndType(Uri.parse(videoPath), "video/*");
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.no_app_found), Toast.LENGTH_LONG).show();
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
