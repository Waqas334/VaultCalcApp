package com.androidbull.calculator.photo.vault.callbacks;

import com.androidbull.calculator.photo.vault.model.AllImagesModel;

import java.util.ArrayList;

public interface OnAllImagesLoadedListener {
    void onAllImagesLoaded(ArrayList<AllImagesModel> arrayList);
}
