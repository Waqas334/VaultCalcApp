package com.safe.gallery.calculator.fullscreenimage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.app.BaseActivity;
import com.safe.gallery.calculator.fullscreenimage.adapter.FullScreenImageAdapter;
import com.safe.gallery.calculator.image.ImagesActivity;
import com.safe.gallery.calculator.model.AllImagesModel;
import com.safe.gallery.calculator.utils.CustomViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullScreenImageActivity extends BaseActivity {


    private static final String FILE_RENAMED = "file_renamed";
    public static final String OBJECT = "object";
    public static final String POSITION = "position";
    int High;
    int Low = 1;
    FullScreenImageAdapter adapter;
    ArrayList<AllImagesModel> imageList;
    private boolean isFileDeleted;
    private boolean isFileRenamed;
    private boolean isImageSavedAfterEditing;
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
                FullScreenImageActivity.this.getSupportActionBar().setTitle(new File(((AllImagesModel) FullScreenImageActivity.this.imageList.get(position)).getImagePath()).getName());
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
        setContentView((int) R.layout.activity_full_screen_image);
        ButterKnife.bind((Activity) this);
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
            getSupportActionBar().setTitle(new File(((AllImagesModel) this.imageList.get(this.viewPager.getCurrentItem())).getImagePath()).getName());
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

    @SuppressLint("WrongConstant")
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            onBackPressed();
        } else if (item.getItemId() == R.id.itm_share) {
            if (this.imageList != null && this.imageList.size() > 0) {

                AllImagesModel image = (AllImagesModel) this.imageList.get(this.viewPager.getCurrentItem());
                Intent sendIntent = new Intent();
                sendIntent.setAction("android.intent.action.SEND");
                sendIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(image.getImagePath())));
                sendIntent.setType("image/*");
                try {
                    startActivity(sendIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "No Application found to perform this action.", 0).show();
                }
            }
        } else if (item.getItemId() == R.id.itm_delete && this.imageList != null && this.imageList.size() > 0) {
            final AlertDialog alertDialog = new Builder(this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Are you sure to delete this files?");
            alertDialog.setCancelable(false);
            alertDialog.setButton(-1, (CharSequence) "Yes", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                    FullScreenImageActivity.this.adapter.removeItem((AllImagesModel) FullScreenImageActivity.this.imageList.get(FullScreenImageActivity.this.viewPager.getCurrentItem()));
                    FullScreenImageActivity.this.isFileDeleted = true;
                    if(FullScreenImageActivity.this.adapter.getCount()==0){
                        startActivity(new Intent(FullScreenImageActivity.this, ImagesActivity.class));
                        finish();
                    }

                }
            });
            alertDialog.setButton(-2, (CharSequence) "No", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
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
