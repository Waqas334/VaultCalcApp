package com.safe.gallery.calculator.files;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.safe.gallery.calculator.R;
import com.squareup.picasso.Picasso;

import java.io.File;

public class CustomListSingleOnly extends ArrayAdapter<String> {

    String ParentFolder;
    private final Activity context;
    private final String[] web;

    public CustomListSingleOnly(Activity context, String[] web, String path) {
        super(context, R.layout.list_single_only, web);
        this.context = context;
        this.web = web;
        this.ParentFolder = path;
    }

    public View getView(int position, View view, ViewGroup parent) {
        View rowView = this.context.getLayoutInflater().inflate(R.layout.list_single_only, null, true);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        ((TextView) rowView.findViewById(R.id.txt)).setText(this.web[position]);
        if (new File(this.ParentFolder + "/" + this.web[position]).isDirectory()) {
            imageView.setImageResource(R.drawable.folder);
        } else if (new File(this.ParentFolder + "/" + this.web[position]).isFile()) {
            Picasso.with(this.context).load(new File(this.ParentFolder + "/" + this.web[position])).placeholder((int) R.drawable.document_gray).resize(50, 50).into(imageView);
        }
        return rowView;
    }
}
