package com.safe.gallery.calculator.utils;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.TextView;

import com.safe.gallery.calculator.R;

public class CenterTitleToolbar extends Toolbar {
    private TextView titleView;

    public CenterTitleToolbar(Context context) {
        this(context, null);
    }

    public CenterTitleToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.toolbarStyle);
    }

    public CenterTitleToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.titleView = new TextView(getContext());
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, new int[]{R.attr.titleTextAppearance}, defStyleAttr, 0);
        try {
            int textAppearanceStyleResId = a.getResourceId(0, 0);
            if (textAppearanceStyleResId > 0) {
                this.titleView.setTextAppearance(context, textAppearanceStyleResId);
            }
            addView(this.titleView, new LayoutParams(-2, -2));
        } finally {
            a.recycle();
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.titleView.setX((float) ((getWidth() - this.titleView.getWidth()) / 2));
    }

    public void setTitle(CharSequence title) {
        this.titleView.setText(title);
    }
}
