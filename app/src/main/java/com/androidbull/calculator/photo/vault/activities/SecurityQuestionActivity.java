package com.androidbull.calculator.photo.vault.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import com.androidbull.calculator.photo.vault.MyBassActivity;
import com.androidbull.calculator.photo.R;
import com.androidbull.calculator.photo.vault.MainApplication;

import org.w3c.dom.Text;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.androidbull.calculator.photo.vault.utils.AppConstants.PREF_SECURITY_QUESTION_INDEX;


public class SecurityQuestionActivity extends MyBassActivity implements View.OnClickListener {

    public static final String ADD = "add";
    public static final String CHANGE = "change";
    public static final String FORGOT_PASS = "forgot_pass";
    public static final String TYPE = "type";

    Button btnSubmit;

    EditText etAnswer;

    private int position;
    private String selectedQuestion;

    AppCompatSpinner spinQuestions;
    Toolbar toolbar;

    private String type;

    private SharedPreferences sharedPreferences;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_question);

        findViews();
        initViews();


    }

    private void findViews() {

        btnSubmit = findViewById(R.id.btn_submit);
        etAnswer = findViewById(R.id.et_answer);
        spinQuestions = findViewById(R.id.spin_questions);
        toolbar = findViewById(R.id.toolbar);

    }

    private void initViews() {
        btnSubmit.setOnClickListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.change_security_question));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (getIntent().getExtras() != null) {
            type = getIntent().getStringExtra(TYPE);
        }
        spinQuestions.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedQuestion = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        if (type != null && type.equals(CHANGE)) {
            String[] array = getResources().getStringArray(R.array.questions);
            String question = MainApplication.getInstance().getSecurityQuestion();
            etAnswer.setText(MainApplication.getInstance().getSecurityAnswer());
            for (int i = 0; i < array.length; i++) {
                position = i;
                if (question.equals(array[i])) {
                    spinQuestions.post(new runnable());
                    return;
                }
            }
        } else if (type != null && type.equals(FORGOT_PASS)) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.str_please_enter_security_answer);
            }
            ((TextView) findViewById(R.id.tv_select_sec_question)).setText(R.string.str_security_question);
            ((ImageView) findViewById(R.id.spnIcon)).setVisibility(View.GONE);
            spinQuestions.setSelection(getSavedSecurityQuestionIndex());
            spinQuestions.setEnabled(false);
        }

    }

    private void saveSelectedQuestionIndex(int index) {
        sharedPreferences.edit().putInt(PREF_SECURITY_QUESTION_INDEX, index).apply();
    }

    private int getSavedSecurityQuestionIndex() {
        return sharedPreferences.getInt(PREF_SECURITY_QUESTION_INDEX, 0);
    }


    class runnable implements Runnable {
        runnable() {
        }

        public void run() {
            spinQuestions.setSelection(position);
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_submit) {
            if (selectedQuestion == null || selectedQuestion.equals(getString(R.string.select_security_question))) {
                Toast.makeText(this, getString(R.string.select_security_question), Toast.LENGTH_LONG).show();
            } else if (etAnswer.getText().toString().isEmpty()) {
                Toast.makeText(this, getString(R.string.select_security_answer), Toast.LENGTH_LONG).show();
            } else if (type == null || !type.equals(FORGOT_PASS)) {
                MainApplication.getInstance().setSecurityQuestion(selectedQuestion);
                MainApplication.getInstance().setSecurityAnswer(etAnswer.getText().toString());
                saveSelectedQuestionIndex(spinQuestions.getSelectedItemPosition());
                setBackData();
            } else if (!selectedQuestion.equalsIgnoreCase(MainApplication.getInstance().getSecurityQuestion())) {
                Toast.makeText(this, getString(R.string.security_question_incorrect), Toast.LENGTH_LONG).show();
            } else if (etAnswer.getText().toString().equalsIgnoreCase(MainApplication.getInstance().getSecurityAnswer())) {
                showPassword();
            } else {
                Toast.makeText(this, getString(R.string.security_answer_incorrect), Toast.LENGTH_LONG).show();
            }
        }
    }


    private void showPassword() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_templete);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setLayout(-1, -2);
        }
        ((TextView) dialog.findViewById(R.id.dialog_title)).setText(getString(R.string.done));
        ((ImageView) dialog.findViewById(R.id.dialog_iv_header)).setImageResource(R.drawable.ic_check_white_new_24dp);
        ((TextView) dialog.findViewById(R.id.dialog_tv_message)).setText(getString(R.string.your_pass_is, MainApplication.getInstance().getPassword()));
        dialog.findViewById(R.id.img_close).setOnClickListener(view -> {
            dialog.dismiss();
            finish();
        });
        dialog.findViewById(R.id.btn_ok).setOnClickListener(view -> {
            dialog.dismiss();
            finish();
        });
        dialog.show();
    }

    private void setBackData() {

        setResult(-1, new Intent());
        if (type != null && type.equals(CHANGE)) {
            finish();
        } else if (type != null && type.equals(ADD)) {
            finish();
            Intent homeIntent = new Intent(this, HomeActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
    }
}
