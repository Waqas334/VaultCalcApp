package com.safe.gallery.calculator.callbacks;

import com.safe.gallery.calculator.model.AllVideosModel;

import java.util.ArrayList;

public interface OnVideosLoadedListener {
    void onVideosLoaded(ArrayList<AllVideosModel> arrayList);
}
