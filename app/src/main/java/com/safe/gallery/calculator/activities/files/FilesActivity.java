package com.safe.gallery.calculator.activities.files;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.safe.gallery.calculator.MainApplication;
import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.activities.BaseActivity;
import com.safe.gallery.calculator.adapters.files.FilesAdapter;
import com.safe.gallery.calculator.callbacks.OnFilesLoadedListener;
import com.safe.gallery.calculator.db.DBHelper;
import com.safe.gallery.calculator.model.AllFilesModel;
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

public class FilesActivity extends BaseActivity implements OnFilesLoadedListener {

    private FilesAdapter adapter;
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


    class runnable implements Runnable {
        runnable() {
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
        initViews();
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
        getSupportActionBar().setTitle(getString(R.string.file));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

    }

    private void initViews() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setAdapter() {

        adapter = new FilesAdapter(this);
        recyclerview.setAdapter(adapter);
        GetHiddenFiles task = new GetHiddenFiles();
        task.onFilesLoadedListener = this;
        task.execute(new Void[0]);

    }

    @OnClick({R.id.fab_add, R.id.btn_unhide})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_unhide:

                recoverFiles();
                return;

            case R.id.fab_add:

                startActivityForResult(new Intent(this, AddFileActivity.class), 1012);
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
                Toast.makeText(this, getString(R.string.select_1_file), Toast.LENGTH_SHORT).show();
                return;
            }
            showProgressDialog(selectedFiles);
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {

                class C05621 implements Runnable {
                    C05621() {
                    }

                    public void run() {
                        publishProgress(selectedFiles.size());
                    }
                }

                class C05632 implements Runnable {
                    C05632() {
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
                        //toolbar.setNavigationIcon((int) R.drawable.ic_arrow);
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

                        }

                        setAdapter();
                    }
                }

                public void run() {
                    if (count == selectedFiles.size()) {
                        timer.cancel();
                        count = 0;
                        runOnUiThread(new C05632());
                    } else if (isFileCopied) {
                        runOnUiThread(new C05621());
                        isFileCopied = false;
                        File src = new File(selectedFiles.get(count));
                        File file = new File(AppConstants.FILE_EXPORT_PATH);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        moveFile(src, new File(AppConstants.FILE_EXPORT_PATH, src.getName()));
                        count = count + 1;
                    }
                }
            }, 0, 200);
        }
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
        ((TextView) dialog.findViewById(R.id.txt_title)).setText(getString(R.string.moving_files));
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
                        runOnUiThread(new runnable());
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
            case 16908332:
                onBackPressed();
                break;
            case R.id.itm_delete:
                final AlertDialog alertDialog = new Builder(this).create();
                alertDialog.setTitle(getString(R.string.alert));
                alertDialog.setMessage(getString(R.string.delete_file_desc));
                alertDialog.setCancelable(false);
                alertDialog.setButton(-1, getString(R.string.yes), (dialog, which) -> {
                    deleteSelectedFiles();
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
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);


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
        btnUnhide.setVisibility(needToshow ?View.VISIBLE: View.GONE);
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
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

            }
            btnUnhide.setVisibility(View.INVISIBLE);

            //toolbar.setNavigationIcon((int) R.drawable.ic_arrow);
            return;
        }
        setBackData();
    }

    private void setBackData() {
        setResult(-1, new Intent());
        finish();
    }

    private void enableMenuItems(boolean isEnabled) {
        if (menuItemEdit != null) {
            menuItemEdit.setVisible(isEnabled);
        }
    }

    public void onFilesLoaded(ArrayList<AllFilesModel> allFileModels) {
        if (allFileModels == null || allFileModels.size() <= 0) {
            MainApplication.getInstance().saveFilesCount(0);
            enableMenuItems(false);
            viewanimator.setDisplayedChild(2);
            return;
        }
        adapter.addItems(allFileModels);
        MainApplication.getInstance().saveFilesCount(allFileModels.size());
        enableMenuItems(true);
        viewanimator.setDisplayedChild(1);
    }

    public void openFile(String path) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(path)), "application/*");
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.no_app_found), Toast.LENGTH_SHORT).show();
        }
    }

}
