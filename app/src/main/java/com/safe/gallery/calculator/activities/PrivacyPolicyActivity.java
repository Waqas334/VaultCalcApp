package com.safe.gallery.calculator.activities;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.webkit.WebView;

import com.safe.gallery.calculator.R;

public class PrivacyPolicyActivity extends BaseActivity {

    private WebView mWebView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        setupToolBar();
        loadContent();

    }

    private void setupToolBar() {

        toolbar = findViewById(R.id.privacy_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.privacy_policy));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private void loadContent() {

        mWebView = findViewById(R.id.txtInformtation);
        mWebView.loadUrl("file:///android_asset/privacy.html");

    }

}