package com.madebyk.android.cloudimages.Liked;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.madebyk.android.cloudimages.R;
import com.madebyk.android.cloudimages.Utils.BottomNavigationViewHelper;

public class LikedActivity extends AppCompatActivity {

    private static final String TAG = "LikedActivity";
    private static final int ACTIVITY_NUM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.AppTheme);
        }
        else{
            setTheme(R.style.dankMode);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked);

        ImageView toolbar_image = (ImageView) findViewById(R.id.snippet_toolbar_image);
        TextView toolbar_text = (TextView) findViewById(R.id.snippet_toolbar_text);
        toolbar_image.setImageResource(R.drawable.ic_liked);
        toolbar_text.setText(R.string.your_liked);
        Toolbar toolbar = (Toolbar) findViewById(R.id.images_toolbar);
        setSupportActionBar(toolbar);

        setUpBottomNavigation();
    }

    private void setUpBottomNavigation(){
        Log.d(TAG, "setUpBottomNavigation: setting up bottom navigation view");
        BottomNavigationViewEx view = (BottomNavigationViewEx)findViewById(R.id.images_bottom_navigation_view);
        BottomNavigationViewHelper.setupBottomNavigationView(view);
        BottomNavigationViewHelper.enableNavigation(LikedActivity.this, view);
        Menu menu = view.getMenu();
        MenuItem item = menu.getItem(ACTIVITY_NUM);
        item.setChecked(true);
        Log.d(TAG, "setUpBottomNavigation: Completed...");
    }
}
