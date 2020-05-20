package com.safe.gallery.calculator.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatSpinner;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.app.BaseActivity;
import com.safe.gallery.calculator.app.MainApplication;
import com.safe.gallery.calculator.utils.CenterTitleToolbar;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class SecurityQuestionActivity extends BaseActivity {

    public static final String ADD = "add";
    public static final String CHANGE = "change";
    public static final String FORGOT_PASS = "forgot_pass";
    public static final String TYPE = "type";
    private String[] array;

    Button btnSubmit;

    EditText etAnswer;

    private int position;
    private String question;
    private String selectedQuestion;

    AppCompatSpinner spinQuestions;
    CenterTitleToolbar toolbar;

    private String type;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_security_question);
        ButterKnife.bind((Activity) this);

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

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle((CharSequence) "Security Question");


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
            array = getResources().getStringArray(R.array.questions);
            question = MainApplication.getInstance().getSecurityQuestion();
            etAnswer.setText(MainApplication.getInstance().getSecurityAnswer());
            for (int i = 0; i < array.length; i++) {
                position = i;
                if (question.equals(array[i])) {
                    spinQuestions.post(new runnable());
                    return;
                }
            }
        }
    }
    class runnable implements Runnable {
        runnable() {
        }

        public void run()
        {
            spinQuestions.setSelection(position);
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.btn_submit})
    public void onClick() {
        if (selectedQuestion == null) {
            Toast.makeText(this, "Please select security question", Toast.LENGTH_LONG).show();
        } else if (selectedQuestion.equals("Select Your Question")) {
            Toast.makeText(this, "Please select security question", Toast.LENGTH_LONG).show();
        } else if (etAnswer.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter security answer", Toast.LENGTH_LONG).show();
        } else if (type == null || !type.equals(FORGOT_PASS)) {
            MainApplication.getInstance().setSecurityQuestion(selectedQuestion);
            MainApplication.getInstance().setSecurityAnswer(etAnswer.getText().toString());
            setBackData();
        } else if (!selectedQuestion.equalsIgnoreCase(MainApplication.getInstance().getSecurityQuestion())) {
            Toast.makeText(this, "Security question is incorrect!", Toast.LENGTH_LONG).show();
        } else if (etAnswer.getText().toString().equalsIgnoreCase(MainApplication.getInstance().getSecurityAnswer())) {
            showPassword();
        } else {
            Toast.makeText(this, "Security answer is incorrect!", Toast.LENGTH_LONG).show();
        }
    }

    private void showPassword() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_security_question);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setLayout(-1, -2);
        }
        TextView btnOk = (TextView) dialog.findViewById(R.id.btn_ok);
        ImageView imgClose = (ImageView) dialog.findViewById(R.id.img_close);
        ((TextView) dialog.findViewById(R.id.et_file_name)).setText("Your password is \n " + MainApplication.getInstance().getPassword());
        imgClose.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });
        btnOk.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }

    private void setBackData() {

        setResult(-1, new Intent());
        if (type != null && type.equals(CHANGE)) {
            finish();
        } else if (type != null && type.equals(ADD)) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }
    }
}
