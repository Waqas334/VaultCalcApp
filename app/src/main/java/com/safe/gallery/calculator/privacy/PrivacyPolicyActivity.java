package com.safe.gallery.calculator.privacy;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.app.BaseActivity;

public class PrivacyPolicyActivity extends BaseActivity {

    WebView txtInformtation;
    ImageView imgBack;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy);

        findViews();
        initViews();

    }

    private void findViews() {

        toolbar = findViewById(R.id.toolbar);
        imgBack = findViewById(R.id.ic_back);

    }

    private void initViews() {

        txtInformtation = findViewById(R.id.txtInformtation);
        txtInformtation.loadUrl("file:///android_asset/privacy.html");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }

}