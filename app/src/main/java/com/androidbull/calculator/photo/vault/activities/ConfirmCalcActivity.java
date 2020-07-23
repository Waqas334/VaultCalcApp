package com.androidbull.calculator.photo.vault.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbull.calculator.photo.R;
import com.androidbull.calculator.photo.vault.MainApplication;
import com.androidbull.calculator.photo.vault.MyBassActivity;
import com.androidbull.calculator.photo.vault.utils.share.Share;
import com.androidbull.calculator.photo.vault.utils.share.share_calc;

public class ConfirmCalcActivity extends MyBassActivity implements View.OnClickListener, View.OnTouchListener {


    TextView  tv_eight;

    TextView  tv_five;

    TextView  tv_two;

    TextView  tv_dot;

    TextView  tv_divide;

    TextView tv_nine;
    /* renamed from: G */
    TextView tv_six;
    /* renamed from: H */
    TextView  tv_three;
    /* renamed from: I */
    TextView  tv_sign;
    /* renamed from: J */
    TextView  tv_mul;
    /* renamed from: K */
    TextView  tv_min;
    /* renamed from: L */
    TextView  tv_plus;
    /* renamed from: M */
    TextView  tv_equal;
    /* renamed from: N */
    TextView  tv_sqrt;
    /* renamed from: O */
//    TextView  f6017O;
    /* renamed from: P */
    EditText et_main;
    /* renamed from: Q */
    EditText et_result;
    /* renamed from: R */
//    TextView f6020R;
    /* renamed from: S */

    //Delete layout
//    LinearLayout f6021S;
    /* renamed from: T */
//    LinearLayout f6022T;
    /* renamed from: U */
//    RelativeLayout f6023U;
    /* renamed from: V */
    String f6024V = "";
    /* renamed from: W */
    String f6025W = "";
    /* renamed from: X */
    String f6026X = "";
    /* renamed from: Y */
    Boolean f6027Y = false;
    /* renamed from: Z */
    Editable f6028Z;
    boolean aB;
    boolean aC = false;
    Double aa;
    String ab = "";
    String ac = "";
    int ad = 0;
    Boolean ah = false;
    Boolean ai = false;
    Boolean aj = false;
    Boolean ak = false;
    Boolean al = false;
    Boolean am = false;
    Boolean an = false;
    Boolean ao = false;
    Boolean ap = false;
    Boolean aq = false;
    Boolean ar = true;
    Boolean av = false;
    Boolean az =false;
    private String expressions = "";
    private String firststr = "";

    /* renamed from: n */
    Boolean f6029n = false;
    /* renamed from: o */
    Boolean f6030o = false;
    /* renamed from: p */
    Boolean f6031p = false;
    private String prev = "";
    /* renamed from: q */
    Boolean f6032q = false;
    /* renamed from: r */
    Boolean f6033r = false;
    TextView tv_clear;
    /* renamed from: v */
    TextView tv_seven;
    /* renamed from: w */
    TextView tv_four;
    /* renamed from: x */
    TextView tv_one;
    /* renamed from: y */
    TextView tv_zero;
    /* renamed from: z */
    TextView tv_percent;

