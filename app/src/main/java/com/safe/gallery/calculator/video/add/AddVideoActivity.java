package com.safe.gallery.calculator.video.add;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.safe.gallery.calculator.Constant;
import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.app.AppConstants;
import com.safe.gallery.calculator.app.BaseActivity;
import com.safe.gallery.calculator.app.MainApplication;
import com.safe.gallery.calculator.callbacks.OnAllVideosLoadedListener;
import com.safe.gallery.calculator.db.DBHelper;
import com.safe.gallery.calculator.model.AllVideosModel;
import com.safe.gallery.calculator.utils.CenterTitleToolbar;
import com.safe.gallery.calculator.video.add.adapter.AllVideoAdapter;

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


    DBHelper dbHelper;
    TextView txtError;
    CenterTitleToolbar toolbar;
    private TextView txtCount;

    ViewAnimator viewanimator;
    AllVideoAdapter adapter;

    private boolean isFileCopied = true;
    Button btnHide;
    private int count;

    private String destPath;
    private Dialog dialog;
    private boolean isAllSelected;

    private boolean isImageAddedToNewAlbum;
    private boolean isTransfering = true;
    private MenuItem itemSelectAll;
    private ViewGroup nativeAdContainer;
    private int progress = 0;
    private ProgressDialog progressDialog;
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
        setContentView((int) R.layout.activity_adds_image);
        ButterKnife.bind((Activity) this);
        dbHelper = new DBHelper(this);

        findViews();
        addBanner();
        setHeaderInfo();
        Init();
    }

    private void findViews() {

        btnHide = findViewById(R.id.btn_hide);
        recyclerview = findViewById(R.id.recyclerview);
        toolbar = findViewById(R.id.toolbar);
        txtError = findViewById(R.id.txt_error);
        viewanimator = findViewById(R.id.viewanimator);
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
       // toolbar.setNavigationIcon((int) R.drawable.ic_close);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.add_video));

        if(getSupportActionBar()!=null){
            Drawable drawable= getResources().getDrawable(R.drawable.ic_close);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 70, 70, true));
            newdrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(newdrawable);

        }
    }

    private void Init() {

        MainApplication.getInstance().LogFirebaseEvent("5", "AddVideo");
        File file = new File(AppConstants.VIDEO_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        destPath = file.getAbsolutePath();
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AllVideoAdapter(this);
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
            case 16908332:
                onBackPressed();
                break;
            case R.id.action_select_all:
                if (!isAllSelected) {
                    if (adapter != null) {
                        adapter.selectAllItem();
                    }
                    isAllSelected = true;
                    item.setIcon(R.drawable.ic_check_filled);
                    showHideButton(true);
                    break;
                }
                if (adapter != null) {
                    adapter.deSelectAllItem();
                }
                isAllSelected = false;
                item.setIcon(R.drawable.ic_check_box_outline);
                showHideButton(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showHideButton(boolean value) {
        btnHide.setVisibility(value ? 0 : 8);
    }

    public void setSelectAll(boolean selectAll) {
        if (itemSelectAll == null) {
            return;
        }
        if (selectAll) {
            itemSelectAll.setIcon(R.drawable.ic_check_filled);
        } else {
            itemSelectAll.setIcon(R.drawable.ic_check_box_outline);
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
                Toast.makeText(this, "Please select at least one image!", 0).show();
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
                        File src = new File((String) selectedFiles.get(count));
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
        progressbar = (ProgressBar) dialog.findViewById(R.id.progress_bar);
        txtCount = (TextView) dialog.findViewById(R.id.txt_count);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.txt_title);
        nativeAdContainer = (LinearLayout) dialog.findViewById(R.id.native_ad_container);
        txtTitle.setText("Moving Video(s)");
        txtCount.setText("Moving 1 of " + files.size());
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
                        runOnUiThread(new C06512());
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
}
