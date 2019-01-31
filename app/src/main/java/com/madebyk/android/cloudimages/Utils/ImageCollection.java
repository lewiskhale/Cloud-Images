package com.madebyk.android.cloudimages.Utils;

import com.madebyk.android.cloudimages.R;

import java.util.ArrayList;
import java.util.List;

public class ImageCollection {

    private static final String TAG = "ImageCollection";
    private static ArrayList<Integer> images;

    public static ArrayList<Integer> getImages(){
        images = new ArrayList<>();

        int image1 = R.drawable.image_1;
        int image2 = R.drawable.image_2;
        int image3 = R.drawable.image_3;

        images.add(image1);
        images.add(image2);
        images.add(image3);
        return images;
    }

}
