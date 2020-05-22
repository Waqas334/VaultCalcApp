package com.safe.gallery.calculator.adapters.video;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.safe.gallery.calculator.R;
import com.safe.gallery.calculator.MainApplication;
import com.safe.gallery.calculator.model.AllVideosModel;
import com.safe.gallery.calculator.activities.video.AddVideoActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import butterknife.Unbinder;
import butterknife.internal.Utils;

public class AddVideoAdapter extends Adapter<ViewHolder> {

    private ArrayList<AllVideosModel> bucketsArraylist = new ArrayList();
    private Context context;

    class C06562 implements Comparator<AllVideosModel> {
        C06562() {
        }

        public int compare(AllVideosModel obj1, AllVideosModel obj2) {
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

            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            img = (ImageView) itemView.findViewById(R.id.img);
            txtSize = (TextView) itemView.findViewById(R.id.txt_size);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);

            //ButterKnife.bind((Object) this, itemView);
            this.mView = itemView;
        }
    }

    public class ImageViewHolder_ViewBinding implements Unbinder {
        private ImageViewHolder target;

        @UiThread
        public ImageViewHolder_ViewBinding(ImageViewHolder target, View source) {
            this.target = target;
            target.img = (ImageView) Utils.findRequiredViewAsType(source, R.id.img, "field 'img'", ImageView.class);
            target.txtTitle = (TextView) Utils.findRequiredViewAsType(source, R.id.txt_title, "field 'txtTitle'", TextView.class);
            target.txtSize = (TextView) Utils.findRequiredViewAsType(source, R.id.txt_size, "field 'txtSize'", TextView.class);
            target.checkbox = (CheckBox) Utils.findRequiredViewAsType(source, R.id.checkbox, "field 'checkbox'", CheckBox.class);
        }

        @CallSuper
        public void unbind() {
            ImageViewHolder target = this.target;
            if (target == null) {
                throw new IllegalStateException("Bindings already cleared.");
            }
            this.target = null;
            target.img = null;
            target.txtTitle = null;
            target.txtSize = null;
            target.checkbox = null;
        }
    }

    public AddVideoAdapter(Context context) {
        this.context = context;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_video, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AllVideosModel bucket = (AllVideosModel) bucketsArraylist.get(position);
        if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).checkbox.setOnCheckedChangeListener(null);
            if (!bucket.getOldPath().isEmpty()) {
                File fFile = new File(bucket.getOldPath());
                Glide.with(this.context)
                        .load(Uri.fromFile(fFile))
                        .into(((ImageViewHolder) holder).img);
                ((ImageViewHolder) holder).txtTitle.setText(fFile.getName());
                ((ImageViewHolder) holder).txtSize.setText(MainApplication.getInstance().getFileSize(fFile.length()));
            }
            if (bucket.isSelected()) {
                ((ImageViewHolder) holder).checkbox.setChecked(true);
            } else {
                ((ImageViewHolder) holder).checkbox.setChecked(false);
            }
            ((ImageViewHolder) holder).checkbox.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (((ImageViewHolder) holder).checkbox.isChecked()) {
                        bucket.setSelected(true);
                    } else {
                        bucket.setSelected(false);
                    }
                    AddVideoAdapter.this.checkIfAllFilesDeselected();
                }
            });
        }
    }

    private void checkIfAllFilesDeselected() {
        List<String> selectedFiles = new ArrayList();
        Iterator it = bucketsArraylist.iterator();
        while (it.hasNext()) {
            AllVideosModel bucket = (AllVideosModel) it.next();
            if (bucket.isSelected()) {
                selectedFiles.add(bucket.getOldPath());
            }
        }
        if (selectedFiles.size() != 0) {
            ((AddVideoActivity) this.context).showHideButton(true);
        } else {
            ((AddVideoActivity) this.context).showHideButton(false);
            ((AddVideoActivity) this.context).setSelectAll(false);
        }
        if (selectedFiles.size() == bucketsArraylist.size()) {
            ((AddVideoActivity) this.context).setSelectAll(true);
        } else {
            ((AddVideoActivity) this.context).setSelectAll(false);
        }
    }

    public List<String> getSelectedImages() {
        List<String> selectedFiles = new ArrayList();
        Iterator it = bucketsArraylist.iterator();
        while (it.hasNext()) {
            AllVideosModel bucket = (AllVideosModel) it.next();
            if (bucket.isSelected()) {
                selectedFiles.add(bucket.getOldPath());
            }
        }
        return selectedFiles;
    }

    public void selectAllItem() {
        Iterator it = bucketsArraylist.iterator();
        while (it.hasNext()) {
            ((AllVideosModel) it.next()).setSelected(true);
        }
        notifyDataSetChanged();
    }

    public void deSelectAllItem() {
        Iterator it = bucketsArraylist.iterator();
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
        return bucketsArraylist == null ? 0 : bucketsArraylist.size();
    }

    public void addItems(ArrayList<AllVideosModel> allBuckets) {
        if (allBuckets != null) {
            Collections.sort(allBuckets, new C06562());
            bucketsArraylist.addAll(allBuckets);
            notifyDataSetChanged();
        }
    }
}
