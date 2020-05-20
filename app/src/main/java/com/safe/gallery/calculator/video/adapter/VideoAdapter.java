package com.safe.gallery.calculator.video.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.app.MainApplication;
import com.safe.gallery.calculator.model.AllVideosModel;
import com.safe.gallery.calculator.video.VideoActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VideoAdapter extends Adapter<ViewHolder> {
    private ArrayList<AllVideosModel> buckets;
    private Context context;
    private boolean isLongPressed = false;

    class ImageViewHolder extends ViewHolder {

        CheckBox checkbox;

        ImageView img;

        View mView;

        TextView txtSize;

        TextView txtTitle;

        public ImageViewHolder(View itemView) {
            super(itemView);

            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            img = (ImageView) itemView.findViewById(R.id.img);
            txtSize = (TextView) itemView.findViewById(R.id.txt_size);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);

            //ButterKnife.bind((Object) this, itemView);
            this.mView = itemView;
        }
    }

    public VideoAdapter(Context context) {
        this.context = context;
        this.buckets = new ArrayList();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_video, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AllVideosModel bucket = (AllVideosModel) this.buckets.get(position);
        if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).checkbox.setOnCheckedChangeListener(null);
            if (bucket.isEditable()) {
                ((ImageViewHolder) holder).checkbox.setVisibility(0);
            } else {
                ((ImageViewHolder) holder).checkbox.setVisibility(8);
            }
            if (bucket.isSelected()) {
                ((ImageViewHolder) holder).checkbox.setChecked(true);
            } else {
                ((ImageViewHolder) holder).checkbox.setChecked(false);
            }
            if (bucket.isSelected()) {
                ((ImageViewHolder) holder).checkbox.setChecked(true);
            } else {
                ((ImageViewHolder) holder).checkbox.setChecked(false);
            }
            if (!bucket.getOldPath().isEmpty()) {
                File fFile = new File(bucket.getOldPath());
                Glide.with(this.context)
                        .load(Uri.fromFile(fFile))
                        .into(((ImageViewHolder) holder).img);
                ((ImageViewHolder) holder).txtTitle.setText(fFile.getName());
                ((ImageViewHolder) holder).txtSize.setText(MainApplication.getInstance().getFileSize(fFile.length()));
            }
            ((ImageViewHolder) holder).mView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (bucket.isEditable()) {
                        bucket.setSelected(!bucket.isSelected());
                        if (bucket.isSelected()) {
                            ((ImageViewHolder) holder).checkbox.setChecked(true);
                        } else {
                            ((ImageViewHolder) holder).checkbox.setChecked(false);
                        }
                        VideoAdapter.this.checkIfAllFilesDeselected();
                        return;
                    }
                    ((VideoActivity) VideoAdapter.this.context).openVideo(bucket.getOldPath());
                }
            });
            ((ImageViewHolder) holder).checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (bucket.isEditable()) {
                        if (isChecked) {
                            bucket.setSelected(true);
                        } else {
                            bucket.setSelected(false);
                        }
                        VideoAdapter.this.checkIfAllFilesDeselected();
                    }
                }
            });
        }
    }

    private void checkIfAllFilesDeselected() {
        List<String> selectedFiles = new ArrayList();
        Iterator it = this.buckets.iterator();
        while (it.hasNext()) {
            AllVideosModel bucket = (AllVideosModel) it.next();
            if (bucket.isSelected()) {
                selectedFiles.add(bucket.getOldPath());
            }
        }
        if (selectedFiles.size() == 0) {
            ((VideoActivity) this.context).showDeleteButton(false);
            ((VideoActivity) this.context).showSelectAllButton(false);
        } else if (selectedFiles.size() == this.buckets.size()) {
            ((VideoActivity) this.context).showDeleteButton(true);
            ((VideoActivity) this.context).showSelectAllButton(true);
        } else {
            ((VideoActivity) this.context).showDeleteButton(true);
            ((VideoActivity) this.context).showSelectAllButton(false);
        }
    }

    public List<String> getSelectedImages() {
        List<String> selectedFiles = new ArrayList();
        Iterator it = this.buckets.iterator();
        while (it.hasNext()) {
            AllVideosModel bucket = (AllVideosModel) it.next();
            if (bucket.isSelected()) {
                selectedFiles.add(bucket.getOldPath());
            }
        }
        return selectedFiles;
    }

    public List<String> getSelectedImagePaths() {
        List<String> selectedFiles = new ArrayList();
        Iterator it = this.buckets.iterator();
        while (it.hasNext()) {
            AllVideosModel bucket = (AllVideosModel) it.next();
            if (bucket.isSelected()) {
                selectedFiles.add(bucket.getOldPath());
            }
        }
        return selectedFiles;
    }

    public void selectAllItem() {
        Iterator it = this.buckets.iterator();
        while (it.hasNext()) {
            ((AllVideosModel) it.next()).setSelected(true);
        }
        this.isLongPressed = true;
        notifyDataSetChanged();
    }

    public void deSelectAllItem() {
        Iterator it = this.buckets.iterator();
        while (it.hasNext()) {
            ((AllVideosModel) it.next()).setSelected(false);
        }
        this.isLongPressed = false;
        notifyDataSetChanged();
    }

    public void removeSelectedFiles() {
        List<AllVideosModel> selectedFiles = new ArrayList();
        Iterator it = this.buckets.iterator();
        while (it.hasNext()) {
            AllVideosModel bucket = (AllVideosModel) it.next();
            if (bucket.isSelected()) {
                selectedFiles.add(new AllVideosModel(bucket.getOldPath(), bucket.getLastModified()));
            }
        }
        this.buckets.removeAll(selectedFiles);
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

    public void addItems(ArrayList<AllVideosModel> allBuckets) {
        this.buckets.addAll(allBuckets);
        notifyDataSetChanged();
    }

    public void isItemEditable(boolean isEditable) {
        Iterator it = this.buckets.iterator();
        while (it.hasNext()) {
            ((AllVideosModel) it.next()).setEditable(isEditable);
        }
        notifyDataSetChanged();
    }
}
