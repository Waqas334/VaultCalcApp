package com.safe.gallery.calculator.activities;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.safe.gallery.calculator.R;

import java.io.File;
import java.util.ArrayList;


public class Adapter_ImageFolder extends RecyclerView.Adapter<Adapter_ImageFolder.ViewHolder> {


    private ArrayList<File> al_my_photos = new ArrayList<>();
    Context context;
    Activity activity;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


    public Adapter_ImageFolder(Context context, ArrayList<File> al_video,OnItemClickListener onItemClickListener) {
        this.al_my_photos = al_video;
        this.context = context;
        mOnItemClickListener = onItemClickListener;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        LinearLayout lnselect;
        TextView name;

        public ViewHolder(View v) {
            super(v);
            img = v.findViewById(R.id.img);
            name = v.findViewById(R.id.name);
            lnselect =v.findViewById(R.id.lnselect);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_image, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

        Log.e("TAG", "onBindViewHolder: " + al_my_photos.get(position));
        holder.name.setText(al_my_photos.get(position).getName());


        Glide.with(context).load(al_my_photos.get(position))
                .into(holder.img);

        holder.lnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mOnItemClickListener.onItemClick(v, position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return al_my_photos.size();

    }
}