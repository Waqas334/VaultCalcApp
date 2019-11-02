package com.safe.gallery.calculator.image;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.safe.gallery.calculator.Constant;
import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.app.AppConstants;
import com.safe.gallery.calculator.app.BaseActivity;
import com.safe.gallery.calculator.app.MainApplication;
import com.safe.gallery.calculator.callbacks.OnImagesLoadedListener;
import com.safe.gallery.calculator.db.DBHelper;
import com.safe.gallery.calculator.fullscreenimage.FullScreenImageActivity;
import com.safe.gallery.calculator.image.adapter.ImagesAdapter;
import com.safe.gallery.calculator.image.add.AddImageActivity;
import com.safe.gallery.calculator.model.AllImagesModel;
import com.safe.gallery.calculator.utils.CenterTitleToolbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImagesActivity extends BaseActivity implements OnImagesLoadedListener {

    String TAG = "TAG";
    private LinearLayout adView;
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
    CenterTitleToolbar toolbar;
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
        addBanner();
    }

    public void addBanner() {

        Random rand = new Random();
        int randomNum = 0 + rand.nextInt(5);

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


                // adContainer.setVisibility(View.GONE);
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


    class C05982 implements Runnable {
        C05982() {
        }

        public void run() {
            ImagesActivity.this.progressbar.setProgress(ImagesActivity.this.progress);
        }
    }


    private void setHeaderInfo() {
       // this.toolbar.setNavigationIcon((int) R.drawable.ic_arrow);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setTitle(getString(R.string.image));


        if(getSupportActionBar()!=null){
            Drawable drawable= getResources().getDrawable(R.drawable.ic_arrow);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 70, 70, true));
            newdrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(newdrawable);

        }
    }

    private void Init() {
        // LoadBannerAd();
        this.recyclerview.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private void setAdapter() {
        MainApplication.getInstance().LogFirebaseEvent("3", "AddImage");
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
                return;
            case R.id.fab_add:


                Random rand = new Random();
                int randomNum = 0 + rand.nextInt(5);

                Log.e("TAG", "onClick: " + randomNum);

                if (randomNum == 0) {

                    if (!MainApplication.getInstance().requestNewInterstitial()) {

                        startActivityForResult(new Intent(this, AddImageActivity.class), 1012);
                        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                        return;

                    } else {

                        MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();

                                MainApplication.getInstance().mInterstitialAd.setAdListener(null);
                                MainApplication.getInstance().mInterstitialAd = null;
                                MainApplication.getInstance().ins_adRequest = null;
                                MainApplication.getInstance().LoadAds();

                                startActivityForResult(new Intent(ImagesActivity.this, AddImageActivity.class), 1012);
                                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                                return;
                            }

                            @Override
                            public void onAdFailedToLoad(int i) {
                                super.onAdFailedToLoad(i);
                            }

                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                            }
                        });
                    }
                } else {

                    startActivityForResult(new Intent(this, AddImageActivity.class), 1012);
                    overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                    return;
                }

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
                            ImagesActivity.this.menuItemSelect.setIcon(R.drawable.ic_check_box_outline);
                        }
                        if (ImagesActivity.this.menuItemDelete != null) {
                            ImagesActivity.this.menuItemDelete.setVisible(false);
                        }
                        ImagesActivity.this.isEditable = false;
                        if(getSupportActionBar()!=null){
                            Drawable drawable= getResources().getDrawable(R.drawable.ic_arrow);
                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                            Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 70, 70, true));
                            newdrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            getSupportActionBar().setHomeAsUpIndicator(newdrawable);

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
            case 16908332:
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
                if(getSupportActionBar()!=null){
                    Drawable drawable= getResources().getDrawable(R.drawable.ic_close);
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 70, 70, true));
                    newdrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeAsUpIndicator(newdrawable);

                }
               // this.toolbar.setNavigationIcon((int) R.drawable.ic_close);
                break;
            case R.id.itm_select:
                if (this.menuItemSelect != null) {
                    if (!this.isSelectAll) {
                        this.menuItemSelect.setIcon(R.drawable.ic_check_filled);
                        if (this.adapter != null) {
                            this.adapter.selectAllItem();
                        }
                        showDeleteButton(true);
                        this.isSelectAll = true;
                        break;
                    }
                    this.menuItemSelect.setIcon(R.drawable.ic_check_box_outline);
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
            this.menuItemSelect.setIcon(needToShow ? R.drawable.ic_check_filled : R.drawable.ic_check_box_outline);
            this.isSelectAll = needToShow;
        }
    }

    public void startFullScreenImageActivity(final ArrayList<AllImagesModel> buckets, final int position) {

        Random rand = new Random();
        int randomNum = 0 + rand.nextInt(5);

        Log.e(TAG, "rand:--> " + randomNum);

        if (randomNum == 0) {

            if (!MainApplication.getInstance().requestNewInterstitial()) {

                startActivity(new Intent(this, FullScreenImageActivity.class).putExtra(FullScreenImageActivity.OBJECT, buckets).putExtra(FullScreenImageActivity.POSITION, position));

            } else {

                MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();

                        MainApplication.getInstance().mInterstitialAd.setAdListener(null);
                        MainApplication.getInstance().mInterstitialAd = null;
                        MainApplication.getInstance().ins_adRequest = null;
                        MainApplication.getInstance().LoadAds();


                        startActivity(new Intent(ImagesActivity.this, FullScreenImageActivity.class).putExtra(FullScreenImageActivity.OBJECT, buckets).putExtra(FullScreenImageActivity.POSITION, position));

                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                    }
                });
            }
        } else {


            startActivity(new Intent(this, FullScreenImageActivity.class).putExtra(FullScreenImageActivity.OBJECT, buckets).putExtra(FullScreenImageActivity.POSITION, position));
        }


    }

    public void onBackPressed() {
        if (this.isEditable) {
            if (this.menuItemEdit != null) {
                this.menuItemEdit.setVisible(true);
            }
            if (this.menuItemSelect != null) {
                this.menuItemSelect.setVisible(false);
                this.menuItemSelect.setIcon(R.drawable.ic_check_box_outline);
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
            if(getSupportActionBar()!=null){
                Drawable drawable= getResources().getDrawable(R.drawable.ic_arrow);
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                Drawable newdrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 70, 70, true));
                newdrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(newdrawable);

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
