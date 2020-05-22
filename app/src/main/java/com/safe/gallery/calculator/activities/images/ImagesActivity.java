package com.safe.gallery.calculator.activities.images;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
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
import com.safe.gallery.calculator.callbacks.OnImagesLoadedListener;
import com.safe.gallery.calculator.db.DBHelper;
import com.safe.gallery.calculator.activities.FullScreenImageActivity;
import com.safe.gallery.calculator.adapters.images.ImagesAdapter;
import com.safe.gallery.calculator.model.AllImagesModel;

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

public class ImagesActivity extends BaseActivity implements OnImagesLoadedListener {

    private static final String TAG = "ImagesActivity";
    private ImagesAdapter adapter;
    @BindView(R.id.banner_container)
    LinearLayout bannerContainer;
    @BindView(R.id.btn_unhide)
    Button btnUnhide;
    private int count;
    DBHelper dbHelper;
    private Dialog dialog;
    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;
    private boolean isEditable;
    private boolean isFileCopied = true;
    private boolean isSelectAll;
    private MenuItem menuItemDelete;
    private MenuItem menuItemEdit;
    private MenuItem menuItemSelect;
    private LinearLayout nativeAdContainer;
    private int progress;
    private ProgressDialog progressDialog;
    private ProgressBar progressbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    /* renamed from: t */
    private Timer f16t;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private TextView txtCount;
    @BindView(R.id.txt_error)
    TextView txtError;
    @BindView(R.id.viewanimator)
    ViewAnimator viewanimator;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_image);
        ButterKnife.bind((Activity) this);
        this.dbHelper = new DBHelper(this);
        setHeaderInfo();
        Init();
    }

    //This is to show the Progressbar
    class C05982 implements Runnable {
        C05982() {
        }

        public void run() {
            ImagesActivity.this.progressbar.setProgress(ImagesActivity.this.progress);
        }
    }


    private void setHeaderInfo() {
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setTitle(getString(R.string.image));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void Init() {
        this.recyclerview.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private void setAdapter() {
        this.adapter = new ImagesAdapter(this);
        this.recyclerview.setAdapter(this.adapter);
        GetHiddenImages task = new GetHiddenImages();
        task.onImagesLoadedListener = this;
        task.execute(new Void[0]);
    }


    @OnClick({R.id.fab_add, R.id.btn_unhide})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_unhide:
                recoverFiles();
                onBackPressed();
                return;
            case R.id.fab_add:

                startActivityForResult(new Intent(this, AddImageActivity.class), 1012);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                return;


            default:
                return;
        }
    }

    private void recoverFiles() {

        if (this.adapter != null) {
            final List<String> selectedFiles = this.adapter.getSelectedImages();
            if (selectedFiles == null || selectedFiles.size() <= 0) {
                Toast.makeText(this, "Please select at least one image!", 0).show();
                return;
            }
            showProgressDialog(selectedFiles);
            this.f16t = new Timer();
            this.f16t.scheduleAtFixedRate(new TimerTask() {

                class C05951 implements Runnable {
                    C05951() {
                    }

                    public void run() {
                        ImagesActivity.this.publishProgress(selectedFiles.size());
                    }
                }

                class C05962 implements Runnable {
                    C05962() {
                    }

                    public void run() {
                        ImagesActivity.this.hideProgressDialog();
                        ImagesActivity.this.btnUnhide.setVisibility(8);
                        if (ImagesActivity.this.menuItemEdit != null) {
                            ImagesActivity.this.menuItemEdit.setVisible(true);
                        }
                        if (ImagesActivity.this.menuItemSelect != null) {
                            ImagesActivity.this.menuItemSelect.setVisible(false);
                            ImagesActivity.this.menuItemSelect.setIcon(R.drawable.ic_check_box_outline_white_48dp);
                        }
                        if (ImagesActivity.this.menuItemDelete != null) {
                            ImagesActivity.this.menuItemDelete.setVisible(false);
                        }
                        ImagesActivity.this.isEditable = false;
                        if (getSupportActionBar() != null) {
//                            Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow);
//                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//                            Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 70, 70, true));
//                            newdrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

                        }
                        //ImagesActivity.this.toolbar.setNavigationIcon((int) R.drawable.ic_arrow);
                        ImagesActivity.this.setAdapter();
                    }
                }

                public void run() {
                    if (ImagesActivity.this.count == selectedFiles.size()) {
                        ImagesActivity.this.f16t.cancel();
                        ImagesActivity.this.count = 0;
                        ImagesActivity.this.runOnUiThread(new C05962());
                    } else if (ImagesActivity.this.isFileCopied) {
                        ImagesActivity.this.runOnUiThread(new C05951());
                        ImagesActivity.this.isFileCopied = false;
                        File src = new File((String) selectedFiles.get(ImagesActivity.this.count));
                        File file = new File(AppConstants.IMAGE_EXPORT_PATH);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        ImagesActivity.this.moveFile(src, new File(AppConstants.IMAGE_EXPORT_PATH, src.getName()));
                        ImagesActivity.this.count = ImagesActivity.this.count + 1;
                    }
                }
            }, 0, 200);
        }
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
                        runOnUiThread(new C05982());
                    } else {
                        out.close();
                        this.isFileCopied = true;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                ImagesActivity.this.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(dst)));
                            }
                        });
                        in.close();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                ImagesActivity.this.deleteFilePath(src);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1012 && resultCode == -1) {
            this.viewanimator.setDisplayedChild(0);
            setAdapter();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_menu, menu);
        this.menuItemSelect = menu.findItem(R.id.itm_select);
        this.menuItemDelete = menu.findItem(R.id.itm_delete);
        this.menuItemEdit = menu.findItem(R.id.itm_edit);
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
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Are you sure to delete selected files?");
                alertDialog.setCancelable(false);
                alertDialog.setButton(-1, (CharSequence) "Yes", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ImagesActivity.this.deleteSelectedFiles();
                        onBackPressed();
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setButton(-2, (CharSequence) "No", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                break;
            case R.id.itm_edit:
                this.isEditable = true;
                item.setVisible(false);
                if (this.menuItemSelect != null) {
                    this.menuItemSelect.setVisible(true);
                }
                if (this.adapter != null) {
                    this.adapter.isItemEditable(true);
                }
                if (getSupportActionBar() != null) {
//                    Drawable drawable = getResources().getDrawable(R.drawable.ic_close);
//                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//                    Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 70, 70, true));
//                    newdrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
//                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white);

                }
                // this.toolbar.setNavigationIcon((int) R.drawable.ic_close);
                break;
            case R.id.itm_select:
                if (this.menuItemSelect != null) {
                    if (!this.isSelectAll) {
                        this.menuItemSelect.setIcon(R.drawable.ic_check_box_white_48dp);
                        if (this.adapter != null) {
                            this.adapter.selectAllItem();
                        }
                        showDeleteButton(true);
                        this.isSelectAll = true;
                        break;
                    }
                    this.menuItemSelect.setIcon(R.drawable.ic_check_box_outline_white_48dp);
                    if (this.adapter != null) {
                        this.adapter.deSelectAllItem();
                    }
                    showDeleteButton(false);
                    this.isSelectAll = false;
                    break;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteSelectedFiles() {
        if (this.adapter != null) {
            List<String> selectedFiles = this.adapter.getSelectedImagePaths();
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
        if (this.menuItemDelete != null) {
            this.menuItemDelete.setVisible(needToshow);
        }
        this.btnUnhide.setVisibility(needToshow ? 0 : 8);
    }

    public void showSelectAllButton(boolean needToShow) {
        if (this.menuItemSelect != null) {
            this.menuItemSelect.setIcon(needToShow ? R.drawable.ic_check_box_white_48dp : R.drawable.ic_check_box_outline_white_48dp);
            this.isSelectAll = needToShow;
        }
    }

    public void startFullScreenImageActivity(final ArrayList<AllImagesModel> buckets, final int position) {

        Intent fullScreenImageActivityIntent = new Intent(this, FullScreenImageActivity.class);
        fullScreenImageActivityIntent.putExtra(FullScreenImageActivity.OBJECT, buckets);
        fullScreenImageActivityIntent.putExtra(FullScreenImageActivity.POSITION, position);
        startActivity(fullScreenImageActivityIntent);
        finish();

    }

    public void onBackPressed() {
        if (this.isEditable) {
            if (this.menuItemEdit != null) {
                this.menuItemEdit.setVisible(true);
            }
            if (this.menuItemSelect != null) {
                this.menuItemSelect.setVisible(false);
                this.menuItemSelect.setIcon(R.drawable.ic_check_box_outline_white_48dp);
            }
            if (this.menuItemDelete != null) {
                this.menuItemDelete.setVisible(false);
            }
            this.isEditable = false;
            if (this.adapter != null) {
                this.adapter.isItemEditable(false);
            }
            if (this.adapter != null) {
                this.adapter.deSelectAllItem();
            }
            //this.toolbar.setNavigationIcon((int) R.drawable.ic_arrow);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

            }
            this.btnUnhide.setVisibility(8);
            return;
        }
        setBackData();
    }

    private void setBackData() {
        setResult(-1, new Intent());
        finish();
    }

    private void enableMenuItems(boolean isEnabled) {
        if (this.menuItemEdit != null) {
            this.menuItemEdit.setVisible(isEnabled);
        }
    }

    public void onImagesLoaded(ArrayList<AllImagesModel> allImageModels) {
        if (allImageModels == null || allImageModels.size() <= 0) {
            Log.i(TAG, "onImagesLoaded: zero images");
            MainApplication.getInstance().saveImageCount(0);
            enableMenuItems(false);
            this.viewanimator.setDisplayedChild(2);
            return;
        }
        this.adapter.addItems(allImageModels);
        MainApplication.getInstance().saveImageCount(allImageModels.size());
        enableMenuItems(true);
        this.viewanimator.setDisplayedChild(1);
    }

  /*  private void LoadBannerAd() {
        new AdsManager().LoadBannerAd(this, this.bannerContainer);
    }*/

    protected void onStop() {
        super.onStop();
        if (this.f16t != null) {
            this.f16t.cancel();
        }
        hideProgressDialog();
        Log.e("TAG", "onStop: ");
    }

}
