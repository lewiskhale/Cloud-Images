package com.madebyk.android.cloudimages.LocalImages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.madebyk.android.cloudimages.Adapters.LocalFullImageAdapter;
import com.madebyk.android.cloudimages.R;
import com.madebyk.android.cloudimages.data.image_info;

import java.util.ArrayList;

public class LocalImagesFullActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ImageView back_arrow;

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
        ArrayList<image_info> list_of_images = (ArrayList<image_info>) intent.getSerializableExtra("list_of_images");
        View decorView = getWindow().getDecorView();

        viewPager = (ViewPager) findViewById(R.id.viewPager_id);
        LocalFullImageAdapter adapter = new LocalFullImageAdapter(LocalImagesFullActivity.this, list_of_images, decorView);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }
}
