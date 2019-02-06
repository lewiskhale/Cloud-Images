package com.madebyk.android.cloudimages.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.madebyk.android.cloudimages.R;
import com.madebyk.android.cloudimages.data.image_info;

import java.util.List;

public class CloudImagesAdapterFull extends PagerAdapter {

    private Context mContext;
    private List<image_info> list_of_images;
    private View mDecorView;

    public CloudImagesAdapterFull(Context context, List<image_info> images, View decorView) {
        mContext = context;
        list_of_images = images;
        mDecorView = decorView;
        hideSystemUI();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return list_of_images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.images_fullscreen_image, null);

        ImageView upload_button = (ImageView) view.findViewById(R.id.fullscreen_image_nav_bar_upload_or_download);
        upload_button.setImageResource(R.drawable.ic_cloud_download);

        //
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressbar_circular);
        progressBar.setVisibility(View.GONE);

        ImageView fullscreenImage = view.findViewById(R.id.image_fullscreen_image);

        Glide
                .with(mContext)
                .load(list_of_images.get(position).getImage_Path())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_hourglass)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(fullscreenImage);

        ((ViewPager) container).addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (container.getParent() != null) {
            ((ViewPager) container).removeView((View) object);
        }
    }

    private void hideSystemUI() {
        mDecorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
