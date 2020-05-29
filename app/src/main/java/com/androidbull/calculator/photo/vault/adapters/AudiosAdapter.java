package com.androidbull.calculator.photo.vault.adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidbull.calculator.photo.R;
import com.androidbull.calculator.photo.vault.MainApplication;
import com.androidbull.calculator.photo.vault.activities.audio.AudiosActivity;
import com.androidbull.calculator.photo.vault.model.AllAudioModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AudiosAdapter extends Adapter<ViewHolder> {

    private ArrayList<AllAudioModel> bucketArraylist;
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

            this.mView = itemView;
        }
    }

    public AudiosAdapter(Context context) {
        this.context = context;
        this.bucketArraylist = new ArrayList();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_file_hide, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AllAudioModel bucket = this.bucketArraylist.get(position);
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
                    AudiosAdapter.this.checkIfAllFilesDeselected();
                    return;
                }
                ((AudiosActivity) AudiosAdapter.this.context).openAudio(bucket.getOldPath());
            });
            ((ImageViewHolder) holder).checkbox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
                if (bucket.isEditable()) {
                    if (isChecked) {
                        bucket.setSelected(true);
                    } else {
                        bucket.setSelected(false);
                    }
                    AudiosAdapter.this.checkIfAllFilesDeselected();
                }
            });
        }
    }

    private void checkIfAllFilesDeselected() {
        List<String> selectedFiles = new ArrayList();
        Iterator it = this.bucketArraylist.iterator();
        while (it.hasNext()) {
            AllAudioModel bucket = (AllAudioModel) it.next();
            if (bucket.isSelected()) {
                selectedFiles.add(bucket.getOldPath());
            }
        }
        if (selectedFiles.size() == 0) {
            ((AudiosActivity) context).showDeleteButton(false);
            ((AudiosActivity) this.context).showSelectAllButton(false);
        } else if (selectedFiles.size() == this.bucketArraylist.size()) {
            ((AudiosActivity) this.context).showDeleteButton(true);
            ((AudiosActivity) this.context).showSelectAllButton(true);
        } else {
            ((AudiosActivity) this.context).showDeleteButton(true);
            ((AudiosActivity) this.context).showSelectAllButton(false);
        }
    }

    public List<String> getSelectedImages() {
        List<String> selectedFiles = new ArrayList();
        Iterator it = this.bucketArraylist.iterator();
        while (it.hasNext()) {
            AllAudioModel bucket = (AllAudioModel) it.next();
            if (bucket.isSelected()) {
                selectedFiles.add(bucket.getOldPath());
            }
        }
        return selectedFiles;
    }

    public List<String> getSelectedImagePaths() {
        List<String> selectedFiles = new ArrayList();
        Iterator it = this.bucketArraylist.iterator();
        while (it.hasNext()) {
            AllAudioModel bucket = (AllAudioModel) it.next();
            if (bucket.isSelected()) {
                selectedFiles.add(bucket.getOldPath());
            }
        }
        return selectedFiles;
    }

    public void selectAllItem() {
        Iterator it = this.bucketArraylist.iterator();
        while (it.hasNext()) {
            ((AllAudioModel) it.next()).setSelected(true);
        }
        notifyDataSetChanged();
    }

    public void deSelectAllItem() {
        Iterator it = this.bucketArraylist.iterator();
        while (it.hasNext()) {
            ((AllAudioModel) it.next()).setSelected(false);
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
        return this.bucketArraylist == null ? 0 : this.bucketArraylist.size();
    }

    public void addItems(ArrayList<AllAudioModel> allBuckets) {
        this.bucketArraylist.addAll(allBuckets);
        notifyDataSetChanged();
    }

    public void isItemEditable(boolean isEditable) {
        Iterator it = this.bucketArraylist.iterator();
        while (it.hasNext()) {
            ((AllAudioModel) it.next()).setEditable(isEditable);
        }
        notifyDataSetChanged();
    }
}
