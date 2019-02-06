package com.madebyk.android.cloudimages.Settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.madebyk.android.cloudimages.R;
import com.madebyk.android.cloudimages.Utils.BottomNavigationViewHelper;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    //member vars
    private ListView listView;
    private ArrayAdapter<String> adapter;

    private static final String TAG = "LocalImagesActivity";
    private static final int ACTIVITY_NUM = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.AppTheme);
        } else {
            setTheme(R.style.dankMode);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setUpToolbar();
        setUpBottomNavigation();
        setUpListView();
    }

    private void setUpToolbar() {
        ImageView toolbar_image = (ImageView) findViewById(R.id.snippet_toolbar_image);
        TextView toolbar_text = (TextView) findViewById(R.id.snippet_toolbar_text);
        toolbar_image.setImageResource(R.drawable.ic_settings);
        toolbar_text.setText(R.string.settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.images_toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpBottomNavigation() {
        Log.d(TAG, "setUpBottomNavigation: setting up bottom navigation view");
        BottomNavigationViewEx view = (BottomNavigationViewEx) findViewById(R.id.images_bottom_navigation_view);
        BottomNavigationViewHelper.setupBottomNavigationView(view);
        BottomNavigationViewHelper.enableNavigation(SettingsActivity.this, view);
        Menu menu = view.getMenu();
        MenuItem item = menu.getItem(ACTIVITY_NUM);
        item.setChecked(true);
        Log.d(TAG, "setUpBottomNavigation: Completed...");
    }

    private void setUpListView() {

        final ArrayList<String> options = new ArrayList<>();
        options.add("Account");         //Edit profile and sign out
        options.add("Theme");
        options.add("Rate the App");
        options.add("Share the App");
        options.add("Support");
        options.add("About");

        listView = (ListView) findViewById(R.id.settings_listView);
        adapter = new ArrayAdapter<>(SettingsActivity.this, android.R.layout.simple_list_item_1, options);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0: //Account Settings
                        Intent intent = new Intent(SettingsActivity.this, AccountSettings.class);
                        startActivity(intent);
                        break;
                    case 1:
                        String[] themes = {"light", "dark"};
                        AlertDialog.Builder theme_choice = new AlertDialog.Builder(SettingsActivity.this);
                        theme_choice.setTitle("Choose the theme")
                                .setItems(themes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which == 0){     //Light theme
                                            setTheme(R.style.AppTheme);
                                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                        }
                                        else{               //Dark theme
                                            setTheme(R.style.dankMode);
                                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                        }
                                        restartApp();
                                    }
                                });
                        theme_choice.show();
                        break;
                    case 2:
                        Toast.makeText(SettingsActivity.this, "You clicked item named: " + options.get(position), Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(SettingsActivity.this, "You clicked item named: " + options.get(position), Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(SettingsActivity.this, "You clicked item named: " + options.get(position), Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(SettingsActivity.this, "You clicked item named: " + options.get(position), Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(SettingsActivity.this, "You clicked item named: " + options.get(position), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void restartApp() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
        finish();
    }
}
