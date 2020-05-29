package com.androidbull.calculator.photo.vault.callbacks;

import com.androidbull.calculator.photo.vault.model.AllVideosModel;

import java.util.ArrayList;

public interface OnVideosLoadedListener {
    void onVideosLoaded(ArrayList<AllVideosModel> arrayList);
}
