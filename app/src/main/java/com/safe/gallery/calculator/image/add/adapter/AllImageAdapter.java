package com.safe.gallery.calculator.image.add.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.image.add.AddImageActivity;
import com.safe.gallery.calculator.model.AllImagesModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import butterknife.Unbinder;
import butterknife.internal.Utils;

public class AllImageAdapter extends Adapter<ViewHolder> {
    private ArrayList<AllImagesModel> buckets;
    private Context context;
    private boolean isLongPressed = false;

    class C06142 implements Comparator<AllImagesModel> {
        C06142() {
        }

        public int compare(AllImagesModel obj1, AllImagesModel obj2) {
            return Long.valueOf(obj2.getImageLastModified()).compareTo(Long.valueOf(obj1.getImageLastModified()));
        }
    }

    class ImageViewHolder extends ViewHolder {

        ImageView img;

        ImageView imgSelection;
        View mView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.img);
            imgSelection = (ImageView) itemView.findViewById(R.id.img_selection);
           // ButterKnife.bind((Object) this, itemView);
            this.mView = itemView;
        }
    }

    public AllImageAdapter(Context context) {
        this.context = context;
        this.buckets = new ArrayList();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(this.context).inflate(R.layout.raw_all_images, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {

        final AllImagesModel bucket = (AllImagesModel) this.buckets.get(position);
        if (holder instanceof ImageViewHolder) {
            if (!bucket.getImagePath().isEmpty()) {
                Glide.with(this.context).load(Uri.fromFile(new File(bucket.getImagePath()))).into(((ImageViewHolder) holder).img);
            }
            if (bucket.isSelected()) {
                ((ImageViewHolder) holder).imgSelection.setVisibility(View.VISIBLE);
//                ((ImageViewHolder) holder).imgSelection.setBackgroundColor(context.getResources().getColor(R.color.newColorAccent));
            } else {
//                ((ImageViewHolder) holder).imgSelection.setImageResource(0);
                ((ImageViewHolder) holder).imgSelection.setVisibility(View.INVISIBLE);
            }
            ((ImageViewHolder) holder).mView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {

                    bucket.setSelected(!bucket.isSelected());
                    if (bucket.isSelected()) {
//                        ((ImageViewHolder) holder).imgSelection.setImageResource(R.drawable.ic_check_white_new_24dp);
//                        ((ImageViewHolder) holder).imgSelection.setBackgroundColor(context.getResources().getColor(R.color.newColorAccent));
                        ((ImageViewHolder) holder).imgSelection.setVisibility(View.VISIBLE);

                    } else {
                        ((ImageViewHolder) holder).imgSelection.setVisibility(View.INVISIBLE);
//                        ((ImageViewHolder) holder).imgSelection.setImageResource(0);
                    }
                    AllImageAdapter.this.checkIfAllFilesDeselected();
                }
            });
        }
    }

    private void checkIfAllFilesDeselected() {
        List<String> selectedFiles = new ArrayList();
        Iterator it = this.buckets.iterator();
        while (it.hasNext()) {
            AllImagesModel bucket = (AllImagesModel) it.next();
            if (bucket.isSelected()) {
                selectedFiles.add(bucket.getImagePath());
            }
        }
        if (selectedFiles.size() != 0) {
            ((AddImageActivity) this.context).showHideButton(true);
        } else {
            ((AddImageActivity) this.context).showHideButton(false);
        }
        if (selectedFiles.size() == this.buckets.size()) {
            ((AddImageActivity) this.context).setSelectAll(true);
        } else {
            ((AddImageActivity) this.context).setSelectAll(false);
        }
    }

    public List<String> getSelectedImages() {
        List<String> selectedFiles = new ArrayList();
        Iterator it = this.buckets.iterator();
        while (it.hasNext()) {
            AllImagesModel bucket = (AllImagesModel) it.next();
            if (bucket.isSelected()) {
                selectedFiles.add(bucket.getImagePath());
            }
        }
        return selectedFiles;
    }

    public void selectAllItem() {
        Iterator it = this.buckets.iterator();
        while (it.hasNext()) {
            ((AllImagesModel) it.next()).setSelected(true);
        }
        notifyDataSetChanged();
    }

    public void deSelectAllItem() {
        Iterator it = this.buckets.iterator();
        while (it.hasNext()) {
            ((AllImagesModel) it.next()).setSelected(false);
        }
        notifyDataSetChanged();
    }

    public int getItemViewType(int position) {
        return position;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemCount() {
        return this.buckets == null ? 0 : this.buckets.size();
    }

    public void addItems(ArrayList<AllImagesModel> allBuckets) {
        if (allBuckets != null) {
            Collections.sort(allBuckets, new C06142());
            this.buckets.addAll(allBuckets);
            notifyDataSetChanged();
        }
    }
}
