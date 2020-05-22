package com.safe.gallery.calculator.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.safe.gallery.calculator.MyBassActivity;
import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.adapters.IntruderAdapter;
import com.safe.gallery.calculator.utils.AppConstants;
import com.safe.gallery.calculator.utils.share.Share;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class IntruderActivity extends MyBassActivity {

    private Dialog dialog;
    private FloatingActionButton fabDelete;
    private ImageView mIvEmpty;
    private TextView mTvEmpty;


    public static ArrayList<File> al_my_photos = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private IntruderAdapter intruderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intruder);

        findViews();
        initViews();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.intruder));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);


        if (new File(AppConstants.INTRUDER_PATH).listFiles() != null) {
            if (new File(AppConstants.INTRUDER_PATH).listFiles().length == 0) {
                noOneIntruded();


            } else {
                fabDelete.show();
                mIvEmpty.setVisibility(View.GONE);
                mTvEmpty.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        } else
            noOneIntruded();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void noOneIntruded() {
        //No file is there or no one intruder
        fabDelete.hide();
        mIvEmpty.setVisibility(View.VISIBLE);
        mTvEmpty.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    private static final String TAG = "IntruderActivity";
    private View.OnClickListener deleteClickListener = v -> {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(IntruderActivity.this);
        alertDialog.setTitle(getString(R.string.are_you_sure));
        alertDialog.setMessage(getString(R.string.ask_delete_all_intruder));
        alertDialog.setPositiveButton(android.R.string.yes, (dialog, which) -> deleteIntruders());
        alertDialog.setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss());
        alertDialog.show();

    };

    private void deleteIntruders() {
        File rootFile = new File(AppConstants.INTRUDER_PATH);
        Dialog progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.dialog_progress);
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            progressDialog.getWindow().setLayout(-1, -2);
        }
        ProgressBar progressBar = progressDialog.findViewById(R.id.progress_bar);
        TextView count = progressDialog.findViewById(R.id.txt_count);
        TextView title = progressDialog.findViewById(R.id.txt_title);
        title.setText(getString(R.string.deleting));
        int intruders = rootFile.listFiles().length;
        Log.i(TAG, "deleteIntruders: number of intruders: " + intruders);

        count.setText(String.valueOf(intruders));
        progressBar.setMax(intruders);
        progressDialog.show();

        int progress = 1;
        boolean isError = false;
        for (File child : rootFile.listFiles()) {
            Log.i(TAG, "onClick: deleting child: " + child.getName());
            if (child.delete())
                progressBar.setProgress(progress++);
            else
                isError = true;
        }

        if (!isError) {
            //All intruders photo deleted
            progressDialog.dismiss();
            Share.al_my_photos_photo.clear();
            intruderAdapter = new IntruderAdapter(this, Share.al_my_photos_photo, null);
            mRecyclerView.setAdapter(intruderAdapter);
            noOneIntruded();
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(getString(R.string.sorry));
            alert.setMessage(getString(R.string.something_went_wrong));
            alert.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                dialog.dismiss();
                initViews();
            });
            alert.show();
        }


    }

    private void findViews() {

        fabDelete = findViewById(R.id.intruder_fab_delete);
        fabDelete.setOnClickListener(deleteClickListener);

        mTvEmpty = findViewById(R.id.intruder_tv_empty);
        mIvEmpty = findViewById(R.id.intruder_iv_empty);

        mRecyclerView = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        mRecyclerView.setLayoutManager(recyclerViewLayoutManager);

    }

    private void initViews() {

        al_my_photos.clear();
        Share.al_my_photos_photo.clear();
        File path = new File(AppConstants.INTRUDER_PATH);

        if (path.exists()) {

            File[] allFiles = path.listFiles((dir, name) -> (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")));

            if (allFiles.length > 0) {

                for (int i = 0; i < allFiles.length; i++) {

                    al_my_photos.add(allFiles[i]);

                }
                Collections.sort(al_my_photos, Collections.reverseOrder());
                Share.al_my_photos_photo.addAll(al_my_photos);
                Collections.reverse(Share.al_my_photos_photo);

                intruderAdapter = new IntruderAdapter(getApplicationContext(), Share.al_my_photos_photo, (view, position) -> adapterItemClickListener(position));

                mRecyclerView.setAdapter(intruderAdapter);

            }
        }

    }

    private void adapterItemClickListener(int position) {
        try {

            dialog = new Dialog(IntruderActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dlg_exit1);
            int widthInPixel = Resources.getSystem().getDisplayMetrics().widthPixels;
            dialog.getWindow().setLayout(widthInPixel- 50, Toolbar.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            ImageView img, close;

            img = dialog.findViewById(R.id.img);
            close = dialog.findViewById(R.id.close);
            img.setImageURI(Uri.fromFile(Share.al_my_photos_photo.get(position)));

            close.setOnClickListener(v -> dialog.dismiss());

        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    protected void onDestroy() {

        if (dialog != null) {

            dialog.cancel();

        }
        super.onDestroy();
    }


}
