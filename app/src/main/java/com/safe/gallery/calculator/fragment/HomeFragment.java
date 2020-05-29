package com.safe.gallery.calculator.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.cardview.widget.CardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.activities.IntruderActivity;
import com.safe.gallery.calculator.utils.AppConstants;
import com.safe.gallery.calculator.activities.audio.AudiosActivity;
import com.safe.gallery.calculator.db.DBHelper;
import com.safe.gallery.calculator.activities.files.FilesActivity;
import com.safe.gallery.calculator.activities.images.ImagesActivity;
import com.safe.gallery.calculator.activities.video.VideoActivity;
import com.safe.gallery.calculator.utils.Utils;

import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    private int PERMISSION_REQUEST_CODE = 100;
    private final String INCOGNITO_BROWSER_PACKAGE_NAME = "com.androidbull.incognito.browser";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_laoyut, container, false);
        ButterKnife.bind(this, view);
        findViews(view);
        return view;
    }

    private void findViews(View v) {

        CardView image = v.findViewById(R.id.card_picture);
        CardView audios = v.findViewById(R.id.card_audio);
        CardView videos = v.findViewById(R.id.card_video);
        CardView files = v.findViewById(R.id.card_files);

        CardView intruder = v.findViewById(R.id.card_intruder);
        CardView browser = v.findViewById(R.id.card_browser);

        intruder.setOnClickListener(v16 -> startActivity(new Intent(getActivity(), IntruderActivity.class)));


        browser.setOnClickListener(v15 -> {
            showIncognitoBrowserDialog();
        });

        image.setOnClickListener(v14 -> startActivityForResult(new Intent(getActivity(), ImagesActivity.class), AppConstants.REFRESH_LIST));

        audios.setOnClickListener(v13 -> startActivityForResult(new Intent(getActivity(), AudiosActivity.class), AppConstants.REFRESH_LIST));
        videos.setOnClickListener(v12 -> HomeFragment.this.startActivityForResult(new Intent(HomeFragment.this.getActivity(), VideoActivity.class), AppConstants.REFRESH_LIST));
        files.setOnClickListener(v1 -> startActivityForResult(new Intent(getActivity(), FilesActivity.class), AppConstants.REFRESH_LIST));

    }

    private void showIncognitoBrowserDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_templete);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setLayout(-1, -2);
        }

        ((ImageView) dialog.findViewById(R.id.dialog_iv_header)).setImageResource(R.drawable.incognito_with_round_background);
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getString(R.string.incognito_browser_dialog_title));
        ((TextView) dialog.findViewById(R.id.dialog_tv_message)).setText(getString(R.string.incognito_browser_download_desc));
        TextView btnCancel = dialog.findViewById(R.id.btn_cancel);
        TextView btnOk = dialog.findViewById(R.id.btn_ok);
        btnOk.setText(getString(R.string.download_now));
        btnCancel.setText(getString(R.string.later));
        dialog.findViewById(R.id.img_close).setOnClickListener(view -> dialog.dismiss());
        btnCancel.setOnClickListener(view -> dialog.dismiss());
        btnOk.setOnClickListener(view -> {
            dialog.dismiss();
            Utils.gotoPlayStore(INCOGNITO_BROWSER_PACKAGE_NAME, getContext());
        });
        dialog.show();
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getActivity().getSystemService("device_policy");
        if (VERSION.SDK_INT < 23) {
            return;
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != 0
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != 0) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != PERMISSION_REQUEST_CODE) {
            return;
        }
        if (grantResults.length <= 0 || grantResults[0] != 0 || grantResults[1] != 0) {
            final AlertDialog alertDialog = new Builder(getActivity()).create();
            alertDialog.setTitle(getString(R.string.grant_permission));
            alertDialog.setCancelable(false);
            alertDialog.setMessage(getString(R.string.grant_permission_desc));
            alertDialog.setButton(-1, getString(R.string.dismiss), (dialogInterface, i) -> {
                alertDialog.dismiss();
                HomeFragment.this.getActivity().finish();
            });
            alertDialog.show();
        }
    }


}
