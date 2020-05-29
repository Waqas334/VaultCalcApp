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
import com.androidbull.calculator.photo.vault.activities.images.ImagesActivity;
import com.androidbull.calculator.photo.vault.model.AllImagesModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImagesAdapter extends Adapter<ViewHolder> {

    private ArrayList<AllImagesModel> bucketsArraylist;
    private Context context;

    class ImageViewHolder extends ViewHolder {

        ImageView img;

        ImageView imgSelection;
        View mView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img);
            imgSelection = itemView.findViewById(R.id.img_selection);

            //ButterKnife.bind((Object) this, itemView);
            this.mView = itemView;
        }
    }


    public ImagesAdapter(Context context) {
        this.context = context;
        this.bucketsArraylist = new ArrayList();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(this.context).inflate(R.layout.raw_all_images, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AllImagesModel bucket = this.bucketsArraylist.get(position);
        if (holder instanceof ImageViewHolder) {
            if (!bucket.getImagePath().isEmpty()) {
                Glide.with(this.context).load(Uri.fromFile(new File(bucket.getImagePath()))).into(((ImageViewHolder) holder).img);
            }
            if (bucket.isSelected()) {
                ((ImageViewHolder) holder).imgSelection.setVisibility(View.VISIBLE);
            } else {
                ((ImageViewHolder) holder).imgSelection.setVisibility(View.INVISIBLE);

            }
            ((ImageViewHolder) holder).mView.setOnClickListener(view -> {
                if (bucket.isEditable()) {
                    bucket.setSelected(!bucket.isSelected());
                    if (bucket.isSelected()) {
                        ((ImageViewHolder) holder).imgSelection.setVisibility(View.VISIBLE);
                    } else {
                        ((ImageViewHolder) holder).imgSelection.setVisibility(View.INVISIBLE);

                    }
                    checkIfAllFilesDeselected();
                    return;
                }
                ((ImagesActivity) context).startFullScreenImageActivity(bucketsArraylist, holder.getAdapterPosition());
            });
        }
    }

    private void checkIfAllFilesDeselected() {
        List<String> selectedFiles = new ArrayList();
        Iterator it = this.bucketsArraylist.iterator();
        while (it.hasNext()) {
            AllImagesModel bucket = (AllImagesModel) it.next();
            if (bucket.isSelected()) {
                selectedFiles.add(bucket.getImagePath());
            }
        }
        if (selectedFiles.size() == 0) {
            ((ImagesActivity) this.context).showDeleteButton(false);
            ((ImagesActivity) this.context).showSelectAllButton(false);
        } else if (selectedFiles.size() == this.bucketsArraylist.size()) {
            ((ImagesActivity) this.context).showDeleteButton(true);
            ((ImagesActivity) this.context).showSelectAllButton(true);
        } else {
            ((ImagesActivity) this.context).showDeleteButton(true);
            ((ImagesActivity) this.context).showSelectAllButton(false);
        }
    }

    public List<String> getSelectedImages() {
        List<String> selectedFiles = new ArrayList();
        Iterator it = this.bucketsArraylist.iterator();
        while (it.hasNext()) {
            AllImagesModel bucket = (AllImagesModel) it.next();
            if (bucket.isSelected()) {
                selectedFiles.add(bucket.getImagePath());
            }
        }
        return selectedFiles;
    }

    public List<String> getSelectedImagePaths() {

        List<String> selectedFiles = new ArrayList();
        Iterator it = this.bucketsArraylist.iterator();
        while (it.hasNext()) {
            AllImagesModel bucket = (AllImagesModel) it.next();
            if (bucket.isSelected()) {
                selectedFiles.add(bucket.getImagePath());
            }
        }
        return selectedFiles;
    }

    public void selectAllItem() {

        Iterator it = this.bucketsArraylist.iterator();
        while (it.hasNext()) {
            ((AllImagesModel) it.next()).setSelected(true);
        }
        notifyDataSetChanged();
    }

    public void deSelectAllItem() {

        Iterator it = this.bucketsArraylist.iterator();
        while (it.hasNext()) {
            ((AllImagesModel) it.next()).setSelected(false);
        }
        notifyDataSetChanged();
    }

    public void isItemEditable(boolean isEditable) {
        Iterator it = this.bucketsArraylist.iterator();
        while (it.hasNext()) {
            ((AllImagesModel) it.next()).setEditable(isEditable);
        }
        notifyDataSetChanged();
    }

    public void removeSelectedFiles() {

        List<AllImagesModel> selectedFiles = new ArrayList();
        Iterator it = this.bucketsArraylist.iterator();
        while (it.hasNext()) {
            AllImagesModel bucket = (AllImagesModel) it.next();
            if (bucket.isSelected()) {
                selectedFiles.add(new AllImagesModel(bucket.getImagePath(), bucket.getImageLastModified()));
            }
        }
        this.bucketsArraylist.removeAll(selectedFiles);
        notifyDataSetChanged();
    }

    public int getItemViewType(int position) {
        return position;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemCount() {
        return this.bucketsArraylist == null ? 0 : this.bucketsArraylist.size();
    }

    public void addItems(ArrayList<AllImagesModel> allBuckets) {
        this.bucketsArraylist.addAll(allBuckets);
        notifyDataSetChanged();
    }
}
