package com.safe.gallery.calculator.activities;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.core.internal.view.SupportMenu;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.app.BaseActivity;
import com.safe.gallery.calculator.app.MainApplication;

import butterknife.ButterKnife;

public class ChangePasswordActivity extends BaseActivity {

    String Code = "";
    String ConfPassword = "";
    LinearLayout LLMain;

    EditText PassTextString;
    EditText Passtxt1;

    EditText Passtxt2;

    EditText Passtxt3;

    EditText Passtxt4;
    String Password = "";

    EditText edtFormula;
    private boolean hasChanged = false;

    ImageView imgEquals;
    int mSelectedColor;

    LinearLayout lnMore;
    private double num = 0.0d;
    private int operator = 1;
    private boolean readyToClear = false;

    EditText txtCalc;

    TextView txtDel;

    ImageView txtDevide;
    TextView txtDot;
    TextView txtEight;
    TextView txtFive;
    TextView txtFour;
    ImageView txtMinus;
    ImageView txtMultiply;
    TextView txtNine;
    TextView txtOne;
    TextView txtPersent;

    ImageView txtPlus;

    TextView txtPlusMinus;

    TextView txtSeven;

    TextView txtSix;

    TextView txtThree;

    TextView txtTwo;

    TextView txtZero;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_change_password);

        ButterKnife.bind((Activity) this);

        findViews();
        ButtonControls();
        reset();
    }

    private void findViews() {

        LLMain=findViewById(R.id.LLMain);
        PassTextString=findViewById(R.id.PassTextString);
        Passtxt1=findViewById(R.id.Passtxt1);
        Passtxt2=findViewById(R.id.Passtxt2);
        Passtxt3=findViewById(R.id.Passtxt3);
        Passtxt4=findViewById(R.id.Passtxt4);
        edtFormula=findViewById(R.id.formula);
        imgEquals=findViewById(R.id.img_equals);
        lnMore=findViewById(R.id.more);
        txtCalc=findViewById(R.id.result);
        txtDel=findViewById(R.id.txt_del);
        txtDevide=findViewById(R.id.txtDevide);
        txtDot=findViewById(R.id.txt_dot);
        txtEight=findViewById(R.id.txt_eight);
        txtFive=findViewById(R.id.txt_five);
        txtFour=findViewById(R.id.txt_four);
        txtMinus=findViewById(R.id.txtMinus);
        txtMultiply=findViewById(R.id.txtMultiply);
        txtNine=findViewById(R.id.txt_nine);
        txtOne=findViewById(R.id.txt_one);
        txtPersent=findViewById(R.id.txt_persent);
        txtPlus=findViewById(R.id.txtPlus);
        txtPlusMinus=findViewById(R.id.txt_plus_minus);
        txtSeven=findViewById(R.id.txt_seven);
        txtSix=findViewById(R.id.txt_six);
        txtThree=findViewById(R.id.txt_three);
        txtTwo=findViewById(R.id.txt_two);
        txtZero=findViewById(R.id.txt_zero);

    }

    public void ButtonControls() {

        handleEventsForControl();
        if (MainApplication.getInstance().getPassword().equals("") || MainApplication.getInstance().getPassword().equals(" ")) {
            LLMain.setVisibility(0);
            PassTextString.setVisibility(0);
            txtCalc.setVisibility(4);
            PassTextString.setText("Please Enter your current password.");
            return;
        }
        LLMain.setVisibility(0);
        PassTextString.setVisibility(0);
        txtCalc.setVisibility(4);
        PassTextString.setText("Please Enter your current password.");
    }

    public void handleEventsForControl() {

        txtZero.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                handleNumber(0);
            }
        });
        txtOne.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                handleNumber(1);
            }
        });
        txtTwo.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                handleNumber(2);
            }
        });
        txtThree.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                handleNumber(3);
            }
        });
        txtFour.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                handleNumber(4);
            }
        });
        txtFive.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                handleNumber(5);
            }
        });
        txtSix.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                handleNumber(6);
            }
        });
        txtSeven.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                handleNumber(7);
            }
        });
        txtEight.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                handleNumber(8);
            }
        });
        imgEquals.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                handleEquals(0);
            }
        });
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
        ((ImageView) dialog.findViewById(R.id.img_close)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                Password = "";
                ConfPassword = "";
            }
        });
        btnOk.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                PassTextString.setText("Please Enter your New Password.");
                Code = "";
                Password = "";
                ConfPassword = "";
                Passtxt1.setText("");
                Passtxt2.setText("");
                Passtxt3.setText("");
                Passtxt4.setText("");
            }
        });
        dialog.show();
    }

    public void CurrentPasswordErrorDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_current_pass_wrong);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setLayout(-1, -2);
        }
        TextView btnCancel = (TextView) dialog.findViewById(R.id.btn_cancel);
        TextView btnOk = (TextView) dialog.findViewById(R.id.btn_ok);
        ((ImageView) dialog.findViewById(R.id.img_close)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                Password = "";
                ConfPassword = "";
            }
        });
        btnOk.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                Code = "";
                Password = "";
                ConfPassword = "";
                Passtxt1.setText("");
                Passtxt2.setText("");
                Passtxt3.setText("");
                Passtxt4.setText("");
            }
        });
        dialog.show();
    }

    public void showConfirmDialog(final String password) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_confirm_set_password);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setLayout(-1, -2);
        }
        TextView txt = (TextView) dialog.findViewById(R.id.txt);
        //TextView btnCancel = (TextView) dialog.findViewById(R.id.btn_cancel);
        TextView btnOk = (TextView) dialog.findViewById(R.id.btn_ok);
        SpannableString code = new SpannableString(Code);
        code.setSpan(new ForegroundColorSpan(SupportMenu.CATEGORY_MASK), 0, code.length(), 0);
        txt.setText("You have enter the following numbers as your Passcode.\n" + code);
        btnOk.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                PassTextString.setText("Please Enter your Confirm Password.");
                Password = password;
                Code = "";
                Passtxt1.setText("");
                Passtxt2.setText("");
                Passtxt3.setText("");
                Passtxt4.setText("");
                reset();
            }
        });
        dialog.show();
    }

    public void samePassNotAllowedDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_choose_other_pass);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setLayout(-1, -2);
        }
        TextView txt = (TextView) dialog.findViewById(R.id.txt);
        TextView btnCancel = (TextView) dialog.findViewById(R.id.btn_cancel);
        TextView btnOk = (TextView) dialog.findViewById(R.id.btn_ok);
        ((ImageView) dialog.findViewById(R.id.img_close)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                reset();
                Code = "";
                Passtxt1.setText("");
                Passtxt2.setText("");
                Passtxt3.setText("");
                Passtxt4.setText("");
            }
        });
        btnOk.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                Code = "";
                Passtxt1.setText("");
                Passtxt2.setText("");
                Passtxt3.setText("");
                Passtxt4.setText("");
            }
        });
        dialog.show();
    }

    private void handleEquals(int newOperator) {
        String txt1 = Code;
        txtCalc.setText(txt1);
        if (txt1.length() >= 4 && txt1.length() <= 12) {
            if (Password.equals("")) {
                showConfirmDialog(txt1);
            } else if (Password.equals(txt1)) {
                Password = txt1;
                MainApplication.getInstance().savePassword(Password);
                ProgressChangeSuccess();
            } else {
                PasswordErrorDialog();
            }
        }
        operator = newOperator;
    }

    public void ProgressChangeSuccess() {

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
        ((ImageView) dialog.findViewById(R.id.img_close)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                reset();
                finish();
            }
        });
        btnOk.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                reset();
                finish();
            }
        });
        dialog.show();
    }

    private void handleNumber(int num) {
        EnterText("" + num);
        hasChanged = true;
    }

    public void EnterText(String Value) {
        if (Code.length() == 4) {
            return;
        }
        if (Passtxt1.getText().toString().equals(" ") || Passtxt1.getText().toString().equals("")) {
            Passtxt1.setText(Value);
            Code += Value;
        } else if (Passtxt2.getText().toString().equals(" ") || Passtxt2.getText().toString().equals("")) {
            Passtxt2.setText(Value);
            Code += Value;
        } else if (Passtxt3.getText().toString().equals(" ") || Passtxt3.getText().toString().equals("")) {
            Passtxt3.setText(Value);
            Code += Value;
        } else if (Passtxt4.getText().toString().equals(" ") || Passtxt4.getText().toString().equals("")) {
            Passtxt4.setText(Value);
            Code += Value;
            if (MainApplication.getInstance().getPassword().equals(Code)) {
                if (PassTextString.getText().toString().equals("Please Enter your current password.")) {
                    Code = "";
                    Password = "";
                    ConfPassword = "";
                    Passtxt1.setText("");
                    Passtxt2.setText("");
                    Passtxt3.setText("");
                    Passtxt4.setText("");
                    PassTextString.setText("Please Enter your New Password.");
                } else if (PassTextString.getText().toString().equals("Please Enter your New Password.")) {
                    if (MainApplication.getInstance().getPassword().equals(Code)) {
                        samePassNotAllowedDialog();
                    } else {
                        showConfirmDialog(Code);
                    }
                } else if (!PassTextString.getText().toString().equals("Please Enter your Confirm Password.")) {
                } else {
                    if (Password.equals(Code)) {
                        Password = Code;
                        MainApplication.getInstance().savePassword(Password);
                        ProgressChangeSuccess();
                        return;
                    }
                    PasswordErrorDialog();
                }
            } else if (PassTextString.getText().toString().equals("Please Enter your current password.")) {
                CurrentPasswordErrorDialog();
            }
        }
    }

    private void reset() {
        num = 0.0d;
        txtCalc.setText("0");
        edtFormula.setText("");
        txtCalc.setSelection(1);
        operator = 1;
    }
}
