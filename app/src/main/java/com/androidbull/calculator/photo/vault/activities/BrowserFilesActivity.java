package com.androidbull.calculator.photo.vault.activities;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidbull.calculator.photo.R;
import com.androidbull.calculator.photo.vault.adapters.BrowserFilesAdapter;
import com.androidbull.calculator.photo.vault.callbacks.OnFilesLoadedListener;
import com.androidbull.calculator.photo.vault.model.AllFilesModel;
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

import static com.androidbull.calculator.photo.vault.utils.Utils.isPackageInstalled;
import static com.androidbull.calculator.photo.vault.utils.Utils.openAppInPlayStore;

public class BrowserFilesActivity extends BaseActivity implements OnFilesLoadedListener {

    private Button btnDownloadBrowser, btnUnHide;
    private RecyclerView rvBrowserFiles;
    private BrowserFilesAdapter browserFilesAdapter;
    private ArrayList<AllFilesModel> files;
    private TextView tvNothingDownloaded;
    private MenuItem menuItemDelete, menuItemEdit, menuItemSelect;
    private boolean isEditable, isSelectAll;
    private Toolbar toolbar;
    private int count;
    private Dialog dialog;
    private boolean isFileCopied = true;
    private Timer f16t;
    private int progress;
    private ProgressBar progressbar;
    private TextView txtCount, tvHeading;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_files);

        initUi();
        initActions();
        setHeaderInfo();

        initBrowserFilesRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setHeaderInfo() {
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.image));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    private void getFiles() {

        GetHiddenBrowserFiles task = new GetHiddenBrowserFiles();
        task.onFilesLoadedListener = this;
        task.execute();
    }

    private void initUi() {
        rvBrowserFiles = findViewById(R.id.rvBrowserFiles);
        btnDownloadBrowser = findViewById(R.id.btnDownloadBrowser);
        btnUnHide = findViewById(R.id.btnUnhide);
        tvNothingDownloaded = findViewById(R.id.tvNothingDownloaded);
        tvHeading = findViewById(R.id.tvHeading);
        toolbar = findViewById(R.id.toolbar);

    }

    private void initActions() {
        btnDownloadBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAppInPlayStore(BrowserFilesActivity.this, getString(R.string.browser_package_name));
            }
        });

        btnUnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unHideFiles();
            }
        });
    }

    private boolean isBrowserInstalled() {
        return isPackageInstalled(getString(R.string.browser_package_name), getPackageManager()) ||
                isPackageInstalled(getString(R.string.paid_browser_package_name), getPackageManager());
    }

    private void initBrowserFilesRecyclerView() {
        files = new ArrayList<>();
        browserFilesAdapter = new BrowserFilesAdapter(this, files, (file, position) -> {
            try {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(file.getOldPath())), getMimeType(Uri.fromFile(new File(file.getOldPath()))));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(BrowserFilesActivity.this, getString(R.string.no_app_found), Toast.LENGTH_SHORT).show();
            }
        });
        rvBrowserFiles.setLayoutManager(new GridLayoutManager(this, 3));
        rvBrowserFiles.setAdapter(browserFilesAdapter);
    }

    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    @Override
    public void onFilesLoaded(ArrayList<AllFilesModel> files) {


        if (files.isEmpty()) {
            tvNothingDownloaded.setVisibility(View.VISIBLE);
            tvHeading.setVisibility(View.GONE);
            if (isBrowserInstalled()) {
                btnDownloadBrowser.setVisibility(View.GONE);
            } else {
                btnDownloadBrowser.setVisibility(View.VISIBLE);
            }
        } else {
            tvNothingDownloaded.setVisibility(View.GONE);
            tvHeading.setVisibility(View.VISIBLE);
            enableMenuItems(true);
        }

        updateBrowserFilesAdapter(files);
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
            if (browserFilesAdapter != null) {
                browserFilesAdapter.isItemEditable(false);
            }
            if (browserFilesAdapter != null) {
                browserFilesAdapter.deSelectAllItem();
            }
            //this.toolbar.setNavigationIcon((int) R.drawable.ic_arrow);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

            }
            btnUnHide.setVisibility(View.GONE);
        }
    }

    private void updateBrowserFilesAdapter(ArrayList<AllFilesModel> files) {
        this.files.clear();
        this.files = files;
        browserFilesAdapter.updateAdapter(files);
    }

    private void enableMenuItems(boolean isEnabled) {
        if (menuItemEdit != null) {
            menuItemEdit.setVisible(isEnabled);
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.image_menu, menu);
        menuItemSelect = menu.findItem(R.id.itm_select);
        menuItemDelete = menu.findItem(R.id.itm_delete);
        menuItemEdit = menu.findItem(R.id.itm_edit);
        getFiles();
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
            case R.id.itm_delete:
                final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
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
                if (browserFilesAdapter != null) {
                    browserFilesAdapter.isItemEditable(true);
                }
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);


                break;
            case R.id.itm_select:
                if (menuItemSelect != null) {
                    if (!isSelectAll) {
                        menuItemSelect.setIcon(R.drawable.ic_check_box_white_48dp);
                        if (browserFilesAdapter != null) {
                            browserFilesAdapter.selectAllItem();
                        }
                        showDeleteButton(true);
                        isSelectAll = true;
                        break;
                    }
                    menuItemSelect.setIcon(R.drawable.ic_check_box_outline_white_48dp);
                    if (browserFilesAdapter != null) {
                        browserFilesAdapter.deSelectAllItem();
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
        if (browserFilesAdapter != null) {
            List<String> selectedFiles = browserFilesAdapter.getSelectedImagePaths();
            if (selectedFiles != null && selectedFiles.size() > 0) {
                for (String selectedFile : selectedFiles) {
                    new File(selectedFile).delete();
                }
            }
            showDeleteButton(false);
            getFiles();
        }
    }

    public void showDeleteButton(boolean needToshow) {
        if (menuItemDelete != null) {
            menuItemDelete.setVisible(needToshow);
        }
        btnUnHide.setVisibility(needToshow ? View.VISIBLE : View.GONE);
    }

    public void showSelectAllButton(boolean needToShow) {
        if (menuItemSelect != null) {
            menuItemSelect.setIcon(needToShow ? R.drawable.ic_check_box_white_48dp : R.drawable.ic_check_box_outline_white_48dp);
            isSelectAll = needToShow;
        }
    }


    private void unHideFiles() {

        if (browserFilesAdapter != null) {
            final List<String> selectedFiles = browserFilesAdapter.getSelectedImages();
            if (selectedFiles == null || selectedFiles.size() <= 0) {
                Toast.makeText(this, getString(R.string.select_atleat_1_image), Toast.LENGTH_SHORT).show();
                return;
            }
            showProgressDialog(selectedFiles);
            this.f16t = new Timer();
            this.f16t.scheduleAtFixedRate(new TimerTask() {

                class C05951 implements Runnable {
                    C05951() {
                    }

                    public void run() {
                        publishProgress(selectedFiles.size());
                    }
                }

                class C05962 implements Runnable {
                    C05962() {
                    }

                    public void run() {
                        hideProgressDialog();
                        btnUnHide.setVisibility(View.GONE);
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
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

                        }
                        getFiles();
                    }
                }

                public void run() {
                    if (count == selectedFiles.size()) {
                        f16t.cancel();
                        count = 0;
                        runOnUiThread(new C05962());
                    } else if (isFileCopied) {
                        runOnUiThread(new C05951());
                        isFileCopied = false;
                        File src = new File(selectedFiles.get(count));
                        File file = new File(AppConstants.BROWSER_FILE_EXPORT_PATH);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        moveFile(src, new File(AppConstants.BROWSER_FILE_EXPORT_PATH, src.getName()));
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
        ((TextView) dialog.findViewById(R.id.txt_title)).setText(getString(R.string.moving_images));
        txtCount.setText(getString(R.string.moving_1_of, files.size()));
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
            txtCount.setText(getString(R.string.moving_dash_of_dash_size, (count + 1), size));
        }
    }

    class C05982 implements Runnable {
        C05982() {
        }

        public void run() {
            progressbar.setProgress(progress);
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
                        runOnUiThread(new C05982());
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

    private void deleteFilePath(File file) {
        try {
            String[] selectionArgs = new String[]{file.getAbsolutePath()};
            ContentResolver contentResolver = getContentResolver();
            Uri filesUri = MediaStore.Files.getContentUri("external");
            contentResolver.delete(filesUri, "_data=?", selectionArgs);
            if (file.exists()) {
                contentResolver.delete(filesUri, "_data=?", selectionArgs);
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void runOnUiThread() {
    }

    protected void onStop() {
        super.onStop();
        if (f16t != null) {
            f16t.cancel();
        }
        hideProgressDialog();
        Log.e("TAG", "onStop: ");
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
            if (browserFilesAdapter != null) {
                browserFilesAdapter.isItemEditable(false);
            }
            if (browserFilesAdapter != null) {
                browserFilesAdapter.deSelectAllItem();
            }
            //this.toolbar.setNavigationIcon((int) R.drawable.ic_arrow);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

            }
            btnUnHide.setVisibility(View.GONE);
            return;
        }
        setBackData();
    }


    private void setBackData() {
        setResult(-1, new Intent());
        finish();
    }
}
