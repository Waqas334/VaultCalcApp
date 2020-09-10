package com.androidbull.calculator.photo.vault.adapters.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.androidbull.calculator.photo.vault.MainApplication;
import com.androidbull.calculator.photo.R;
import com.androidbull.calculator.photo.vault.activities.video.VideoActivity;
import com.androidbull.calculator.photo.vault.model.AllVideosModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VideoAdapter extends Adapter<ViewHolder> {
    private ArrayList<AllVideosModel> buckets;
    private Context context;

    class ImageViewHolder extends ViewHolder {

        CheckBox checkbox;

        ImageView img;

        View mView;

        TextView txtSize;

        TextView txtTitle;

        public ImageViewHolder(View itemView) {
            super(itemView);

            checkbox = itemView.findViewById(R.id.checkbox);
            img = itemView.findViewById(R.id.img);
            txtSize = itemView.findViewById(R.id.txt_size);
            txtTitle = itemView.findViewById(R.id.txt_title);

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
        final AllVideosModel bucket = this.buckets.get(position);
        if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).checkbox.setOnCheckedChangeListener(null);
            ((ImageViewHolder) holder).checkbox.setVisibility(bucket.isEditable() ? View.VISIBLE : View.GONE);
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

               /* Bitmap bmThumbnail;
                bmThumbnail = ThumbnailUtils.createVideoThumbnail(fFile.getPath(), MediaStore.Images.Thumbnails.MICRO_KIND);
                ((ImageViewHolder) holder).img.setImageBitmap(bmThumbnail);
*/

                ((ImageViewHolder) holder).txtTitle.setText(fFile.getName());
                ((ImageViewHolder) holder).txtSize.setText(MainApplication.getInstance().getFileSize(fFile.length()));
            }
            ((ImageViewHolder) holder).mView.setOnClickListener(view -> {
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
            });
            ((ImageViewHolder) holder).checkbox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (bucket.isEditable()) {
                    if (isChecked) {
                        bucket.setSelected(true);
                    } else {
                        bucket.setSelected(false);
                    }
                    VideoAdapter.this.checkIfAllFilesDeselected();
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
        notifyDataSetChanged();
    }

    public void deSelectAllItem() {
        Iterator it = this.buckets.iterator();
        while (it.hasNext()) {
            ((AllVideosModel) it.next()).setSelected(false);
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
