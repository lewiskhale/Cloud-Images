package com.madebyk.android.cloudimages.Images_Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.madebyk.android.cloudimages.Adapters.ImagesAdapterFull;
import com.madebyk.android.cloudimages.R;

import java.util.ArrayList;

public class ImagesFullActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private boolean flag =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_viewpager);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        ArrayList<Integer> list_of_images = intent.getIntegerArrayListExtra("images");

        View decorView = getWindow().getDecorView();
//      Hide both the navigation bar and the status bar.
//      SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
//      a general rule, you should design your app to hide the status bar whenever you
//      hide the navigation bar.

        if(flag == false) {
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
            flag = true;
        }
        else{
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            flag = false;
        }
        viewPager = (ViewPager) findViewById(R.id.viewPager_id);
        ImagesAdapterFull adapter = new ImagesAdapterFull(ImagesFullActivity.this, list_of_images);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }
}
