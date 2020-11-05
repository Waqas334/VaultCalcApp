package com.androidbull.calculator.photo.vault.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidbull.calculator.photo.R;
import com.androidbull.calculator.photo.vault.activities.BrowserFilesActivity;
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

import static com.androidbull.calculator.photo.vault.MainApplication.theme_boolean;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private final int PERMISSION_REQUEST_CODE = 100;
    private AdView adView;
    private AdListener adListener;
    private final String INCOGNITO_BROWSER_PACKAGE_NAME = "com.androidbull.incognito.browser";
    private LinearLayout mLlAd;

    private CardView mcvPhotos, mcvAudios, mcvVideos, mcvFiles, mcvIntruder, mcvBrowser;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi(view);
        initActions();
    }


    private void initUi(View view) {

        mcvPhotos = view.findViewById(R.id.cvPhotos);
        mcvAudios = view.findViewById(R.id.cvAudios);
        mcvVideos = view.findViewById(R.id.cvVideos);
        mcvFiles = view.findViewById(R.id.cvFiles);
        mcvIntruder = view.findViewById(R.id.cvIntruder);
        mcvBrowser = view.findViewById(R.id.cvBrowser);

        initAds();

    }


    private void initActions() {

        mcvPhotos.setOnClickListener(v -> startActivityForResult(new Intent(requireContext(), ImagesActivity.class), AppConstants.REFRESH_LIST));
        mcvAudios.setOnClickListener(v -> startActivityForResult(new Intent(requireContext(), AudiosActivity.class), AppConstants.REFRESH_LIST));
        mcvVideos.setOnClickListener(v -> startActivityForResult(new Intent(requireContext(), VideoActivity.class), AppConstants.REFRESH_LIST));
        mcvFiles.setOnClickListener(v -> startActivityForResult(new Intent(requireContext(), FilesActivity.class), AppConstants.REFRESH_LIST));
        mcvIntruder.setOnClickListener(v -> startActivity(new Intent(requireContext(), IntruderActivity.class)));
        mcvBrowser.setOnClickListener(v -> startActivity(new Intent(requireContext(), BrowserFilesActivity.class)));
    }

    private void initAds() {
        adView = new AdView(requireContext(), requireContext().getString(R.string.home_banner_ad_id), AdSize.BANNER_HEIGHT_50);
        mLlAd = requireView().findViewById(R.id.home_banner_container);
        mLlAd.addView(adView);
        AdView.AdViewLoadConfig loadAdConfig = adView.buildLoadAdConfig()
                .withAdListener(adListener)
                .build();

        adView.loadAd(loadAdConfig);
        adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                Log.e(TAG, "onError: Ad WError: " + adError.getErrorMessage());
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
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences mPrefs = getContext().getSharedPreferences("THEME", 0);
        theme_boolean = mPrefs.getBoolean("theme_boolean", true);
        if (theme_boolean) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }


    private void showIncognitoBrowserDialog() {
        final Dialog dialog = new Dialog(requireContext());
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
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != 0
                || ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != 0) {
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
            final AlertDialog alertDialog = new Builder(requireActivity()).create();
            alertDialog.setTitle(getString(R.string.grant_permission));
            alertDialog.setCancelable(false);
            alertDialog.setMessage(getString(R.string.grant_permission_desc));
            alertDialog.setButton(-1, getString(R.string.dismiss), (dialogInterface, i) -> {
                alertDialog.dismiss();
                HomeFragment.this.requireActivity().finish();
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
