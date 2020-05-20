package com.safe.gallery.calculator.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.app.MainApplication;
import com.safe.gallery.calculator.share.Share;
import com.safe.gallery.calculator.share.share_calc;

import net.objecthunter.exp4j.ExpressionBuilder;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ConfirmCalcActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    public static String Radian_Degree = "DEG";

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
    Boolean f6027Y = Boolean.valueOf(false);
    /* renamed from: Z */
    Editable f6028Z;
    String aA = "";
    boolean aB;
    boolean aC = false;
    Double aa;
    String ab = "";
    String ac = "";
    int ad = 0;
    int ae = 0;
    int af = 0;
    int ag = 0;
    Boolean ah = Boolean.valueOf(false);
    Boolean ai = Boolean.valueOf(false);
    Boolean aj = Boolean.valueOf(false);
    Boolean ak = Boolean.valueOf(false);
    Boolean al = Boolean.valueOf(false);
    Boolean am = Boolean.valueOf(false);
    Boolean an = Boolean.valueOf(false);
    Boolean ao = Boolean.valueOf(false);
    Boolean ap = Boolean.valueOf(false);
    Boolean aq = Boolean.valueOf(false);
    Boolean ar = Boolean.valueOf(true);
    Boolean as = Boolean.valueOf(true);
    Boolean at = Boolean.valueOf(true);
    Boolean au = Boolean.valueOf(true);
    Boolean av = Boolean.valueOf(false);
    Boolean aw = Boolean.valueOf(false);
    Boolean ax = Boolean.valueOf(false);
    Double ay = Double.valueOf(0.0d);
    Boolean az = Boolean.valueOf(false);
    private AlphaAnimation click_anim;
    private String expressions = "";
    private String firststr = "";

    /* renamed from: n */
    Boolean f6029n = Boolean.valueOf(false);
    /* renamed from: o */
    Boolean f6030o = Boolean.valueOf(false);
    /* renamed from: p */
    Boolean f6031p = Boolean.valueOf(false);
    private String prev = "";
    /* renamed from: q */
    Boolean f6032q = Boolean.valueOf(false);
    /* renamed from: r */
    Boolean f6033r = Boolean.valueOf(false);
    private Double result = Double.valueOf(0.0d);
    /* renamed from: s */
    int f6034s;
    /* renamed from: t */
    int f6035t;
    private String text = "";
    /* renamed from: u */
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


        this.click_anim = new AlphaAnimation(1.0f, 0.5f);
        // this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }


    class C16052 implements TextWatcher {

        final ConfirmCalcActivity f6001a;

        C16052(ConfirmCalcActivity scientific_CalculatorActivity2) {
            this.f6001a = scientific_CalculatorActivity2;
        }

        public void afterTextChanged(Editable editable) {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (this.f6001a.et_result.getText().toString().equalsIgnoreCase("") || this.f6001a.et_result.getText().toString().equalsIgnoreCase("0")) {
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
        if (share_calc.flag_expand.booleanValue()) {
            Log.e("flag_expand", "" + share_calc.flag_expand);
            share_calc.flag_expand = Boolean.valueOf(false);
        }
    }

    private void dot_operation() {
        if (this.et_main.getText().length() > 0) {
            this.ad = 0;
            if (this.ai.booleanValue()) {
                this.ah = Boolean.valueOf(false);
            }
            if (this.ah.booleanValue()) {
                this.et_main.setText("");
                this.ab = "";
                this.et_result.setText("0");
                this.ah = Boolean.valueOf(false);
            }
            char[] toCharArray = this.ab.toCharArray();
            for (int length = toCharArray.length - 1; length >= 0; length--) {
                if (toCharArray[length] == '.') {
                    this.ad++;
                }
                if (this.ad == 1) {
                    break;
                }
            }
            if (this.ad == 0 && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '.' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '+' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '-' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '/' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '*' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '%') {
                char charAt = this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1);
                this.et_result.setText(this.ab);
                Log.e("dot", "" + charAt);
                if (charAt < '0' || charAt > '9') {
                    Log.e("dot", "else" + charAt);
                    this.et_main.append("* 0.");
                    this.ab += "";
                    this.et_result.setText(this.ab);
                    return;
                }
                if (this.av.booleanValue()) {
                    this.ac += ".";
                }
                Log.e("dot", "if" + charAt);
                this.et_main.append(".");
                this.ab += ".";
                this.et_result.setText(this.ab);
            }
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
        this.tv_sign = findViewById(R.id.tv_sign);
        this.tv_mul = findViewById(R.id.tv_mul);
        this.tv_min = findViewById(R.id.tv_min);
        this.tv_plus = findViewById(R.id.tv_plus);
        this.tv_equal = findViewById(R.id.tv_equal);
        this.tv_sqrt = findViewById(R.id.tv_sqrt);
        this.et_main = (EditText) findViewById(R.id.et_main);
        this.et_result = (EditText) findViewById(R.id.tv_Display);
//        this.f6020R = (TextView) findViewById(R.id.tv_divide);
//        this.f6021S = (LinearLayout) findViewById(R.id.ll_delete);
//        this.f6022T = (LinearLayout) findViewById(R.id.ll_calc);
//        this.f6023U = (RelativeLayout) findViewById(R.id.rl_calc_layout);
    }

    private void mid_calculation() {
        if (this.f6026X.equals("/") && this.f6025W.equals("0")) {
            this.f6033r = Boolean.valueOf(true);
            this.et_result.setText("0");
            this.et_main.setText("Can't divide by 0");
            this.ab = "";
            return;
        }
        String valueOf = String.valueOf(this.f6028Z);
        Log.e("string", "" + valueOf);
        try {
            this.aa = Double.valueOf(new ExpressionBuilder(valueOf).build().evaluate());
            Log.e("result", "" + this.aa);
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }
        if (String.valueOf(this.aa).contains("E")) {
            Double d = (Double) new ExtendedDoubleEvaluator().evaluate(String.valueOf(this.aa).replaceAll("%", "").replace("E", "*10^"));
            String valueOf2 = String.valueOf(d.doubleValue() / 100.0d);
            Log.e("new result", "" + d);
            Log.e("new result", "" + valueOf2);
        }
        this.aa = Double.valueOf(Double.parseDouble(new DecimalFormat(".##########################################").format(this.aa)));
        try {
            CharSequence charSequence = null;
            ExtendedDoubleEvaluator extendedDoubleEvaluator = new ExtendedDoubleEvaluator();
            Object obj;
            if (this.az.booleanValue()) {
                int i;
                int i2 = 0;
                int i3 = 0;
                for (i = 0; i < valueOf.length(); i++) {
                    if (valueOf.charAt(i) == '(') {
                        i2++;
                        Log.e("count_left_bracket", "" + i2);
                    }
                    if (valueOf.charAt(i) == ')') {
                        i3++;
                        Log.e("count_right_bracket", "" + i3);
                    }
                }
                i = i2 - i3;
                Log.e("diff", "" + i);
                charSequence = valueOf;
                int i4 = 0;
                while (i4 < i) {
                    i4++;
                    obj = charSequence + ")";
                }
                this.az = Boolean.valueOf(false);
            } else {
                obj = valueOf;
            }
            this.et_result.setText(charSequence);
            Log.e("Answer", this.aa + "");
            Double d = this.aa;
            Long valueOf3 = Long.valueOf(new Double(d.doubleValue()).longValue());
            Log.e("Double", d + "");
            Log.e("long", valueOf3 + "");
            Locale locale = Locale.US;
            NumberFormat instance = NumberFormat.getInstance();
            instance.setMaximumIntegerDigits(20);
            instance.setMaximumFractionDigits(20);
            instance.setGroupingUsed(false);
            this.ab = instance.format(this.aa);
            if (this.ab.length() > 16) {
                this.ab = this.ab.substring(0, 16);
            } else {
                this.et_result.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
            }
            if (this.ab.equalsIgnoreCase("-0")) {
                this.ab = "0";
            }
            this.et_result.setText(this.ab);
            this.et_main.setText(this.ab);
            this.f6024V = this.ab;
        } catch (Exception e2) {
            Log.e("TAG", "Toast");
            Toast.makeText(this, "Syntax Error", 0).show();
            this.et_main.setText("");
            this.et_result.setText("0");
            this.ab = "";
            this.ac = "";
            this.prev = "";
            this.firststr = "";
            this.aC = false;
            e2.printStackTrace();
            Log.e("Exception", e2 + "");
        }
    }

    private void operation() {
        if (this.et_main.getText().toString().length() != 0) {
            if (this.ah.booleanValue()) {
                this.ah = Boolean.valueOf(false);
            }
            if (!this.et_main.getText().toString().equals("Can't divide by 0") && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '+' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '-' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '/' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '*' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '%') {
                if (this.et_result.length() > 0) {
                    int i;
                    if (this.et_result.getText().charAt(0) == '-') {
                        Log.e("TAG", "if for loop :");
                        i = 1;
                        while (i < this.et_result.length()) {
                            if (this.et_result.getText().charAt(i) == '+' || this.et_result.getText().charAt(i) == '-' || this.et_result.getText().charAt(i) == '*' || this.et_result.getText().charAt(i) == '/') {
                                this.aC = false;
                                break;
                            } else {
                                this.aC = true;
                                i++;
                            }
                        }
                    } else {
                        Log.e("TAG", "else for loop :");
                        i = 0;
                        while (i < this.et_result.length()) {
                            if (this.et_result.getText().charAt(i) == '+' || this.et_result.getText().charAt(i) == '-' || this.et_result.getText().charAt(i) == '*' || this.et_result.getText().charAt(i) == '/') {
                                this.aC = false;
                                break;
                            } else {
                                this.aC = true;
                                i++;
                            }
                        }
                    }
                }
                String obj = this.et_result.getText().toString();
                if (obj.length() > 16) {
                    obj.substring(0, 16);
                } else {
                    this.et_result.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                }
                if (this.aC) {
                    if (this.et_result.getText().charAt(0) != '-' || this.et_result.length() <= 1) {
                        Log.e("TAG", "display : " + this.ab);
                        Log.e("TAG", "display length : " + this.ab.length());
                        this.et_main.setText(this.et_main.getText().toString().substring(0, this.et_main.getText().toString().length() - this.ab.length()) + "-" + this.ab);
                        this.et_result.setText("-" + this.ab);
                        this.ar = Boolean.valueOf(false);
                        this.ab = this.et_result.getText().toString();
                    } else {
                        Log.e("TAG", "oth pos - in display");
                        obj = this.et_result.getText().toString();
                        Log.e("TAG", "s before : " + obj);
                        Log.e("TAG", "s before replacing string : " + this.et_result.getText().toString().substring(1));
                        CharSequence replace = obj.replace(this.et_result.getText().toString(), this.et_result.getText().toString().substring(1));
                        this.et_result.setText(this.et_result.getText().toString().substring(1));
                        if (this.f6025W.equalsIgnoreCase("")) {
                            this.et_main.setText(replace);
                        } else {
                            this.et_main.setText(this.firststr + replace);
                        }
                        this.ab = this.et_result.getText().toString();
                    }
                }
                if (this.f6027Y.booleanValue()) {
                    this.f6025W = this.ab;
                    Log.e("str2", this.f6025W);
                    return;
                }
                this.f6024V = this.ab;
                Log.e("str1", this.f6024V);
            }
        }
    }

    public boolean isTablet(Context context) {
        return ((context.getResources().getConfiguration().screenLayout & 15) == 4) || ((context.getResources().getConfiguration().screenLayout & 15) == 3);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_clear:
                this.f6029n = Boolean.valueOf(false);
                this.et_main.setText("");
                this.et_result.setText("0");
                this.ab = "";
                this.f6025W = "";
                this.f6024V = "";
                this.f6026X = "";
                this.f6033r = Boolean.valueOf(false);
                this.f6031p = Boolean.valueOf(false);
                this.f6032q = Boolean.valueOf(false);
                this.ac = "";
                this.prev = "";
                this.firststr = "";
                this.f6026X = "";
                this.aC = false;
                return;
            case R.id.tv_divide:
                if (!this.et_main.getText().toString().equals("Can't divide by 0")) {
                    if (this.et_main.getText().length() == 0) {
                        Toast.makeText(this, "Select Number First", 0).show();
                        return;
                    }
                    Log.e("divide", this.et_main.getText().toString());
                    if (!this.f6033r.booleanValue()) {
                        this.f6031p = Boolean.valueOf(false);
                        this.f6032q = Boolean.valueOf(false);
                        if (this.ah.booleanValue()) {
                            this.et_main.setText(this.ab);
                            this.ab = this.aa + "";
                            this.et_result.setText(this.ab);
                            this.ah = Boolean.valueOf(false);
                        }
                        this.am = Boolean.valueOf(true);
                        this.f6030o = Boolean.valueOf(false);
                        if (this.av.booleanValue()) {
                            this.ac += "/";
                        }
                        if (this.et_main.length() > 0) {
                            this.et_result.setText(this.ab);
                            if (this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '+' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '-' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '/' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '*' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '%') {
                                this.f6028Z = this.et_main.getText();
                                mid_calculation();
                                if (!this.et_main.getText().toString().equals("Can't divide by 0")) {
                                    this.et_main.append("/");
                                    this.f6029n = Boolean.valueOf(true);
                                    this.f6027Y = Boolean.valueOf(true);
                                    this.f6026X = "/";
                                    if (this.ap.booleanValue()) {
                                        this.ab += "/";
                                    }
                                } else {
                                    return;
                                }
                            } else if (this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) == '+' || this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) == '-' || this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) == '*' || this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) == '/') {
                                this.et_main.setText(this.et_main.getText().toString().substring(0, this.et_main.getText().length() - 1));
                                this.f6028Z = this.et_main.getText();
                                mid_calculation();
                                if (!this.et_main.getText().toString().equals("Can't divide by 0")) {
                                    this.et_main.append("/");
                                    this.f6029n = Boolean.valueOf(true);
                                    this.f6027Y = Boolean.valueOf(true);
                                    this.f6026X = "/";
                                    if (this.ap.booleanValue()) {
                                        this.ab += "/";
                                    }
                                } else {
                                    return;
                                }
                            }
                            this.prev = this.et_main.getText().toString();
                            this.firststr = this.et_main.getText().toString();
                            return;
                        }
                        Toast.makeText(this, "Select Number First", 0).show();
                        return;
                    }
                    return;
                }
                return;
            case R.id.tv_dot:
                if (!this.f6033r.booleanValue()) {
                    try {
                        dot_operation();
                        return;
                    } catch (Exception e) {
                        return;
                    }
                }
                return;
            case R.id.tv_eight:
                this.f6033r = Boolean.valueOf(false);
                if (this.et_main.getText().toString().equalsIgnoreCase("0")) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("");
                }
                if (this.f6032q.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.f6032q = Boolean.valueOf(false);
                }
                if (this.f6031p.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.f6031p = Boolean.valueOf(false);
                }
                if (this.ah.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.ah = Boolean.valueOf(false);
                }
                if (this.aj.booleanValue() || this.ak.booleanValue() || this.am.booleanValue() || this.al.booleanValue() || this.an.booleanValue() || this.ao.booleanValue()) {
                    if (!this.f6030o.booleanValue()) {
                        this.ab = "";
                        this.f6030o = Boolean.valueOf(false);
                    }
                    this.aj = Boolean.valueOf(false);
                    this.ak = Boolean.valueOf(false);
                    this.am = Boolean.valueOf(false);
                    this.al = Boolean.valueOf(false);
                    this.an = Boolean.valueOf(false);
                    this.ao = Boolean.valueOf(false);
                    this.aq = Boolean.valueOf(false);
                }
                if (this.av.booleanValue()) {
                    this.ac += "8";
                }
                if (this.et_result.getText().toString().length() != 16 || this.f6029n.booleanValue()) {
                    this.et_main.append("8");
                    this.ab += "8";
                    this.et_result.setText(this.ab);
                }
                if (this.f6027Y.booleanValue()) {
                    this.f6025W = this.ab;
                    Log.e("str2", this.f6025W);
                    return;
                }
                this.f6024V = this.ab;
                Log.e("str1", this.f6024V);
                return;
            case R.id.tv_equal:


                if (MainApplication.getInstance().getPassword().equals("")) {


                    int cpass = Integer.parseInt(this.et_main.getText().toString());
                    if (cpass == Share.pass) {

                        MainApplication.getInstance().savePassword(String.valueOf(cpass));
                        ProgressDialogSuccess();


                    } else {

                        PasswordErrorDialog();

                    }


                    return;
                }
                if (!this.f6033r.booleanValue()) {
                    if (!(!this.ah.booleanValue() || this.f6026X.equals("") || this.f6025W == null || this.f6025W.equalsIgnoreCase(""))) {
                        Log.e("str2equal", this.f6025W);
                        this.f6028Z = new SpannableStringBuilder(this.et_result.getText().toString() + this.f6026X + this.f6025W);
                        mid_calculation();
                    }
                    if (this.et_main.getText().length() <= 0) {
                        Toast.makeText(this, "Select Number First", 0).show();
                        return;
                    } else if (this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '+' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '-' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '/' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '*' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '%') {
                        this.aj = Boolean.valueOf(false);
                        this.ak = Boolean.valueOf(false);
                        this.am = Boolean.valueOf(false);
                        this.al = Boolean.valueOf(false);
                        this.an = Boolean.valueOf(false);
                        this.ao = Boolean.valueOf(false);
                        this.f6028Z = this.et_main.getText();
                        this.ah = Boolean.valueOf(true);
                        mid_calculation();
                        this.ac = "";
                        return;
                    } else {
                        return;
                    }
                }
                return;
            case R.id.tv_five:
                this.f6033r = Boolean.valueOf(false);
                if (this.et_main.getText().toString().equalsIgnoreCase("0")) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("");
                }
                if (this.f6032q.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.f6032q = Boolean.valueOf(false);
                }
                if (this.f6031p.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.f6031p = Boolean.valueOf(false);
                }
                if (this.ah.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.ah = Boolean.valueOf(false);
                }
                if (this.aj.booleanValue() || this.ak.booleanValue() || this.am.booleanValue() || this.al.booleanValue() || this.an.booleanValue() || this.ao.booleanValue()) {
                    if (!this.f6030o.booleanValue()) {
                        this.ab = "";
                        this.f6030o = Boolean.valueOf(false);
                    }
                    this.aj = Boolean.valueOf(false);
                    this.ak = Boolean.valueOf(false);
                    this.am = Boolean.valueOf(false);
                    this.al = Boolean.valueOf(false);
                    this.an = Boolean.valueOf(false);
                    this.ao = Boolean.valueOf(false);
                    this.aq = Boolean.valueOf(false);
                }
                if (this.av.booleanValue()) {
                    this.ac += "5";
                }
                if (this.et_result.getText().toString().length() != 16 || this.f6029n.booleanValue()) {
                    this.et_main.append("5");
                    this.ab += "5";
                    this.et_result.setText(this.ab);
                    this.f6029n = Boolean.valueOf(false);
                }
                if (this.f6027Y.booleanValue()) {
                    this.f6025W = this.ab;
                    Log.e("str2", this.f6025W);
                    return;
                }
                this.f6024V = this.ab;
                Log.e("str1", this.f6024V);
                return;
            case R.id.tv_four:
                this.f6033r = Boolean.valueOf(false);
                if (this.et_main.getText().toString().equalsIgnoreCase("0")) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("");
                }
                if (this.f6032q.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.f6032q = Boolean.valueOf(false);
                }
                if (this.f6031p.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.f6031p = Boolean.valueOf(false);
                }
                if (this.ah.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.ah = Boolean.valueOf(false);
                }
                if (this.aj.booleanValue() || this.ak.booleanValue() || this.am.booleanValue() || this.al.booleanValue() || this.an.booleanValue() || this.ao.booleanValue()) {
                    if (!this.f6030o.booleanValue()) {
                        this.ab = "";
                        this.f6030o = Boolean.valueOf(false);
                    }
                    this.aj = Boolean.valueOf(false);
                    this.ak = Boolean.valueOf(false);
                    this.am = Boolean.valueOf(false);
                    this.al = Boolean.valueOf(false);
                    this.an = Boolean.valueOf(false);
                    this.ao = Boolean.valueOf(false);
                    this.aq = Boolean.valueOf(false);
                }
                if (this.av.booleanValue()) {
                    this.ac += "4";
                }
                if (this.et_result.getText().toString().length() != 16 || this.f6029n.booleanValue()) {
                    this.et_main.append("4");
                    this.ab += "4";
                    this.et_result.setText(this.ab);
                    this.f6029n = Boolean.valueOf(false);
                }
                if (this.f6027Y.booleanValue()) {
                    this.f6025W = this.ab;
                    Log.e("str2", this.f6025W);
                    return;
                }
                this.f6024V = this.ab;
                Log.e("str1", this.f6024V);
                return;
            case R.id.tv_min:
                if (!this.et_main.getText().toString().equals("Can't divide by 0")) {
                    if (this.et_main.getText().length() == 0) {
                        Toast.makeText(this, "Select Number First", 0).show();
                        return;
                    }
                    Log.e("minus", this.et_main.getText().toString());
                    if (!this.f6033r.booleanValue()) {
                        this.f6027Y = Boolean.valueOf(true);
                        this.f6031p = Boolean.valueOf(false);
                        this.f6032q = Boolean.valueOf(false);
                        if (this.ah.booleanValue()) {
                            this.et_main.setText(this.ab);
                            this.ab = this.aa + "";
                            this.et_result.setText(this.ab);
                            this.ah = Boolean.valueOf(false);
                        }
                        this.ak = Boolean.valueOf(true);
                        this.f6030o = Boolean.valueOf(false);
                        if (this.av.booleanValue()) {
                            this.ac += "-";
                        }
                        if (this.et_main.length() > 0) {
                            this.et_result.setText(this.ab);
                            if (this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '+' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '-' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '/' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '*' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '%') {
                                this.f6028Z = this.et_main.getText();
                                mid_calculation();
                                if (!this.et_main.getText().toString().equals("Can't divide by 0")) {
                                    this.et_main.append("-");
                                    this.f6026X = "-";
                                    this.f6029n = Boolean.valueOf(true);
                                    this.f6027Y = Boolean.valueOf(true);
                                } else {
                                    return;
                                }
                            } else if (this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) == '+' || this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) == '-' || this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) == '*' || this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) == '/') {
                                this.et_main.setText(this.et_main.getText().toString().substring(0, this.et_main.getText().length() - 1));
                                this.f6028Z = this.et_main.getText();
                                mid_calculation();
                                if (!this.et_main.getText().toString().equals("Can't divide by 0")) {
                                    this.et_main.append("-");
                                    this.f6026X = "-";
                                    this.f6029n = Boolean.valueOf(true);
                                    this.f6027Y = Boolean.valueOf(true);
                                } else {
                                    return;
                                }
                            }
                            this.prev = this.et_main.getText().toString();
                            this.firststr = this.et_main.getText().toString();
                            return;
                        }
                        Toast.makeText(this, "Select Number First", 0).show();
                        return;
                    }
                    return;
                }
                return;
            case R.id.tv_mul:
                if (!this.et_main.getText().toString().equals("Can't divide by 0")) {
                    if (this.et_main.getText().length() == 0) {
                        Toast.makeText(this, "Select Number First", 0).show();
                        return;
                    }
                    Log.e("multiply", this.et_main.getText().toString());
                    if (!this.f6033r.booleanValue()) {
                        this.f6031p = Boolean.valueOf(false);
                        this.f6032q = Boolean.valueOf(false);
                        if (this.ah.booleanValue()) {
                            this.et_main.setText(this.ab);
                            this.ab = this.aa + "";
                            this.et_result.setText(this.ab);
                            this.ah = Boolean.valueOf(false);
                        }
                        this.al = Boolean.valueOf(true);
                        this.f6030o = Boolean.valueOf(false);
                        if (this.av.booleanValue()) {
                            this.ac += "*";
                        }
                        if (this.et_main.length() > 0) {
                            this.et_result.setText(this.ab);
                            if (this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '+' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '-' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '/' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '*' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '%') {
                                this.f6028Z = this.et_main.getText();
                                mid_calculation();
                                if (!this.et_main.getText().toString().equals("Can't divide by 0")) {
                                    this.et_main.append("*");
                                    this.f6026X = "*";
                                    this.f6029n = Boolean.valueOf(true);
                                    this.f6027Y = Boolean.valueOf(true);
                                } else {
                                    return;
                                }
                            } else if (this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) == '+' || this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) == '-' || this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) == '*' || this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) == '/') {
                                this.et_main.setText(this.et_main.getText().toString().substring(0, this.et_main.getText().length() - 1));
                                this.f6028Z = this.et_main.getText();
                                mid_calculation();
                                if (!this.et_main.getText().toString().equals("Can't divide by 0")) {
                                    this.et_main.append("*");
                                    this.f6026X = "*";
                                    this.f6029n = Boolean.valueOf(true);
                                    this.f6027Y = Boolean.valueOf(true);
                                } else {
                                    return;
                                }
                            }
                            this.prev = this.et_main.getText().toString();
                            this.firststr = this.et_main.getText().toString();
                            return;
                        }
                        Toast.makeText(this, "Select Number First", 0).show();
                        return;
                    }
                    return;
                }
                return;
            case R.id.tv_nine:
                this.f6033r = Boolean.valueOf(false);
                if (this.et_main.getText().toString().equalsIgnoreCase("0")) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("");
                }
                if (this.f6032q.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.f6032q = Boolean.valueOf(false);
                }
                if (this.f6031p.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.f6031p = Boolean.valueOf(false);
                }
                if (this.ah.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.ah = Boolean.valueOf(false);
                }
                if (this.aj.booleanValue() || this.ak.booleanValue() || this.am.booleanValue() || this.al.booleanValue() || this.an.booleanValue() || this.ao.booleanValue()) {
                    if (!this.f6030o.booleanValue()) {
                        this.ab = "";
                        this.f6030o = Boolean.valueOf(false);
                    }
                    this.aj = Boolean.valueOf(false);
                    this.ak = Boolean.valueOf(false);
                    this.am = Boolean.valueOf(false);
                    this.al = Boolean.valueOf(false);
                    this.an = Boolean.valueOf(false);
                    this.ao = Boolean.valueOf(false);
                    this.aq = Boolean.valueOf(false);
                }
                if (this.av.booleanValue()) {
                    this.ac += "9";
                }
                if (this.et_result.getText().toString().length() != 16 || this.f6029n.booleanValue()) {
                    this.f6029n = Boolean.valueOf(false);
                    this.et_main.append("9");
                    this.ab += "9";
                    this.et_result.setText(this.ab);
                }
                if (this.f6027Y.booleanValue()) {
                    this.f6025W = this.ab;
                    Log.e("str2", this.f6025W);
                    return;
                }
                this.f6024V = this.ab;
                Log.e("str1", this.f6024V);
                return;
            case R.id.tv_one:
                this.f6033r = Boolean.valueOf(false);
                if (this.et_main.getText().toString().equalsIgnoreCase("0")) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("");
                }
                if (this.f6031p.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.f6031p = Boolean.valueOf(false);
                }
                if (this.f6032q.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.f6032q = Boolean.valueOf(false);
                }
                if (this.ah.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.ah = Boolean.valueOf(false);
                }
                if (this.aj.booleanValue() || this.ak.booleanValue() || this.am.booleanValue() || this.al.booleanValue() || this.an.booleanValue() || this.ao.booleanValue()) {
                    if (!this.f6030o.booleanValue()) {
                        this.ab = "";
                        this.f6030o = Boolean.valueOf(false);
                    }
                    this.aj = Boolean.valueOf(false);
                    this.ak = Boolean.valueOf(false);
                    this.am = Boolean.valueOf(false);
                    this.al = Boolean.valueOf(false);
                    this.an = Boolean.valueOf(false);
                    this.ao = Boolean.valueOf(false);
                }
                if (this.av.booleanValue()) {
                    this.ac += "1";
                }
                if (this.et_result.getText().toString().length() != 16 || this.f6029n.booleanValue()) {
                    this.et_main.append("1");
                    this.ab += "1";
                    this.et_result.setText(this.ab);
                    this.f6029n = Boolean.valueOf(false);
                }
                if (this.f6027Y.booleanValue()) {
                    this.f6025W = this.ab;
                    Log.e("str2", this.f6025W);
                    return;
                }
                this.f6024V = this.ab;
                Log.e("str1", this.f6024V);
                return;
            case R.id.tv_percent:
                if (!this.f6033r.booleanValue()) {
                    try {
                        if (this.et_main.length() > 0) {
                            this.az = Boolean.valueOf(true);
                            try {
                                this.aa = Double.valueOf(new ExpressionBuilder(this.et_main.getText().toString() + "/100").build().evaluate());
                                Log.e("result", "" + this.aa);
                            } catch (ArithmeticException e2) {
                            }
                            this.f6026X = "";
                            NumberFormat instance = NumberFormat.getInstance();
                            instance.setMaximumIntegerDigits(20);
                            instance.setMaximumFractionDigits(20);
                            instance.setGroupingUsed(false);
                            String format = instance.format(this.aa);
                            if (format.length() > 16) {
                                format = format.substring(0, 16);
                            } else {
                                this.et_result.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                            }
                            this.et_main.setText(format + "");
                            this.ab = format + "";
                            this.et_result.setText(format + "");
                            this.f6024V = format;
                            this.f6025W = "";
                            this.f6032q = Boolean.valueOf(true);
                            return;
                        }
                        Toast.makeText(this, "Select Number First", 0).show();
                        return;
                    } catch (Exception e3) {
                        Log.e("TAG", "Invalid for percentage" + e3.getMessage());
                        return;
                    }
                }
                return;
            case R.id.tv_plus:
                if (!this.et_main.getText().toString().equals("Can't divide by 0")) {
                    if (this.et_main.getText().length() == 0) {
                        Toast.makeText(this, "Select Number First", 0).show();
                        return;
                    }
                    Log.e("plus", this.et_main.getText().toString());
                    if (!this.f6033r.booleanValue()) {
                        this.f6031p = Boolean.valueOf(false);
                        this.f6032q = Boolean.valueOf(false);
                        if (this.ah.booleanValue()) {
                            this.et_main.setText(this.ab);
                            this.ab = this.aa + "";
                            this.et_result.setText(this.ab);
                            this.ah = Boolean.valueOf(false);
                        }
                        this.aj = Boolean.valueOf(true);
                        this.f6030o = Boolean.valueOf(false);
                        if (this.av.booleanValue()) {
                            this.ac += "+";
                        }
                        if (this.et_main.length() > 0) {
                            this.et_result.setText(this.ab);
                            if (this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '+' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '-' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '/' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '*' && this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '%') {
                                this.f6028Z = this.et_main.getText();
                                Log.e("if", "if");
                                mid_calculation();
                                if (!this.et_main.getText().toString().equals("Can't divide by 0")) {
                                    this.et_main.append("+");
                                    this.f6026X = "+";
                                    this.f6029n = Boolean.valueOf(true);
                                    this.f6027Y = Boolean.valueOf(true);
                                } else {
                                    return;
                                }
                            } else if (this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) == '+' || this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) == '-' || this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) == '*' || this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) == '/') {
                                this.et_main.setText(this.et_main.getText().toString().substring(0, this.et_main.getText().length() - 1));
                                this.f6028Z = this.et_main.getText();
                                Log.e("else", "else");
                                mid_calculation();
                                if (!this.et_main.getText().toString().equals("Can't divide by 0")) {
                                    this.et_main.append("+");
                                    this.f6026X = "+";
                                    this.f6027Y = Boolean.valueOf(true);
                                    if (this.et_result.getText().toString().length() == 16) {
                                        this.f6029n = Boolean.valueOf(true);
                                    }
                                } else {
                                    return;
                                }
                            }
                            this.prev = this.et_main.getText().toString();
                            this.firststr = this.et_main.getText().toString();
                            return;
                        }
                        Toast.makeText(this, "Select Number First", 0).show();
                        return;
                    }
                    return;
                }
                return;
            case R.id.tv_sign:
                if (!this.et_main.getText().toString().equals("0") && !this.et_main.getText().toString().equals("Invalid Input") && !this.et_main.getText().toString().equals("Can't divide by 0")) {
                    try {
                        operation();
                        return;
                    } catch (Exception e4) {
                        return;
                    }
                }
                return;
            case R.id.tv_seven:
                this.f6033r = Boolean.valueOf(false);
                if (this.et_main.getText().toString().equalsIgnoreCase("0")) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("");
                }
                if (this.f6032q.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.f6032q = Boolean.valueOf(false);
                }
                if (this.f6031p.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.f6031p = Boolean.valueOf(false);
                }
                if (this.ah.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.ah = Boolean.valueOf(false);
                }
                if (this.aj.booleanValue() || this.ak.booleanValue() || this.am.booleanValue() || this.al.booleanValue() || this.an.booleanValue() || this.ao.booleanValue()) {
                    if (!this.f6030o.booleanValue()) {
                        this.ab = "";
                        this.f6030o = Boolean.valueOf(false);
                    }
                    this.aj = Boolean.valueOf(false);
                    this.ak = Boolean.valueOf(false);
                    this.am = Boolean.valueOf(false);
                    this.al = Boolean.valueOf(false);
                    this.an = Boolean.valueOf(false);
                    this.ao = Boolean.valueOf(false);
                    this.aq = Boolean.valueOf(false);
                }
                if (this.av.booleanValue()) {
                    this.ac += "7";
                }
                if (this.et_result.getText().toString().length() != 16 || this.f6029n.booleanValue()) {
                    this.f6029n = Boolean.valueOf(false);
                    this.et_main.append("7");
                    this.ab += "7";
                    this.et_result.setText(this.ab);
                }
                if (this.f6027Y.booleanValue()) {
                    this.f6025W = this.ab;
                    Log.e("str2", this.f6025W);
                    return;
                }
                this.f6024V = this.ab;
                Log.e("str1", this.f6024V);
                return;
            case R.id.tv_six:
                this.f6033r = Boolean.valueOf(false);
                if (this.et_main.getText().toString().equalsIgnoreCase("0")) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("");
                }
                if (this.f6032q.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.f6032q = Boolean.valueOf(false);
                }
                if (this.f6031p.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.f6031p = Boolean.valueOf(false);
                }
                if (this.ah.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.ah = Boolean.valueOf(false);
                }
                if (this.aj.booleanValue() || this.ak.booleanValue() || this.am.booleanValue() || this.al.booleanValue() || this.an.booleanValue() || this.ao.booleanValue()) {
                    if (!this.f6030o.booleanValue()) {
                        this.ab = "";
                        this.f6030o = Boolean.valueOf(false);
                    }
                    this.aj = Boolean.valueOf(false);
                    this.ak = Boolean.valueOf(false);
                    this.am = Boolean.valueOf(false);
                    this.al = Boolean.valueOf(false);
                    this.an = Boolean.valueOf(false);
                    this.ao = Boolean.valueOf(false);
                    this.aq = Boolean.valueOf(false);
                }
                if (this.av.booleanValue()) {
                    this.ac += "6";
                }
                if (this.et_result.getText().toString().length() != 16 || this.f6029n.booleanValue()) {
                    this.et_main.append("6");
                    this.ab += "6";
                    this.et_result.setText(this.ab);
                    this.f6029n = Boolean.valueOf(false);
                }
                if (this.f6027Y.booleanValue()) {
                    this.f6025W = this.ab;
                    Log.e("str2", this.f6025W);
                    return;
                }
                this.f6024V = this.ab;
                Log.e("str1", this.f6024V);
                return;
            case R.id.tv_sqrt:
                if (!this.f6033r.booleanValue()) {
                    if (this.et_main.length() > 0) {
                        this.f6026X = "";
                        sqrlEquals();
                        return;
                    }
                    Toast.makeText(this, "Select Number First", 0).show();
                    return;
                }
                return;
