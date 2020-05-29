package com.androidbull.calculator.photo.vault.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.androidbull.calculator.photo.R;
import com.androidbull.calculator.photo.vault.activities.images.ImagesActivity;
import com.androidbull.calculator.photo.vault.adapters.FullScreenImageAdapter;
import com.androidbull.calculator.photo.vault.model.AllImagesModel;
import com.androidbull.calculator.photo.vault.utils.CustomViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullScreenImageActivity extends BaseActivity {


    public static final String OBJECT = "object";
    public static final String POSITION = "position";
    int High;
    int Low = 1;
    FullScreenImageAdapter adapter;
    ArrayList<AllImagesModel> imageList;
    @BindView(R.id.main_linear)
    RelativeLayout mainLinear;
    private int position;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewPager)
    CustomViewPager viewPager;

    class C05851 implements OnPageChangeListener {
        C05851() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
            if (FullScreenImageActivity.this.imageList != null && FullScreenImageActivity.this.imageList.size() > 0) {
                FullScreenImageActivity.this.getSupportActionBar().setTitle(new File(FullScreenImageActivity.this.imageList.get(position).getImagePath()).getName());
                if (FullScreenImageActivity.this.High != 0) {
                    int result = new Random().nextInt(FullScreenImageActivity.this.High - FullScreenImageActivity.this.Low) + FullScreenImageActivity.this.Low;
                    Log.e("random number", "" + result);
                    if (result % 9 == 0 || result % 11 == 0) {
                        //FullScreenImageActivity.this.showInterstitial();
                    }
                }
            }
        }

        public void onPageScrollStateChanged(int state) {
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        ButterKnife.bind(this);
        //AddMobe();
        if (getIntent().getExtras() != null) {
            this.imageList = getIntent().getParcelableArrayListExtra(OBJECT);
            this.position = getIntent().getIntExtra(POSITION, 0);
            this.adapter = new FullScreenImageAdapter(this);
            this.viewPager.setAdapter(this.adapter);
            if (this.imageList != null) {
                this.High = this.imageList.size();
                this.adapter.addItems(this.imageList);
                this.viewPager.setCurrentItem(this.position);
                setHeaderInfo();
            }
        }
        this.viewPager.addOnPageChangeListener(new C05851());
    }

    private void setHeaderInfo() {
        setSupportActionBar(this.toolbar);
        if (this.imageList != null) {
            getSupportActionBar().setTitle(new File(this.imageList.get(this.viewPager.getCurrentItem()).getImagePath()).getName());
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();
    }

    public void showHideUI() {
        if (getSupportActionBar().isShowing()) {
            this.toolbar.animate().translationY((float) (-this.toolbar.getHeight())).setInterpolator(new AccelerateInterpolator(2.0f)).start();
            getSupportActionBar().hide();
            return;
        }
        this.toolbar.animate().translationY(0.0f).setInterpolator(new DecelerateInterpolator(2.0f)).start();
        getSupportActionBar().show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.full_screen_image_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.itm_share) {
            if (this.imageList != null && this.imageList.size() > 0) {

                AllImagesModel image = this.imageList.get(this.viewPager.getCurrentItem());
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(image.getImagePath())));
                sendIntent.setType("image/*");
                try {
                    startActivity(sendIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this,getString(R.string.no_app_found), Toast.LENGTH_SHORT).show();
                }
            }
        } else if (item.getItemId() == R.id.itm_delete && this.imageList != null && this.imageList.size() > 0) {
            final AlertDialog alertDialog = new Builder(this).create();
            alertDialog.setTitle(getString(R.string.alert));
            alertDialog.setMessage(getString(R.string.delete_file_desc));
            alertDialog.setCancelable(false);
            alertDialog.setButton(-1, getString(R.string.yes), (dialog, which) -> {
                alertDialog.dismiss();
                FullScreenImageActivity.this.adapter.removeItem(FullScreenImageActivity.this.imageList.get(FullScreenImageActivity.this.viewPager.getCurrentItem()));
                if(FullScreenImageActivity.this.adapter.getCount()==0){
                    startActivity(new Intent(FullScreenImageActivity.this, ImagesActivity.class));
                    finish();
                }

            });
            alertDialog.setButton(-2, getString(R.string.no), (dialog, which) -> alertDialog.dismiss());
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        startActivity(new Intent(this, ImagesActivity.class));
        finish();
        super.onBackPressed();
    }



}
