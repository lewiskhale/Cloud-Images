package com.madebyk.android.cloudimages.CloudImages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;

import com.madebyk.android.cloudimages.Adapters.CloudImagesAdapterFull;
import com.madebyk.android.cloudimages.R;
import com.madebyk.android.cloudimages.data.image_info;

import java.util.ArrayList;
import java.util.List;

public class CloudImagesFullActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.AppTheme);
        }
        else{
            setTheme(R.style.dankMode);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_viewpager);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        List<image_info> list_of_images = (ArrayList<image_info>) intent.getSerializableExtra("list_of_images");

        View decorView = getWindow().getDecorView();
        viewPager = (ViewPager) findViewById(R.id.viewPager_id);
        CloudImagesAdapterFull adapter = new CloudImagesAdapterFull(CloudImagesFullActivity.this, list_of_images, decorView);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }
}
