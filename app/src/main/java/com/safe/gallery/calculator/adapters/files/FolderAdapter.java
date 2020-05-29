package com.safe.gallery.calculator.adapters.files;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.safe.gallery.calculator.R;

import java.io.File;

public class FolderAdapter extends ArrayAdapter<String> {
    private static final String TAG = "FolderAdapter";
    String ParentFolder;
    private final Activity context;
    private final String[] web;

    public FolderAdapter(Activity context, String[] web, String path) {
        super(context, R.layout.list_single_only, web);
        this.context = context;
        this.web = web;
        this.ParentFolder = path;
    }

    public View getView(int position, View view, ViewGroup parent) {
        View rowView = this.context.getLayoutInflater().inflate(R.layout.list_single_only, null, true);
        ImageView imageView = rowView.findViewById(R.id.img);
        ((TextView) rowView.findViewById(R.id.txt)).setText(this.web[position]);
        File file = new File(this.ParentFolder + "/" + this.web[position]);
        Log.i(TAG, "getView: name: " + file.getName());
        Log.i(TAG, "getView: getAbsolute: " + file.getAbsolutePath());
        Log.i(TAG, "getView: getPath: " + file.getPath());
        if (file.isDirectory()) {
            imageView.setImageResource(R.drawable.ic_folder_with_round_bg);
        } else if (file.isFile()) {
            Glide.with(getContext()).load(new File(this.ParentFolder + "/" + this.web[position]))
                    .placeholder(R.drawable.ic_file_white_24dp)
                    .into(imageView);

//            Picasso.get().load().placeholder((int) R.drawable.document_gray).resize(50, 50).into(imageView);
        }
        return rowView;
    }
}
