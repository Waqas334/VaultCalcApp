package com.safe.gallery.calculator.album;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.safe.gallery.calculator.Constant;
import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.app.AppConstants;
import com.safe.gallery.calculator.app.MainApplication;
import com.safe.gallery.calculator.audios.AudiosActivity;
import com.safe.gallery.calculator.db.DBHelper;
import com.safe.gallery.calculator.files.FilesActivity;
import com.safe.gallery.calculator.image.ImagesActivity;
import com.safe.gallery.calculator.utils.PolicyManager;
import com.safe.gallery.calculator.video.VideoActivity;

import java.util.Random;

import butterknife.ButterKnife;

public class AlbumsFragment extends Fragment {

    DBHelper dbHelper;
    private String type;

    RelativeLayout image, audios, videos, files;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_laoyut, container, false);
        ButterKnife.bind((Object) this, view);
        addBanner(view);
        findViews(view);
        return view;
    }

    private void findViews(View v) {

        image = v.findViewById(R.id.image);
        audios = v.findViewById(R.id.audios);
        videos = v.findViewById(R.id.videos);
        files = v.findViewById(R.id.files);

        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Random rand = new Random();
                int randomNum = 0 + rand.nextInt(5);
                if (randomNum == 0) {

                    if (!MainApplication.getInstance().requestNewInterstitial()) {

                        startActivityForResult(new Intent(getActivity(), ImagesActivity.class), AppConstants.REFRESH_LIST);

                    } else {

                        MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();

                                MainApplication.getInstance().mInterstitialAd.setAdListener(null);
                                MainApplication.getInstance().mInterstitialAd = null;
                                MainApplication.getInstance().ins_adRequest = null;
                                MainApplication.getInstance().LoadAds();

                                startActivityForResult(new Intent(getActivity(), ImagesActivity.class), AppConstants.REFRESH_LIST);

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


                    startActivityForResult(new Intent(getActivity(), ImagesActivity.class), AppConstants.REFRESH_LIST);

                }

            }
        });

        audios.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Random rand = new Random();
                int randomNum = 0 + rand.nextInt(5);
                if (randomNum == 0) {

                    if (!MainApplication.getInstance().requestNewInterstitial()) {

                        startActivityForResult(new Intent(getActivity(), AudiosActivity.class), AppConstants.REFRESH_LIST);

                    } else {

                        MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();

                                MainApplication.getInstance().mInterstitialAd.setAdListener(null);
                                MainApplication.getInstance().mInterstitialAd = null;
                                MainApplication.getInstance().ins_adRequest = null;
                                MainApplication.getInstance().LoadAds();

                                startActivityForResult(new Intent(getActivity(), AudiosActivity.class), AppConstants.REFRESH_LIST);

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


                    startActivityForResult(new Intent(getActivity(), AudiosActivity.class), AppConstants.REFRESH_LIST);

                }

            }
        });
        videos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Random rand = new Random();
                int randomNum = 0 + rand.nextInt(5);
                if (randomNum == 0) {

                    if (!MainApplication.getInstance().requestNewInterstitial()) {

                        AlbumsFragment.this.startActivityForResult(new Intent(AlbumsFragment.this.getActivity(), VideoActivity.class), AppConstants.REFRESH_LIST);

                    } else {

                        MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();

                                MainApplication.getInstance().mInterstitialAd.setAdListener(null);
                                MainApplication.getInstance().mInterstitialAd = null;
                                MainApplication.getInstance().ins_adRequest = null;
                                MainApplication.getInstance().LoadAds();

                                AlbumsFragment.this.startActivityForResult(new Intent(AlbumsFragment.this.getActivity(), VideoActivity.class), AppConstants.REFRESH_LIST);

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


                    AlbumsFragment.this.startActivityForResult(new Intent(AlbumsFragment.this.getActivity(), VideoActivity.class), AppConstants.REFRESH_LIST);

                }


            }
        });
        files.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Random rand = new Random();
                int randomNum = 0 + rand.nextInt(5);
                if (randomNum == 0) {

                    if (!MainApplication.getInstance().requestNewInterstitial()) {

                        startActivityForResult(new Intent(getActivity(), FilesActivity.class), AppConstants.REFRESH_LIST);

                    } else {

                        MainApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();

                                MainApplication.getInstance().mInterstitialAd.setAdListener(null);
                                MainApplication.getInstance().mInterstitialAd = null;
                                MainApplication.getInstance().ins_adRequest = null;
                                MainApplication.getInstance().LoadAds();

                                startActivityForResult(new Intent(getActivity(), FilesActivity.class), AppConstants.REFRESH_LIST);

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


                    startActivityForResult(new Intent(getActivity(), FilesActivity.class), AppConstants.REFRESH_LIST);

                }


            }
        });

    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getActivity().getSystemService("device_policy");
        ComponentName demoDeviceAdmin = new ComponentName(getActivity(), PolicyManager.class);
        if (devicePolicyManager == null || !devicePolicyManager.isAdminActive(demoDeviceAdmin)) {
            MainApplication.getInstance().LogFirebaseEvent("1", "Home");

            AddMobe();
            this.dbHelper = new DBHelper(getActivity());

        } else {
            MainApplication.getInstance().LogFirebaseEvent("1", "Home");

            AddMobe();
            this.dbHelper = new DBHelper(getActivity());

        }
        if (VERSION.SDK_INT < 23) {
            return;
        }
        if (ContextCompat.checkSelfPermission(getActivity(), "android.permission.READ_EXTERNAL_STORAGE") != 0 || ContextCompat.checkSelfPermission(getActivity(), "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 100);
        }
    }


    private void enableAdmin() {

        ComponentName demoDeviceAdmin = new ComponentName(getActivity(), PolicyManager.class);
        Log.e("DeviceAdminActive==", "" + demoDeviceAdmin);
        Intent intent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
        intent.putExtra("android.app.extra.DEVICE_ADMIN", demoDeviceAdmin);
        intent.putExtra("android.app.extra.ADD_EXPLANATION", "Disable app");
        intent.putExtra("android.app.extra.ADD_EXPLANATION", "After activating admin, you will be able to block application uninstallation.");
        startActivityForResult(intent, PolicyManager.ACTIVATION_REQUEST);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != 100) {
            return;
        }
        if (grantResults.length <= 0 || grantResults[0] != 0 || grantResults[1] != 0) {
            final AlertDialog alertDialog = new Builder(getActivity()).create();
            alertDialog.setTitle("Grant Permission");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Please grant all permissions to access additional functionality.");
            alertDialog.setButton(-1, (CharSequence) "DISMISS", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.dismiss();
                    AlbumsFragment.this.getActivity().finish();
                }
            });
            alertDialog.show();
        }
    }




    private void AddMobe() {

        if (AlbumsFragment.this.type == null) {
            return;
        }
        if (AlbumsFragment.this.type.equals(AppConstants.IMAGE)) {
            AlbumsFragment.this.startActivityForResult(new Intent(AlbumsFragment.this.getActivity(), ImagesActivity.class), AppConstants.REFRESH_LIST);
        } else {
            AlbumsFragment.this.startActivityForResult(new Intent(AlbumsFragment.this.getActivity(), VideoActivity.class), AppConstants.REFRESH_LIST);
        }
    }


    public void addBanner(View view) {

        final AdView mAdView = new AdView(getActivity());
        mAdView.setAdSize(AdSize.BANNER);
        final View adContainer = view.findViewById(R.id.layoutViewAdd);


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
