package com.androidbull.calculator.photo.vault.callbacks;

import com.androidbull.calculator.photo.vault.model.AllFilesModel;

import java.util.ArrayList;

public interface OnFilesLoadedListener {
    void onFilesLoaded(ArrayList<AllFilesModel> arrayList);
}
