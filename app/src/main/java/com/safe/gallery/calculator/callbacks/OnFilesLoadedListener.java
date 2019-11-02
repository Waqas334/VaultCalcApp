package com.safe.gallery.calculator.callbacks;

import com.safe.gallery.calculator.model.AllFilesModel;

import java.util.ArrayList;

public interface OnFilesLoadedListener {
    void onFilesLoaded(ArrayList<AllFilesModel> arrayList);
}
