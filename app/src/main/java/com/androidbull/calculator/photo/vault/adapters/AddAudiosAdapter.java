package com.androidbull.calculator.photo.vault.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.androidbull.calculator.photo.vault.MainApplication;
import com.androidbull.calculator.photo.R;
import com.androidbull.calculator.photo.vault.activities.audio.AddAudiosActivity;
import com.androidbull.calculator.photo.vault.model.AllAudioModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class AddAudiosAdapter extends Adapter<ViewHolder> {

    private ArrayList<AllAudioModel> bucketsArraylist = new ArrayList();
    private Context context;

    class C05142 implements Comparator<AllAudioModel> {
        C05142() {
        }

        public int compare(AllAudioModel obj1, AllAudioModel obj2) {
            return Long.valueOf(obj2.getLastModified()).compareTo(Long.valueOf(obj1.getLastModified()));
        }
    }

    class ImageViewHolder extends ViewHolder {

        CheckBox checkbox;

        ImageView img;

        View mView;

        TextView txtSize;

        TextView txtTitle;

        public ImageViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind((Object) this, itemView);

            checkbox = itemView.findViewById(R.id.checkbox);
            img = itemView.findViewById(R.id.img);
            txtSize = itemView.findViewById(R.id.txt_size);
            txtTitle = itemView.findViewById(R.id.txt_title);

            this.mView = itemView;
        }
    }


    public AddAudiosAdapter(Context context) {
        this.context = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_file_hide, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AllAudioModel bucket = bucketsArraylist.get(position);
        if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).checkbox.setOnCheckedChangeListener(null);
            if (!bucket.getOldPath().isEmpty()) {
                File fFile = new File(bucket.getOldPath());
                ((ImageViewHolder) holder).txtTitle.setText(fFile.getName());
                ((ImageViewHolder) holder).txtSize.setText(MainApplication.getInstance().getFileSize(fFile.length()));
            }
            if (bucket.isSelected()) {
                ((ImageViewHolder) holder).checkbox.setChecked(true);
            } else {
                ((ImageViewHolder) holder).checkbox.setChecked(false);
            }
            ((ImageViewHolder) holder).checkbox.setOnClickListener(view -> {
                if (((ImageViewHolder) holder).checkbox.isChecked()) {
                    bucket.setSelected(true);
                } else {
                    bucket.setSelected(false);
                }
                checkIfAllFilesDeselected();
            });
        }
    }

    private void checkIfAllFilesDeselected() {
        List<String> selectedFiles = new ArrayList();
        Iterator it = bucketsArraylist.iterator();
        while (it.hasNext()) {
            AllAudioModel bucket = (AllAudioModel) it.next();
            if (bucket.isSelected()) {
                selectedFiles.add(bucket.getOldPath());
            }
        }
        if (selectedFiles.size() != 0) {
            ((AddAudiosActivity) context).showHideButton(true);
        } else {
            ((AddAudiosActivity) context).showHideButton(false);
            ((AddAudiosActivity) context).setSelectAll(false);
        }
        if (selectedFiles.size() == bucketsArraylist.size()) {
            ((AddAudiosActivity) context).setSelectAll(true);
        } else {
            ((AddAudiosActivity) context).setSelectAll(false);
        }
    }

    public List<String> getSelectedImages() {
        List<String> selectedFiles = new ArrayList();
        Iterator it = bucketsArraylist.iterator();
        while (it.hasNext()) {
            AllAudioModel bucket = (AllAudioModel) it.next();
            if (bucket.isSelected()) {
                selectedFiles.add(bucket.getOldPath());
            }
        }
        return selectedFiles;
    }

    public void selectAllItem() {
        Iterator it = bucketsArraylist.iterator();
        while (it.hasNext()) {
            ((AllAudioModel) it.next()).setSelected(true);
        }
        notifyDataSetChanged();
    }

    public void deSelectAllItem() {
        Iterator it = bucketsArraylist.iterator();
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
        return bucketsArraylist == null ? 0 : bucketsArraylist.size();
    }

    public void addItems(ArrayList<AllAudioModel> allBuckets) {
        if (allBuckets != null) {
            Collections.sort(allBuckets, new C05142());
            bucketsArraylist.addAll(allBuckets);
            notifyDataSetChanged();
        }
    }
}
