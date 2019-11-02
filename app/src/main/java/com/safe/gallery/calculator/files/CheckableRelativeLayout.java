package com.safe.gallery.calculator.files;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class CheckableRelativeLayout extends RelativeLayout implements Checkable {

    private List<Checkable> checkableList;
    private boolean isChecked;

    public CheckableRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialise(attrs);
    }

    public CheckableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise(attrs);
    }

    public CheckableRelativeLayout(Context context, int checkableId) {
        super(context);
        initialise(null);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        isChecked = isChecked;
        for (Checkable c : checkableList) {
            c.setChecked(isChecked);
        }
    }

    public void toggle() {
        isChecked = !isChecked;
        for (Checkable c : checkableList) {
            c.toggle();
        }
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            findCheckableChildren(getChildAt(i));
        }
    }

    private void initialise(AttributeSet attrs) {
        isChecked = false;
        checkableList = new ArrayList(5);
    }

    private void findCheckableChildren(View v) {
        if (v instanceof Checkable) {
            checkableList.add((Checkable) v);
        }
        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            int childCount = vg.getChildCount();
            for (int i = 0; i < childCount; i++) {
                findCheckableChildren(vg.getChildAt(i));
            }
        }
    }
}
