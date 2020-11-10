package com.androidbull.calculator.photo.vault.activities;

import android.annotation.TargetApi;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.androidbull.calculator.photo.R;
import com.androidbull.calculator.photo.vault.utils.Utils;

public class PrivacyPolicyActivity extends BaseActivity {

    private WebView mWebView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        mWebView = findViewById(R.id.txtInformtation);

        setupToolBar();

        if (Utils.isNetworkAvailable(this))
            loadContent();
        else
            Toast.makeText(this, getString(R.string.str_not_connected_to_internet), Toast.LENGTH_SHORT).show();

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


        mWebView.getSettings().setJavaScriptEnabled(true); // enable javascript

        mWebView.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(PrivacyPolicyActivity.this, description, Toast.LENGTH_SHORT).show();
            }

            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });

        mWebView.loadUrl("https://tictactoe-brain-games.blogspot.com/2020/06/calculator-photo-vault-privacy-policy.html");

    }

}