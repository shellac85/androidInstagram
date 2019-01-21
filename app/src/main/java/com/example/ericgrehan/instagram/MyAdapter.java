package com.example.ericgrehan.instagram;

import android.content.Context;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.example.ericgrehan.instagram.Models.Images;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<Images> imageList;
    Context mContext;

    public MyAdapter(Context context,ArrayList<Images> myDataset) {
        mContext = context;
        imageList = myDataset;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.my_image_view,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Images currentimage = imageList.get(i);
        String currentImage = currentimage.getImageUrl().replace("https","http");
        //String currentImage = "http://aljanh.net/data/archive/img/2231164042.jpeg";

        Picasso.get().load(currentImage).fit().centerInside().into(myViewHolder.imageView);
        Picasso.get().setLoggingEnabled(true);

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public MyViewHolder(@NonNull View v) {
            super(v);
            imageView = v.findViewById(R.id.myCardImageView);
        }
    }
}
