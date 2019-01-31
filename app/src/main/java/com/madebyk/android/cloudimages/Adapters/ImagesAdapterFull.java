package com.madebyk.android.cloudimages.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.madebyk.android.cloudimages.R;

import java.util.List;

public class ImagesAdapterFull extends PagerAdapter {

    private Context mContext;
    private List<Integer> list_of_images;

    public ImagesAdapterFull(Context context, List<Integer> images) {
        mContext = context;
        list_of_images = images;
    }

    @Override
    public boolean isViewFromObject( View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return list_of_images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view  = inflater.inflate(R.layout.images_fullscreen_image, null);
        ImageView fullscreenImage = view.findViewById(R.id.image_fullscreen_image);

        Glide
                .with(mContext)
                .load(list_of_images.get(position))
                .into(fullscreenImage);
        ((ViewPager) container).addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if(container.getParent() != null){
            ((ViewPager) container).removeView((View) object);
        }
    }
}
