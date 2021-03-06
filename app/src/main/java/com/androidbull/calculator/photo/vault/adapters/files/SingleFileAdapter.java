package com.androidbull.calculator.photo.vault.adapters.files;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.androidbull.calculator.photo.R;
import com.androidbull.calculator.photo.vault.activities.files.AddFileActivity;

import java.io.File;

public class SingleFileAdapter extends ArrayAdapter<String> {

    private static final String TAG = "SingleFileAdapter";
    String ParentFolder;
    private final Activity context;
    private final String[] web;

    public SingleFileAdapter(Activity context, String[] web, String path) {
        super(context, R.layout.list_single, web);
        this.context = context;
        this.web = web;
        this.ParentFolder = path;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        View rowView = this.context.getLayoutInflater().inflate(R.layout.list_single, null, true);
        TextView txtTitle = rowView.findViewById(R.id.txt);
        TextView txtFileType = rowView.findViewById(R.id.tv_file_type);
        final CheckBox chk = rowView.findViewById(R.id.myCheckBox);
        chk.setOnCheckedChangeListener((compoundButton, b) -> {
            if (chk.isChecked()) {
                ((AddFileActivity) SingleFileAdapter.this.context).setFiles(true, SingleFileAdapter.this.web[position]);
            } else {
                ((AddFileActivity) SingleFileAdapter.this.context).setFiles(false, SingleFileAdapter.this.web[position]);
            }
        });
        txtTitle.setText(this.web[position]);
        File file = new File(this.ParentFolder + "/" + this.web[position]);
        String fileNameWithExtension = file.getName();
        Log.i(TAG, "getView: name: " + file.getName());
        Log.i(TAG, "getView: getAbsolute: " + file.getAbsolutePath());
        Log.i(TAG, "getView: getPath: " + file.getPath());

        String extension = fileNameWithExtension.substring(fileNameWithExtension.lastIndexOf(".") + 1);
        txtFileType.setText(extension.toUpperCase());

//        Picasso.with(this.context).load(new File(this.ParentFolder + "/" + this.web[position])).placeholder((int) R.drawable.document).resize(50, 50).into(imageView);
        return rowView;
    }

}