    //my variable
    String firstString = "";
    String secondString = "";
    String oprator = "";
    String completeString = "";
    boolean opererationSelected = false;
    boolean firstOpratorSelected = false;
    double answer;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_confirm_calc);

        initviews();
        initlisteners();
        check_tablet();
        if (MainApplication.getInstance().getPassword().isEmpty()) {

            showConfirmDialog();

            return;
        }

    }


    class C16052 implements TextWatcher {

        final ConfirmCalcActivity f6001a;

        C16052(ConfirmCalcActivity scientific_CalculatorActivity2) {
            this.f6001a = scientific_CalculatorActivity2;
        }

        public void afterTextChanged(Editable editable) {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (this.f6001a.et_result.getText().toString().equalsIgnoreCase("")
                    || this.f6001a.et_result.getText().toString().equalsIgnoreCase("0")) {
                this.f6001a.et_result.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
            }
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!this.f6001a.et_result.getText().toString().equalsIgnoreCase("")) {
                this.f6001a.et_result.setSelection(this.f6001a.et_result.getText().toString().length() - 1);
            }
        }
    }


    class C16063 implements TextWatcher {
        /* renamed from: a */
        final /* synthetic */ ConfirmCalcActivity f6002a;

        C16063(ConfirmCalcActivity scientific_CalculatorActivity2) {
            this.f6002a = scientific_CalculatorActivity2;
        }

        public void afterTextChanged(Editable editable) {
            if (!this.f6002a.et_main.getText().toString().equalsIgnoreCase("")) {
                this.f6002a.et_main.setSelection(this.f6002a.et_main.getText().toString().length() - 1);
            }
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!this.f6002a.et_main.getText().toString().equalsIgnoreCase("")) {
                this.f6002a.et_main.setSelection(this.f6002a.et_main.getText().toString().length() - 1);
            }
        }
    }


    private void check_tablet() {
//        this.f6017O = (ImageView) findViewById(R.id.iv_square_root);
//        this.f6017O.setOnClickListener(this);
        if (share_calc.flag_expand) {
            Log.e("flag_expand", "" + share_calc.flag_expand);
            share_calc.flag_expand = false;
        }
    }



    private void initlisteners() {
        this.tv_zero.setOnClickListener(this);
        this.tv_one.setOnClickListener(this);
        this.tv_two.setOnClickListener(this);
        this.tv_three.setOnClickListener(this);
        this.tv_four.setOnClickListener(this);
        this.tv_five.setOnClickListener(this);
        this.tv_six.setOnClickListener(this);
        this.tv_seven.setOnClickListener(this);
        this.tv_eight.setOnClickListener(this);
        this.tv_nine.setOnClickListener(this);
        this.tv_plus.setOnClickListener(this);
        this.tv_min.setOnClickListener(this);
        this.tv_mul.setOnClickListener(this);
        this.tv_divide.setOnClickListener(this);
        this.tv_percent.setOnClickListener(this);
        this.tv_clear.setOnClickListener(this);
        this.tv_equal.setOnClickListener(this);
        this.tv_sqrt.setOnClickListener(this);
//        this.f6021S.setOnClickListener(this);
        this.tv_sign.setOnClickListener(this);
        this.tv_dot.setOnClickListener(this);
        this.tv_zero.setOnTouchListener(this);
        this.tv_one.setOnTouchListener(this);
        this.tv_two.setOnTouchListener(this);
        this.tv_three.setOnTouchListener(this);
        this.tv_four.setOnTouchListener(this);
        this.tv_five.setOnTouchListener(this);
        this.tv_six.setOnTouchListener(this);
        this.tv_seven.setOnTouchListener(this);
        this.tv_eight.setOnTouchListener(this);
        this.tv_nine.setOnTouchListener(this);
        this.tv_plus.setOnTouchListener(this);
        this.tv_min.setOnTouchListener(this);
        this.tv_mul.setOnTouchListener(this);
        this.tv_divide.setOnTouchListener(this);
        this.tv_percent.setOnTouchListener(this);
        this.tv_clear.setOnTouchListener(this);
        this.tv_equal.setOnTouchListener(this);
        this.tv_sqrt.setOnTouchListener(this);
//        this.f6021S.setOnTouchListener(this);
        this.tv_sign.setOnTouchListener(this);
        this.tv_dot.setOnTouchListener(this);
        this.et_result.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        this.et_result.setSingleLine();
        this.et_result.addTextChangedListener(new ConfirmCalcActivity.C16052(this));
        this.et_main.setSingleLine();
        this.et_result.setSingleLine();
        this.et_main.addTextChangedListener(new ConfirmCalcActivity.C16063(this));
    }

    private void initviews() {
        this.tv_clear = findViewById(R.id.tv_clear);
        this.tv_seven = findViewById(R.id.tv_seven);
        this.tv_four = findViewById(R.id.tv_four);
        this.tv_one = findViewById(R.id.tv_one);
        this.tv_zero = findViewById(R.id.tv_zero);
        this.tv_percent = findViewById(R.id.tv_percent);
        this.tv_eight = findViewById(R.id.tv_eight);
        this.tv_five = findViewById(R.id.tv_five);
        this.tv_two = findViewById(R.id.tv_two);
        this.tv_dot = findViewById(R.id.tv_dot);
        this.tv_divide = findViewById(R.id.tv_divide);
        this.tv_nine = findViewById(R.id.tv_nine);
        this.tv_six = findViewById(R.id.tv_six);
        this.tv_three = findViewById(R.id.tv_three);
        this.tv_sign = findViewById(R.id.tv_sqrt);
        this.tv_mul = findViewById(R.id.tv_mul);
        this.tv_min = findViewById(R.id.tv_min);
        this.tv_plus = findViewById(R.id.tv_plus);
        this.tv_equal = findViewById(R.id.tv_equal);
        this.tv_sqrt = findViewById(R.id.tv_sqrt);
        this.et_main = findViewById(R.id.et_main);
        this.et_result = findViewById(R.id.tv_Display);
//        this.f6020R = (TextView) findViewById(R.id.tv_divide);
//        this.f6021S = (LinearLayout) findViewById(R.id.ll_delete);
//        this.f6022T = (LinearLayout) findViewById(R.id.ll_calc);
//        this.f6023U = (RelativeLayout) findViewById(R.id.rl_calc_layout);
    }





    public boolean isTablet(Context context) {
        return ((context.getResources().getConfiguration().screenLayout & 15) == 4) || ((context.getResources().getConfiguration().screenLayout & 15) == 3);
    }

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_equal:
                if (opererationSelected) {

                    if (!(secondString.isEmpty())) {
                        answer = calculateResult(oprator);
                        et_result.setText(Double.toString(answer));
                        et_main.setText("");
                        firstString = "";
                        secondString = "";
                        opererationSelected = false;
                        oprator = "";
                        completeString = "";
                    }
                }else {
                    Log.d("Equal Opration" , "" +
                            "Enterd int oelse");
                    if (MainApplication.getInstance().getPassword().equals("")) {
                        int cpass;
                        if (!et_main.getText().toString().equals("")) {
                            Log.d("Equal Opration " , et_main.getText().toString());
                            cpass = Integer.parseInt(this.et_main.getText().toString());
                            if (cpass == Share.pass) {

                                MainApplication.getInstance().savePassword(String.valueOf(cpass));
                                ProgressDialogSuccess();


                            } else {

                                PasswordErrorDialog();

                            }
                        }else{
                            Toast.makeText(this, "First Enter a Number", Toast.LENGTH_SHORT).show();
                        }


                        return;
                    }
//                    if (!this.f6033r) {
//                        if (!(!this.ah || this.f6026X.equals("") || this.f6025W == null || this.f6025W.equalsIgnoreCase(""))) {
//                            Log.e("str2equal", this.f6025W);
//                            this.f6028Z = new SpannableStringBuilder(this.et_result.getText().toString() + this.f6026X + this.f6025W);
//
//                        }
//                        if (this.et_main.getText().length() <= 0) {
//                            Toast.makeText(this, getString(R.string.select_num_first), Toast.LENGTH_SHORT).show();
//                            return;
//                        } else if (this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '+' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '-' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '/' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '*' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '%') {
//                            this.aj = false;
//                            this.ak = false;
//                            this.am = false;
//                            this.al = false;
//                            this.an = false;
//                            this.ao = false;
//                            this.f6028Z = this.et_main.getText();
//                            this.ah = false;
//                            this.ac = "";
//                            return;
//                        } else {
//                            return;
//                        }
//                    }

                }
                return;

            case R.id.tv_clear:
                et_main.setText("");
                et_result.setText("0");
                opererationSelected = false;
                firstString = "";
                secondString = "";
                oprator = "";
                return;
            case R.id.tv_dot:
                et_result.setText("");
                dotOpration();

                return;

            case R.id.tv_nine:
                et_result.setText("");
                getvalue("9");
                return;

            case R.id.tv_eight:
                et_result.setText("");
                getvalue("8");
                return;

            case R.id.tv_seven:
                et_result.setText("");
                getvalue("7");
                return;

            case R.id.tv_six:
                et_result.setText("");
                getvalue("6");
                return;

            case R.id.tv_five:
                et_result.setText("");
                getvalue("5");
                return;

            case R.id.tv_four:
                et_result.setText("");
                getvalue("4");
                return;

            case R.id.tv_three:
                et_result.setText("");
                getvalue("3");
                return;
            case R.id.tv_two:
                et_result.setText("");
                getvalue("2");
                return;

            case R.id.tv_one:
                et_result.setText("");
                getvalue("1");
                return;
            case R.id.tv_zero:
                et_result.setText("");
                getvalue("0");
                return;

            //oprators

            case R.id.tv_plus:
                if (!(et_result.getText().toString().isEmpty())){
                    opererationSelected = true;
                    oprator = "+";
                    firstString = et_result.getText().toString();
                    completeString = firstString + " " + oprator +" ";
                    et_main.setText(completeString);
                    et_result.setText("");

                }
                else if (firstString.isEmpty()) {
                    Toast.makeText(this, "First Enter a number", Toast.LENGTH_SHORT).show();
                } else {
                    opererationSelected = true;
                    oprator = "+";
                    completeString = firstString + " " + oprator +" ";
                    et_main.setText(completeString);
                }
                return;

            case R.id.tv_min:
                if (!(et_result.getText().toString().isEmpty())){
                    opererationSelected = true;
                    oprator = "-";
                    firstString = et_result.getText().toString();
                    completeString = firstString + " " + oprator +" ";
                    et_main.setText(completeString);
                    et_result.setText("");

                }
                else if (firstString.isEmpty()) {
                    Toast.makeText(this, "First Enter a number", Toast.LENGTH_SHORT).show();

                } else {
                    opererationSelected = true;
                    oprator = "-";
                    completeString = firstString + " " + oprator +" ";
                    et_main.setText(completeString);
                }

                return;

            case R.id.tv_mul:
                if (!(et_result.getText().toString().isEmpty())){
                    opererationSelected = true;
                    oprator = "*";
                    firstString = et_result.getText().toString();
                    completeString = firstString + " " + oprator +" ";
                    et_main.setText(completeString);
                    et_result.setText("");

                }
                else if (firstString.isEmpty()) {
                    Toast.makeText(this, "First Enter a number", Toast.LENGTH_SHORT).show();
                }
                else {
                    opererationSelected = true;
                    oprator = "*";
                    completeString = firstString + " " + oprator +" ";
                    et_main.setText(completeString);
                }

                return;

            case R.id.tv_divide:
                if (!(et_result.getText().toString().isEmpty())){
                    opererationSelected = true;
                    oprator = "/";
                    firstString = et_result.getText().toString();
                    completeString = firstString + " " + oprator +" ";
                    et_main.setText(completeString);
                    et_result.setText("");

                }
                else if (firstString.isEmpty()) {
                    Toast.makeText(this, "First Enter a number", Toast.LENGTH_SHORT).show();
                }
                else {
                    opererationSelected = true;
                    oprator = "/";
                    completeString = firstString + " " + oprator +" ";
                    et_main.setText(completeString);
                }

                return;

            case R.id.tv_sqrt:
                if (et_main.getText().toString().isEmpty()) {
                    Toast.makeText(this, "First Enter Number", Toast.LENGTH_SHORT).show();

                }
                else {
                    double val = Double.valueOf(et_main.getText().toString());
                    answer = val * val;
                    et_result.setText(Double.toString(answer));
                    et_main.setText("");
                    firstString = "";
                }
                return;

            default:
                return;
        }
    }


    protected void onDestroy() {
        super.onDestroy();
        this.aB = false;
    }

    protected void onResume() {
        super.onResume();
        this.aB = true;

    }

    protected void onStop() {
        super.onStop();
        this.aB = false;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                view.setAlpha(0.2f);
                view.callOnClick();
                break;
            case 1:
                view.setAlpha(1.0f);
                break;
        }
        return true;
    }



    private void showConfirmDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_templete);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setLayout(-1, -2);
        }
        ((TextView)dialog.findViewById(R.id.dialog_title)).setText(getString(R.string.confirm_password));
        ((TextView)dialog.findViewById(R.id.dialog_tv_message)).setText(getString(R.string.confirm_pass_desc));
        ((ImageView)dialog.findViewById(R.id.dialog_iv_header)).setImageResource(R.drawable.ic_lock_white_new);
        dialog.findViewById(R.id.btn_cancel).setVisibility(View.GONE);
        dialog.findViewById(R.id.dialog_vertical_seperator).setVisibility(View.GONE);
        dialog.findViewById(R.id.img_close).setOnClickListener(view -> dialog.dismiss());


        dialog.findViewById(R.id.btn_ok).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    public void ProgressDialogSuccess() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_templete);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setLayout(-1, -2);
        }
        ((TextView)dialog.findViewById(R.id.dialog_title)).setText(getString(R.string.done));
        ((TextView)dialog.findViewById(R.id.dialog_tv_message)).setText(getString(R.string.pass_set_desc));
        ((ImageView)dialog.findViewById(R.id.dialog_iv_header)).setImageResource(R.drawable.ic_done_white);
        dialog.findViewById(R.id.btn_cancel).setVisibility(View.GONE);
        dialog.findViewById(R.id.dialog_vertical_seperator).setVisibility(View.GONE);
        TextView btnOk = dialog.findViewById(R.id.btn_ok);
        dialog.findViewById(R.id.img_close).setOnClickListener(view -> dialog.dismiss());
        btnOk.setOnClickListener(view -> {
            dialog.dismiss();

            String secretQuestion = MainApplication.getInstance().getSecurityQuestion();
            if (TextUtils.isEmpty(secretQuestion)) {
                startActivity(new Intent(ConfirmCalcActivity.this, SecurityQuestionActivity.class).putExtra(SecurityQuestionActivity.TYPE, SecurityQuestionActivity.ADD));
                finish();
                return;
            }
            startActivity(new Intent(ConfirmCalcActivity.this, HomeActivity.class));
            finish();
        });
        dialog.show();
    }

    public void PasswordErrorDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_templete);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setLayout(-1, -2);
        }
        ((TextView)dialog.findViewById(R.id.dialog_title)).setText(getString(R.string.error));
        ((ImageView)dialog.findViewById(R.id.dialog_iv_header)).setImageResource(R.drawable.ic_close_white_48dp);
        ((TextView) dialog.findViewById(R.id.dialog_tv_message)).setText(getString(R.string.confirm_pass_error_desc));
        (dialog.findViewById(R.id.btn_cancel)).setVisibility(View.GONE);
        (dialog.findViewById(R.id.dialog_vertical_seperator)).setVisibility(View.GONE);
        TextView btnOk = dialog.findViewById(R.id.btn_ok);
        dialog.findViewById(R.id.img_close).setOnClickListener(view -> dialog.dismiss());
        btnOk.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    //get value method
    public void getvalue(String value) {
        if (opererationSelected) {
            secondString += value;
            completeString = completeString + value;
            et_main.setText(completeString);
//            answer = calculateResult(oprator);
//            tv_Display.setText(Double.toString(answer));
//            firstString = Double.toString(answer);
//            secondString = "";

        } else {
            firstString += value;
            et_main.setText(firstString);
        }
    }

    //dot opration
    public void dotOpration() {
        if (opererationSelected) {
            if (!secondString.contains(".")) {
                secondString += ".";
                completeString += ".";
                et_main.setText(completeString);
            }

        } else {
            if (!firstString.contains(".")) {
                firstString += ".";

                et_main.setText(firstString);
            }
        }
    }

    //calculate result
    public double calculateResult(String oprator) {
        double ans = 0;

        switch (oprator) {
            case "-":
                ans = Double.valueOf(firstString) - Double.valueOf(secondString);
                firstString = "";
                secondString = "";
                oprator = "";
                opererationSelected = false;

                break;
            case "+":
                ans = Double.valueOf(firstString) + Double.valueOf(secondString);
                firstString = "";
                secondString = "";
                oprator = "";
                opererationSelected = false;
                break;

            case "/":
                if (secondString == "0") {
                    et_result.setText("infinity");
                    firstString = "";
                    secondString = "";
                    oprator = "";
                    opererationSelected = false;
                } else {
                    ans = Double.valueOf(firstString) / Double.valueOf(secondString);
                    firstString = "";
                    secondString = "";
                    oprator = "";
                    opererationSelected = false;
                }
                break;
            case "*":
                ans = Double.valueOf(firstString) * Double.valueOf(secondString);
                break;
        }
        return ans;

    }



}
