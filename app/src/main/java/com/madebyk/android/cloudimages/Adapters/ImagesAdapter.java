package com.madebyk.android.cloudimages.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.madebyk.android.cloudimages.Images_Home.ImagesFullActivity;
import com.madebyk.android.cloudimages.R;

import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesHolder> {

    private static final String TAG = "ImagesAdapter";
    private ArrayList<Integer> list_of_cloud_images;
    private Context mContext;

    public ImagesAdapter(Context context, ArrayList<Integer> images){
        mContext = context;
        list_of_cloud_images = images;
    }

    @Override
    public ImagesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.images_image_item, parent, false);
        return new ImagesHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImagesHolder holder,final int position) {

        Log.d(TAG, "onBindViewHolder: entered the onBindViewHolder");
        Glide
                .with(mContext)
                .load(list_of_cloud_images.get(position))
                .apply(new RequestOptions()
                        .override(550, 600)
                        .fitCenter()
                        .dontAnimate()
                        .placeholder(R.drawable.ic_hourglass)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .thumbnail(0.01f)
                .into(holder.thumbnail);
        
        //On Click Listener to be added
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ImagesFullActivity.class);
                intent.putExtra("position", position);
                intent.putIntegerArrayListExtra("images", list_of_cloud_images);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_of_cloud_images.size();
    }

    public static class ImagesHolder extends RecyclerView.ViewHolder {

        private ImageView thumbnail;

        public ImagesHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.image_item);
            thumbnail.setAdjustViewBounds(true);
        }
    }
}
