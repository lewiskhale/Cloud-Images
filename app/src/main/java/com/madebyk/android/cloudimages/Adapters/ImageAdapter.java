package com.madebyk.android.cloudimages.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.madebyk.android.cloudimages.CloudImages.CloudImagesFullActivity;
import com.madebyk.android.cloudimages.LocalImages.LocalImagesFullActivity;
import com.madebyk.android.cloudimages.R;
import com.madebyk.android.cloudimages.data.image_info;

import java.io.Serializable;
import java.net.URI;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.LocalImageViewHolder> {

    //member vars
    private Context mContext;
    private List<image_info> images;
    private int FullImageChooser;

    public ImageAdapter(Context context, List<image_info> list, int chooser) {
        mContext = context;
        images = list;
        FullImageChooser = chooser;
    }

    @Override
    public LocalImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.images_image_item, parent, false);

        return new LocalImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocalImageViewHolder holder, final int position) {
        String image_location = images.get(position).getImage_Path();
        Glide
                .with(mContext)
                .load(image_location)
                .apply(new RequestOptions()
                        .override(400, 600)
                        .fitCenter()
                        .dontAnimate()
                        .placeholder(R.drawable.ic_hourglass)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .thumbnail(Glide.with(mContext).load(image_location))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.thumbnail);
        holder.progressBar.setVisibility(View.GONE);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FullImageChooser == 0) {
                    Intent intent = new Intent(mContext, CloudImagesFullActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("list_of_images", (Serializable) images);
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, LocalImagesFullActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("list_of_images", (Serializable) images);
                    mContext.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class LocalImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView thumbnail;
        private ProgressBar progressBar;

        public LocalImageViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.image_item);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar_image_item);
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}
