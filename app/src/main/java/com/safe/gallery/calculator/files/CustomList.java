package com.safe.gallery.calculator.files;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.safe.gallery.calculator.R;
import com.squareup.picasso.Picasso;

import java.io.File;

public class CustomList extends ArrayAdapter<String> {

    String ParentFolder;
    private final Activity context;
    int selectedPosition = -1;
    private final String[] web;

    public CustomList(Activity context, String[] web, String path) {
        super(context, R.layout.list_single, web);
        this.context = context;
        this.web = web;
        this.ParentFolder = path;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        View rowView = this.context.getLayoutInflater().inflate(R.layout.list_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        final CheckBox chk = (CheckBox) rowView.findViewById(R.id.myCheckBox);
        chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chk.isChecked()) {
                    ((FileSelectionActivity) CustomList.this.context).setFiles(true, CustomList.this.web[position]);
                } else {
                    ((FileSelectionActivity) CustomList.this.context).setFiles(false, CustomList.this.web[position]);
                }
            }
        });
        txtTitle.setText(this.web[position]);
        Picasso.with(this.context).load(new File(this.ParentFolder + "/" + this.web[position])).placeholder((int) R.drawable.document).resize(50, 50).into(imageView);
        return rowView;
    }

    private void makeOtherUnChecked(int position, CheckBox chk) {
        for (int i = 0; i < this.web.length; i++) {
            if (i != position) {
                chk.setChecked(false);
            }
        }
        notifyDataSetChanged();
    }
}
