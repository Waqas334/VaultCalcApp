package com.androidbull.calculator.photo.vault.adapters;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.androidbull.calculator.photo.R;
import com.androidbull.calculator.photo.vault.activities.BrowserFilesActivity;
import com.androidbull.calculator.photo.vault.activities.files.FilesActivity;
import com.androidbull.calculator.photo.vault.adapters.video.VideoAdapter;
import com.androidbull.calculator.photo.vault.model.AllFilesModel;
import com.androidbull.calculator.photo.vault.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BrowserFilesAdapter extends RecyclerView.Adapter<BrowserFilesAdapter.BrowserFilesViewHolder> {

    private ArrayList<AllFilesModel> files;
    private OnFileItemClickListener onFileItemClickListener;
    private Context context;
    private boolean isLongPressed = false;


    public BrowserFilesAdapter(Context context, ArrayList<AllFilesModel> files, OnFileItemClickListener onFileItemClickListener) {
        this.files = files;
        this.onFileItemClickListener = onFileItemClickListener;
        this.context = context;
    }

    public void updateAdapter(ArrayList<AllFilesModel> files) {
        this.files = files;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BrowserFilesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new BrowserFilesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_broswer_file, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BrowserFilesViewHolder holder, int position) {
        AllFilesModel file = files.get(position);

        holder.bind(file, position);


        if (file.isSelected()) {
            holder.ivSelection.setVisibility(View.VISIBLE);
        } else {
            holder.ivSelection.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(view -> {

            if (file.isEditable()) {
                file.setSelected(!file.isSelected());
                if (file.isSelected()) {
                    holder.ivSelection.setVisibility(View.VISIBLE);
                } else {
                    holder.ivSelection.setVisibility(View.INVISIBLE);
                }
                checkIfAllFilesDeselected();
                return;
            }
            onFileItemClickListener.onFileItemClick(file, position);
        });

    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    class BrowserFilesViewHolder extends RecyclerView.ViewHolder {

        private TextView tvFileName;
        private ImageView ivFileThumbnail, ivSelection, ivVideoPlay;

        public BrowserFilesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFileName = itemView.findViewById(R.id.tvFileName);
            ivFileThumbnail = itemView.findViewById(R.id.ivFileThumbnail);
            ivSelection = itemView.findViewById(R.id.ivSelection);
            ivVideoPlay = itemView.findViewById(R.id.ivVideoPlay);

        }

        public void bind(AllFilesModel file, int position) {
            File tempFile = new File(file.getOldPath());
            tvFileName.setText(tempFile.getName());


            if (Utils.isImageFile(tempFile.getPath())) {
                Glide.with(context)
                        .load(Uri.fromFile(tempFile))
                        .into(ivFileThumbnail);
            } else if (Utils.isVideoFile(tempFile.getPath())) {
                Glide.with(context)
                        .load(Uri.fromFile(tempFile))
                        .into(ivFileThumbnail);
                ivVideoPlay.setVisibility(View.VISIBLE);
            } else if (Utils.isAudioFile(tempFile.getPath())) {
                ivFileThumbnail.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_audio_svg));
            } else {
                ivFileThumbnail.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_file_white_24dp));
            }

        }
    }


    private void checkIfAllFilesDeselected() {

        List<String> selectedFiles = new ArrayList();
        Iterator it = this.files.iterator();
        while (it.hasNext()) {
            AllFilesModel bucket = (AllFilesModel) it.next();
            if (bucket.isSelected()) {
                selectedFiles.add(bucket.getOldPath());
            }
        }
        if (selectedFiles.size() == 0) {
            ((BrowserFilesActivity) context).showDeleteButton(false);
            ((BrowserFilesActivity) context).showSelectAllButton(false);
        } else if (selectedFiles.size() == this.files.size()) {
            ((BrowserFilesActivity) context).showDeleteButton(true);
            ((BrowserFilesActivity) context).showSelectAllButton(true);
        } else {
            ((BrowserFilesActivity) context).showDeleteButton(true);
            ((BrowserFilesActivity) context).showSelectAllButton(false);
        }
    }

    public List<String> getSelectedImages() {
        List<String> selectedFiles = new ArrayList();
        Iterator it = files.iterator();
        while (it.hasNext()) {
            AllFilesModel bucket = (AllFilesModel) it.next();
            if (bucket.isSelected()) {
                selectedFiles.add(bucket.getOldPath());
            }
        }
        return selectedFiles;
    }

    public List<String> getSelectedImagePaths() {
        List<String> selectedFiles = new ArrayList();
        Iterator it = files.iterator();
        while (it.hasNext()) {
            AllFilesModel bucket = (AllFilesModel) it.next();
            if (bucket.isSelected()) {
                selectedFiles.add(bucket.getOldPath());
            }
        }
        return selectedFiles;
    }

    public void selectAllItem() {
        Iterator it = files.iterator();
        while (it.hasNext()) {
            ((AllFilesModel) it.next()).setSelected(true);
        }
        isLongPressed = true;
        notifyDataSetChanged();
    }

    public void deSelectAllItem() {
        Iterator it = files.iterator();
        while (it.hasNext()) {
            ((AllFilesModel) it.next()).setSelected(false);
        }
        isLongPressed = false;
        notifyDataSetChanged();
    }

    public void isItemEditable(boolean isEditable) {
        Iterator it = files.iterator();
        while (it.hasNext()) {
            ((AllFilesModel) it.next()).setEditable(isEditable);
        }
        notifyDataSetChanged();
    }

    public interface OnFileItemClickListener {
        void onFileItemClick(AllFilesModel file, int position);
    }
}
