package com.androidbull.calculator.photo.vault.fragment;

import android.Manifest;
import android.app.Dialog;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidbull.calculator.photo.R;
import com.androidbull.calculator.photo.vault.activities.IntruderActivity;
import com.androidbull.calculator.photo.vault.utils.AppConstants;
import com.androidbull.calculator.photo.vault.activities.audio.AudiosActivity;
import com.androidbull.calculator.photo.vault.activities.files.FilesActivity;
import com.androidbull.calculator.photo.vault.activities.images.ImagesActivity;
import com.androidbull.calculator.photo.vault.activities.video.VideoActivity;
import com.androidbull.calculator.photo.vault.utils.Utils;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;

import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    private int PERMISSION_REQUEST_CODE = 100;
    private final String INCOGNITO_BROWSER_PACKAGE_NAME = "com.androidbull.incognito.browser";
    private AdView adView;
    private LinearLayout mLlAd;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_laoyut, container, false);
        ButterKnife.bind(this, view);
        findViews(view);
        return view;
    }
    AdListener adListener;
    private static final String TAG = "HomeFragment";
    private void findViews(View v) {

        adView =  new AdView(getContext(),getContext().getString(R.string.home_banner_ad_id), AdSize.BANNER_HEIGHT_50);
        mLlAd = v.findViewById(R.id.home_banner_container);
        mLlAd.addView(adView);
        AdView.AdViewLoadConfig loadAdConfig = adView.buildLoadAdConfig()
                .withAdListener(adListener)
                .build();

        adView.loadAd(loadAdConfig);
        adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e(TAG, "onError: Ad WError: " + adError.getErrorMessage() );
            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.i(TAG, "onAdLoaded: ");
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.i(TAG, "onAdClicked: ");

            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.i(TAG, "onLoggingImpression: ");
            }
        };

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


    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

}
