package com.androidbull.calculator.photo.vault.adapters.images;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.androidbull.calculator.photo.R;
import com.androidbull.calculator.photo.vault.activities.images.AddImageActivity;
import com.androidbull.calculator.photo.vault.model.AllImagesModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class AddImageAdapter extends Adapter<ViewHolder> {
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

            img = itemView.findViewById(R.id.img);
            imgSelection = itemView.findViewById(R.id.img_selection);
            // ButterKnife.bind((Object) this, itemView);
            this.mView = itemView;
        }
    }

    public AddImageAdapter(Context context) {
        this.context = context;
        this.buckets = new ArrayList();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(this.context).inflate(R.layout.raw_all_images, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {

        final AllImagesModel bucket = this.buckets.get(position);
        if (holder instanceof ImageViewHolder) {
            if (!bucket.getImagePath().isEmpty()) {
                Glide.with(this.context).load(Uri.fromFile(new File(bucket.getImagePath()))).into(((ImageViewHolder) holder).img);
            }
            ((ImageViewHolder) holder).imgSelection.setVisibility(bucket.isSelected() ? View.VISIBLE : View.INVISIBLE);
            ((ImageViewHolder) holder).mView.setOnClickListener(view -> {
                bucket.setSelected(!bucket.isSelected());
                if (bucket.isSelected()) {
                    ((ImageViewHolder) holder).imgSelection.setVisibility(View.VISIBLE);

                } else {
                    ((ImageViewHolder) holder).imgSelection.setVisibility(View.INVISIBLE);
                }
                AddImageAdapter.this.checkIfAllFilesDeselected();
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
