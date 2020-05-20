package com.safe.gallery.calculator.files.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.app.MainApplication;
import com.safe.gallery.calculator.files.FilesActivity;
import com.safe.gallery.calculator.model.AllFilesModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FilesAdapter extends Adapter<ViewHolder> {

    private static final String TAG = "FilesAdapter";
    private ArrayList<AllFilesModel> buckets;
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

            mView = itemView;
        }
    }


    public FilesAdapter(Context context) {

        this.context = context;
        this.buckets = new ArrayList();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_file, parent, false));

    }

    public void onBindViewHolder(final ViewHolder holder, int position) {

        final AllFilesModel bucket = (AllFilesModel) buckets.get(position);

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
                        checkIfAllFilesDeselected();
                        return;
                    }
                    ((FilesActivity) context).openFile(bucket.getOldPath());
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
                        checkIfAllFilesDeselected();
                    }
                }
            });
        }
    }

    private void checkIfAllFilesDeselected() {

        List<String> selectedFiles = new ArrayList();
        Iterator it = this.buckets.iterator();
        while (it.hasNext()) {
            AllFilesModel bucket = (AllFilesModel) it.next();
            if (bucket.isSelected()) {
                selectedFiles.add(bucket.getOldPath());
            }
        }
        if (selectedFiles.size() == 0) {
            ((FilesActivity) context).showDeleteButton(false);
            ((FilesActivity) context).showSelectAllButton(false);
        } else if (selectedFiles.size() == this.buckets.size()) {
            ((FilesActivity) this.context).showDeleteButton(true);
            ((FilesActivity) this.context).showSelectAllButton(true);
        } else {
            ((FilesActivity) this.context).showDeleteButton(true);
            ((FilesActivity) this.context).showSelectAllButton(false);
        }
    }

    public List<String> getSelectedImages() {
        List<String> selectedFiles = new ArrayList();
        Iterator it = buckets.iterator();
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
        Iterator it = buckets.iterator();
        while (it.hasNext()) {
            AllFilesModel bucket = (AllFilesModel) it.next();
            if (bucket.isSelected()) {
                selectedFiles.add(bucket.getOldPath());
            }
        }
        return selectedFiles;
    }

    public void selectAllItem() {
        Iterator it = buckets.iterator();
        while (it.hasNext()) {
            ((AllFilesModel) it.next()).setSelected(true);
        }
        isLongPressed = true;
        notifyDataSetChanged();
    }

    public void deSelectAllItem() {
        Iterator it = buckets.iterator();
        while (it.hasNext()) {
            ((AllFilesModel) it.next()).setSelected(false);
            Log.i(TAG, "deSelectAllItem: item deselected");
        }
        isLongPressed = false;
        notifyDataSetChanged();
    }

    public void removeSelectedFiles() {
        List<AllFilesModel> selectedFiles = new ArrayList();
        Iterator it = buckets.iterator();
        while (it.hasNext()) {
            AllFilesModel bucket = (AllFilesModel) it.next();
            if (bucket.isSelected()) {
                selectedFiles.add(new AllFilesModel(bucket.getOldPath(), bucket.getLastModified()));
            }
        }
        buckets.removeAll(selectedFiles);
        notifyDataSetChanged();
    }

    public int getItemViewType(int position) {
        return position;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public int getItemCount() {
        return buckets == null ? 0 : buckets.size();
    }

    public void addItems(ArrayList<AllFilesModel> allBuckets) {
        buckets.addAll(allBuckets);
        notifyDataSetChanged();
    }

    public void isItemEditable(boolean isEditable) {
        Iterator it = buckets.iterator();
        while (it.hasNext()) {
            ((AllFilesModel) it.next()).setEditable(isEditable);
        }
        notifyDataSetChanged();
    }
}
