package com.safe.gallery.calculator.album;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.activities.IntruderActivity;
import com.safe.gallery.calculator.app.AppConstants;
import com.safe.gallery.calculator.app.MainApplication;
import com.safe.gallery.calculator.audios.AudiosActivity;
import com.safe.gallery.calculator.db.DBHelper;
import com.safe.gallery.calculator.files.FilesActivity;
import com.safe.gallery.calculator.image.ImagesActivity;
import com.safe.gallery.calculator.utils.PolicyManager;
import com.safe.gallery.calculator.video.VideoActivity;

import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    DBHelper dbHelper;
    private String type;

    CardView image, audios, videos, files, intruder, browser;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_laoyut, container, false);
        ButterKnife.bind((Object) this, view);
        findViews(view);
        return view;
    }

    private void findViews(View v) {

        image = v.findViewById(R.id.card_picture);
        audios = v.findViewById(R.id.card_audio);
        videos = v.findViewById(R.id.card_video);
        files = v.findViewById(R.id.card_files);

        intruder = v.findViewById(R.id.card_intruder);
        browser = v.findViewById(R.id.card_browser);

        intruder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), IntruderActivity.class));

            }
        });


        browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO WAQAS Add Browser on Click Listener
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivityForResult(new Intent(getActivity(), ImagesActivity.class), AppConstants.REFRESH_LIST);

            }
        });

        audios.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                startActivityForResult(new Intent(getActivity(), AudiosActivity.class), AppConstants.REFRESH_LIST);

            }
        });
        videos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                HomeFragment.this.startActivityForResult(new Intent(HomeFragment.this.getActivity(), VideoActivity.class), AppConstants.REFRESH_LIST);


            }
        });
        files.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivityForResult(new Intent(getActivity(), FilesActivity.class), AppConstants.REFRESH_LIST);

            }
        });

    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getActivity().getSystemService(Context.DEVICE_POLICY_SERVICE);
//        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getActivity().getSystemService("device_policy");
        ComponentName demoDeviceAdmin = new ComponentName(getActivity(), PolicyManager.class);
        if (devicePolicyManager == null || !devicePolicyManager.isAdminActive(demoDeviceAdmin)) {

            AddMobe();
            this.dbHelper = new DBHelper(getActivity());

        } else {
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

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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
                    HomeFragment.this.getActivity().finish();
                }
            });
            alertDialog.show();
        }
    }


    private void AddMobe() {

        if (HomeFragment.this.type == null) {
            return;
        }
        if (HomeFragment.this.type.equals(AppConstants.IMAGE)) {
            HomeFragment.this.startActivityForResult(new Intent(HomeFragment.this.getActivity(), ImagesActivity.class), AppConstants.REFRESH_LIST);
        } else {
            HomeFragment.this.startActivityForResult(new Intent(HomeFragment.this.getActivity(), VideoActivity.class), AppConstants.REFRESH_LIST);
        }
    }


}
