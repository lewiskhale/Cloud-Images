package com.madebyk.android.cloudimages.Images_Home;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.madebyk.android.cloudimages.Adapters.ImagesAdapter;
import com.madebyk.android.cloudimages.R;
import com.madebyk.android.cloudimages.Utils.BottomNavigationViewHelper;
import com.madebyk.android.cloudimages.Utils.ImageCollection;

public class ImagesActivity extends AppCompatActivity {

    private static final String TAG = "ImagesActivity";
    private Context context;
    private static final int ACTIVITY_NUM = 0;

    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager manager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.AppTheme);
        }
        else{
            setTheme(R.style.dankMode);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        Log.d(TAG, "onCreate: ImagesActivity started");

        ImageView toolbar_image = (ImageView) findViewById(R.id.snippet_toolbar_image);
        TextView toolbar_text = (TextView) findViewById(R.id.snippet_toolbar_text);
        toolbar_image.setImageResource(R.drawable.ic_home);
        toolbar_text.setText(R.string.images_title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.images_toolbar);
        setSupportActionBar(toolbar);

        setUpBottomNavigation();
        setUpRecyclerView();
    }

    private void setUpRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_id);
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            manager = new StaggeredGridLayoutManager(3,OrientationHelper.VERTICAL);
        }
        else{
            manager = new StaggeredGridLayoutManager(3,OrientationHelper.HORIZONTAL);
        }
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);
        adapter = new ImagesAdapter(ImagesActivity.this, ImageCollection.getImages());
        recyclerView.setAdapter(adapter);
    }

    private void setUpBottomNavigation(){
        Log.d(TAG, "setUpBottomNavigation: setting up bottom navigation view");
        BottomNavigationViewEx view = (BottomNavigationViewEx)findViewById(R.id.images_bottom_navigation_view);
        BottomNavigationViewHelper.setupBottomNavigationView(view);
        BottomNavigationViewHelper.enableNavigation(ImagesActivity.this, view);
        Menu menu = view.getMenu();
        MenuItem item = menu.getItem(ACTIVITY_NUM);
        item.setChecked(true);
        Log.d(TAG, "setUpBottomNavigation: Completed...");
    }
}