//            case R.id.iv_square_root:
//                if (!this.f6033r.booleanValue()) {
//                    if (this.ae == 0) {
//                        share_calc.flag_expand = Boolean.valueOf(true);
//                        this.ae = 1;
//                        return;
//                    }
//                    share_calc.flag_expand = Boolean.valueOf(false);
//                    this.ae = 0;
//                    return;
//                }
//                return;
            case R.id.tv_three:
                this.f6033r = Boolean.valueOf(false);
                if (this.et_main.getText().toString().equalsIgnoreCase("0")) {
                    this.et_main.setText("");
                    this.et_result.setText("");
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                }
                if (this.f6032q.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.f6032q = Boolean.valueOf(false);
                }
                if (this.f6031p.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.f6031p = Boolean.valueOf(false);
                }
                if (this.ah.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.ah = Boolean.valueOf(false);
                }
                if (this.aj.booleanValue() || this.ak.booleanValue() || this.am.booleanValue() || this.al.booleanValue() || this.an.booleanValue() || this.ao.booleanValue()) {
                    if (!this.f6030o.booleanValue()) {
                        this.ab = "";
                        this.f6030o = Boolean.valueOf(false);
                    }
                    this.aj = Boolean.valueOf(false);
                    this.ak = Boolean.valueOf(false);
                    this.am = Boolean.valueOf(false);
                    this.al = Boolean.valueOf(false);
                    this.an = Boolean.valueOf(false);
                    this.ao = Boolean.valueOf(false);
                    this.aq = Boolean.valueOf(false);
                }
                if (this.av.booleanValue()) {
                    this.ac += "3";
                }
                if (this.et_result.getText().toString().length() != 16 || this.f6029n.booleanValue()) {
                    this.et_main.append("3");
                    this.ab += "3";
                    this.et_result.setText(this.ab);
                    this.f6029n = Boolean.valueOf(false);
                }
                if (this.f6027Y.booleanValue()) {
                    this.f6025W = this.ab;
                    Log.e("str2", this.f6025W);
                    return;
                }
                this.f6024V = this.ab;
                Log.e("str1", this.f6024V);
                return;
            case R.id.tv_two:
                this.f6033r = Boolean.valueOf(false);
                if (this.et_main.getText().toString().equalsIgnoreCase("0")) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("");
                }
                if (this.f6032q.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.f6032q = Boolean.valueOf(false);
                }
                if (this.f6031p.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.f6031p = Boolean.valueOf(false);
                }
                if (this.ah.booleanValue()) {
                    this.et_main.setText("");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.et_result.setText("0");
                    this.ah = Boolean.valueOf(false);
                }
                if (this.aj.booleanValue() || this.ak.booleanValue() || this.am.booleanValue() || this.al.booleanValue() || this.an.booleanValue() || this.ao.booleanValue()) {
                    if (!this.f6030o.booleanValue()) {
                        this.ab = "";
                        this.f6030o = Boolean.valueOf(false);
                    }
                    this.aj = Boolean.valueOf(false);
                    this.ak = Boolean.valueOf(false);
                    this.am = Boolean.valueOf(false);
                    this.al = Boolean.valueOf(false);
                    this.an = Boolean.valueOf(false);
                    this.ao = Boolean.valueOf(false);
                }
                if (this.av.booleanValue()) {
                    this.ac += "2";
                }
                if (this.et_result.getText().toString().length() != 16 || this.f6029n.booleanValue()) {
                    this.et_main.append("2");
                    this.ab += "2";
                    this.et_result.setText(this.ab);
                    this.f6029n = Boolean.valueOf(false);
                }
                if (this.f6027Y.booleanValue()) {
                    this.f6025W = this.ab;
                    Log.e("str2", this.f6025W);
                    return;
                }
                this.f6024V = this.ab;
                Log.e("str1", this.f6024V);
                return;
            /*case R.id.iv_x_exclamation:
                if (!this.f6033r.booleanValue() && this.et_main.length() != 0) {
                    String str;
                    Boolean valueOf;
                    this.az = Boolean.valueOf(true);
                    Boolean.valueOf(false);
                    Log.e("string", this.ac);
                    if (this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) == ')') {
                        str = this.ac;
                        valueOf = Boolean.valueOf(true);
                    } else {
                        str = this.et_main.getText().toString();
                        valueOf = Boolean.valueOf(false);
                    }
                    String str2 = "";
                    try {
                        CharSequence charSequence;
                        CalculateFactorial calculateFactorial = new CalculateFactorial();
                        int[] factorial = calculateFactorial.factorial((int) Double.parseDouble(String.valueOf(new ExtendedDoubleEvaluator().evaluate(str))));
                        int res = calculateFactorial.getRes();
                        if (res > 20) {
                            for (int i = res - 1; i >= res - 20; i--) {
                                if (i == res - 2) {
                                    str2 = str2 + ".";
                                }
                                str2 = str2 + factorial[i];
                            }
                            charSequence = str2 + "E" + (res - 1);
                        } else {
                            charSequence = str2;
                            int i2 = res - 1;
                            while (i2 >= 0) {
                                *//*String str3 = charSequence + factorial[i2];
                                i2--;
                                Object obj = str3;*//*
                            }
                        }
                        if (valueOf.booleanValue()) {
                            CharSequence d = ((Double) new ExtendedDoubleEvaluator().evaluate(this.et_main.getText().toString().replace(this.ac, charSequence))).toString();
                            this.et_main.setText(d);
                            this.et_result.setText(d);
                            this.ac = "";
                            this.aw = Boolean.valueOf(false);
                            return;
                        }
                        this.et_main.setText(charSequence);
                        this.et_result.setText(charSequence);
                        return;
                    } catch (Exception e32) {
                        if (e32.toString().contains("ArrayIndexOutOfBoundsException")) {
                            this.et_main.setText("Result too big!");
                        } else {
                            this.et_main.setText("Invalid!!");
                        }
                        e32.printStackTrace();
                        return;
                    }
                }
                return;*/
            case R.id.tv_zero:
                if (!this.et_main.getText().toString().equalsIgnoreCase("0")) {
                    if (this.f6031p.booleanValue()) {
                        this.et_main.setText("");
                        this.ab = "";
                        this.f6025W = "";
                        this.f6024V = "";
                        this.f6026X = "";
                        this.et_result.setText("0");
                        this.f6031p = Boolean.valueOf(false);
                    }
                    if (this.f6032q.booleanValue()) {
                        this.et_main.setText("");
                        this.ab = "";
                        this.f6025W = "";
                        this.f6024V = "";
                        this.f6026X = "";
                        this.et_result.setText("0");
                        this.f6032q = Boolean.valueOf(false);
                    }
                    this.f6033r = Boolean.valueOf(false);
                    if (this.ah.booleanValue()) {
                        this.et_main.setText("");
                        this.f6025W = "";
                        this.f6024V = "";
                        this.f6026X = "";
                        this.ab = "";
                        this.et_result.setText("0");
                        this.ah = Boolean.valueOf(false);
                    }
                    if (this.aj.booleanValue() || this.ak.booleanValue() || this.am.booleanValue() || this.al.booleanValue() || this.an.booleanValue() || this.ao.booleanValue()) {
                        if (!this.f6030o.booleanValue()) {
                            this.ab = "";
                            this.f6030o = Boolean.valueOf(false);
                        }
                        this.aj = Boolean.valueOf(false);
                        this.ak = Boolean.valueOf(false);
                        this.am = Boolean.valueOf(false);
                        this.al = Boolean.valueOf(false);
                        this.an = Boolean.valueOf(false);
                        this.ao = Boolean.valueOf(false);
                    }
                    if (this.av.booleanValue()) {
                        this.ac += "0";
                    }
                    if (this.et_result.getText().toString().length() != 16 || this.f6029n.booleanValue()) {
                        if (this.et_result.getText().length() != 1 || !this.et_result.getText().toString().equalsIgnoreCase("0") || !this.et_main.getText().toString().equalsIgnoreCase("0")) {
                            this.et_main.append("0");
                            this.ab += "0";
                            this.et_result.setText(this.ab);
                            this.f6029n = Boolean.valueOf(false);
                        } else {
                            return;
                        }
                    }
                    if (this.f6027Y.booleanValue()) {
                        this.f6025W = this.ab;
                        Log.e("str2", this.f6025W);
                        return;
                    }
                    this.f6024V = this.ab;
                    Log.e("str2", this.f6024V);
                    return;
                }
                return;
            /*case R.id.ll_delete:
                this.f6033r = Boolean.valueOf(false);
                int length = this.et_main.getText().length();
                if (this.et_main.getText().toString().equals("Can't divide by 0")) {
                    this.f6029n = Boolean.valueOf(false);
                    this.et_main.setText("");
                    this.et_result.setText("0");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.f6033r = Boolean.valueOf(false);
                    this.f6031p = Boolean.valueOf(false);
                    this.f6032q = Boolean.valueOf(false);
                    this.ac = "";
                    this.prev = "";
                    this.firststr = "";
                    this.f6026X = "";
                    this.aC = false;
                    return;
                } else if (this.et_main.getText().toString().equals("Invalid Input")) {
                    this.f6029n = Boolean.valueOf(false);
                    this.et_main.setText("");
                    this.et_result.setText("0");
                    this.ab = "";
                    this.f6025W = "";
                    this.f6024V = "";
                    this.f6026X = "";
                    this.f6033r = Boolean.valueOf(false);
                    this.f6031p = Boolean.valueOf(false);
                    this.f6032q = Boolean.valueOf(false);
                    this.ac = "";
                    this.prev = "";
                    this.firststr = "";
                    this.f6026X = "";
                    this.aC = false;
                    return;
                } else {
                    if (length > 0) {
                        if (Character.isDigit(this.et_main.getText().charAt(length - 1)) || this.et_main.getText().toString().substring(length - 1).equalsIgnoreCase(".") || !Character.isDigit(this.et_main.getText().charAt(length - 2))) {
                            if (this.et_main.getText().charAt(length - 1) == '+' || this.et_main.getText().charAt(length - 1) == '-' || this.et_main.getText().charAt(length - 1) == '*' || this.et_main.getText().charAt(length - 1) == '/') {
                                Log.e("TAG", "here for back operator called : " + this.et_main.getText().charAt(length - 1));
                                this.firststr = "";
                            } else {
                                this.firststr = "";
                            }
                            this.et_main.getText().delete(length - 1, length);
                            if (!(this.et_main.getText().toString().equalsIgnoreCase("") || this.et_main.getText().toString().charAt(this.et_main.getText().toString().length() - 1) != '-' || Character.isDigit(this.et_main.getText().length() - 1))) {
                                this.et_main.setText(this.et_main.getText().toString().substring(0, this.et_main.getText().toString().length() - 1));
                            }
                            if (this.ah.booleanValue()) {
                                this.ab = this.et_main.getText().toString();
                            } else if (this.f6025W != null) {
                                if (this.f6025W.equalsIgnoreCase("")) {
                                    this.ab = this.et_main.getText().toString();
                                } else {
                                    this.f6025W = this.f6025W.substring(0, this.f6025W.length() - 1);
                                    if (this.f6025W.equalsIgnoreCase("-")) {
                                        this.f6025W = "";
                                    }
                                    this.ab = this.f6025W;
                                }
                            }
                            this.et_result.setText(this.ab);
                            if (this.et_main.getText().toString().length() == 1 && !Character.isDigit(this.et_main.getText().toString().charAt(0))) {
                                this.et_result.setText("0");
                                this.et_main.setText("");
                                this.ab = "";
                            }
                        } else {
                            this.f6030o = Boolean.valueOf(true);
                            this.f6027Y = Boolean.valueOf(false);
                            this.et_main.getText().delete(length - 1, length);
                            this.ab = this.f6024V;
                            this.et_result.setText(this.ab);
                            Log.e("idis2", this.ab);
                            this.f6025W = "";
                        }
                    }
                    if (this.et_main.getText().length() == 0) {
                        this.et_main.setText("");
                        this.et_result.setText("0");
                        this.ab = "";
                    }
                    if (this.et_main.getText().length() == 0) {
                        this.f6029n = Boolean.valueOf(false);
                        this.et_main.setText("");
                        this.et_result.setText("0");
                        this.ab = "";
                        this.f6025W = "";
                        this.f6024V = "";
                        this.f6026X = "";
                        this.f6033r = Boolean.valueOf(false);
                        this.f6031p = Boolean.valueOf(false);
                        this.f6032q = Boolean.valueOf(false);
                        this.ac = "";
                        this.prev = "";
                        this.firststr = "";
                        this.f6026X = "";
                        this.aC = false;
                    }
                    this.aC = false;
                    return;
                }*/
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

    public void sqrlEquals() {
        this.f6031p = Boolean.valueOf(true);
        String obj = this.et_main.getText().toString();
        if (!Character.isDigit(obj.charAt(obj.length() - 1))) {
            obj = obj.substring(0, obj.length() - 1);
        }
        Log.e("first", "" + obj.toString().charAt(0));
        if (Character.isDigit(obj.toString().charAt(0))) {
            Double valueOf = Double.valueOf(Double.parseDouble(new DecimalFormat(".########################################################").format((Double) new ExtendedDoubleEvaluator().evaluate(String.valueOf(obj)))));
            Log.e("add", "" + valueOf);
            if (this.et_result.length() != 0) {
                this.expressions = "sqrt(" + valueOf + ")";
            }
            if (this.expressions.length() == 0) {
                //this.expressions = IdManager.DEFAULT_VERSION_NAME;
            }
            DoubleEvaluator doubleEvaluator = new DoubleEvaluator();
            try {
                this.result = (Double) new ExtendedDoubleEvaluator().evaluate(this.expressions);
                NumberFormat instance = NumberFormat.getInstance();
                instance.setMaximumIntegerDigits(50);
                instance.setMaximumFractionDigits(50);
                instance.setGroupingUsed(false);
                obj = instance.format(this.result);
                if (obj.equalsIgnoreCase("nan")) {
                    this.et_result.setText("0");
                    this.et_main.setText("Invalid Input");
                    this.f6033r = Boolean.valueOf(true);
                } else {
                    if (obj.length() > 16) {
                        obj = obj.substring(0, 16);
                    } else {
                        this.et_result.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                    }
                    this.et_result.setText(obj + "");
                    this.et_main.setText("" + obj);
                    this.ab = obj + "";
                }
                this.f6024V = this.et_result.getText().toString();
                this.f6025W = "";
                return;
            } catch (Exception e) {
                Log.e("TAG", "Toast invalid expression");
                Toast.makeText(this, "Invalid Expression", 0).show();
                this.et_main.setText("");
                this.et_result.setText("0");
                this.ab = "";
                this.ac = "";
                this.prev = "";
                this.firststr = "";
                this.aC = false;
                this.expressions = "";
                e.printStackTrace();
                return;
            }
        }
        this.et_result.setText("0");
        this.et_main.setText("Invalid Input");
        this.f6033r = Boolean.valueOf(true);
    }

    private void showConfirmDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_confirm_set_password);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setLayout(-1, -2);
        }
        TextView txt = (TextView) dialog.findViewById(R.id.txt);

        TextView btnOk = (TextView) dialog.findViewById(R.id.btn_ok);
        ((ImageView) dialog.findViewById(R.id.img_close)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                dialog.dismiss();

            }
        });
        dialog.show();
    }

    public void ProgressDialogSuccess() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_successfully_set_password);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setLayout(-1, -2);
        }
        TextView btnCancel = (TextView) dialog.findViewById(R.id.btn_cancel);
        TextView btnOk = (TextView) dialog.findViewById(R.id.btn_ok);
        ((ImageView) dialog.findViewById(R.id.img_close)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();

                String secretQuestion = MainApplication.getInstance().getSecurityQuestion();
                String email = MainApplication.getInstance().getEmail();
                if (TextUtils.isEmpty(secretQuestion)) {
                    startActivity(new Intent(ConfirmCalcActivity.this, SecurityQuestionActivity.class).putExtra(SecurityQuestionActivity.TYPE, SecurityQuestionActivity.ADD));
                    finish();
                    return;
                }
                startActivity(new Intent(ConfirmCalcActivity.this, HomeActivity.class));
                finish();
            }
        });
        dialog.show();
    }

    public void PasswordErrorDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_set_password_failed);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setLayout(-1, -2);
        }
        TextView btnCancel = (TextView) dialog.findViewById(R.id.btn_cancel);
        TextView btnOk = (TextView) dialog.findViewById(R.id.btn_ok);
        ((ImageView) dialog.findViewById(R.id.img_close)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }

}
