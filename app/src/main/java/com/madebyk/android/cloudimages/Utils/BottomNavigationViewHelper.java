package com.madebyk.android.cloudimages.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.madebyk.android.cloudimages.CloudImages.CloudImagesActivity;
import com.madebyk.android.cloudimages.LocalImages.LocalImagesActivity;
import com.madebyk.android.cloudimages.R;
import com.madebyk.android.cloudimages.Settings.SettingsActivity;

public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavigationViewHel";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx) {
        Log.d(TAG, "setupBottomNavigationView: using bottom nav view");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view){
        Log.d(TAG, "enableNavigation: has started");
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            
            public boolean onNavigationItemSelected(MenuItem item){
                switch (item.getItemId()){
                    case R.id.ic_download_navtool:
                        if(!(context instanceof CloudImagesActivity)){
                            Intent intent1 = new Intent(context, CloudImagesActivity.class);
                            context.startActivity(intent1);
                            ((Activity) context).finish();
                        }
                        break;
                    case R.id.ic_upload_navtool:
                        if(!(context instanceof LocalImagesActivity)) {
                            Intent intent2 = new Intent(context, LocalImagesActivity.class);
                            context.startActivity(intent2);
                            ((Activity) context).finish();
                        }
                        break;
                    case R.id.ic_options:
                        if(!(context instanceof SettingsActivity)) {
                            Intent intent3 = new Intent(context, SettingsActivity.class);
                            context.startActivity(intent3);
                            ((Activity) context).finish();
                        }
                        break;
                }
                return false;
            }
        });
        Log.d(TAG, "enableNavigation: Completed...");
    }
}
