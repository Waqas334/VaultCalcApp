package com.androidbull.calculator.photo.vault.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.androidbull.calculator.photo.vault.MyBassActivity;
import com.androidbull.calculator.photo.R;
import com.androidbull.calculator.photo.vault.MainApplication;

public class NewChangePasswordActivity extends MyBassActivity {
    private static final String TAG = "NewChangePasswordActivi";
    private EditText mEt1, mEt2, mEt3, mEt4;
    private TextView mTvErrorMessage;
    private TextView mTvInstruction;
    private String newPassword;
    private int CURRENT_STATUS = 0;
    /*
    0 = Enter current password
    1 = Enter New Password
    2 = Enter Confirm Password
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_change_password);


        Toolbar toolbar = findViewById(R.id.change_pass_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.change_password));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEt1 = findViewById(R.id.change_pass_et_1);
        mEt2 = findViewById(R.id.change_pass_et_2);
        mEt3 = findViewById(R.id.change_pass_et_3);
        mEt4 = findViewById(R.id.change_pass_et_4);
        Button mBtnNext = findViewById(R.id.change_pass_btn_next);
        mBtnNext.setOnClickListener(nextClickListener);
        mTvErrorMessage = findViewById(R.id.change_password_tv_error);
        mTvInstruction = findViewById(R.id.change_pass_tv_instruction);

        mEt1.requestFocus();



        mEt2.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL)
                mEt1.requestFocus();
            return false;
        });
        mEt3.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL)
                mEt2.requestFocus();
            return false;
        });
        mEt4.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL)
                mEt3.requestFocus();
            return false;
        });


        mEt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (after == 1) {
                    mEt3.clearFocus();
                    mEt4.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (after == 1) {
                    mEt2.clearFocus();
                    mEt3.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i(TAG, "beforeTextChanged: s: " + s + "\n\n\nStart: " + start + "\n\n\nCount: " + count + "\n\n\nAfter: " + after);
                if (after == 1) {
                    mEt1.clearFocus();
                    mEt2.requestFocus();
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(TAG, "onTextChanged: s: " + s + "\n\n\nStart: " + start + "\n\n\nCount: " + count);

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(TAG, "onTextChanged: editable: " + s);

            }
        });

    }


    private View.OnClickListener nextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTvErrorMessage.setVisibility(View.GONE);
            String pass = mEt1.getText().toString() + mEt2.getText().toString() + mEt3.getText().toString() + mEt4.getText().toString();
            if (isPassValid(pass)) return;
            if (CURRENT_STATUS == 0) {
                //Please enter current password
                if (!MainApplication.getInstance().getPassword().equals(pass)) {
                    mTvErrorMessage.setText(getString(R.string.error_password_not_matched));
                    mTvErrorMessage.setVisibility(View.VISIBLE);
                    return;
                }
                CURRENT_STATUS = 1;
                resetEditTexts();
                mTvInstruction.setText(getString(R.string.enter_new_pass));
            } else if (CURRENT_STATUS == 1) {
                //Please enter new password
                CURRENT_STATUS = 2;
                resetEditTexts();
                mTvInstruction.setText(getString(R.string.enter_confirm_pass));
                newPassword = pass;
            } else if (CURRENT_STATUS == 2) {
                //Repeat password entered

                if (pass.equals(newPassword)) {
                    MainApplication.getInstance().savePassword(pass);
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewChangePasswordActivity.this);
                    builder.setTitle(getString(R.string.done));
                    builder.setMessage(getString(R.string.password_updated));
                    builder.setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss());
                    builder.setOnDismissListener(dialog -> NewChangePasswordActivity.this.finish());
                    builder.show();

                } else {
                    mTvErrorMessage.setText(getString(R.string.invalid_confirm_pass));
                }


            }


        }
    };

    private void resetEditTexts() {
        mEt1.setText("");
        mEt2.setText("");
        mEt3.setText("");
        mEt4.setText("");
        mEt1.requestFocus();
    }

    private boolean isPassValid(String pass) {
        if (pass.length() != 4) {
            mTvErrorMessage.setText(getString(R.string.password_must_be_4_digit));
            mTvErrorMessage.setVisibility(View.VISIBLE);
            return true;
        }

        if (!isAllNumbers(pass)) {
            mTvErrorMessage.setText(getString(R.string.pass_must_be_numeric));
            mTvErrorMessage.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

    private boolean isAllNumbers(String string) {
        if (string.matches("\\d+(?:\\.\\d+)?"))
            return true;
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
