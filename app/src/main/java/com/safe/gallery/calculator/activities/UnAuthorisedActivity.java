package com.safe.gallery.calculator.activities;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.safe.gallery.calculator.Constant;
import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.share.Share;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;

import static android.os.Environment.DIRECTORY_PICTURES;

public class UnAuthorisedActivity extends AppCompatActivity {

    Dialog dialog;
    ImageView ic_back;

    public static ArrayList<File> al_my_photos = new ArrayList<>();
    private File[] allFiles;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_authorised);

        addBanner();
        findViews();
        initViews();

    }

    private void findViews() {

        ic_back = findViewById(R.id.ic_back);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerViewLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private void initViews() {

        al_my_photos.clear();
        Share.al_my_photos_photo.clear();
        File path = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES) + "/" + ".Calculator Vault" + "/");

        if (path.exists()) {

            allFiles = path.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"));
                }
            });

            if (allFiles.length > 0) {

                for (int i = 0; i < allFiles.length; i++) {

                    al_my_photos.add(allFiles[i]);

                }
                Collections.sort(al_my_photos, Collections.reverseOrder());
                Share.al_my_photos_photo.addAll(al_my_photos);
                Collections.reverse(Share.al_my_photos_photo);

                Adapter_ImageFolder obj_adapter = new Adapter_ImageFolder(getApplicationContext(), Share.al_my_photos_photo, new Adapter_ImageFolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        try {

                            dialog = new Dialog(UnAuthorisedActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dlg_exit1);
                            dialog.getWindow().setLayout((int) (DisplayMetricsHandler.getScreenWidth() - 50), Toolbar.LayoutParams.WRAP_CONTENT);
                            dialog.setCancelable(true);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();

                            ImageView img, close;

                            img = dialog.findViewById(R.id.img);
                            close = dialog.findViewById(R.id.close);
                            img.setImageURI(Uri.fromFile(Share.al_my_photos_photo.get(position)));

                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();

                                }
                            });

                        } catch (Exception e) {
                            e.getMessage();
                        }

                    }
                });

                recyclerView.setAdapter(obj_adapter);

            } else {


            }
        }

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {

        if (dialog != null) {

            dialog.cancel();

        }
        super.onDestroy();
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


                adContainer.setVisibility(View.GONE);
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

}
