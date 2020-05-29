package com.androidbull.calculator.photo.vault.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.androidbull.calculator.photo.R;

import java.io.File;
import java.util.ArrayList;


public class IntruderAdapter extends RecyclerView.Adapter<IntruderAdapter.ViewHolder> {


    private ArrayList<File> al_my_photos;
    private Context context;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public IntruderAdapter(Context context, ArrayList<File> al_video, OnItemClickListener onItemClickListener) {
        this.al_my_photos = al_video;
        this.context = context;
        mOnItemClickListener = onItemClickListener;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        LinearLayout lnselect;
        TextView name;

        ViewHolder(View v) {
            super(v);
            img = v.findViewById(R.id.img);
            name = v.findViewById(R.id.name);
            lnselect =v.findViewById(R.id.lnselect);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

        Log.e("TAG", "onBindViewHolder: " + al_my_photos.get(position));
        holder.name.setText(al_my_photos.get(position).getName());


        Glide.with(context).load(al_my_photos.get(position))
                .into(holder.img);

        holder.lnselect.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, position));

    }

    @Override
    public int getItemCount() {
        return al_my_photos.size();

    }
}