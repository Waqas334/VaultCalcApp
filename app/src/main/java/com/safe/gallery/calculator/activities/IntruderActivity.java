package com.safe.gallery.calculator.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.app.AppConstants;
import com.safe.gallery.calculator.share.Share;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;

public class IntruderActivity extends AppCompatActivity {

    Dialog dialog;
    private FloatingActionButton fabDelete;
    private ImageView mIvEmpty;
    private TextView mTvEmpty;


    public static ArrayList<File> al_my_photos = new ArrayList<>();
    private File[] allFiles;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intruder);

        findViews();
        initViews();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Intruder");
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

    private void noOneIntruded() {
        //No file is there or no one intruder
        fabDelete.hide();
        mIvEmpty.setVisibility(View.VISIBLE);
        mTvEmpty.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    private static final String TAG = "IntruderActivity";
    private View.OnClickListener deleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(IntruderActivity.this);
            alertDialog.setTitle("Are you sure?");
            alertDialog.setMessage("Do you want to delete all intruder photos?");
            alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteIntruders();
                }
            });
            alertDialog.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();

        }
    };

    private void deleteIntruders() {
        File rootFile = new File(AppConstants.INTRUDER_PATH);
        for (File child : rootFile.listFiles()) {
            Log.i(TAG, "onClick: deleting child: " + child.getName());
            child.delete();
        }
    }

    private void findViews() {

        fabDelete = findViewById(R.id.intruder_fab_delete);
        fabDelete.setOnClickListener(deleteClickListener);

        mTvEmpty = findViewById(R.id.intruder_tv_empty);
        mIvEmpty = findViewById(R.id.intruder_iv_empty);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerViewLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        mRecyclerView.setLayoutManager(recyclerViewLayoutManager);

    }

    private void initViews() {

        al_my_photos.clear();
        Share.al_my_photos_photo.clear();
        File path = new File(AppConstants.INTRUDER_PATH);

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

                            dialog = new Dialog(IntruderActivity.this);
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

                mRecyclerView.setAdapter(obj_adapter);

            } else {


            }
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
